package org.squidmin.bigquery.dto;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.squidmin.bigquery.IntegrationTest;
import org.squidmin.bigquery.dao.RecordExample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RecordExampleTest extends IntegrationTest {

    @Test
    public void getField_givenPartiallyPopulatedValues_whenCalled_thenValuesAreSetCorrectly() {
        List<String> fieldNames = new ArrayList<>();
        bqConfig.getSchemaDefault().getFields().forEach(field -> fieldNames.add(field.getName()));

        final String id = "321", columnA = "asdf1", columnB = "asdf2";

        RecordExample record = RecordExample.builder().id(id).columnA(columnA).columnB(columnB).build();

        List<String> actualValues = fieldNames.stream().map(record::getField).collect(Collectors.toList());
        List<String> expectedValues = Arrays.asList(id, null, null, columnA, columnB);

        Assertions.assertIterableEquals(expectedValues, actualValues);
    }

}
