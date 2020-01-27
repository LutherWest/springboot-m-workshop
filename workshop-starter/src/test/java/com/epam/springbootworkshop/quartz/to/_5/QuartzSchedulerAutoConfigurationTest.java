package com.epam.springbootworkshop.quartz.to._5;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.epam.springbootworkshop.quartz.to.AutoPackageConfig;

class QuartzSchedulerAutoConfigurationTest {
    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withUserConfiguration(AutoPackageConfig.class)
            .withConfiguration(AutoConfigurations.of(QuartzSchedulerAutoConfiguration.class));

    @Test
    void shouldApplyConfigurationProperties() {
        runner.withUserConfiguration(Config.class)
                .run(ctx -> {
                    Assertions.assertThat(ctx)
                            .hasSingleBean(SchedulerFactoryBean.class)
                            .hasSingleBean(Scheduler.class);
                    Assertions.assertThat(Config.wasCustomized).isTrue();
                });
    }

    @TestConfiguration
    static class Config {
        static boolean wasCustomized = false;

        @Bean
        public QuartzSchedulerFactoryBeanCustomizer customizer() {
            return schedulerFactoryBean -> wasCustomized = true;
        }
    }
}
