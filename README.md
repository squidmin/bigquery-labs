# bigquery-labs

GCP BigQuery labs using the Java SDK.

Made with:
- **IntelliJ IDEA 2023.1 (Ultimate Edition)**
- **openjdk 11.0.17**
- **Apache Maven 3.8.7**


---


## Install & build

<details>
<summary>Download, install, and initialize the gcloud SDK on your local machine</summary>

Refer to the <a href="https://cloud.google.com/sdk/docs/install#other_installation_options">`gcloud` CLI documentation</a> to complete this step.

Install the `gcloud` SDK to the user's home directory (e.g., `/Users/USERNAME/google-cloud-sdk`). 

When it's finished installing, add the `gcloud` executable to your system's `$PATH` and run the command:

```shell
gcloud init
```

</details>


<details>
<summary>gcloud CLI: Generate an Application Default Credentials (ADC) access token</summary>

If you're running the application locally, you can use the following command to generate an access token:

```shell
export GOOGLE_APPLICATION_CREDENTIALS="$(gcloud auth application-default print-access-token)"
```

</details>


<details>
<summary>gcloud CLI: Generate a GCP service account access token</summary>

Run this command to generate an access token for a specific GCP service account:

```shell
export GOOGLE_APPLICATION_CREDENTIALS=$(gcloud auth print-access-token --impersonate-service-account='SA_EMAIL_ADDRESS')
```

**Replace the following**:
- `SA_EMAIL_ADDRESS`: the email address of the service account to impersonate.

Example:

```shell
export GOOGLE_APPLICATION_CREDENTIALS=$(gcloud auth print-access-token --impersonate-service-account='9644524330-compute@developer.gserviceaccount.com')
```

</details>


<details>
<summary>Create and store a service account key</summary>

This section refers to usage of a GCP service account key (.json) file stored on your local file system.

To map a local `gcloud` installation to a volume on a container instance running the application, include the `-v` parameter in the `docker run` command used to start a container instance, as described below. 

### macOS

Assuming the user's service account key file is stored in the same directory as their local `gcloud` installation:

`/Users/USERNAME/.config/gcloud`

and the target volume on the container instance is: 

`/root/.config/gcloud`

the command to run the container instance would be:

```shell
docker run \
  --rm -it \
  -e GOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS \
  -v $HOME/.config/gcloud:/root/.config/gcloud \
  -v $HOME/.m2:/root/.m2 \
  biquery-labs
```

**Replace the following**:

- `USERNAME`: the current user's username

so that the path to the service account key file is correct, e.g.:

`/Users/squidmin/.config/gcloud/sa-private-key.json`

Read <a href="https://cloud.google.com/iam/docs/keys-create-delete#iam-service-account-keys-create-gcloud">here</a> for more information about creating service account keys.

</details>


<details>
<summary>Add the Maven wrapper</summary>

Ensure that Maven is already installed on the machine that will run the container.

In the root of this project, run the command:

```shell
mvn wrapper:wrapper
```

Read <a href="https://maven.apache.org/install.html">here</a> for more information about installing Maven.

</details>


<details>
<summary>Build a JAR</summary>

```shell
./mvnw clean package -P integration \
  -DdefaultProjectId=$GCP_PROJECT_ID \
  -DGOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS \
  -DGCP_ADC_ACCESS_TOKEN=$GCP_ADC_ACCESS_TOKEN \
  -DGCP_SA_ACCESS_TOKEN=$GCP_SA_ACCESS_TOKEN
```

</details>


<details>
<summary>Build a container image</summary>

```shell
docker build \
  --build-arg GCP_PROJECT_ID=$GCP_PROJECT_ID \
  -t bigquery-labs .
```

</details>


---


## Run the application


### 1a. Prepare the host machine environment

<details>
<summary>Expand</summary>

Until the main application entrypoint is developed, run the application's functionality via the `mvn test` interface.

Pass required environment variables on your local system to the VM options, as shown in the `createTableWithCustomSchema` example below:

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#createTableWithCustomSchema \
  test -P integration \
  -DdefaultProjectId="lofty-root-378503" \
  -DdefaultDataset="test_dataset_integration" \
  -DdefaultTable="test_table_integration_custom" \
  -Dschema="id:STRING,client_name:STRING,active:BOOL,creation_timestamp:DATETIME,last_update_timestamp:DATETIME" \
  -DGOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS
```

</details>


### 1b. Prepare the container environment

<details>
<summary>Expand</summary>

### Environment variables

The `GOOGLE_APPLICATION_CREDENTIALS` environment variable is used to store the path to the user's service account key file.
The user's service account key file is mapped to the `/root/.config/gcloud` directory on the container instance.

```shell
export GOOGLE_APPLICATION_CREDENTIALS=/root/.config/gcloud/sa-private-key.json
```

The `ADC_ACCESS_TOKEN` environment variable is used to store an OAuth2 access token for reaching BigQuery RESTful services _using Application Default Credentials_.

```shell
export GCP_ADC_ACCESS_TOKEN=$(gcloud auth application-default print-access-token)
```

The `GCP_SA_ACCESS_TOKEN` environment variable is used to store an OAuth2 access token for reaching BigQuery RESTful services _as a specific service account_.

```shell
export GCP_SA_ACCESS_TOKEN=$(gcloud auth application-default print-access-token)
```

### Utility script (WIP)

The `run.sh` script at the root level of the project will set required environment variables automatically.

It accepts short and long arguments for each environment variable.

</details>


### 2. Run an interactive container instance

<details>
<summary>Expand</summary>

### `docker run`

```shell
docker run --rm -it \
  -e GOOGLE_APPLICATION_CREDENTIALS=lofty-root-378503 \
  -e GCP_SA_ACCESS_TOKEN=access_token_placeholder \
  -e ADC_ACCESS_TOKEN=$(gcloud auth application-default print-access-token) \
  -v $HOME/.config/gcloud:/root/.config/gcloud \
  -v $HOME/.m2:/root/.m2 \
  bigquery-labs
```

---

### `./run.sh` utility script

```shell
./run.sh \
  -gcppid lofty-root-378503 \
  -gac $HOME/.config/gcloud/sa-private-key.json \
  -saat access_token_placeholder \
  -adcat $(gcloud auth application-default print-access-token)
```

Or use long arguments:

```shell
./run.sh \
  --GCP_PROJECT_ID lofty-root-378503 \
  --GOOGLE_APPLICATION_CREDENTIALS $HOME/.config/gcloud/sa-private-key.json \
  --GCP_SA_ACCESS_TOKEN access_token_placeholder \
  --GCP_ADC_ACCESS_TOKEN $(gcloud auth application-default print-access-token)
```

### `run.sh` options

<details>
<summary>Expand</summary>

`run.sh` implements the following options:

- `--default`: Start the application on the user's host system with default run environment settings.<br>

  <details>
  <summary>Example</summary>

  ```shell
  ./run.sh --default
  ```

  </details>

- `-ci`, `--container-instance`: Run a container instance pointing to the root directory of the application.

  <details>
  <summary>Example</summary>

  ```shell
  ./run.sh --container-instance
  ```

  </details>

- `-nci`, `--no-container-instance`: Build and run the application without starting a container instance.

  <details>
  <summary>Example</summary>

  ```shell
  ./run.sh -nci
  ```

  </details>

- `-i SA_EMAIL_ADDRESS`, `--impersonate SA_EMAIL_ADDRESS`: Impersonate a GCP service account.

  Replace `SA_EMAIL_ADDRESS` with the email address of the service account to impersonate.

  <details>
  <summary>Example</summary>

  ```shell
  ./run.sh --default -ci --impersonate-service-account SA_EMAIL_ADDRESS
  ```

  **Replace the following**:
  - `SA_EMAIL_ADDRESS`: the impersonated service account's email address.

  </details>

When specifying both the `--default` and `--container-instance` options, a container instance will start with default run environment settings.

<details>
<summary>Example</summary>

```shell
./run.sh --default --container-instance
```

</details>

</details>

</details>


### 3. Run the JAR

> Note: This section currently is only used for testing the main application entrypoint.
>
> This project currently invokes the BigQuery Java SDK (and later the BigQuery RESTful services) via the `mvn test` interface. Use that for now.
> 
> _**Refer to**_: <a href="export GOOGLE_APPLICATION_CREDENTIALS=/root/.config/gcloud/sa-private-key.json">`/src/test` README.md</a>.

<details>
<summary>Using "exec java" command. Specify a profile.</summary>

```shell
exec java -jar \
  -Dspring.profiles.active=local \
  ./target/bigquery-labs-0.0.1-SNAPSHOT.jar
```

</details>


<details>
<summary>With Maven. Specify a profile.</summary>

```shell
mvn spring-boot:run \
  -Dspring-boot.run.profiles=local \
  -DGOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS
```

</details>


---


## Table admin

### `bq` CLI

<details>
<summary>List datasets</summary>

```shell
bq ls --filter labels.key:value \
  --max_results integer \
  --format=prettyjson \
  --project_id project_id
```

**Replace the following**:
- `key:value`: a label key and value, if applicable.
- `integer`: an integer representing the number of datasets to list.
- `project_id`: the name of the GCP project to target.

**Examples**:

```shell
bq ls --format=pretty
```

</details>


<details>
<summary>Create a dataset</summary>

Refer to the <a href="https://cloud.google.com/bigquery/docs/datasets#create-dataset">GCP documentation for creating datasets</a>.

**Examples**:

```shell
bq --location=us mk \
  --dataset \
  --default_partition_expiration=3600 \
  --default_table_expiration=3600 \
  --description="An example." \
  --label=test_label_1:test_value_1 \
  --label=test_label_2:test_value_2 \
  --max_time_travel_hours=168 \
  --storage_billing_model=LOGICAL \
  lofty-root-378503:test_dataset_name_lofty
```

The Cloud Key Management Service (KMS) key parameter (`KMS_KEY_NAME`) can be specified.
This parameter is used to pass the name of the default Cloud Key Management Service key used to protect newly created tables in this dataset.
You cannot create a Google-encrypted table in a dataset with this parameter set.

```shell
bq --location=us mk \
  --dataset \
  --default_kms_key=KMS_KEY_NAME \
  ...
  lofty-root-378503:test_dataset_name_lofty
```

</details>


<details>
<summary>Delete a dataset</summary>

Refer to the <a href="https://cloud.google.com/bigquery/docs/managing-datasets#delete_a_dataset">GCP documentation for deleting a dataset</a>.

#### Examples:

Remove all tables in the dataset (`-r` flag):

```shell
bq rm -r -f -d lofty-root-378503:test_dataset_name_lofty
```

</details>


<details>
<summary>Create a table with a configured schemaDefault</summary>

**Create an empty table with an inline schemaDefault definition**

```shell
bq mk --table project_id:dataset.table schemaDefault
```

**Replace the following**:
- `project_id`: the name of the GCP project to target.
- `dataset`: the name of the BigQuery dataset to target.
- `table`: the name of the BigQuery table to target.
- `schemaDefault`: an inline schemaDefault definition.

Example:

```shell
bq mk --table \
  lofty-root-378503:test_dataset_name_lofty.test_table_name_lofty \
  id:STRING,fieldA:STRING,fieldB:STRING,fieldC:STRING,fieldD:STRING
```

### Specify the schemaDefault in a JSON schemaDefault file

For an example JSON schemaDefault file, refer to: `/schemaDefault/example.json`.

**Create an empty table**

```shell
bq mk --table \
  project_id:dataset.table \
  path_to_schema_file
```

Example:

```shell
bq mk --table \
  lofty-root-378503:test_dataset_name_lofty.test_table_name_lofty \
  ./schemaDefault/example.json
```

**Create a table with CSV data**

```shell
bq --location=location load \
  --source_format=format \
  project_id:dataset.table \
  path_to_data_file \
  path_to_schema_file
```

Example:

```shell
bq --location=us load \
  --source_format=CSV \
  lofty-root-378503:test_dataset_name_lofty.test_table_name_lofty \
  ./csv/example.csv \
  ./schemaDefault/example.json
```

Refer to the BigQuery documentation: <a href="https://cloud.google.com/bigquery/docs/loading-data-cloud-storage-csv#details_of_loading_csv_data">Details of loading CSV data</a>.

</details>


<details>
<summary>Delete a table</summary>

```shell
bq rm --table test_dataset_name_lofty.test_table_name_lofty
```

</details>


<details>
<summary>Show table schemaDefault</summary>

Example:

```shell
bq show \
  --schemaDefault \
  --format=prettyjson \
  lofty-root-378503:test_dataset_name_lofty.test_table_name_lofty
```

The table schemaDefault can be written to a file:

```shell
bq show \
  --schemaDefault \
  --format=prettyjson \
  lofty-root-378503:test_dataset_name_lofty.test_table_name_lofty \ > ./schemaDefault/example_show-write.json
```

</details>


<details>
<summary>Modify table schemas</summary>

```shell
bq update \
  lofty-root-378503:test_dataset_name_lofty.test_table_name_lofty \
  ./schemaDefault/example_update.json
```

Refer to the <a href="https://cloud.google.com/bigquery/docs/managing-table-schemas">GCP documentation on modifying table schemas.</a>.

</details>


<details>
<summary>Insert data into a table</summary>

**Examples**:

Insert for known values:

```shell
bq insert test_dataset_name_lofty.test_table_name_lofty ./json/example.json
```

Specify a template suffix (`--template_suffix` or `-x`):

```shell
bq insert --ignore_unknown_values \
  --template_suffix=_insert \
  test_dataset_name_lofty.test_table_name_lofty \
  ./json/example.json
```

Refer to the <a href="">`bq insert` documentation</a>.

</details>


<details>
<summary>Run an interactive query</summary>

```shell
bq query \
  --use_legacy_sql=false \
  'query_string'
```

Example:

```shell
bq query \
  --use_legacy_sql=false \
  'SELECT
    id, fieldC
  FROM
    `lofty-root-378503.test_dataset_name_lofty.test_table_name_lofty`
  LIMIT
    3;'
```

</details>
