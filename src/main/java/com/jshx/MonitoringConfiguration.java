package com.jshx;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.AdminServlet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@EnableMetrics
public class MonitoringConfiguration extends MetricsConfigurerAdapter {

    @Autowired
    private MetricRegistry metricRegistry;

    @Autowired
    private HealthCheckRegistry healthCheckRegistry;

    @PostConstruct
    public void init() {
        configureReporters(metricRegistry);
    }

    @Bean
    public MetricsServletContextListener metricsServletContextListener(MetricRegistry metricRegistry, HealthCheckRegistry healthCheckRegistry) {
        return new MetricsServletContextListener(metricRegistry, healthCheckRegistry);
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        return new ServletRegistrationBean(new AdminServlet(),"/dropwizard/*");
    }

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        registerReporter(JmxReporter.forRegistry(metricRegistry).build()).start();
    }

}
