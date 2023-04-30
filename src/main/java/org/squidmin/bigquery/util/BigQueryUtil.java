package org.squidmin.bigquery.util;

import com.google.cloud.bigquery.*;
import lombok.extern.slf4j.Slf4j;
import org.squidmin.bigquery.config.DataTypes;
import org.squidmin.bigquery.logger.Logger;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class BigQueryUtil {

    public static void methodName() {

    }

    public static class InlineSchemaTranslator {
        public static com.google.cloud.bigquery.Schema translate(org.squidmin.bigquery.config.Schema schema, DataTypes dataTypes) {
            Logger.log(String.format("Generating Schema object using: \"%s\"...", schema.getFields()), Logger.LogType.CYAN);
            List<Field> fields = new ArrayList<>();
            schema.getFields().forEach(
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

        public static com.google.cloud.bigquery.Schema translate(String schema, DataTypes dataTypes) {
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
                return StandardSQLTypeName.STRING;
            }
        }
    }

}
