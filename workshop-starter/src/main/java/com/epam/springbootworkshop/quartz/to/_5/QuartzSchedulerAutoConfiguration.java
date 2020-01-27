package com.epam.springbootworkshop.quartz.to._5;

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
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
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
import com.epam.springbootworkshop.quartz.to._4.QuartzProperties;

@Configuration(proxyBeanMethods = false)
@Import(AutoConfigureQuartzJobImportBeanDefinitionRegistrar.class)
@ConditionalOnClass({ Scheduler.class, SchedulerFactoryBean.class })
@ConditionalOnProperty(name = "scheduling.enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureBefore(QuartzAutoConfiguration.class)
@EnableConfigurationProperties(QuartzProperties.class)
public class QuartzSchedulerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean({SchedulerFactoryBean.class, Scheduler.class})
    public SchedulerFactoryBean scheduler(JobFactory jobFactory, ObjectProvider<Trigger[]> triggers,
                                          QuartzProperties properties, Environment environment,
                                          ObjectProvider<QuartzSchedulerFactoryBeanCustomizer> customizers) {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setSchedulerName(properties.getSchedulerName());
        bean.setWaitForJobsToCompleteOnShutdown(properties.getWaitForJobsCompleteOnShutdown());
        bean.setOverwriteExistingJobs(properties.getOverwriteExistingJobs());
        bean.setJobFactory(jobFactory);
        triggers.ifAvailable(bean::setTriggers);

        Properties props = new Properties();
        props.put("org.quartz.scheduler.instanceId", "AUTO");
        props.put("org.quartz.threadPool.threadCount", "2");
        props.put("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");

        Binder.get(environment)
                .bind("scheduling.properties", Bindable.mapOf(String.class, String.class))
                .ifBound(props::putAll);

        bean.setQuartzProperties(props);
        // ...
        customizers.orderedStream().forEach(customizer -> customizer.customize(bean));

        return bean;
    }

    @Bean
    @ConditionalOnMissingBean
    public JobFactory beanFactoryJobFactory() {
        return new SpringBeanJobFactory();
    }
}
