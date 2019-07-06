package com.amazon.api.proactive.model.schema;


public class WeatherAlertActivatedSchema extends EventSchema {

    public WeatherAlertActivatedSchema(WeatherAlertType alertType, String source) {
        this.setName("AMAZON.WeatherAlert.Activated");
        this.payload = new EventWeatherAlertPayload(alertType,source);
    }

    private EventWeatherAlertPayload payload;

    public EventWeatherAlertPayload getPayload() {
        return payload;
    }

    public void setPayload(EventWeatherAlertPayload payload) {
        this.payload = payload;
    }

    private class EventWeatherAlertPayload {

        public EventWeatherAlertPayload(WeatherAlertType alertType, String source) {
            this.weatherAlert = new WeatherAlert();
            this.weatherAlert.setAlertType(alertType);
            this.weatherAlert.setSource(source);
        }

        private WeatherAlert weatherAlert;

        public WeatherAlert getWeatherAlert() {
            return weatherAlert;
        }

        public void setWeatherAlert(WeatherAlert weatherAlert) {
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

}
