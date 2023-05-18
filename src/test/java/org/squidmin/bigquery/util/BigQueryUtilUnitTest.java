package org.squidmin.bigquery.util;

import org.junit.Test;
import org.squidmin.bigquery.IntegrationTest;
import org.squidmin.bigquery.dto.ExampleResponseItem;
import org.squidmin.bigquery.fixture.BigQueryTestFixture;
import org.squidmin.bigquery.logger.Logger;

import java.util.List;

public class BigQueryUtilUnitTest extends IntegrationTest {

    @Test
    public void stub() {
        List<ExampleResponseItem> actual = BigQueryUtil.toList(
            BigQueryTestFixture.validTableResultForExampleResponseItem(bqConfig.getSchemaDefault(), bqConfig.getDataTypes()),
            bqConfig.getSelectFieldsDefault(),
            false
        );
        Logger.log(actual.toString(), Logger.LogType.INFO);
    }

}
