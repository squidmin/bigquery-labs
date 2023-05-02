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
        Logger.echoHorizontalLine(Logger.LogType.CYAN);
        Logger.log("Run environment CLI arguments", Logger.LogType.CYAN);
        Logger.echoHorizontalLine(Logger.LogType.CYAN);
        Logger.log(String.format("PROFILE                         %s", System.getProperty("PROFILE")), Logger.LogType.CYAN);
        Logger.log(String.format("GCP_SA_KEY_PATH                 %s", System.getProperty("GCP_SA_KEY_PATH")), Logger.LogType.CYAN);
        Logger.log(String.format("GCP_ADC_ACCESS_TOKEN            %s", System.getProperty("GCP_ADC_ACCESS_TOKEN")), Logger.LogType.CYAN);
        Logger.log(String.format("GCP_SA_ACCESS_TOKEN             %s", System.getProperty("GCP_SA_ACCESS_TOKEN")), Logger.LogType.CYAN);
        Logger.log(String.format("GCP_DEFAULT_USER_PROJECT_ID     %s", System.getProperty("GCP_DEFAULT_USER_PROJECT_ID")), Logger.LogType.CYAN);
        Logger.log(String.format("GCP_DEFAULT_USER_DATASET        %s", System.getProperty("GCP_DEFAULT_USER_DATASET")), Logger.LogType.CYAN);
        Logger.log(String.format("GCP_DEFAULT_USER_TABLE          %s", System.getProperty("GCP_DEFAULT_USER_TABLE")), Logger.LogType.CYAN);
        Logger.log(String.format("GCP_SA_PROJECT_ID               %s", System.getProperty("GCP_SA_PROJECT_ID")), Logger.LogType.CYAN);
        Logger.log(String.format("GCP_SA_DATASET                  %s", System.getProperty("GCP_SA_DATASET")), Logger.LogType.CYAN);
        Logger.log(String.format("GCP_SA_TABLE                    %s", System.getProperty("GCP_SA_TABLE")), Logger.LogType.CYAN);
        Logger.echoHorizontalLine(Logger.LogType.CYAN);
    }

    private void initialize() {
        initRunEnvironmentDefaultValues();
        initRunEnvironmentOverriddenValues();
        initRunEnvironmentMetadata();
        initRunEnvironmentActiveProperties();
    }

    private void initRunEnvironmentDefaultValues() {
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

    private void initRunEnvironmentOverriddenValues() {
        defaultProjectIdCliOverride = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.GCP_DEFAULT_USER_PROJECT_ID.name());
        defaultDatasetCliOverride = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.GCP_DEFAULT_USER_DATASET.name());
        defaultTableCliOverride = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.GCP_DEFAULT_USER_TABLE.name());

        saProjectIdCliOverride = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.GCP_SA_PROJECT_ID.name());
        saDatasetCliOverride = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.GCP_SA_DATASET.name());
        saTableCliOverride = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.GCP_SA_TABLE.name());

        schemaOverrideString = System.getProperty(BigQueryTestFixture.CLI_ARG_KEYS.SCHEMA.name());
        if (StringUtils.isNotEmpty(schemaOverrideString)) {
            _schemaOverride = BigQueryUtil.InlineSchemaTranslator.translate(schemaOverrideString, bqConfig.getDataTypes());
        }
    }

    private void initRunEnvironmentMetadata() {
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

    private void initRunEnvironmentActiveProperties() {
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
