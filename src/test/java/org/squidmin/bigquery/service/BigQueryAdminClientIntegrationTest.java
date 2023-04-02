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
        Logger.echoBigQueryResourceMetadata(bqResourceMetadata, Logger.EchoOption.DEFAULT);
    }

    @Test
    public void listDatasets() {
        bigQueryAdminClient.listDatasets(PROJECT_ID);
    }

    @Test
    public void createDataset() {
        bigQueryAdminClient.createDataset(DATASET_NAME);
    }

    @Test
    public void createTableWithDefaultSchema() {
        echoAppContextSummary();
        Assertions.assertTrue(
            bigQueryAdminClient.createTable(DATASET_NAME, TABLE_NAME)
        );
    }

    @Test
    public void createTableWithCustomSchema() {
        echoAppContextSummary();
        Assertions.assertTrue(
            bigQueryAdminClient.createTable(
                DATASET_NAME,
                TABLE_NAME,
                BigQueryUtil.translate(schemaOverride)
            )
        );
    }



}
