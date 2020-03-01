package com.epam.springbootworkshop.to;

import org.quartz.JobExecutionContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.epam.springbootworkshop.quartz.from.RepeatableQuartzJob;

@SpringBootApplication
public class QuartzAutoConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzAutoConfigApplication.class, args);
    }

    @RepeatableQuartzJob(repeatInterval = 2000)
    public static class JobClass extends QuartzJobBean {

        @Override
        protected void executeInternal(JobExecutionContext context) {
            System.out.println("Hello _2");
        }
    }
}
