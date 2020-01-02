package com.epam.springbootworkshop.quartz._3;

import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;
import java.util.ServiceLoader;

public class Runner {
    public static void main(String[] args) {
//        ServiceLoader.load(GreetingService.class)
//                .findFirst()
//                .ifPresent(GreetingService::hello);
        ClassLoader cl = Runner.class.getClassLoader();
        List<GreetingService> services =
                SpringFactoriesLoader.loadFactories(GreetingService.class, cl);
        services.forEach(GreetingService::hello);
    }
}
