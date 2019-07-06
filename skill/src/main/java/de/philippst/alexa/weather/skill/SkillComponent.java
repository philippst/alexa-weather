package de.philippst.alexa.weather.skill;

import com.amazon.ask.Skill;
import dagger.Component;
import de.philippst.alexa.weather.skill.service.ServiceModule;

@Component(modules = {SkillModule.class, ServiceModule.class})
interface SkillComponent {
    Skill getSkill();
}