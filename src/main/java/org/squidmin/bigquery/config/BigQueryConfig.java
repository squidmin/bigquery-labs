package org.squidmin.bigquery.config;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import jdk.jshell.JShell;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.squidmin.bigquery.logger.Logger;
import org.squidmin.bigquery.util.StringUtils;

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
                          DataTypes dataTypes) {

        this.defaultProjectId = defaultProjectId;
        this.defaultDataset = defaultDataset;
        this.defaultTable = defaultTable;

        this.saProjectId = saProjectId;
        this.saDataset = saDataset;
        this.saTable = saTable;

        this.gcpAccessToken = System.getProperty("GCP_SA_ACCESS_TOKEN");
//        Logger.log(String.format("GCP_SA_ACCESS_TOKEN == %s", this.gcpAccessToken), Logger.LogType.CYAN);

        this.saKeyPath = System.getProperty("GOOGLE_APPLICATION_CREDENTIALS");
//        Logger.log(String.format("BQ JDK: GOOGLE_APPLICATION_CREDENTIALS == %s", this.saKeyPath), Logger.LogType.CYAN);
        File credentialsPath = new File(saKeyPath);

        this.schema = schema;
        this.dataTypes = dataTypes;

        BigQueryOptions.Builder bqOptionsBuilder = BigQueryOptions.newBuilder();
        bqOptionsBuilder.setProjectId(defaultProjectId).setLocation("us");
        GoogleCredentials credentials;
        boolean isBqJdkAuthenticatedUsingSaKeyFile;
        try (FileInputStream stream = new FileInputStream(credentialsPath)) {
            credentials = ServiceAccountCredentials.fromStream(stream);
            Logger.log("BQ JDK: SETTING SERVICE ACCOUNT CREDENTIALS (GOOGLE_APPLICATION_CREDENTIALS) TO BQ OPTIONS.", Logger.LogType.CYAN);
            bqOptionsBuilder.setCredentials(credentials);
            isBqJdkAuthenticatedUsingSaKeyFile = true;
        } catch (IOException e) {
            Logger.log(e.getMessage(), Logger.LogType.ERROR);
            if (e.getMessage().contains("'type' value 'authorized_user' not recognized. Expecting 'service_account'")) {
                Logger.log("If you're trying to use Application Default Credentials, use the command:", Logger.LogType.ERROR);
                Logger.log("    gcloud auth application-default print-access-token", Logger.LogType.ERROR);
                Logger.log("to generate a GCP access token and set the output of the command to the \"GOOGLE_APPLICATION_CREDENTIALS\" environment variable.", Logger.LogType.ERROR);
            }
            isBqJdkAuthenticatedUsingSaKeyFile = false;
        }

        if (isBqJdkAuthenticatedUsingSaKeyFile) {
            Logger.log("BigQuery Java SDK has authenticated successfully using a service account key file.", Logger.LogType.INFO);
        } else {
            Logger.log("BigQuery JDK was not able to authenticate using a service account key file.", Logger.LogType.INFO);
            Logger.log("Attempting to authenticate using Application Default Credentials access token.", Logger.LogType.INFO);
        }

        String adcAccessToken = System.getProperty("GCP_ADC_ACCESS_TOKEN");
        if (!isBqJdkAuthenticatedUsingSaKeyFile && StringUtils.isNotEmpty(adcAccessToken)) {
            bigQuery = bqOptionsBuilder.setCredentials(
                GoogleCredentials.newBuilder()
                    .setAccessToken(
                        AccessToken.newBuilder().setTokenValue(adcAccessToken).build()
                    ).build()
            ).build().getService();
        } else {
            bigQuery = bqOptionsBuilder.build().getService();
            Logger.log("Was not able to authenticate using Application Default Credentials access token.", Logger.LogType.INFO);
        }

    }

}
