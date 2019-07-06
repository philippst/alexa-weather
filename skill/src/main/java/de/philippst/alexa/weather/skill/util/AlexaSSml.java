package de.philippst.alexa.weather.skill.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class AlexaSSml {

    public static String dateToSpeech(LocalDate inputDate, LocalDate referenceDate){
        int daysBetween = Period.between(referenceDate,inputDate).getDays();

        switch(daysBetween){
            case 0:
                return "heute";
            case 1:
                return "morgen";
            default:
                return String.format("<say-as interpret-as='date' format='dmy'>%s</say-as>",
                        inputDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
        }
    }

    public static String locationToSpeech(String location){
        return location
                .replace("(Westf.)","Westfalen")
                .replace("(Rhld.)","Rheinland")
                .replace("i.d. OPf.","in der Oberpfalz")
                .replace("(Oldb)","im Oldenburger Land")
                .replace("v.d. Höhe","vor der Höhe")
                .replace("a. Erlbach","am Erlbach")
                .replace("Buchholz/Nordh.","Buchholz in der Nordheide")
                .replace("Rehlingen-S.","Rehlingen-Siersburg")
                .replace("Nienbg. (Weser)","Nienburg/Weser")
                .replace("Bad Neuenahr-A.","Bad Neuenahr-Ahrweiler")
                .replace("a.d. Lahn","an der Lahn");
    }
}
