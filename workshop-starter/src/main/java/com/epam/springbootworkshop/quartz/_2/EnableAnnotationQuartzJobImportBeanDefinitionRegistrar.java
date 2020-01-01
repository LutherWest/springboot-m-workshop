package com.epam.springbootworkshop.quartz._2;

import java.util.Optional;

import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

public class EnableAnnotationQuartzJobImportBeanDefinitionRegistrar
        extends BasicQuartzJobImportBeanDefinitionRegistrar {
    @Override
    protected String[] getBasePackages(AnnotationMetadata metadata) {
        String annotationName = EnableRepeatableQuartzJob.class.getName();
        AnnotationAttributes attrs = Optional.ofNullable(metadata.getAnnotationAttributes(annotationName))
                .map(AnnotationAttributes::new)
                .orElseThrow(() -> new IllegalStateException(String.format("Unable to obtain annotation " +
                        "attributes for %s!", annotationName)));

        return Optional.of(attrs.getStringArray("basePackages"))
                .filter(array -> array.length > 0)
                .orElseGet(() -> new String[] { ClassUtils.getPackageName(metadata.getClassName()) });
    }
}
