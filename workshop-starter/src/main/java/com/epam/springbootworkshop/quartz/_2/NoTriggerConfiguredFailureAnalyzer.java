package com.epam.springbootworkshop.quartz._2;

import org.quartz.Trigger;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

public class NoTriggerConfiguredFailureAnalyzer extends AbstractFailureAnalyzer<NoTriggerConfiguredException> {
    private static final String DESCRIPTION = "There is no configured " + Trigger.class.getName() + " in application.";
    private static final String ACTION = "- Disable scheduling auto configuration by setting scheduling.enabled=false\n"
            + "- Create a @Bean for " + Trigger.class.getSimpleName() + ", "
            + SimpleTriggerFactoryBean.class.getSimpleName() + " or " + CronTriggerFactoryBean.class.getSimpleName()
            + "\n"
            + "- Create " + QuartzJobBean.class.getSimpleName() +
            " annotated with @" + RepeatableQuartzJob.class.getSimpleName();

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, NoTriggerConfiguredException cause) {
        return new FailureAnalysis(DESCRIPTION, ACTION, cause);
    }
}

//public interface DeferredImportSelector extends ImportSelector {
//
//    @Override
//    String[] selectImports(AnnotationMetadata importingClassMetadata);
//
//    @Nullable
//    default Class<? extends Group> getImportGroup() {
//        return null;
//    }
//
//    interface Group {
//        //...
//    }
//}
