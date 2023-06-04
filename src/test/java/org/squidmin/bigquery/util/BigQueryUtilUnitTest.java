package org.squidmin.bigquery.util;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.squidmin.bigquery.IntegrationTest;
import org.squidmin.bigquery.dto.sandbox.ExampleResponseItem;
import org.squidmin.bigquery.fixture.BigQueryTestFixture;
import org.squidmin.bigquery.logger.Logger;

import java.util.List;

public class BigQueryUtilUnitTest extends IntegrationTest {

    @Test
    public void toList_givenValidTableResult_whenCalled_thenReturnValidResponseBody() {
        List<ExampleResponseItem> actual = BigQueryUtil.toList(
            BigQueryTestFixture.validTableResultForExampleResponse(bqConfig.getSchemaDefault(), bqConfig.getDataTypes()),
            bqConfig.getSelectFieldsDefault(),
            false
        );
        Assertions.assertNotNull(actual);
        Logger.log(actual.toString(), Logger.LogType.INFO);
    }

}
