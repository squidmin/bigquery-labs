package org.squidmin.bigquery.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.squidmin.bigquery.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@ComponentScan(basePackages = {
    "org.squidmin.bigquery"
})
@Getter
@Slf4j
public class BigQueryConfig {

    private final String defaultProjectId;
    private final String defaultDataset;
    private final String defaultTable;

    private final String saProjectId;
    private final String saDataset;
    private final String saTable;

    private final String saKeyPath;
    private final String gcpAccessToken;

    private final Schema schema;

    private final BigQuery bigQuery;

    private final DataTypes dataTypes;

    @Autowired
    public BigQueryConfig(@Value("${bigquery.application-default.project-id}") String defaultProjectId,
                          @Value("${bigquery.application-default.dataset}") String defaultDataset,
                          @Value("${bigquery.application-default.table}") String defaultTable,
                          @Value("${bigquery.service-account.project-id}") String saProjectId,
                          @Value("${bigquery.service-account.dataset}") String saDataset,
                          @Value("${bigquery.service-account.table}") String saTable,
                          Schema schema,
                          DataTypes dataTypes) throws IOException {

        this.defaultProjectId = defaultProjectId;
        this.defaultDataset = defaultDataset;
        this.defaultTable = defaultTable;

        this.saProjectId = saProjectId;
        this.saDataset = saDataset;
        this.saTable = saTable;

        this.gcpAccessToken = System.getProperty("GCP_ACCESS_TOKEN");
        Logger.log(String.format("GCP_ACCESS_TOKEN == %s", this.gcpAccessToken), Logger.LogType.CYAN);

        this.saKeyPath = System.getProperty("GOOGLE_APPLICATION_CREDENTIALS");
        Logger.log(String.format("BQ JDK: GOOGLE_APPLICATION_CREDENTIALS == %s", this.saKeyPath), Logger.LogType.CYAN);
        File credentialsPath = new File(saKeyPath);

        this.schema = schema;
        this.dataTypes = dataTypes;

        BigQueryOptions.Builder bqOptionsBuilder = BigQueryOptions.newBuilder();
        bqOptionsBuilder.setProjectId(defaultProjectId).setLocation("us");
        GoogleCredentials credentials;
        try (FileInputStream stream = new FileInputStream(credentialsPath)) {
            credentials = ServiceAccountCredentials.fromStream(stream);
        }
        Logger.log("BQ JDK: SETTING SERVICE ACCOUNT CREDENTIALS (GOOGLE_APPLICATION_CREDENTIALS) TO BQ OPTIONS.", Logger.LogType.CYAN);
        bqOptionsBuilder.setCredentials(credentials);
        bigQuery = bqOptionsBuilder.build().getService();

    }

}
