package org.squidmin.bigquery.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

@Configuration
@Profile("integration")
@Slf4j
public class IntegrationTestConfig {

    @Value("${bigquery.application-default.project-id}")
    private String defaultProjectId;

    @Value("${bigquery.application-default.dataset}")
    private String defaultDataset;

    @Value("${bigquery.application-default.table}")
    private String defaultTable;

    @Value("${bigquery.service-account.project-id}")
    private String saProjectId;

    @Value("${bigquery.service-account.dataset}")
    private String saDataset;

    @Value("${bigquery.service-account.table}")
    private String saTable;

    @Autowired
    private Schema schema;

    @Autowired
    private DataTypes dataTypes;

    private BigQueryConfig bqConfig;

    @Bean
    public String defaultProjectId() { return defaultProjectId; }

    @Bean
    public String defaultDataset() { return defaultDataset; }

    @Bean
    public String defaultTable() { return defaultTable; }

    @Bean
    public String saProjectId() { return saProjectId; }

    @Bean
    public String saDataset() { return saDataset; }

    @Bean
    public String saTable() { return saTable; }

    @Bean
    public Schema schema() { return schema; }

    @Bean
    public DataTypes dataTypes() {
        return dataTypes;
    }

    @Bean
    public BigQueryConfig bigQueryConfig() throws IOException {
        bqConfig = new BigQueryConfig(
            defaultProjectId,
            defaultDataset,
            defaultTable,
            saProjectId,
            saDataset,
            saTable,
            schema,
            dataTypes
        );
        return bqConfig;
    }

}
