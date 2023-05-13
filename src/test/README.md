# Tests

Instructions for running tests.


### Table admin

If you're running the tests locally in a container instance, map your local `gcloud` directory to the `/root/.config/gcloud` directory in the container (as described <a href="https://github.com/squidmin/bigquery-labs#run-the-application">here</a>.)

<details>
<summary>List BigQuery resource config for a particular Spring profile</summary>

### `mvn test`

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#echoBigQueryResourceConfig \
  test -P PROFILE_NAME \
  -DGCP_SA_KEY_PATH=GCP_SA_KEY_PATH \
  -DGCP_ADC_ACCESS_TOKEN=GCP_ADC_ACCESS_TOKEN \
  -DGCP_SA_ACCESS_TOKEN=GCP_SA_ACCESS_TOKEN \
  -DGCP_DEFAULT_USER_PROJECT_ID=GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_integration"
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate for the method execution.
- `GCP_SA_KEY_PATH`: path to a GCP service account key for an impersonated service account.
- `GCP_ADC_ACCESS_TOKEN`: application default user's GCP access token.
- `GCP_SA_ACCESS_TOKEN`: access token for an impersonated GCP service account.
- `GCP_DEFAULT_USER_PROJECT_ID`: application default user's GCP project ID.
- `GCP_DEFAULT_USER_DATASET`: application default user's dataset.

Example:

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#echoBigQueryResourceConfig \
  test -P integration \
  -DGCP_SA_KEY_PATH=$GCP_SA_KEY_PATH \
  -DGCP_ADC_ACCESS_TOKEN=$GCP_ADC_ACCESS_TOKEN \
  -DGCP_SA_ACCESS_TOKEN=$GCP_SA_ACCESS_TOKEN \
  -DGCP_DEFAULT_USER_PROJECT_ID=$GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_integration"
```

---

### Using `-DargLine` to specify application profile

The `-DargLine` parameter can also indicate the application profile to activate.

```shell
./mvnw \
  -DargLine="-Dspring.profiles.active=PROFILE_NAME" \
  -Dtest=BigQueryAdminClientIntegrationTest#echoBigQueryResourceConfig \
  test \
  -DGCP_SA_KEY_PATH=GCP_SA_KEY_PATH \
  -DGCP_ADC_ACCESS_TOKEN=GCP_ADC_ACCESS_TOKEN \
  -DGCP_SA_ACCESS_TOKEN=GCP_SA_ACCESS_TOKEN \
  -DGCP_DEFAULT_USER_PROJECT_ID=GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_integration"
```

Example:

```shell
./mvnw \
  -DargLine="-Dspring.profiles.active=integration" \
  -Dtest=BigQueryAdminClientIntegrationTest#echoBigQueryResourceConfig \
  test \
  -DGCP_SA_KEY_PATH=$GCP_SA_KEY_PATH \
  -DGCP_ADC_ACCESS_TOKEN=$GCP_ADC_ACCESS_TOKEN \
  -DGCP_SA_ACCESS_TOKEN=$GCP_SA_ACCESS_TOKEN \
  -DGCP_DEFAULT_USER_PROJECT_ID=$GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_integration"
```

</details>


<details>
<summary>List datasets</summary>

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#listDatasets \
  test -P integration \
  -DGCP_SA_KEY_PATH=$GCP_SA_KEY_PATH \
  -DGCP_ADC_ACCESS_TOKEN=$GCP_ADC_ACCESS_TOKEN \
  -DGCP_SA_ACCESS_TOKEN=$GCP_SA_ACCESS_TOKEN \
  -DGCP_DEFAULT_USER_PROJECT_ID=$GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_integration"
```

</details>


<details>
<summary>Create a dataset</summary>

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#createDataset \
  test -P integration \
  -DGCP_SA_KEY_PATH=$GCP_SA_KEY_PATH \
  -DGCP_ADC_ACCESS_TOKEN=$GCP_ADC_ACCESS_TOKEN \
  -DGCP_SA_ACCESS_TOKEN=$GCP_SA_ACCESS_TOKEN \
  -DGCP_DEFAULT_USER_PROJECT_ID=$GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_integration"
```

</details>


<details>
<summary>Delete a dataset</summary>

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#deleteDataset \
  test -P integration \
  -DGCP_SA_KEY_PATH=$GCP_SA_KEY_PATH \
  -DGCP_ADC_ACCESS_TOKEN=$GCP_ADC_ACCESS_TOKEN \
  -DGCP_SA_ACCESS_TOKEN=$GCP_SA_ACCESS_TOKEN \
  -DGCP_DEFAULT_USER_PROJECT_ID=$GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_integration"
```

</details>


<details>
<summary>Create a table with the configured default schema</summary>

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#createTableWithDefaultSchema \
  test -P integration \
  -DGCP_SA_KEY_PATH=$GCP_SA_KEY_PATH \
  -DGCP_ADC_ACCESS_TOKEN=$GCP_ADC_ACCESS_TOKEN \
  -DGCP_SA_ACCESS_TOKEN=$GCP_SA_ACCESS_TOKEN \
  -DGCP_DEFAULT_USER_PROJECT_ID=$GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_integration" \
  -DGCP_DEFAULT_USER_TABLE="test_table_integration_default"
```

</details>


<details>
<summary>Create a table with a custom schema</summary>

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#createTableWithCustomSchema \
  test -P PROFILE_NAME \
  -DGCP_SA_KEY_PATH=$GCP_SA_KEY_PATH \
  -DGCP_ADC_ACCESS_TOKEN=$GCP_ADC_ACCESS_TOKEN \
  -DGCP_SA_ACCESS_TOKEN=$GCP_SA_ACCESS_TOKEN \
  -DGCP_DEFAULT_USER_PROJECT_ID=$GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_integration" \
  -Dschema="name_1:datatype_1,name_2:datatype_2,[...],name_n:datatype_n"
```

Example:

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#createTableWithCustomSchema \
  test -P integration \
  -DGCP_SA_KEY_PATH=$GCP_SA_KEY_PATH \
  -DGCP_ADC_ACCESS_TOKEN=$GCP_ADC_ACCESS_TOKEN \
  -DGCP_SA_ACCESS_TOKEN=$GCP_SA_ACCESS_TOKEN \
  -DGCP_DEFAULT_USER_PROJECT_ID=$GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_integration" \
  -DGCP_DEFAULT_USER_TABLE="test_table_integration_custom" \
  -DSCHEMA="id:STRING,client_name:STRING,active:BOOL,creation_timestamp:DATETIME,last_update_timestamp:DATETIME"
```

</details>


<details>
<summary>Delete a table</summary>

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#deleteTable \
  test -P integration \
  -DGCP_SA_KEY_PATH=$GCP_SA_KEY_PATH \
  -DGCP_ADC_ACCESS_TOKEN=$GCP_ADC_ACCESS_TOKEN \
  -DGCP_SA_ACCESS_TOKEN=$GCP_SA_ACCESS_TOKEN \
  -DGCP_DEFAULT_USER_PROJECT_ID=$GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_integration" \
  -DGCP_DEFAULT_USER_TABLE="test_table_integration_custom"
```

</details>


<details>
<summary>Insert rows</summary>

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#insert \
  test -P integration \
  -DGCP_SA_KEY_PATH=$GCP_SA_KEY_PATH \
  -DGCP_ADC_ACCESS_TOKEN=$GCP_ADC_ACCESS_TOKEN \
  -DGCP_SA_ACCESS_TOKEN=$GCP_SA_ACCESS_TOKEN \
  -DGCP_DEFAULT_USER_PROJECT_ID=$GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_integration" \
  -DGCP_DEFAULT_USER_TABLE="test_table_integration_custom"
```

</details>
