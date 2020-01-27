package com.epam.springbootworkshop.quartz.to._2;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.epam.springbootworkshop.quartz.to.AutoPackageConfig;

import static org.mockito.Mockito.mock;

class QuartzSchedulerAutoConfigurationTest {
    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withUserConfiguration(AutoPackageConfig.class)
            .withConfiguration(AutoConfigurations.of(QuartzSchedulerAutoConfiguration.class));

    @Test
    void shouldNotApplyAutoConfigurationWhenNoRequiredClass() {
        runner.withClassLoader(new FilteredClassLoader(Scheduler.class))
                .run(ctx -> Assertions.assertThat(ctx)
                        .doesNotHaveBean(SchedulerFactoryBean.class)
                        .doesNotHaveBean(Scheduler.class)
                        .doesNotHaveBean(JobFactory.class));
    }

    @Test
    void shouldNotApplyAutoConfigurationWhenItNotEnabledByProperty() {
        runner.withPropertyValues("scheduling.enabled=false")
                .run(ctx -> Assertions.assertThat(ctx)
                        .doesNotHaveBean(SchedulerFactoryBean.class)
                        .doesNotHaveBean(Scheduler.class)
                        .doesNotHaveBean(JobFactory.class));
    }

    @Test
    void shouldNotRegisterSchedulerWhenAlreadyRegistered() {
        runner.withUserConfiguration(SchedulerConfig.class)
                .run(ctx -> {
                    Assertions.assertThat(ctx)
                            .hasSingleBean(Scheduler.class)
                            .hasSingleBean(JobFactory.class);
                    Assertions.assertThat(ctx.getBean(Scheduler.class))
                            .isEqualTo(SchedulerConfig.MOCK);
                });
    }

    @TestConfiguration
    static class SchedulerConfig {
        static final Scheduler MOCK = mock(Scheduler.class);

        @Bean
        public Scheduler scheduler() {
            return MOCK;
        }
    }

}
