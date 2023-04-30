package org.squidmin.bigquery.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.paging.Page;
import com.google.cloud.bigquery.*;
import lombok.extern.slf4j.Slf4j;
import org.squidmin.bigquery.config.DataTypes;
import org.squidmin.bigquery.config.tables.sandbox.SchemaDefault;
import org.squidmin.bigquery.config.tables.sandbox.SelectFieldsDefault;
import org.squidmin.bigquery.dto.ResponseExample;
import org.squidmin.bigquery.logger.Logger;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class BigQueryUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<ResponseExample> toList(byte[] tableResult, SelectFieldsDefault selectFieldsDefault) throws IOException {
        List<ResponseExample> response = new ArrayList<>();
        Map _schema = mapper.readValue(tableResult, Map.class);
        ArrayList rows = (ArrayList) _schema.get("rows");
        if (null != rows) {
            for (int i = 0; i < rows.size(); i++) {
                ResponseExample r = new ResponseExample();
                Map row = (Map) rows.get(i);
                ArrayList f = (ArrayList) row.get("f");
                List<String> _selectFields = selectFieldsDefault.getFields();
                for (int j = 0; j < _selectFields.size(); j++) {
                    String name = _selectFields.get(j);
                    Map v = (Map) f.get(j);
                    String value = (String) v.get("v");
                    if (_selectFields.contains(name)) { r.setFromBigQueryResponse(name, value); }
                }
                response.add(r);
            }
        }
        return response;
    }

    public static void logDataTypes(DataTypes dataTypes) {
        Logger.log("Supported data types: ", Logger.LogType.CYAN);
        dataTypes.getDataTypes().forEach(type -> Logger.log(type, Logger.LogType.CYAN));
    }

    public static void logTableInfo(TableInfo tableInfo) {
        log.info("Friendly name: " + tableInfo.getFriendlyName());
        log.info("Description: " + tableInfo.getDescription());
        log.info("Creation time: " + tableInfo.getCreationTime());
        log.info("Expiration time: " + tableInfo.getExpirationTime());
    }

    public static void logDatasets(String projectId, Page<Dataset> datasets) {
        Logger.echoHorizontalLine(Logger.LogType.INFO);
        Logger.log(String.format("Project \"%s\" datasets:", projectId), Logger.LogType.INFO);
        Logger.echoHorizontalLine(Logger.LogType.INFO);
        datasets.iterateAll().forEach(
            dataset -> Logger.log(
                String.format("Dataset ID: %s", dataset.getDatasetId()),
                Logger.LogType.INFO
            )
        );
        Logger.echoHorizontalLine(Logger.LogType.INFO);
    }

    public static void logCreateTable(TableInfo tableInfo) {
        Logger.echoHorizontalLine(Logger.LogType.INFO);
        Logger.log(
            String.format("Creating table \"%s\". Find the table information below:", tableInfo.getTableId()),
            Logger.LogType.INFO
        );
        logTableInfo(tableInfo);
        Logger.echoHorizontalLine(Logger.LogType.INFO);
    }

    public static void echoRunEnvironment(
        String defaultProjectId, String defaultDataset, String defaultTable,
        String saProjectId, String saDataset, String saTable) {

        Logger.log(String.format("Default Project ID: %s", defaultProjectId), Logger.LogType.INFO);
        Logger.log(String.format("Default Dataset name: %s", defaultDataset), Logger.LogType.INFO);
        Logger.log(String.format("Default Table name: %s", defaultTable), Logger.LogType.INFO);

        Logger.log(String.format("Service account Project ID: %s", saProjectId), Logger.LogType.INFO);
        Logger.log(String.format("Service account Dataset name: %s", saDataset), Logger.LogType.INFO);
        Logger.log(String.format("Service account Table name: %s", saTable), Logger.LogType.INFO);

    }

    public enum ProfileOption { DEFAULT, OVERRIDDEN, ACTIVE }
    public static void echoRunEnvironment(RunEnvironment runEnvironment, ProfileOption profileOption) {
        Logger.echoHorizontalLine(Logger.LogType.INFO);
        if (profileOption == ProfileOption.DEFAULT) {
            Logger.log("--- BigQuery default properties ---", Logger.LogType.CYAN);
            echoRunEnvironment(
                runEnvironment.getDefaultProjectIdDefault(),
                runEnvironment.getDefaultDatasetDefault(),
                runEnvironment.getDefaultTableDefault(),
                runEnvironment.getSaProjectIdDefault(),
                runEnvironment.getSaDatasetDefault(),
                runEnvironment.getSaTableDefault()
            );
        } else if (profileOption == ProfileOption.OVERRIDDEN) {
            Logger.log("--- BigQuery overridden properties ---", Logger.LogType.CYAN);
            echoRunEnvironment(
                runEnvironment.getDefaultProjectIdOverride(),
                runEnvironment.getDefaultDatasetOverride(),
                runEnvironment.getDefaultTableOverride(),
                runEnvironment.getSaProjectIdOverride(),
                runEnvironment.getSaDatasetOverride(),
                runEnvironment.getSaTableOverride()
            );
        } else if (profileOption == ProfileOption.ACTIVE) {
            Logger.log("BigQuery resource properties currently configured:", Logger.LogType.CYAN);
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
        Logger.echoHorizontalLine(Logger.LogType.INFO);
    }

    public static class InlineSchemaTranslator {
        public static Schema translate(SchemaDefault schemaDefault, DataTypes dataTypes) {
            Logger.log(String.format("Generating Schema object using: \"%s\"...", schemaDefault.getFields()), Logger.LogType.CYAN);
            List<Field> fields = new ArrayList<>();
            schemaDefault.getFields().forEach(
                f -> {
                    log.info("name={}, type={}", f.getName(), f.getType());
                    fields.add(
                        com.google.cloud.bigquery.Field.of(
                            f.getName(),
                            translateType(f.getType(), dataTypes)
                        )
                    );
                }
            );
            Logger.log("Finished generating schema.", Logger.LogType.CYAN);
            return com.google.cloud.bigquery.Schema.of(fields);
        }

        public static Schema translate(String schema, DataTypes dataTypes) {
            Logger.log(String.format("Generating Schema object using CLI arg: \"%s\"...", schema), Logger.LogType.CYAN);
            List<Field> fields = new ArrayList<>();
            List<String> _fields = Arrays.stream(schema.split(",")).collect(Collectors.toList());
            _fields.forEach(
                f -> {
                    String[] split = f.split(":");
                    String name = split[0], type = split[1];
                    log.info("name={}, type={}", name, type);
                    fields.add(
                        com.google.cloud.bigquery.Field.of(
                            name,
                            translateType(type, dataTypes)
                        )
                    );
                }
            );
            Logger.log("Finished generating schema.", Logger.LogType.CYAN);
            return com.google.cloud.bigquery.Schema.of(fields);
        }
        private static StandardSQLTypeName translateType(String type, DataTypes dataTypes) {
            if (dataTypes.getDataTypes().contains(type)) {
                return StandardSQLTypeName.valueOf(type);
            } else {
                Logger.log(
                    "Error: BigQueryConfig.translateType(): Data type not supported. Defaulting to 'StandardSQLTypeNames.STRING'.",
                    Logger.LogType.ERROR
                );
                logDataTypes(dataTypes);
                return StandardSQLTypeName.STRING;
            }
        }
    }

}
