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
    public void echoBigQueryResourceConfig() {
        BigQueryUtil.logBqProperties(runEnvironment, BigQueryUtil.ProfileOption.DEFAULT);
    }

    @Test
    public void listDatasets() {
        bqAdminClient.listDatasets();
    }

    @Test
    public void createDataset() {
        bqAdminClient.createDataset(GCP_DEFAULT_USER_DATASET);
    }

    @Test
    public void deleteDataset() {
        bqAdminClient.deleteDataset(GCP_DEFAULT_USER_PROJECT_ID, GCP_DEFAULT_USER_DATASET);
    }

    @Test
    public void createTableWithDefaultSchema() {
        BigQueryUtil.logBqProperties(runEnvironment, BigQueryUtil.ProfileOption.ACTIVE);
        Assertions.assertTrue(
            bqAdminClient.createTable(GCP_DEFAULT_USER_DATASET, GCP_DEFAULT_USER_TABLE)
        );
    }

    @Test
    public void createTableWithCustomSchema() {
        BigQueryUtil.logBqProperties(runEnvironment, BigQueryUtil.ProfileOption.ACTIVE);
        Assertions.assertTrue(
            bqAdminClient.createTable(
                GCP_DEFAULT_USER_DATASET,
                GCP_DEFAULT_USER_TABLE,
                BigQueryUtil.InlineSchemaTranslator.translate(schemaOverrideString, bqConfig.getDataTypes())
            )
        );
    }

    @Test
    public void deleteTable() {
        bqAdminClient.deleteTable(
            GCP_DEFAULT_USER_PROJECT_ID,
            GCP_DEFAULT_USER_DATASET,
            GCP_DEFAULT_USER_TABLE
        );
    }

    @Test
    public void insert() {
        List<InsertAllRequest.RowToInsert> rowsInserted = bqAdminClient.insert(
            GCP_DEFAULT_USER_PROJECT_ID,
            GCP_DEFAULT_USER_DATASET,
            GCP_DEFAULT_USER_TABLE,
            BigQueryTestFixture.DEFAULT_ROWS.get()
        );
        Assertions.assertTrue(0 < rowsInserted.size());
        rowsInserted.forEach(row -> Logger.log(String.valueOf(row), Logger.LogType.INFO));
    }

}
