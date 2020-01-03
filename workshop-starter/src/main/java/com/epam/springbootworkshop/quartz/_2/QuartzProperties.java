package com.epam.springbootworkshop.quartz._2;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "scheduling")
@Getter @Setter
public class QuartzProperties {
    private boolean enabled = true;
    private String schedulerName = "name";
    private Boolean waitForJobsCompleteOnShutdown = true;
    private Boolean overwriteExistingJobs = true;
}
