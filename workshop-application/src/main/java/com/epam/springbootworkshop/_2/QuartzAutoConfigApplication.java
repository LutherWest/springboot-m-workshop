package com.epam.springbootworkshop._2;

import com.epam.springbootworkshop.quartz._2.RepeatableQuartzJob;
import org.quartz.JobExecutionContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.quartz.QuartzJobBean;

@SpringBootApplication
public class QuartzAutoConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzAutoConfigApplication.class, args);
    }

    @RepeatableQuartzJob(repeatInterval = 1000)
    public static class JobClass extends QuartzJobBean {

        @Override
        protected void executeInternal(JobExecutionContext context) {
            System.out.println("Hello _2");
        }
    }
}
