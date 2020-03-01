package com.epam.springbootworkshop.quartz.to._4;

import java.util.Properties;

import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.epam.springbootworkshop.quartz.to._1.AutoConfigureQuartzJobImportBeanDefinitionRegistrar;

@Configuration(proxyBeanMethods = false)
@Import(AutoConfigureQuartzJobImportBeanDefinitionRegistrar.class)
@ConditionalOnClass({ Scheduler.class, SchedulerFactoryBean.class })
@ConditionalOnProperty(name = "scheduling.enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureBefore(QuartzAutoConfiguration.class)
public class QuartzSchedulerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean({SchedulerFactoryBean.class, Scheduler.class})
    public SchedulerFactoryBean scheduler(JobFactory jobFactory, ObjectProvider<Trigger[]> triggers) {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setSchedulerName("name");
        bean.setWaitForJobsToCompleteOnShutdown(true);
        bean.setOverwriteExistingJobs(true);
        bean.setJobFactory(jobFactory);
        triggers.ifAvailable(bean::setTriggers);

        Properties props = new Properties();
        props.put("org.quartz.scheduler.instanceId", "AUTO");
        props.put("org.quartz.threadPool.threadCount", "2");
        props.put("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");

        bean.setQuartzProperties(props);

        return bean;
    }

    @Bean
    @ConditionalOnMissingBean
    public JobFactory beanFactoryJobFactory() {
        return new SpringBeanJobFactory();
    }
}
