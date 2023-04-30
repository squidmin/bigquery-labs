package org.squidmin.bigquery.fixture;

import org.squidmin.bigquery.dao.RecordExample;
import org.squidmin.bigquery.logger.LogFont;
import org.squidmin.bigquery.logger.Logger;
import org.squidmin.bigquery.util.RunEnvironment;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BigQueryTestFixture {

    public enum CLI_ARG_KEYS {
        defaultProjectId, defaultDataset, defaultTable,
        saProjectId, saDataset, saTable,
        schema
    }

    public static abstract class QUERIES {
        public static BiFunction<RunEnvironment, String, String> LOOK_UP_BY_ID = (runEnvironment, id) -> String.format(
            "SELECT * FROM %s.%s.%s WHERE id = '%s'",
            runEnvironment.getDefaultProjectId(),
            runEnvironment.getDefaultDataset(),
            runEnvironment.getDefaultTable(),
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
//                Instant.ofEpochMilli(System.currentTimeMillis()),
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

}
