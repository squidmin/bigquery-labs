package org.squidmin.bigquery.config.tables.other;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "tables.other.select")
@RefreshScope
@Getter
public class SelectFieldsOther {

    private final List<String> fields = new ArrayList<>();

}
