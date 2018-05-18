package com.cyreno.config;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IgniteConfig {

    @Bean
    public Ignite igniteInstance() {

        IgniteConfiguration configuration = new IgniteConfiguration();
        configuration.setIgniteInstanceName("ignite-1");
        configuration.setPeerClassLoadingEnabled(true);

        return Ignition.start(configuration);

    }

}