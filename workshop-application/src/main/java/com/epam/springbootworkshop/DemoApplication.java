package com.epam.springbootworkshop;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.epam.springbootworkshop.quartz._2.RepeatableQuartzJob;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

//    @RepeatableQuartzJob(repeatInterval = 1000)
//    public static class MyJob extends QuartzJobBean {
//        @Override
//        protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
//            System.out.println("Wow!");
//        }
//    }
}
