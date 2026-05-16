package com.gasfgrv.graphql.demo_graphql;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.Configuration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class DemoGraphqlApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() throws SQLException {
        Set<String> beans = Arrays.stream(context.getBeanDefinitionNames()).collect(Collectors.toSet());
        assertThat(beans).contains("flyway");

        Map<String, Flyway> beansOfType = context.getBeansOfType(Flyway.class);
        assertThat(beansOfType).hasSize(1).containsKey("flyway");

        String dbName = "PostgreSQL";
        Flyway flyway = (Flyway) context.getBean("flyway");

        Configuration flywayConfiguration = flyway.getConfiguration();
        assertThat(flywayConfiguration.getDatabaseType().getName()).containsIgnoringCase(dbName);

        Connection connection = flywayConfiguration.getDataSource().getConnection();
        assertThat(connection.getClientInfo().entrySet()).hasSize(1);
        assertThat(connection.getMetaData().getURL()).containsIgnoringCase(dbName);
    }

}
