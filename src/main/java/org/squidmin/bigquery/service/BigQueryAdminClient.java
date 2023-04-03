package org.squidmin.bigquery.service;

import com.google.api.gax.paging.Page;
import com.google.cloud.bigquery.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.squidmin.bigquery.config.BigQueryConfig;
import org.squidmin.bigquery.config.DataTypes;
import org.squidmin.bigquery.config.Schema;
import org.squidmin.bigquery.logger.Logger;
import org.squidmin.bigquery.util.BigQueryUtil;

@Service
@EnableConfigurationProperties(value = {Schema.class, DataTypes.class})
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
                Logger.log("Dataset does not contain any models.", Logger.LogType.ERROR);
                return;
            }
            datasets.iterateAll().forEach(
                dataset -> Logger.log(
                    String.format("Dataset ID: %s", dataset.getDatasetId()),
                    Logger.LogType.INFO
                )
            );
        } catch (BigQueryException e) {
            Logger.log("Project does not contain any datasets.", Logger.LogType.ERROR);
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
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
            Logger.log(String.format("Dataset %s created successfully.", newDatasetName), Logger.LogType.INFO);
        } catch (BigQueryException e) {
            Logger.log(
                String.format("%s: Dataset \"%s\" was not created.", e.getClass().getName(), datasetName),
                Logger.LogType.ERROR
            );
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
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
            Logger.logCreateTable(tableInfo);
            bq.create(tableInfo);
            Logger.log("Table created successfully.", Logger.LogType.INFO);
        } catch (BigQueryException e) {
            Logger.log(
                String.format("%s: Table \"%s\" was not created.", e.getClass().getName(), tableName),
                Logger.LogType.ERROR
            );
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
            return false;
        }
        return true;
    }

}
