package com.epam.springbootworkshop._1;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.epam.springbootworkshop.quartz._1.EnableRepeatableQuartzJob;
import com.epam.springbootworkshop.quartz._1.RepeatableQuartzJob;

@SpringBootTest(classes = QuartzTest.Config.class)
public class QuartzTest {

    @Autowired
    private CountDownLatch latch;

    @Test
    void shouldRunJob() throws InterruptedException {
        Assertions.assertThat(latch.await(5, TimeUnit.SECONDS))
                .describedAs("Job should be executed at least 2 times!")
                .isTrue();
    }

    @EnableRepeatableQuartzJob
    @Configuration
    static class Config {

        @Bean
        public CountDownLatch countDownLatch() {
            return new CountDownLatch(2);
        }

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
        public JobFactory beanFactoryJobFactory(ApplicationContext ctx) {
            final SpringBeanJobFactory springBeanJobFactory = new SpringBeanJobFactory();
            springBeanJobFactory.setApplicationContext(ctx);
            return springBeanJobFactory;
        }
    }

    @RepeatableQuartzJob(repeatInterval = 1000)
    static class MyJob extends QuartzJobBean {
        @Autowired
        private CountDownLatch latch;

        @Override
        protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Executed");
            latch.countDown();
        }
    }
}
