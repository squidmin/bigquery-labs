package org.squidmin.bigquery.util;

import com.google.cloud.bigquery.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class BigQueryUtil {

    public static void logTableInfo(TableInfo tableInfo) {
        log.info("Friendly name: " + tableInfo.getFriendlyName());
        log.info("Description: " + tableInfo.getDescription());
        log.info("Creation time: " + tableInfo.getCreationTime());
        log.info("Expiration time: " + tableInfo.getExpirationTime());
    }

    public static com.google.cloud.bigquery.Schema translate(org.squidmin.bigquery.config.Schema schema) {
        log.info("Generating Schema object using: \"" + schema.getFields() + "\"...");
        List<Field> fields = new ArrayList<>();
        schema.getFields().forEach(
            f -> {
                log.info("name={}, type={}", f.getName(), f.getType());
                fields.add(
                    com.google.cloud.bigquery.Field.of(
                        f.getName(),
                        translateType(f.getType())
                    )
                );
            }
        );
        log.info("Finished generating schema...");
        return com.google.cloud.bigquery.Schema.of(fields);
    }

    public static com.google.cloud.bigquery.Schema translate(String schema) {
        log.info("Generating Schema object using CLI arg: \"" + schema + "\"...");
        List<Field> fields = new ArrayList<>();
        List<String> _fields = Arrays.stream(schema.split(";")).collect(Collectors.toList());
        validateFields();
        _fields.forEach(
            f -> {
                String[] split = f.split(",");
                String name = split[0], type = split[1];
                log.info("name={}, type={}", name, type);
                fields.add(
                    com.google.cloud.bigquery.Field.of(
                        name,
                        translateType(type)
                    )
                );
            }
        );
        log.info("Finished generating schema...");
        return com.google.cloud.bigquery.Schema.of(fields);
    }

    private static StandardSQLTypeName translateType(String type) {
        if (type.equalsIgnoreCase("string")) {
            return StandardSQLTypeName.STRING;
        } else if (type.equalsIgnoreCase("datetime")) {
            return StandardSQLTypeName.DATETIME;
        } else if (type.equalsIgnoreCase("bool")) {
            return StandardSQLTypeName.BOOL;
        } else {
            log.error("Error: BigQueryConfig.translateType(): Data type not supported. Defaulting to 'StandardSQLTypeNames.STRING'.");
            return StandardSQLTypeName.STRING;
        }
    }

    private static boolean validateFields() {
        // TODO
        return false;
    }

}
