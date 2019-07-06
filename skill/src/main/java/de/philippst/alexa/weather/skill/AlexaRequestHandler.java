package de.philippst.alexa.weather.skill;

import com.amazon.ask.SkillStreamHandler;
import de.philippst.alexa.weather.skill.DaggerSkillComponent;

@SuppressWarnings("unused")
public final class AlexaRequestHandler extends SkillStreamHandler {

    public AlexaRequestHandler() {
        super(DaggerSkillComponent.create().getSkill());
    }
}