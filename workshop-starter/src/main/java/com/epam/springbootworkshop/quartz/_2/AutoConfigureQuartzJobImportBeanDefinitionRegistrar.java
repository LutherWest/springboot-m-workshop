package com.epam.springbootworkshop.quartz._2;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.core.type.AnnotationMetadata;

public class AutoConfigureQuartzJobImportBeanDefinitionRegistrar
        extends BasicQuartzJobImportBeanDefinitionRegistrar implements BeanFactoryAware {
    private BeanFactory beanFactory;

    @Override
    protected String[] getBasePackages(AnnotationMetadata metadata) {
        return AutoConfigurationPackages.get(this.beanFactory).toArray(new String[0]);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
