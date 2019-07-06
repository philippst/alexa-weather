package de.philippst.alexa.weather.skill.util;


import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlexaSSmlTest {

    @Test
    public void locationToSpeech() {
        assertEquals("Oldenburg im Oldenburger Land",AlexaSSml.locationToSpeech("Oldenburg (Oldb)"));
        assertEquals("Köln Rheinland",AlexaSSml.locationToSpeech("Köln (Rhld.)"));
        assertEquals("Muster in der Oberpfalz",AlexaSSml.locationToSpeech("Muster i.d. OPf."));
    }

    @Test
    void dateToSpeechToday() {
        LocalDate referenceDate = LocalDate.of(2017,1,1);

        LocalDate todayDate = LocalDate.of(2017,1,1);
        LocalDate tomorrowDate = LocalDate.of(2017,1,2);
        LocalDate futureDate = LocalDate.of(2017,1,3);

        assertEquals("heute",AlexaSSml.dateToSpeech(todayDate,referenceDate));
        assertEquals("morgen",AlexaSSml.dateToSpeech(tomorrowDate,referenceDate));
        assertEquals("<say-as interpret-as='date' format='dmy'>03.01.17</say-as>",
                AlexaSSml.dateToSpeech(futureDate,referenceDate));
    }
}