package com.epam.springbootworkshop._2;

import java.lang.reflect.Field;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportMessage;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.ReflectionUtils;

import com.epam.springbootworkshop.quartz._2.QuartzProperties;
import com.epam.springbootworkshop.quartz._2.RepeatableQuartzJob;

@SpringBootApplication
public class QuartzAutoConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzAutoConfigApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(SchedulerFactoryBean scheduler) {
        return args -> {
            final Field field = ReflectionUtils.findField(SchedulerFactoryBean.class, "schedulerName");
            ReflectionUtils.makeAccessible(field);
            System.out.println("Scheduler name = " + ReflectionUtils.getField(field, scheduler));
            System.out.println("Thread pool size = " + scheduler.getScheduler().getMetaData().getThreadPoolSize());
        };
    }

//    @Bean
    public CommandLineRunner conditionEvaluationReport(ConfigurableListableBeanFactory beanFactory) {
        return args -> {
            final ConditionEvaluationReport report = ConditionEvaluationReport.get(beanFactory);
            System.out.println(new ConditionEvaluationReportMessage(report));
        };
    }

    @Bean
    public CommandLineRunner commandLineRunner1(Environment environment) {
        return args -> {
            Binder.get(environment)
                    .bind("scheduling", QuartzProperties.class)
                    .ifBound(props -> {
                        // my-scheduler-name-int
                        System.out.println(props.getSchedulerName());
                    });
        };
    }

    @RepeatableQuartzJob(repeatInterval = 3000)
    public static class JobClass extends QuartzJobBean {

        @Override
        protected void executeInternal(JobExecutionContext context) {
            System.out.println("Hello _2");
        }
    }
}
