package org.squidmin.bigquery.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.squidmin.bigquery.config.BigQueryConfig;

import java.util.ArrayList;

@Component
@Slf4j
public class TableFactory {

    private BigQueryConfig bqConfig;

    @Autowired
    public TableFactory(BigQueryConfig bqConfig) {
        this.bqConfig = bqConfig;
    }

    public com.google.cloud.bigquery.Schema noSchema() {
        return com.google.cloud.bigquery.Schema.of(new ArrayList<>());
    }

    public com.google.cloud.bigquery.Schema withDefaultSchema() {
        return BigQueryUtil.translate(bqConfig.getSchema());
    }

    public com.google.cloud.bigquery.Schema withCustomSchema(String schema) {
        return BigQueryUtil.translate(schema);
    }

}
