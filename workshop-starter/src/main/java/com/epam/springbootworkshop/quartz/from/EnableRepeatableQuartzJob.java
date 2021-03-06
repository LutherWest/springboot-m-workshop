package com.epam.springbootworkshop.quartz.from;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Import(QuartzJobImportBeanDefinitionRegistrar.class)
public @interface EnableRepeatableQuartzJob {

    String[] basePackages() default {};
}
