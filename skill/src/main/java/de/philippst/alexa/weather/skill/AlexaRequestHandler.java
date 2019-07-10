package de.philippst.alexa.weather.skill;

import com.amazon.ask.SkillStreamHandler;

@SuppressWarnings("unused")
public final class AlexaRequestHandler extends SkillStreamHandler {

    public AlexaRequestHandler() {
        super(DaggerSkillComponent.create().getSkill());
    }
}