package org.squidmin.bigquery.service;

import com.google.api.gax.paging.Page;
import com.google.cloud.bigquery.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.squidmin.bigquery.config.BigQueryConfig;
import org.squidmin.bigquery.config.Schema;
import org.squidmin.bigquery.util.BigQueryUtil;

@Service
@EnableConfigurationProperties(value = {Schema.class})
@Slf4j
public class BigQueryAdminClient {

    private String projectId, datasetName, tableName;

    private final BigQuery bq;

    private final BigQueryConfig bqConfig;

    @Autowired
    public BigQueryAdminClient(BigQueryConfig bqConfig) {
        this.bqConfig = bqConfig;
        this.bq = bqConfig.getBigQuery();
        this.projectId = bqConfig.getProjectId();
        this.datasetName = bqConfig.getDatasetName();
        this.tableName = bqConfig.getTableName();
    }

    public void listDatasets() {
        listDatasets(projectId);
    }

    public void listDatasets(String projectId) {
        try {
            BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
            Page<Dataset> datasets = bigquery.listDatasets(projectId, BigQuery.DatasetListOption.pageSize(100));
            if (datasets == null) {
                log.info("Dataset does not contain any models.");
                return;
            }
            datasets.iterateAll()
                .forEach(dataset -> log.info("Dataset ID: {}", dataset.getDatasetId()));
        } catch (BigQueryException e) {
            log.info("Project does not contain any datasets.");
            log.info(e.getMessage());
        }
    }

    public boolean createDataset() {
        return createDataset(datasetName);
    }

    public boolean createDataset(String datasetName) {
        try {
            DatasetInfo datasetInfo = DatasetInfo.newBuilder(datasetName).build();
            Dataset newDataset = bq.create(datasetInfo);
            String newDatasetName = newDataset.getDatasetId().getDataset();
            log.info("Dataset " + newDatasetName + " created successfully.");
        } catch (BigQueryException e) {
            log.info(e.getClass().getName() + ": Dataset \"" + datasetName + "\" was not created.");
            log.info(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean createTable(String datasetName, String tableName) {
        return createTable(datasetName, tableName, BigQueryUtil.translate(bqConfig.getSchema()));
    }

    public boolean createTable(String datasetName, String tableName, com.google.cloud.bigquery.Schema schema) {
        try {
            TableId tableId = TableId.of(datasetName, tableName);
            TableDefinition tableDefinition = StandardTableDefinition.of(schema);
            TableInfo tableInfo = TableInfo.newBuilder(tableId, tableDefinition).build();
            log.info("-".repeat(50));
            log.info("Creating table \"" + tableInfo.getTableId() + "\". Find the table information below:");
            BigQueryUtil.logTableInfo(tableInfo);
            log.info("-".repeat(50));
            bq.create(tableInfo);
            log.info("Table created successfully.");
        } catch (BigQueryException e) {
            log.info(e.getClass().getName() + ": Table \"" + tableName + "\" was not created.");
            log.info(e.getMessage());
            return false;
        }
        return true;
    }

}
