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
import org.squidmin.bigquery.config.tables.sandbox.SchemaDefault;
import org.squidmin.bigquery.fixture.BigQueryTestFixture;
import org.squidmin.bigquery.logger.Logger;
import org.squidmin.bigquery.service.BigQueryAdminClient;
import org.squidmin.bigquery.util.BigQueryUtil;
import org.squidmin.bigquery.util.RunEnvironment;
import org.squidmin.bigquery.util.StringUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BigQueryAdminClient.class, IntegrationTestConfig.class})
@ActiveProfiles("integration")
@Slf4j
public abstract class IntegrationTest {

    @Autowired
    protected BigQueryConfig bqConfig;

    protected BigQueryAdminClient bigQueryAdminClient;

    protected String
        defaultProjectIdDefault, defaultDatasetDefault, defaultTableDefault,
        defaultProjectIdCliOverride, defaultDatasetCliOverride, defaultTableCliOverride,
        saProjectIdDefault, saDatasetDefault, saTableDefault,
        saProjectIdCliOverride, saDatasetCliOverride, saTableCliOverride,
        schemaOverrideString;

    protected SchemaDefault schemaDefault;
    protected com.google.cloud.bigquery.Schema _schemaOverride;

    // The default values of configured BigQuery resource properties can be overridden by the values of CLI arguments.
    protected String
        DEFAULT_PROJECT_ID, DEFAULT_DATASET, DEFAULT_TABLE,
        SA_PROJECT_ID, SA_DATASET, SA_TABLE;
    protected com.google.cloud.bigquery.Schema SCHEMA;

    protected RunEnvironment runEnvironment = RunEnvironment.builder().build();

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
        // Class-level initializers.
        defaultProjectIdDefault = bqConfig.getDefaultProjectId();
        defaultDatasetDefault = bqConfig.getDefaultDataset();
        defaultTableDefault = bqConfig.getDefaultTable();

        saProjectIdDefault = bqConfig.getSaProjectId();
        saDatasetDefault = bqConfig.getSaDataset();
        saTableDefault = bqConfig.getSaTable();

        schemaDefault = bqConfig.getSchemaDefault();

        // Set default run environment properties from Spring @Configuration classes.
        runEnvironment = RunEnvironment.builder()
            .defaultProjectIdDefault(bqConfig.getDefaultProjectId())
            .defaultDatasetDefault(bqConfig.getDefaultDataset())
            .defaultTableDefault(bqConfig.getDefaultTable())
            .saProjectId(bqConfig.getSaProjectId())
            .saDataset(bqConfig.getSaDataset())
            .saTable(bqConfig.getSaTable())
            .schema(BigQueryUtil.InlineSchemaTranslator.translate(bqConfig.getSchemaDefault(), bqConfig.getDataTypes()))
            .build();
    }

    private void initBqResourcePropertyOverriddenValues() {
        defaultProjectIdCliOverride = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.defaultProjectId.name());
        defaultDatasetCliOverride = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.defaultDataset.name());
        defaultTableCliOverride = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.defaultTable.name());

        saProjectIdCliOverride = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.saProjectId.name());
        saDatasetCliOverride = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.saDataset.name());
        saTableCliOverride = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.saTable.name());

        schemaOverrideString = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.schema.name());
        if (StringUtils.isNotEmpty(schemaOverrideString)) {
            _schemaOverride = BigQueryUtil.InlineSchemaTranslator.translate(schemaOverrideString, bqConfig.getDataTypes());
        }
    }

    private void initBqResourcePropertyMetadata() {
        // Run environment defaults.
        runEnvironment.setDefaultProjectIdDefault(defaultProjectIdDefault);
        runEnvironment.setDefaultDatasetDefault(defaultDatasetDefault);
        runEnvironment.setDefaultTableDefault(defaultTableDefault);

        runEnvironment.setSaProjectIdDefault(saProjectIdDefault);
        runEnvironment.setSaDatasetDefault(saDatasetDefault);
        runEnvironment.setSaTableDefault(saTableDefault);

        // Override default properties with values of CLI arguments.
        runEnvironment.setDefaultProjectId(setBqResourceProperty(defaultProjectIdDefault, defaultProjectIdCliOverride));
        runEnvironment.setDefaultDataset(setBqResourceProperty(defaultDatasetDefault, defaultDatasetCliOverride));
        runEnvironment.setDefaultTable(setBqResourceProperty(defaultTableDefault, defaultTableCliOverride));

        runEnvironment.setSaProjectId(setBqResourceProperty(saProjectIdDefault, saProjectIdCliOverride));
        runEnvironment.setSaDataset(setBqResourceProperty(saDatasetDefault, saDatasetCliOverride));
        runEnvironment.setSaTable(setBqResourceProperty(saTableDefault, saTableCliOverride));

        // Set table schema in the run environment.
        runEnvironment.setSchema(
            StringUtils.isNotEmpty(schemaOverrideString) ?
                _schemaOverride :
                BigQueryUtil.InlineSchemaTranslator.translate(schemaDefault, bqConfig.getDataTypes())
        );
    }

    private void initBqResourceActiveProperties() {
        // Set integration test class level variables for active run environment.
        DEFAULT_PROJECT_ID = runEnvironment.getDefaultProjectId();
        DEFAULT_DATASET = runEnvironment.getDefaultDataset();
        DEFAULT_TABLE = runEnvironment.getDefaultTable();

        SA_PROJECT_ID = runEnvironment.getSaProjectId();
        SA_DATASET = runEnvironment.getSaDataset();
        SA_TABLE = runEnvironment.getSaTable();

        SCHEMA = runEnvironment.getSchema();
    }

    private String setBqResourceProperty(String defaultValue, String overrideValue) {
        return null != overrideValue ? overrideValue : defaultValue;
    }

}
