package com.epam.springbootworkshop.quartz._2;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
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

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ Scheduler.class, SchedulerFactoryBean.class })
@AutoConfigureBefore(QuartzAutoConfiguration.class)
@Import(AutoConfigureQuartzJobImportBeanDefinitionRegistrar.class)
@ConditionalOnProperty(name = "scheduling.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(QuartzProperties.class)
public class QuartzSchedulerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SchedulerFactoryBean scheduler(JobFactory jobFactory,
                                          ObjectProvider<Trigger> triggers,
                                          QuartzProperties properties,
                                          Environment environment) {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setSchedulerName(properties.getSchedulerName());
        bean.setWaitForJobsToCompleteOnShutdown(properties.getWaitForJobsCompleteOnShutdown());
        bean.setOverwriteExistingJobs(properties.getOverwriteExistingJobs());
        bean.setJobFactory(jobFactory);

        final Trigger[] configuredTriggers = triggers.stream().toArray(Trigger[]::new);
        if (configuredTriggers.length > 0) {
            bean.setTriggers(configuredTriggers);
        } else {
            throw new NoTriggerConfiguredException();
        }

        Properties props = new Properties();
        props.put("org.quartz.scheduler.instanceId", "AUTO");
        props.put("org.quartz.threadPool.threadCount", "2");
        props.put("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");

        final Map<String, String> additionalProperties = Binder.get(environment)
                .bind("scheduling.properties", Bindable.mapOf(String.class, String.class))
                .orElseGet(Collections::emptyMap);
        props.putAll(additionalProperties);

        bean.setQuartzProperties(props);

        return bean;
    }

    @Bean
    @ConditionalOnMissingBean
    public JobFactory quartzJobFactory() {
        return new SpringBeanJobFactory();
    }
}
