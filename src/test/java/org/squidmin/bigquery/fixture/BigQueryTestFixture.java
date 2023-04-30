package org.squidmin.bigquery.fixture;

import org.squidmin.bigquery.dao.RecordExample;
import org.squidmin.bigquery.util.RunEnvironment;

import java.util.List;
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
        .mapToObj(i -> RecordExample.builder()
            .id(UUID.randomUUID().toString())
            .creationTimestamp(String.valueOf(System.currentTimeMillis()))
            .lastUpdateTimestamp(String.valueOf(System.currentTimeMillis()))
            .columnA("asdf1")
            .columnB("asdf2")
            .build()
        ).collect(Collectors.toList());

}
