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

    private final String defaultProjectId, defaultDataset, defaultTable;
    private final String saProjectId, saDataset, saTable;

    private final BigQuery bq;

    private final BigQueryConfig bqConfig;

    @Autowired
    public BigQueryAdminClient(BigQueryConfig bqConfig) {
        this.bqConfig = bqConfig;
        this.bq = bqConfig.getBigQuery();
        this.defaultProjectId = bqConfig.getDefaultProjectId();
        this.defaultDataset = bqConfig.getDefaultDataset();
        this.defaultTable = bqConfig.getDefaultTable();
        this.saProjectId = bqConfig.getSaProjectId();
        this.saDataset = bqConfig.getSaDataset();
        this.saTable = bqConfig.getSaTable();
    }

    public void listDatasets() {
        try {
            BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
            Page<Dataset> datasets = bigquery.listDatasets(defaultProjectId, BigQuery.DatasetListOption.pageSize(100));
            if (null == datasets) {
                Logger.log(String.format("Dataset \"%s\" does not contain any models.", defaultDataset), Logger.LogType.ERROR);
                return;
            }
            if (!datasets.hasNextPage()) {
                Logger.log(String.format("Project \"%s\" does not contain any datasets.", defaultProjectId), Logger.LogType.INFO);
                return;
            }
            datasets.iterateAll().forEach(
                dataset -> Logger.log(
                    String.format("Dataset ID: %s", dataset.getDatasetId()),
                    Logger.LogType.INFO
                )
            );
        } catch (BigQueryException e) {
            Logger.log(String.format("Project \"%s\" does not contain any datasets.", defaultProjectId), Logger.LogType.ERROR);
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
        }
    }

    public boolean createDataset() {
        return createDataset(defaultDataset);
    }

    public boolean createDataset(String datasetName) {
        try {
            DatasetInfo datasetInfo = DatasetInfo.newBuilder(datasetName).build();
            Dataset newDataset = bq.create(datasetInfo);
            String newDatasetName = newDataset.getDatasetId().getDataset();
            Logger.log(String.format("Dataset \"%s\" created successfully.", newDatasetName), Logger.LogType.INFO);
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

    public boolean createTable(String dataset, String table) {
        return createTable(dataset, table, BigQueryUtil.translate(bqConfig.getSchema()));
    }

    public boolean createTable(String dataset, String table, com.google.cloud.bigquery.Schema schema) {
        try {
            TableId tableId = TableId.of(dataset, table);
            TableDefinition tableDefinition = StandardTableDefinition.of(schema);
            TableInfo tableInfo = TableInfo.newBuilder(tableId, tableDefinition).build();
            Logger.logCreateTable(tableInfo);
            bq.create(tableInfo);
            Logger.log(String.format("Table \"%s\" created successfully.", table), Logger.LogType.INFO);
        } catch (BigQueryException e) {
            Logger.log(
                String.format("%s: Table \"%s\" was not created.", e.getClass().getName(), table),
                Logger.LogType.ERROR
            );
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
            return false;
        }
        return true;
    }

}
