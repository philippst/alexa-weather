package de.philippst.alexa.weather.notification;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.mysql.cj.jdbc.MysqlDataSource;
import de.philippst.alexa.weather.notification.cap.Alert;
import de.philippst.alexa.weather.notification.service.AlexaAlertLogService;
import de.philippst.alexa.weather.notification.service.AlexaUserService;
import de.philippst.alexa.weather.notification.service.CapXmlService;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@SuppressWarnings("unused")
public class LambdaRequestHandler implements RequestHandler<S3Event, Void> {

    private static final Logger logger = LoggerFactory.getLogger(LambdaRequestHandler.class);
    private final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();

    private CapXmlService capXmlService = new CapXmlService();
    private AlertProcessor alertProcessor;

    public LambdaRequestHandler() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://"+System.getenv("RDS_ENDPOINT")+":3306/alexa_weather?useSSL=false");
        dataSource.setUser(System.getenv("RDS_USERNAME"));
        dataSource.setPassword(System.getenv("RDS_PASSWORD"));
        Jdbi jdbi = Jdbi.create(dataSource);
        AlexaUserService alexaUserService = new AlexaUserService(jdbi);
        AlexaAlertLogService alexaAlertLogService = new AlexaAlertLogService(jdbi);
        alertProcessor = new AlertProcessor(alexaUserService,alexaAlertLogService);
    }

    @Override
    public Void handleRequest(S3Event input, Context context) {

        List<String> alertFiles = new ArrayList<>();
        input.getRecords().stream().map(this::processS3Record).forEach(alertFiles::addAll);
        logger.debug("Received #{} alerts.",alertFiles.size());

        try {
            List<Alert> alertList = capXmlService.alertsFromString(alertFiles);
            this.alertProcessor.processAlerts(alertList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<String> processS3Record(S3EventNotification.S3EventNotificationRecord record) {

        ArrayList<String> fileData = new ArrayList<>();

        S3EventNotification.S3Entity s3Entity = record.getS3();

        logger.debug("Received S3EventNotificationRecord: eventName={} eventSource={} bucketName={} objectKey={}",
                record.getEventName(), record.getEventSource(), s3Entity.getBucket().getName(),
                s3Entity.getObject().getKey());

        S3Object s3Object = s3.getObject(s3Entity.getBucket().getName(),s3Entity.getObject().getKey());

        try(
                S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
                ZipInputStream zis = new ZipInputStream(s3ObjectInputStream)
        ){
            logger.debug("Opened ZIP input stream from '{}'",s3Object.getKey());
            ZipEntry zipEntry;
            while((zipEntry = zis.getNextEntry())!=null) {
                logger.debug("ZIP contains file '{}'",zipEntry.getName());
                String entry = IOUtils.toString(zis);
                fileData.add(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileData;
    }
}
