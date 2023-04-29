package org.squidmin.bigquery.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.squidmin.bigquery.IntegrationTest;
import org.squidmin.bigquery.util.BigQueryUtil;
import org.squidmin.bigquery.logger.Logger;

@Slf4j
public class BigQueryAdminClientIntegrationTest extends IntegrationTest {

    @Test
    public void echoDefaultBigQueryResourceMetadata() {
        Logger.echoRunEnvironment(runEnvironment, Logger.ProfileOption.DEFAULT);
    }

    @Test
    public void listDatasets() { bigQueryAdminClient.listDatasets(); }

    @Test
    public void createDataset() {
        bigQueryAdminClient.createDataset(DEFAULT_DATASET);
    }

    @Test
    public void createTableWithDefaultSchema() {
        Logger.echoRunEnvironment(runEnvironment, Logger.ProfileOption.ACTIVE);
        Assertions.assertTrue(
            bigQueryAdminClient.createTable(DEFAULT_DATASET, DEFAULT_TABLE)
        );
    }

    @Test
    public void createTableWithCustomSchema() {
        Logger.echoRunEnvironment(runEnvironment, Logger.ProfileOption.ACTIVE);
        Assertions.assertTrue(
            bigQueryAdminClient.createTable(
                DEFAULT_DATASET,
                DEFAULT_TABLE,
                BigQueryUtil.translate(schemaOverrideString)
            )
        );
    }

}
