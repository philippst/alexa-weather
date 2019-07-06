package de.philippst.alexa.weather.skill;

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;
import dagger.Module;
import dagger.Provides;
import de.philippst.alexa.weather.skill.handler.*;
import de.philippst.alexa.weather.skill.handler.exception.AddressExceptionHandler;
import de.philippst.alexa.weather.skill.handler.exception.PermissionMissingExceptionHandler;
import de.philippst.alexa.weather.skill.handler.exception.TechnicalExceptionHandler;
import de.philippst.alexa.weather.skill.handler.exception.WeatherProviderExceptionHandler;
import de.philippst.alexa.weather.skill.inteceptor.PermissionRequestInterceptor;
import de.philippst.alexa.weather.skill.inteceptor.UserRequestInterceptor;

@Module
public class SkillModule {

    @Provides
    Skill provideSkill(ExitIntentHandler exitIntentHandler,
                       HelpIntentHandler helpIntentHandler,
                       LaunchRequestHandler launchRequestHandler,
                       SkillEnabledHandler skillEnabledHandler,
                       SkillDisabledHandler skillDisabledHandler,
                       SessionEndedRequestHandler sessionEndedRequestHandler,
                       PermissionRequestInterceptor permissionRequestInterceptor,
                       UserRequestInterceptor userRequestInterceptor,
                       ProactiveSubscriptionChangedHandler proactiveSubscriptionChangedHandler,
                       PermissionAcceptedHandler permissionAcceptedHandler,
                       PermissionChangedHandler permissionChangedHandler,
                       NotificationStatusHandler notificationStatusHandler){

        return Skills.custom()
                .withApiClient(new OkHttpApiClient())
                .addRequestInterceptors(
                        permissionRequestInterceptor,
                        userRequestInterceptor
                )
                .addRequestHandlers(
                        proactiveSubscriptionChangedHandler,
                        permissionAcceptedHandler,
                        permissionChangedHandler,
                        exitIntentHandler,
                        helpIntentHandler,
                        launchRequestHandler,
                        notificationStatusHandler,
                        skillDisabledHandler,
                        skillEnabledHandler,
                        sessionEndedRequestHandler
                )
                .addExceptionHandlers(
                        new PermissionMissingExceptionHandler(),
                        new AddressExceptionHandler(),
                        new WeatherProviderExceptionHandler(),
                        new TechnicalExceptionHandler()
                )
                .withSkillId(System.getenv("SKILL_ID"))
                .build();
    }
}
