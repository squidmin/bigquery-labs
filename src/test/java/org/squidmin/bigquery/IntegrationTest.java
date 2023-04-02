package org.squidmin.bigquery;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.squidmin.bigquery.config.BigQueryConfig;
import org.squidmin.bigquery.config.IntegrationTestConfig;
import org.squidmin.bigquery.fixture.BigQueryTestFixture;
import org.squidmin.bigquery.service.BigQueryAdminClient;
import org.squidmin.bigquery.util.BigQueryResourceMetadata;
import org.squidmin.bigquery.util.BigQueryUtil;
import org.squidmin.bigquery.logger.Logger;
import org.squidmin.bigquery.util.TableFactory;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { BigQueryAdminClient.class, IntegrationTestConfig.class })
@ActiveProfiles("integration")
@Slf4j
public abstract class IntegrationTest {

    @Autowired
    protected BigQueryConfig bqConfig;

    @Autowired
    protected TableFactory tableFactory;

    protected BigQueryAdminClient bigQueryAdminClient;

    protected String projectIdDefault, datasetNameDefault, tableNameDefault,
                     projectIdOverride, datasetNameOverride, tableNameOverride, schemaOverride;
    protected org.squidmin.bigquery.config.Schema schemaDefault;
    protected com.google.cloud.bigquery.Schema _schemaOverride;

    // The default values of these fields can be overridden by the values of CLI arguments.
    protected String PROJECT_ID, DATASET_NAME, TABLE_NAME;
    protected com.google.cloud.bigquery.Schema SCHEMA;

    protected BigQueryResourceMetadata bqResourceMetadata = null;

    @Before
    public void before() {
        initialize();
        bigQueryAdminClient = new BigQueryAdminClient(bqConfig);
        Logger.echoActiveProfileId();
    }

    private void initialize() {
        initializeBqResourcePropertyDefaultValues();
        initializeBqResourcePropertyOverriddenValues();
        initializeBqResourcePropertyMetadata();
        initializeBqResourceActiveProperties();
    }

    private void initializeBqResourcePropertyDefaultValues() {
        projectIdDefault = bqConfig.getProjectId();
        datasetNameDefault = bqConfig.getDatasetName();
        tableNameDefault = bqConfig.getTableName();
        schemaDefault = bqConfig.getSchema();
    }

    private void initializeBqResourcePropertyOverriddenValues() {
        projectIdOverride = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.projectId.name());
        datasetNameOverride = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.datasetName.name());
        tableNameOverride = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.tableName.name());
        schemaOverride = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.schema.name());
        if (null != schemaOverride) { _schemaOverride = BigQueryUtil.translate(schemaOverride); }
    }

    private void initializeBqResourcePropertyMetadata() {
        bqResourceMetadata = BigQueryResourceMetadata.builder()
            .projectId(setBqResourceProperty(projectIdDefault, projectIdOverride))
            .datasetName(setBqResourceProperty(datasetNameDefault, datasetNameOverride))
            .tableName(setBqResourceProperty(tableNameDefault, tableNameOverride))
            .schema(null != schemaOverride ? _schemaOverride : BigQueryUtil.translate(schemaDefault))
            .build();
    }

    private void initializeBqResourceActiveProperties() {
        PROJECT_ID = bqResourceMetadata.getProjectId();
        DATASET_NAME = bqResourceMetadata.getDatasetName();
        TABLE_NAME = bqResourceMetadata.getTableName();
        SCHEMA = bqResourceMetadata.getSchema();
    }

    private String setBqResourceProperty(String defaultValue, String overrideValue) {
        return null != overrideValue ? overrideValue : defaultValue;
    }

    protected void echoAppContextSummary() {
        Logger.echoBigQueryResourceMetadata(bqResourceMetadata, Logger.EchoOption.ACTIVE);
    }

}
