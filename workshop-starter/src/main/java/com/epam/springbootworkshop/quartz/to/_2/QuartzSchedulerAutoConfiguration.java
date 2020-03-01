package com.epam.springbootworkshop.quartz.to._2;

import java.util.Properties;

import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.epam.springbootworkshop.quartz.to._1.AutoConfigureQuartzJobImportBeanDefinitionRegistrar;

@Configuration(proxyBeanMethods = false)
@Import(AutoConfigureQuartzJobImportBeanDefinitionRegistrar.class)
public class QuartzSchedulerAutoConfiguration {

    @Bean
    public SchedulerFactoryBean scheduler(JobFactory jobFactory, ObjectProvider<Trigger[]> triggers) {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setSchedulerName("name");
        bean.setWaitForJobsToCompleteOnShutdown(true);
        bean.setOverwriteExistingJobs(true);
        bean.setJobFactory(jobFactory);
        bean.setAutoStartup(true);
        triggers.ifAvailable(bean::setTriggers);

        Properties props = new Properties();
        props.put("org.quartz.scheduler.instanceId", "AUTO");
        props.put("org.quartz.threadPool.threadCount", "2");
        props.put("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");

        bean.setQuartzProperties(props);

        return bean;
    }

    @Bean
    public JobFactory beanFactoryJobFactory() {
        return new SpringBeanJobFactory();
    }
}
