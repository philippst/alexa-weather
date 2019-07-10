package de.philippst.alexa.weather.notification.model;

public class EventWeatherAlertPayload {


    public EventWeatherAlertPayload(WeatherAlertType alertType, String source) {
        this.weatherAlert = new EventWeatherAlertPayload.WeatherAlert();
        this.weatherAlert.setAlertType(alertType);
        this.weatherAlert.setSource(source);
    }

    private EventWeatherAlertPayload.WeatherAlert weatherAlert;

    public EventWeatherAlertPayload.WeatherAlert getWeatherAlert() {
        return weatherAlert;
    }

    public void setWeatherAlert(EventWeatherAlertPayload.WeatherAlert weatherAlert) {
        this.weatherAlert = weatherAlert;
    }

    private class WeatherAlert {
        private String source;
        private WeatherAlertType alertType;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public WeatherAlertType getAlertType() {
            return alertType;
        }

        public void setAlertType(WeatherAlertType alertType) {
            this.alertType = alertType;
        }
    }


}
