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

    // The default values of configured BigQuery resource properties can be overridden by the values of CLI arguments.
    protected String PROJECT_ID, DATASET_NAME, TABLE_NAME;
    protected com.google.cloud.bigquery.Schema SCHEMA;

    protected BigQueryResourceMetadata bqResourceMetadata = BigQueryResourceMetadata.builder().build();

    @Before
    public void before() {
        initialize();
        bigQueryAdminClient = new BigQueryAdminClient(bqConfig);
        Logger.log(String.format("Active profile ID: %s", System.getProperty("profileId")), Logger.LogType.CYAN);
    }

    private void initialize() {
        initBqResourcePropertyDefaultValues();
        initBqResourcePropertyOverriddenValues();
        initBqResourcePropertyMetadata();
        initBqResourceActiveProperties();
    }

    private void initBqResourcePropertyDefaultValues() {
        projectIdDefault = bqConfig.getProjectId();
        datasetNameDefault = bqConfig.getDatasetName();
        tableNameDefault = bqConfig.getTableName();
        schemaDefault = bqConfig.getSchema();
        bqResourceMetadata = BigQueryResourceMetadata.builder()
            .projectIdDefault(bqConfig.getProjectId())
            .datasetNameDefault(bqConfig.getDatasetName())
            .tableNameDefault(bqConfig.getTableName())
            .schema(BigQueryUtil.translate(bqConfig.getSchema()))
            .build();
    }

    private void initBqResourcePropertyOverriddenValues() {
        projectIdOverride = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.projectId.name());
        datasetNameOverride = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.datasetName.name());
        tableNameOverride = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.tableName.name());
        schemaOverride = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.schema.name());
        if (null != schemaOverride) { _schemaOverride = BigQueryUtil.translate(schemaOverride); }
    }

    private void initBqResourcePropertyMetadata() {
        bqResourceMetadata.setProjectId(setBqResourceProperty(projectIdDefault, projectIdOverride));
        bqResourceMetadata.setDatasetName(setBqResourceProperty(datasetNameDefault, datasetNameOverride));
        bqResourceMetadata.setTableName(setBqResourceProperty(tableNameDefault, tableNameOverride));
        bqResourceMetadata.setSchema(null != schemaOverride ? _schemaOverride : BigQueryUtil.translate(schemaDefault));
    }

    private void initBqResourceActiveProperties() {
        PROJECT_ID = bqResourceMetadata.getProjectId();
        DATASET_NAME = bqResourceMetadata.getDatasetName();
        TABLE_NAME = bqResourceMetadata.getTableName();
        SCHEMA = bqResourceMetadata.getSchema();
    }

    private String setBqResourceProperty(String defaultValue, String overrideValue) {
        return null != overrideValue ? overrideValue : defaultValue;
    }

}
