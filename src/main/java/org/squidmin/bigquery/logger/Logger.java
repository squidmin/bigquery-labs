package org.squidmin.bigquery.logger;

import com.google.cloud.bigquery.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.squidmin.bigquery.util.RunEnvironment;

@Slf4j
public class Logger {

    public static short HORIZONTAL_LINE_WIDTH = 50;

    public enum LogType { INFO, DEBUG, ERROR, CYAN }
    public static void log(String str, LogType logType) {
        if (logType == LogType.INFO) {
            str = LogFont.boldGreen(str);
            log.info(str);
        } else if (logType == LogType.DEBUG) {
            str = LogFont.bold(str);
            log.debug(str);
        } else if (logType == LogType.ERROR) {
            log.error(str);
        } else if (logType == LogType.CYAN) {
            str = LogFont.boldCyan(str);
            log.info(str);
        } else {
            log.error("Error: Logger: Invalid LogType.");
        }
    }

    public static void logTableInfo(TableInfo tableInfo) {
        log.info("Friendly name: " + tableInfo.getFriendlyName());
        log.info("Description: " + tableInfo.getDescription());
        log.info("Creation time: " + tableInfo.getCreationTime());
        log.info("Expiration time: " + tableInfo.getExpirationTime());
    }

    public static void logCreateTable(TableInfo tableInfo) {
        echoHorizontalLine(Logger.LogType.INFO);
        log(
            String.format("Creating table \"%s\". Find the table information below:", tableInfo.getTableId()),
            Logger.LogType.INFO
        );
        logTableInfo(tableInfo);
        echoHorizontalLine(Logger.LogType.INFO);
    }

    public static void echoRunEnvironment(
        String defaultProjectId, String defaultDataset, String defaultTable,
        String saProjectId, String saDataset, String saTable) {

        log(String.format("Default Project ID: %s", defaultProjectId), LogType.INFO);
        log(String.format("Default Dataset name: %s", defaultDataset), LogType.INFO);
        log(String.format("Default Table name: %s", defaultTable), LogType.INFO);

        log(String.format("Service account Project ID: %s", saProjectId), LogType.INFO);
        log(String.format("Service account Dataset name: %s", saDataset), LogType.INFO);
        log(String.format("Service account Table name: %s", saTable), LogType.INFO);

    }

    public enum ProfileOption { DEFAULT, OVERRIDDEN, ACTIVE }
    public static void echoRunEnvironment(RunEnvironment runEnvironment, ProfileOption profileOption) {
        echoHorizontalLine(LogType.INFO);
        if (profileOption == ProfileOption.DEFAULT) {
            log("--- BigQuery default properties ---", LogType.CYAN);
            echoRunEnvironment(
                runEnvironment.getDefaultProjectIdDefault(),
                runEnvironment.getDefaultDatasetDefault(),
                runEnvironment.getDefaultTableDefault(),
                runEnvironment.getSaProjectIdDefault(),
                runEnvironment.getSaDatasetDefault(),
                runEnvironment.getSaTableDefault()
            );
        } else if (profileOption == ProfileOption.OVERRIDDEN) {
            log("--- BigQuery overridden properties ---", LogType.CYAN);
            echoRunEnvironment(
                runEnvironment.getDefaultProjectIdOverride(),
                runEnvironment.getDefaultDatasetOverride(),
                runEnvironment.getDefaultTableOverride(),
                runEnvironment.getSaProjectIdOverride(),
                runEnvironment.getSaDatasetOverride(),
                runEnvironment.getSaTableOverride()
            );
        } else if (profileOption == ProfileOption.ACTIVE) {
            log("BigQuery resource properties currently configured:", LogType.CYAN);
            echoRunEnvironment(
                runEnvironment.getDefaultProjectId(),
                runEnvironment.getDefaultDataset(),
                runEnvironment.getDefaultTable(),
                runEnvironment.getSaProjectId(),
                runEnvironment.getSaDataset(),
                runEnvironment.getSaTable()
            );
        } else {
            log.error("Error: IntegrationTest.echoBigQueryResourceMetadata(): Invalid option specified.");
        }
        echoHorizontalLine(LogType.INFO);
    }

    public static void echoHorizontalLine(LogType logType) {
        log("-".repeat(HORIZONTAL_LINE_WIDTH), logType);
    }

}
