# Tests

Instructions for running tests.


### Table admin

<details>
<summary>List BigQuery resource metadata configured for a particular Spring profile</summary>

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#echoDefaultBigQueryResourceMetadata \
  test -P PROFILE_NAME \
  -DprojectId="PROJECT_ID"
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate for the method execution.
- `PROJECT_ID`: the project ID for the GCP project to target.

For example, assuming the name of the profile to activate is `integration`:

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#echoDefaultBigQueryResourceMetadata \
  test -P integration \
  -DprojectId="lofty-root-378503"
```

The `-DargLine` parameter can also indicate the profile to activate.

```shell
./mvnw \
  -DargLine="-Dspring.profiles.active=PROFILE_NAME" \
  -Dtest=BigQueryAdminClientIntegrationTest#echoDefaultBigQueryResourceMetadata \
  test \
  -DprojectId="PROJECT_ID"
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `PROJECT_ID`: the project ID of the GCP project to target.

For example, assuming the name of the profile to activate is `integration`:

```shell
./mvnw \
  -DargLine="-Dspring.profiles.active=integration" \
  -Dtest=BigQueryAdminClientIntegrationTest#echoDefaultBigQueryResourceMetadata \
  test \
  -DprojectId="lofty-root-378503"
```

</details>


<details>
<summary>List datasets</summary>

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#listDatasets \
  test -P PROFILE_NAME \
  -projectId="PROJECT_ID"
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `PROJECT_ID`: the project ID of the GCP project to target.

Example:

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#listDatasets \
  test -P integration \
  -DprojectId=lofty-root-378503
```

</details>


<details>
<summary>Create a dataset</summary>

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#createDataset \
  test -P PROFILE_NAME \
  -DprojectId="PROJECT_ID" \
  -DdatasetName="DATASET_NAME"
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `PROJECT_ID`: the project ID of the GCP project to target.
- `DATASET_NAME`: the name of the dataset to target.

Example:

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#createDataset \
  test -P integration \
  -DprojectId="lofty-root-378503" \
  -DdatasetName="test_dataset_123"
```

</details>


<details>
<summary>Create a table with the configured default schema</summary>

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#createTableWithDefaultSchema \
  test -P PROFILE_NAME \
  -DprojectId="PROJECT_ID" \
  -DdatasetName="DATASET_NAME" \
  -DtableName="TABLE_NAME"
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `PROJECT_ID`: the project ID of the GCP project to target.
- `DATASET_NAME`: the name of the BigQuery dataset to target.
- `TABLE_NAME`: the name of the BigQuery table to target.

Example using the `integration` profile:

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#createTableWithDefaultSchema \
  test -P integration \
  -DprojectId="lofty-root-378503" \
  -DdatasetName="test_dataset_123" \
  -DtableName="test_table_123"
```

</details>


<details>
<summary>Create a table with a custom schema</summary>

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#createTableWithCustomSchema \
  test -P PROFILE_NAME \
  -DprojectId="PROJECT_ID" \
  -DdatasetName="DATASET_NAME" \
  -DtableName="TABLE_NAME" \
  -Dschema="name_1,datatype_1;name_2,datatype_2;[...];name_n,datatype_n"
```

**Replace the following**:
- `PROFILE_NAME`: the name of the profile to activate.
- `PROJECT_ID`: the name of the GCP project ID to target.
- `DATASET_NAME`: the name of the BigQuery dataset to target.
- `TABLE_NAME`: the name of the BigQuery table to target.

Example using the `integration` profile:

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#createTableWithCustomSchema \
  test -P integration \
  -DprojectId="lofty-root-378503" \
  -DdatasetName="test_dataset_name_integration" \
  -DtableName="test_table_name_integration" \
  -Dschema="id,string;client_name,string;active,bool;creation_timestamp,datetime;last_update_timestamp,datetime"
```

</details>