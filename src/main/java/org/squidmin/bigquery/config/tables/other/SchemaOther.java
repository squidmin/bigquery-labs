package org.squidmin.bigquery.config.tables.other;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.squidmin.bigquery.config.Field;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "tables.other.schema")
@RefreshScope
@Getter
public class SchemaOther {

    private final List<Field> fields = new ArrayList<>();

}
