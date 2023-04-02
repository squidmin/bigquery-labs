package org.squidmin.bigquery.logger;

import lombok.extern.slf4j.Slf4j;
import org.squidmin.bigquery.util.BigQueryResourceMetadata;

@Slf4j
public class Logger {

    public static void echoActiveProfileId() {
        log.info(LogFont.boldGreen(String.format("Active profile ID: %s", System.getProperty("profileId"))));
    }

    public static void echoBigQueryResourceMetadata(String projectId, String datasetName, String tableName) {
        log.info(LogFont.boldGreen(String.format("Project ID: %s", projectId)));
        log.info(LogFont.boldGreen(String.format("Dataset name: %s", datasetName)));
        log.info(LogFont.boldGreen(String.format("Table name: %s", tableName)));
    }

    public enum EchoOption { DEFAULT, OVERRIDDEN, ACTIVE }
    public static void echoBigQueryResourceMetadata(BigQueryResourceMetadata bqResourceMetadata, EchoOption echoOption) {
        log.info(LogFont.boldGreen("-".repeat(50)));
        if (echoOption == EchoOption.DEFAULT) {
            log.info(LogFont.boldGreen("--- BigQuery metadata: Default values ---"));
            Logger.echoBigQueryResourceMetadata(
                bqResourceMetadata.getProjectIdDefault(),
                bqResourceMetadata.getDatasetNameDefault(),
                bqResourceMetadata.getTableNameDefault()
            );
        } else if (echoOption == EchoOption.OVERRIDDEN) {
            log.info(LogFont.boldGreen("--- BigQuery metadata: Overridden values ---"));
            Logger.echoBigQueryResourceMetadata(
                bqResourceMetadata.getProjectIdOverride(),
                bqResourceMetadata.getDatasetNameOverride(),
                bqResourceMetadata.getTableNameOverride()
            );
        } else if (echoOption == EchoOption.ACTIVE) {
            log.info(LogFont.boldGreen("The following BigQuery resource properties are configured to be utilized:"));
            Logger.echoBigQueryResourceMetadata(
                bqResourceMetadata.getProjectId(),
                bqResourceMetadata.getDatasetName(),
                bqResourceMetadata.getTableName()
            );
        } else {
            log.error("Error: IntegrationTest.echoBigQueryResourceMetadata(): Invalid option specified.");
        }
        log.info(LogFont.boldGreen("-".repeat(50)));
    }

}
