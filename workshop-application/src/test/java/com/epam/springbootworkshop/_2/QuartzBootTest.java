package com.epam.springbootworkshop._2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.epam.springbootworkshop.quartz._2.RepeatableQuartzJob;

@SpringBootTest(classes = QuartzBootTest.Config.class)
public class QuartzBootTest {

    @Autowired
    private CountDownLatch latch;

    @Test
    void shouldRunJob() throws InterruptedException {
        Assertions.assertThat(latch.await(5, TimeUnit.SECONDS))
                .describedAs("Job should be executed at least 2 times!")
                .isTrue();
    }

    @SpringBootApplication
    @Configuration
    static class Config {

        @Bean
        public CountDownLatch countDownLatch() {
            return new CountDownLatch(2);
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
