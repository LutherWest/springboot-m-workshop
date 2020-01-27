package com.epam.springbootworkshop.from;

import com.epam.springbootworkshop.quartz.from.EnableRepeatableQuartzJob;
import com.epam.springbootworkshop.quartz.from.RepeatableQuartzJob;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.util.Properties;

public class QuartzImportApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(QuartzImportConfig.class);

        Runtime.getRuntime().addShutdownHook(new Thread(ctx::close));
    }

    @Configuration
    @EnableRepeatableQuartzJob
    public static class QuartzImportConfig {
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


    @RepeatableQuartzJob(repeatInterval = 1000)
    public static class JobClass extends QuartzJobBean {

        @Override
        protected void executeInternal(JobExecutionContext context) {
            System.out.println("Hello _1");
        }
    }
}
