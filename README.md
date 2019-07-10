# :umbrella: Amazon Alexa *Wetterwarnung*

Dies ist der Quelltext zum Alexa Skill [Wetterwarnung](https://www.amazon.de/Philippst-Wetterwarnung/dp/B0785WWYXY).

Der Skill wird unregelmäßig und hobbymäßig weiterentwickelt. Fehler, Verbesserungsvorschläge und Erweiterungen sind 
als Github Issue oder Pull Request stets gerne gesehen!


## Beschreibung
Egal ob Sturm, Starkregen, Gewitter, Glatteis, ... mit diesem Skill stets gut informiert über nahende Unwetter und 
Extremwetter in Deutschland. Der Bericht ist ortsgenau und wird minütlich aktualisiert auf Basis der amtlichen Daten 
des Deutschen Wetterdienstes.

Vorteile von diesem Skill:
* Alle Orte in Deutschland
* Aktive Benachrichtigung bei Unwetterlagen
* Ortsgenau auf Gemeindeebene
* Amtliche Daten des Deutschen Wetterdienstes
* Warnmeldungen zum Nachlesen in Alexa App
* Empfehlungen zur Verhaltensweise bei Extremwetter
* Keine Werbung, keine Kosten

Um die lokale Wetterlage ermitteln zu können, verwendet dieser Skill die in der Alexa App gespeicherte Postleitzahl des 
Gerätestandorts. Diese Information kann im Menüpunkt "Einstellungen" unter "Standortangaben" geändert werden. Es muss 
bei erster Verwendung des Skills einmalig der Zugriff auf die Postleitzahl in der Alexa App genehmigt werden.

*Diese Anwendung nutzt Wetter- und Klimadaten die im Rahmen des Gesetzes über den Deutschen Wetterdienst (DWD-Gesetz)
 vom Deutschen Wetterdienst entgeltfrei zur Verfügung gestellt werden. Es besteht keine Geschäftsbeziehung zwischen 
 dem Deutschen Wetterdienst und den Autoren dieser Anwendung. Alle Angaben und die Bereitstellung dieser Anwendung 
 erfolgen ohne Gewähr und Anspruch auf Vollständigkeit.*


## Architektur

Dieser Skill ist in Java entwickelt und besteht aus mehreren Gradle Modulen.

| Gradle Module | Beschreibung                                          | Anmerkung                 |
|---------------|-------------------------------------------------------|---------------------------|
| :skill        | Alexa Skill Endpoint                                  |                           |
| :notification | Verarbeitung von Wetterwarnungen und Benachrichtigung |                           |
| :crawler      | Periodischer Crawler für DWD FTP Server               |                           |
| :common       | Datenmodelle und Utilities zur Wiederverwendung       |                           |

## Skill


### Was passiert wann?


| Action                       | PSC | PC / PA | SE | SD | user | device |
| ---------------------------- |:---:|:-------:|:--:|:--:|:----:|:------:|
| Disable Notification via APP | X   | X       | -  | -  | X    | -      |
| Enable Notification via App  | X   | X       | -  | -  | X    | -      |
| Disable Address perm via App | X   | X       | -  | -  | X    | -      |
| Enable Address perm via App  | X   | X       | -  | -  | X    | -      |
| Enable Skill with Perm       | X   | X       | X  | -  | X    | -      |
| Disable Skill                | X   | -       | -  | X  | X    | -      |


* PSC = Proactive Subscription Changed Handler
* PC = Permission Changed Handler, executed if any permission changed
* PA = Permission Accepted Handler, executed if first permission is added
* SE = Skill Enabled Handler
* SD = Skill Disabled Handler


## Notification

Das *Notification* Modul wird als AWS Lambda Funktion bereitgestellt und durch AWS automatisch bei Eingang neuer 
Dateien im S3 Bucket (Ablage der Alert CAP Dateien durch Crawler) aufgerufen. Als Aufrufparameter erhält die Lambda 
Funktion Details der neu eingegangenen Dateien.



Hinweise zur Entwicklung:
```
# get current skill manifest
ask api get-skill -s amzn1.ask.skill.XXXX --stage development > alexa-weather.json

# update skill manifest
ask api update-skill -s amzn1.ask.skill.XXXX --stage development -f alexa-weather.json

# check update status
ask api get-skill-status -s amzn1.ask.skill.XXXX
```

Important settings in skill manifest (skill.json)
```
...
   "permissions": [
      {
        "name": "alexa:devices:all:address:country_and_postal_code:read"
      },
      {
        "name": "alexa::devices:all:notifications:write"
      }
    ],
    "events": {
      "publications": [
        {
          "eventName": "AMAZON.WeatherAlert.Activated"
        }
      ],
      "endpoint": {
        "uri": "arn:aws:lambda:us-east-1:XXXX:function:alexa-weather"
      },
      "subscriptions": [
        {
          "eventName": "SKILL_ENABLED"
        },
        {
          "eventName": "SKILL_DISABLED"
        },
        {
          "eventName": "SKILL_PERMISSION_ACCEPTED"
        },
        {
          "eventName": "SKILL_PERMISSION_CHANGED"
        },
        {
          "eventName": "SKILL_PROACTIVE_SUBSCRIPTION_CHANGED"
        }
      ],
      "regions": {
        "NA": {
          "endpoint": {
            "uri": "arn:aws:lambda:us-east-1:XXXXX:function:alexa-weather"
          }
        }
      }
    },
...
```

## Crawler


## Common