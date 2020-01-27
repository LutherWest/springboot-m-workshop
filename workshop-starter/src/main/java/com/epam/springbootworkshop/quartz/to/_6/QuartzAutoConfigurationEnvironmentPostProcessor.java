package com.epam.springbootworkshop.quartz.to._6;

import java.util.Map;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.StringUtils;

public class QuartzAutoConfigurationEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication application) {
        if (env.getProperty("scheduling.enabled", Boolean.class, true)) {
            final String newPropertyValue = Optional.ofNullable(env.getProperty("spring.autoconfigure.exclude"))
                    .map(StringUtils::commaDelimitedListToStringArray)
                    .map(arr -> StringUtils.addStringToArray(arr, QuartzAutoConfiguration.class.getName()))
                    .map(StringUtils::arrayToCommaDelimitedString)
                    .orElseGet(QuartzAutoConfiguration.class::getName);

            env.getPropertySources().addFirst(new MapPropertySource("quartz_exclusions",
                    Map.of("spring.autoconfigure.exclude", newPropertyValue)));
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 100;
    }
}
