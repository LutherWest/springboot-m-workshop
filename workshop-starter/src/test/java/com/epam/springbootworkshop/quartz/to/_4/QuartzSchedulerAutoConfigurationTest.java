package com.epam.springbootworkshop.quartz.to._4;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.epam.springbootworkshop.quartz.to.AutoPackageConfig;

class QuartzSchedulerAutoConfigurationTest {
    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withUserConfiguration(AutoPackageConfig.class)
            .withConfiguration(AutoConfigurations.of(QuartzSchedulerAutoConfiguration.class));

    @Test
    void shouldApplyConfigurationProperties() {
        runner.withPropertyValues("scheduling.scheduler-name=changed-name")
                .withPropertyValues("scheduling.properties.org.quartz.threadPool.threadCount=10")
                .run(ctx -> {
                    Assertions.assertThat(ctx)
                            .hasSingleBean(SchedulerFactoryBean.class)
                            .hasSingleBean(Scheduler.class);
                    Assertions.assertThat(ctx.getBean(SchedulerFactoryBean.class))
                            .hasFieldOrPropertyWithValue("schedulerName", "changed-name");
                    Assertions.assertThat(ctx.getBean(Scheduler.class))
                            .hasFieldOrPropertyWithValue("metaData.threadPoolSize", 10);
                });
    }
}
