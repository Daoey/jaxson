package se.teknikhogskolan.jaxson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import se.teknikhogskolan.springcasemanagement.config.h2.H2InfrastructureConfig;
import se.teknikhogskolan.springcasemanagement.config.hsql.HsqlInfrastructureConfig;

@SpringBootApplication
@ComponentScan(basePackages = { "se.teknikhogskolan" }, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = { HsqlInfrastructureConfig.class,
                H2InfrastructureConfig.class }) })
public class JaxsonApplication {

    public static void main(String[] args) {
        SpringApplication.run(JaxsonApplication.class, args);
    }
}