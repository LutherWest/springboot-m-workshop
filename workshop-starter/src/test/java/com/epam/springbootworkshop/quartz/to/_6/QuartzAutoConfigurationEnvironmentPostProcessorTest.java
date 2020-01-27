package com.epam.springbootworkshop.quartz.to._6;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = QuartzAutoConfigurationEnvironmentPostProcessorTest.Config.class)
class QuartzAutoConfigurationEnvironmentPostProcessorTest {

    @Autowired
    private ConfigurableListableBeanFactory beanFactory;

    @Test
    void shouldExcludeQuartzAutoConfiguration() {
        final ConditionEvaluationReport report = ConditionEvaluationReport.get(beanFactory);
        Assertions.assertThat(report.getExclusions())
                .contains(QuartzAutoConfiguration.class.getName());
    }

    @SpringBootApplication
    static class Config {

    }
}
