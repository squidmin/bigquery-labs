package org.squidmin.bigquery.util;

import com.google.cloud.bigquery.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RunEnvironment {

    // Default profile values
    private String defaultProjectIdDefault;
    private String defaultDatasetDefault;
    private String defaultTableDefault;
    private String defaultProjectIdOverride;
    private String defaultDatasetOverride;
    private String defaultTableOverride;

    private String saProjectIdDefault;
    private String saDatasetDefault;
    private String saTableDefault;
    private String saProjectIdOverride;
    private String saDatasetOverride;
    private String saTableOverride;
    //


    // Active profile values
    private String defaultProjectId;
    private String defaultDataset;
    private String defaultTable;
    private String saProjectId;
    private String saDataset;
    private String saTable;

    private Schema schema;

}
