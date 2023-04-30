package org.squidmin.bigquery.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.squidmin.bigquery.config.tables.sandbox.SchemaDefault;
import org.squidmin.bigquery.config.tables.sandbox.SelectFieldsDefault;
import org.squidmin.bigquery.config.tables.sandbox.WhereFieldsDefault;

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
    private SchemaDefault schemaDefault;

    @Autowired
    private DataTypes dataTypes;

    @Autowired
    private SelectFieldsDefault selectFieldsDefault;

    @Autowired
    private WhereFieldsDefault whereFieldsDefault;

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
    public SchemaDefault schema() { return schemaDefault; }

    @Bean
    public DataTypes dataTypes() {
        return dataTypes;
    }

    @Bean
    public SelectFieldsDefault selectFields() { return selectFieldsDefault; }

    @Bean
    public WhereFieldsDefault whereFields() { return whereFieldsDefault; }

    @Bean
    public BigQueryConfig bigQueryConfig() {
        bqConfig = new BigQueryConfig(
            defaultProjectId,
            defaultDataset,
            defaultTable,
            saProjectId,
            saDataset,
            saTable,
            schemaDefault,
            dataTypes,
            selectFieldsDefault,
            whereFieldsDefault
        );
        return bqConfig;
    }

}
