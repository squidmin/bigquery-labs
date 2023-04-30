package org.squidmin.bigquery.service;

import com.google.cloud.bigquery.InsertAllRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.squidmin.bigquery.IntegrationTest;
import org.squidmin.bigquery.fixture.BigQueryTestFixture;
import org.squidmin.bigquery.logger.Logger;
import org.squidmin.bigquery.util.BigQueryUtil;

import java.util.List;

@Slf4j
public class BigQueryAdminClientIntegrationTest extends IntegrationTest {

    @Test
    public void echoDefaultBigQueryResourceMetadata() {
        BigQueryUtil.echoRunEnvironment(runEnvironment, BigQueryUtil.ProfileOption.DEFAULT);
    }

    @Test
    public void listDatasets() {
        bigQueryAdminClient.listDatasets();
    }

    @Test
    public void createDataset() {
        bigQueryAdminClient.createDataset(DEFAULT_DATASET);
    }

    @Test
    public void deleteDataset() {
        bigQueryAdminClient.deleteDataset(DEFAULT_PROJECT_ID, DEFAULT_DATASET);
    }

    @Test
    public void createTableWithDefaultSchema() {
        BigQueryUtil.echoRunEnvironment(runEnvironment, BigQueryUtil.ProfileOption.ACTIVE);
        Assertions.assertTrue(
            bigQueryAdminClient.createTable(DEFAULT_DATASET, DEFAULT_TABLE)
        );
    }

    @Test
    public void createTableWithCustomSchema() {
        BigQueryUtil.echoRunEnvironment(runEnvironment, BigQueryUtil.ProfileOption.ACTIVE);
        Assertions.assertTrue(
            bigQueryAdminClient.createTable(
                DEFAULT_DATASET,
                DEFAULT_TABLE,
                BigQueryUtil.InlineSchemaTranslator.translate(schemaOverrideString, bqConfig.getDataTypes())
            )
        );
    }

    @Test
    public void deleteTable() {
        bigQueryAdminClient.deleteTable(
            runEnvironment.getDefaultProjectId(),
            runEnvironment.getDefaultDataset(),
            runEnvironment.getDefaultTable()
        );
    }

    @Test
    public void insert() {
        List<InsertAllRequest.RowToInsert> rowsInserted = bigQueryAdminClient.insert(BigQueryTestFixture.DEFAULT_ROWS.get());
        rowsInserted.forEach(row -> Logger.log(String.valueOf(row), Logger.LogType.INFO));
    }

}
