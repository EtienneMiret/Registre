package fr.elimerl.registre.config;

import org.springframework.beans.factory.FactoryBean;

import java.time.Clock;
import java.time.ZoneId;

public class ClockProvider implements FactoryBean<Clock> {

  @Override
  public Clock getObject () {
    return Clock.system (ZoneId.of ("Europe/Paris"));
  }

  @Override
  public Class<?> getObjectType () {
    return Clock.class;
  }

}
