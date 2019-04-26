package com.globo.pepe.common.util;

import com.globo.pepe.common.configuration.AmqpConfiguration;
import com.globo.pepe.common.controller.HealthcheckController;
import com.globo.pepe.common.controller.InfoController;
import com.globo.pepe.common.services.AmqpService;
import com.globo.pepe.common.services.JsonLoggerService;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({
    AmqpConfiguration.class,
    AmqpService.class,
    JsonLoggerService.class,
    HealthcheckController.class,
    InfoController.class
})
@Configuration
public @interface EnablePepeCommon {

}
