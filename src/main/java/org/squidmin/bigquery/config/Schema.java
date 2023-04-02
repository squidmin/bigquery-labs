package org.squidmin.bigquery.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "bigquery.schema")
@RefreshScope
@Getter
@Slf4j
public class Schema {

    private final List<Field> fields = new ArrayList<>();

}
