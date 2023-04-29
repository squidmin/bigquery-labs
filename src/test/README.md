# Tests

Instructions for running tests.


### Table admin

If you're running the tests locally in a container instance, map your local `gcloud` directory to the `/root/.config/gcloud` directory in the container (as described <a href="https://github.com/squidmin/bigquery-labs#run-the-application">here</a>.)

<details>
<summary>List BigQuery resource metadata configured for a particular Spring profile</summary>

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#echoDefaultBigQueryResourceMetadata \
  test -P PROFILE_NAME \
  -DdefaultProjectId="DEFAULT_PROJECT_ID" \
  -DGOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate for the method execution.
- `DEFAULT_PROJECT_ID`: the default user's GCP project ID to target.

Example:

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#echoDefaultBigQueryResourceMetadata \
  test -P integration \
  -DdefaultProjectId="lofty-root-378503" \
  -DGOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS
```

The `-DargLine` parameter can also indicate the profile to activate.

```shell
./mvnw \
  -DargLine="-Dspring.profiles.active=PROFILE_NAME" \
  -Dtest=BigQueryAdminClientIntegrationTest#echoDefaultBigQueryResourceMetadata \
  test \
  -DdefaultProjectId="DEFAULT_PROJECT_ID" \
  -DGOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `DEFAULT_PROJECT_ID`: the default user's GCP project ID to target.

For example, assuming the name of the profile to activate is `integration`:

```shell
./mvnw \
  -DargLine="-Dspring.profiles.active=integration" \
  -Dtest=BigQueryAdminClientIntegrationTest#echoDefaultBigQueryResourceMetadata \
  test \
  -DdefaultProjectId="lofty-root-378503" \
  -DGOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS
```

</details>


<details>
<summary>List datasets</summary>

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#listDatasets \
  test -P PROFILE_NAME \
  -DdefaultProjectId="DEFAULT_PROJECT_ID" \
  -DGOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `DEFAULT_PROJECT_ID`: the default user's GCP project ID to target.

Example:

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#listDatasets \
  test -P integration \
  -DdefaultProjectId="lofty-root-378503" \
  -DGOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS
```

</details>


<details>
<summary>Create a dataset</summary>

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#createDataset \
  test -P PROFILE_NAME \
  -DdefaultProjectId="lofty-root-378503" \
  -DdefaultDataset="test_dataset_integration" \
  -DGOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `DEFAULT_PROJECT_ID`: the default user's GCP project ID to target.
- `DEFAULT_DATASET`: the default user's BigQuery dataset to target.

Example:

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#createDataset \
  test -P integration \
  -DdefaultProjectId="lofty-root-378503" \
  -DdefaultDataset="test_dataset_integration" \
  -DGOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS
```

</details>


<details>
<summary>Create a table with the configured default schema</summary>

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#createTableWithDefaultSchema \
  test -P PROFILE_NAME \
  -DdefaultProjectId="DEFAULT_PROJECT_ID" \
  -DdefaultDataset="DEFAULT_DATASET" \
  -DdefaultTable="DEFAULT_TABLE" \
  -DGOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `DEFAULT_PROJECT_ID`: the default user's GCP project ID to target.
- `DEFAULT_DATASET`: the default user's BigQuery dataset to target.
- `DEFAULT_TABLE`: default user's BigQuery table to target.

Example using the `integration` profile:

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#createTableWithDefaultSchema \
  test -P integration \
  -DdefaultProjectId="lofty-root-378503" \
  -DdefaultDataset="test_dataset_integration" \
  -DdefaultTable="test_table_integration_default" \
  -DGOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS
```

</details>


<details>
<summary>Create a table with a custom schema</summary>

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#createTableWithCustomSchema \
  test -P PROFILE_NAME \
  -DdefaultProjectId="DEFAULT_PROJECT_ID" \
  -DdefaultDataset="DEFAULT_DATASET" \
  -DdefaultTable="DEFAULT_TABLE" \
  -Dschema="name_1,datatype_1;name_2,datatype_2;[...];name_n,datatype_n" \
  -DGOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS
```

**Replace the following**:
- `PROFILE_NAME`: the name of the application profile to activate.
- `DEFAULT_PROJECT_ID`: the default user's GCP project ID to target.
- `DEFAULT_DATASET`: the default user's BigQuery dataset to target.
- `DEFAULT_TABLE`: the default user's BigQuery table to target.

Example using the `integration` profile:

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#createTableWithCustomSchema \
  test -P integration \
  -DdefaultProjectId="lofty-root-378503" \
  -DdefaultDataset="test_dataset_integration" \
  -DdefaultTable="test_table_integration_custom" \
  -Dschema="id,string;client_name,string;active,bool;creation_timestamp,datetime;last_update_timestamp,datetime" \
  -DGOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS
```

</details>