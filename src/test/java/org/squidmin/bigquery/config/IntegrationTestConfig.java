package org.squidmin.bigquery.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.squidmin.bigquery.util.TableFactory;

@Configuration
@Profile("integration")
@Slf4j
public class IntegrationTestConfig {

    @Value("${bigquery.projectId}")
    private String projectId;

    @Value("${bigquery.datasetName}")
    private String datasetName;

    @Value("${bigquery.tableName}")
    private String tableName;

    @Autowired
    private Schema schema;

    private BigQueryConfig bqConfig;

    @Bean
    public BigQueryConfig bigQueryConfig() {
        bqConfig = new BigQueryConfig(projectId, datasetName, tableName, schema);
        return bqConfig;
    }

    @Bean
    public Schema schema() {
        log.info("schema == {}", schema.getFields());
        return schema;
    }

    @Bean
    public TableFactory tableFactory() {
        return new TableFactory(bqConfig);
    }

}
