package com.epam.springbootworkshop.quartz.to._1;

import org.quartz.Trigger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Optional;

import com.epam.springbootworkshop.quartz.from.RepeatableQuartzJob;

public class AutoConfigureQuartzJobImportBeanDefinitionRegistrar
        implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware, BeanFactoryAware {
    private Environment environment;
    private ResourceLoader resourceLoader;
    private BeanFactory beanFactory;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        Assert.notNull(environment, "Environment must not be null");
        Assert.notNull(resourceLoader, "ResourceLoader must not be null");

        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false, environment);
        scanner.setResourceLoader(resourceLoader);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RepeatableQuartzJob.class));

        AutoConfigurationPackages.get(beanFactory).stream()
                .map(scanner::findCandidateComponents)
                .flatMap(Collection::stream)
                .forEach(beanDefinition -> {
                    AbstractBeanDefinition jobDetailBeanDefinition =
                            BeanDefinitionBuilder.rootBeanDefinition(JobDetailFactoryBean.class)
                                    .addPropertyValue("name", beanDefinition.getBeanClassName() + "JobDetail")
                                    .addPropertyValue("durability", true)
                                    .addPropertyValue("requestsRecovery", false)
                                    .addPropertyValue("jobClass", beanDefinition.getBeanClassName())
                                    .getBeanDefinition();

                    final String jobDetailBeanName =
                            BeanDefinitionReaderUtils.registerWithGeneratedName(jobDetailBeanDefinition, registry);

                    Assert.isAssignable(AnnotatedBeanDefinition.class, beanDefinition.getClass());
                    AnnotatedBeanDefinition bd = (AnnotatedBeanDefinition) beanDefinition;
                    AnnotationAttributes jobAttrs =
                            Optional.ofNullable(bd.getMetadata().getAnnotationAttributes(RepeatableQuartzJob.class.getName()))
                                    .map(AnnotationAttributes::new)
                                    .orElseThrow(() -> new IllegalStateException(String.format("Unable to obtain "
                                            + "annotation attributes for %s!", RepeatableQuartzJob.class.getName())));

                    AbstractBeanDefinition jobTriggerBeanDefinition =
                            BeanDefinitionBuilder.rootBeanDefinition(SimpleTriggerFactoryBean.class)
                                    .addPropertyValue("name", beanDefinition.getBeanClassName() + "JobTrigger")
                                    .addPropertyValue("misfireInstruction", Trigger.MISFIRE_INSTRUCTION_SMART_POLICY)
                                    .addPropertyValue("repeatCount", -1)
                                    .addPropertyValue("repeatInterval", jobAttrs.getNumber("repeatInterval"))
                                    .addPropertyReference("jobDetail", jobDetailBeanName)
                                    .getBeanDefinition();

                    BeanDefinitionReaderUtils.registerWithGeneratedName(jobTriggerBeanDefinition, registry);
                });
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
