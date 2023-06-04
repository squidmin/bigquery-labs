package org.squidmin.bigquery.fixture;

import com.google.api.gax.paging.Page;
import com.google.cloud.PageImpl;
import com.google.cloud.bigquery.*;
import org.squidmin.bigquery.config.DataTypes;
import org.squidmin.bigquery.config.tables.sandbox.SchemaDefault;
import org.squidmin.bigquery.dao.RecordExample;
import org.squidmin.bigquery.dto.sandbox.ExampleResponseItem;
import org.squidmin.bigquery.util.BigQueryUtil;
import org.squidmin.bigquery.util.RunEnvironment;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BigQueryTestFixture {

    public enum CLI_ARG_KEYS {
        GCP_DEFAULT_USER_PROJECT_ID, GCP_DEFAULT_USER_DATASET, GCP_DEFAULT_USER_TABLE,
        GCP_SA_PROJECT_ID, GCP_SA_DATASET, GCP_SA_TABLE,
        SCHEMA
    }

    public static abstract class QUERIES {
        public static BiFunction<RunEnvironment, String, String> LOOK_UP_BY_ID = (runEnvironment, id) -> String.format(
            "SELECT * FROM %s.%s.%s WHERE id = '%s'",
            runEnvironment.getGcpDefaultUserProjectId(),
            runEnvironment.getGcpDefaultUserDataset(),
            runEnvironment.getGcpDefaultUserTable(),
            id
        );
    }

    public static final Supplier<List<RecordExample>> DEFAULT_ROWS = () -> IntStream.range(0, 5)
        .mapToObj(i -> {
            LocalDateTime now = LocalDateTime.now(TimeZone.getDefault().toZoneId());
            String creationTimestamp = LocalDateTime.of(
                2023, Month.JANUARY, 1,
                now.getHour(), now.getMinute(), now.getSecond()
            ).minusDays(3).format(DateTimeFormatter.ISO_DATE_TIME);
            String lastUpdateTimestamp = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(now.toEpochSecond(ZoneOffset.UTC)),
                TimeZone.getDefault().toZoneId()
            ).format(DateTimeFormatter.ISO_DATE_TIME);
            return RecordExample.builder()
                .id(UUID.randomUUID().toString())
                .creationTimestamp(creationTimestamp)
                .lastUpdateTimestamp(lastUpdateTimestamp)
                .columnA("asdf1")
                .columnB("asdf2")
                .build();
        }).collect(Collectors.toList());

    public static TableResult validTableResultForExampleResponse(SchemaDefault schemaDefault, DataTypes dataTypes) {
        List<Field> fields = new ArrayList<>();
        schemaDefault.getFields().forEach(field ->
            fields.add(
                Field.newBuilder(
                    field.getName(),
                    BigQueryUtil.InlineSchemaTranslator.translateType(field.getType(), dataTypes)
                ).build()
            )
        );
        Schema schema = Schema.of(fields);
        final int ROWS_LIMIT = 5;
        List<FieldValueList> rows = validExampleResponseRows(fields, ROWS_LIMIT);
        Page<FieldValueList> page = new PageImpl<>(null, null, rows);
        return new TableResult(schema, ROWS_LIMIT, page);
    }

    private static List<FieldValueList> validExampleResponseRows(List<Field> fields, int rowsLimit) {
        ExampleResponseItem defaultResponseItem = validExampleResponseItem();
        List<FieldValue> row = new ArrayList<>();
        List<FieldValueList> rows = new ArrayList<>();
        for (int i = 0; i < rowsLimit; i++) {
            fields.forEach(field -> {
                row.add(FieldValue.of(FieldValue.Attribute.PRIMITIVE, defaultResponseItem.getId()));
                row.add(FieldValue.of(FieldValue.Attribute.PRIMITIVE, defaultResponseItem.getCreationTimestamp()));
                row.add(FieldValue.of(FieldValue.Attribute.PRIMITIVE, defaultResponseItem.getLastUpdateTimestamp()));
                row.add(FieldValue.of(FieldValue.Attribute.PRIMITIVE, defaultResponseItem.getColumnA()));
                row.add(FieldValue.of(FieldValue.Attribute.PRIMITIVE, defaultResponseItem.getColumnB()));
            });
        }
        return rows;
    }

    private static ExampleResponseItem validExampleResponseItem() {
        LocalDateTime now = LocalDateTime.now(TimeZone.getDefault().toZoneId());
        String creationTimestamp = LocalDateTime.of(
            2023, Month.JANUARY, 1,
            now.getHour(), now.getMinute(), now.getSecond()
        ).minusDays(3).format(DateTimeFormatter.ISO_DATE_TIME);
        String lastUpdateTimestamp = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(now.toEpochSecond(ZoneOffset.UTC)),
            TimeZone.getDefault().toZoneId()
        ).format(DateTimeFormatter.ISO_DATE_TIME);
        return ExampleResponseItem.builder()
            .id("asdf-2345")
            .creationTimestamp(creationTimestamp)
            .lastUpdateTimestamp(lastUpdateTimestamp)
            .columnA("column_a-test")
            .columnB(String.valueOf(false))
            .build();
    }

}
