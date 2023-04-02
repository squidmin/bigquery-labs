package org.squidmin.bigquery.config;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "org.squidmin.bigquery"
})
@Getter
@Slf4j
public class BigQueryConfig {

    private final String projectId, datasetName, tableName;

    private final Schema schema;

    private final BigQuery bigQuery;

    @Autowired
    public BigQueryConfig(@Value("${bigquery.projectId}") String projectId,
                          @Value("${bigquery.datasetName}") String datasetName,
                          @Value("${bigquery.tableName}") String tableName,
                          Schema schema) {
        this.projectId = projectId;
        this.datasetName = datasetName;
        this.tableName = tableName;
        this.schema = schema;

        bigQuery = BigQueryOptions.newBuilder().setLocation("us").build().getService();
    }

}
