package com.epam.springbootworkshop.quartz.to._5;

import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@FunctionalInterface
public interface QuartzSchedulerFactoryBeanCustomizer {
    void customize(SchedulerFactoryBean schedulerFactoryBean);
}
