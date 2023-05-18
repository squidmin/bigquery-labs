# bigquery-labs

GCP BigQuery labs using the Java SDK.


<details>
<summary>About</summary>

Made with:
- **Adoptium Temurin OpenJDK 11.0.17**
- **Spring Boot v2.4.3**
- **Apache Maven 3.8.7**
- **IntelliJ IDEA 2023.1 (Ultimate Edition)**
- **GoLand 2023.1 (Ultimate Edition)**
- **PyCharm 2023.1 (Ultimate Edition)**

</details>


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
export GCP_ADC_ACCESS_TOKEN="$(gcloud auth application-default print-access-token)"
```

</details>


<details>
<summary>gcloud CLI: Generate a GCP service account access token</summary>

Run this command to generate an access token for a specific GCP service account:

```shell
export GCP_SA_ACCESS_TOKEN=$(gcloud auth print-access-token --impersonate-service-account='GCP_SA_EMAIL_ADDRESS')
```

**Replace the following**:
- `GCP_SA_EMAIL_ADDRESS`: the email address of the service account to impersonate.

Example:

```shell
export GCP_SA_ACCESS_TOKEN=$(gcloud auth print-access-token --impersonate-service-account='sa-developer@your-sa-name.iam.gserviceaccount.com')
```

</details>


<details>
<summary>Create and store a service account key</summary>

This section refers to usage of a GCP service account key (.json) file stored on your local file system.

To map a local `gcloud` installation to a volume on a container instance running the application, include the `-v` parameter in the `docker run` command used to start a container instance, as described below. 

### macOS

Assuming the user's service account key file is stored in the same directory as their local `gcloud` installation:

`/Users/USERNAME/.config/gcloud`

```shell
export LOCAL_GCLOUD_AUTH_DIRECTORY=$HOME/.config/gcloud
```

and the target volume on the container instance is: 

`/root/.config/gcloud`

```shell
export CONTAINER_GCLOUD_AUTH_DIRECTORY=/root/.config/gcloud
```

the command to run the container instance would be:

```shell
docker run --rm -it \
  -e GCP_SA_KEY_PATH=$GCP_SA_KEY_PATH \
  -e GCP_ADC_ACCESS_TOKEN=$GCP_ADC_ACCESS_TOKEN \
  -e GCP_SA_ACCESS_TOKEN=$GCP_SA_ACCESS_TOKEN \
  -e GCP_DEFAULT_USER_PROJECT_ID=$GCP_DEFAULT_USER_PROJECT_ID \
  -e GCP_DEFAULT_USER_DATASET=$GCP_DEFAULT_USER_DATASET \
  -e GCP_DEFAULT_USER_TABLE=$GCP_DEFAULT_USER_TABLE \
  -e GCP_SA_PROJECT_ID=$GCP_SA_PROJECT_ID \
  -e GCP_SA_DATASET=$GCP_SA_DATASET \
  -e GCP_SA_TABLE=$GCP_SA_TABLE \
  -v ${LOCAL_GCLOUD_AUTH_DIRECTORY}:${CONTAINER_GCLOUD_AUTH_DIRECTORY} \
  -v ${LOCAL_MAVEN_REPOSITORY}:${CONTAINER_MAVEN_REPOSITORY} \
  bigquery-labs
```

**Replace the following** in the path to the `gcloud` directory:

- `USERNAME`: the current OS user's username

so that the path to the service account key file is correct, e.g.:

`/Users/squidmin/.config/gcloud/sa-private-key.json`

Read <a href="https://cloud.google.com/iam/docs/keys-create-delete#iam-service-account-keys-create-gcloud">here</a> for more information about creating service account keys.

Read <a href="">here</a> for more information about run config CLI arguments.

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
  -DGCP_SA_KEY_PATH=$GCP_SA_KEY_PATH \
  -DGCP_ADC_ACCESS_TOKEN=$GCP_ADC_ACCESS_TOKEN \
  -DGCP_SA_ACCESS_TOKEN=$GCP_SA_ACCESS_TOKEN \
  -DGCP_DEFAULT_USER_PROJECT_ID=$GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET=$GCP_DEFAULT_USER_DATASET \
  -DGCP_DEFAULT_USER_TABLE=$GCP_DEFAULT_USER_TABLE \
  -DGCP_SA_PROJECT_ID=$GCP_SA_PROJECT_ID \
  -DGCP_SA_DATASET=$GCP_SA_DATASET \
  -DGCP_SA_TABLE=$GCP_SA_TABLE
```

Or use `mvn clean install`:

```shell
./mvnw clean install -P integration \
  -DGCP_SA_KEY_PATH=$HOME/.config/gcloud \
  -DGCP_ADC_ACCESS_TOKEN="$(gcloud auth application-default print-access-token)" \
  -DGCP_SA_ACCESS_TOKEN=placeholder \
  -DGCP_DEFAULT_USER_PROJECT_ID=lofty-root-378503 \
  -DGCP_DEFAULT_USER_DATASET=test_dataset_integration \
  -DGCP_DEFAULT_USER_TABLE=test_table_integration \
  -DGCP_SA_PROJECT_ID=placeholder \
  -DGCP_SA_DATASET=placeholder \
  -DGCP_SA_TABLE=placeholder
```

Read <a href="">here</a> for more information about run config CLI arguments.

</details>


<details>
<summary>Build a container image</summary>

```shell
docker build \
  --build-arg GCP_SA_KEY_PATH=${GCP_SA_KEY_PATH} \
  --build-arg GCP_DEFAULT_USER_PROJECT_ID=${GCP_DEFAULT_USER_PROJECT_ID} \
  --build-arg GCP_DEFAULT_USER_DATASET=${GCP_DEFAULT_USER_DATASET} \
  --build-arg GCP_DEFAULT_USER_TABLE=${GCP_DEFAULT_USER_TABLE} \
  --build-arg GCP_SA_PROJECT_ID=${GCP_SA_PROJECT_ID} \
  --build-arg GCP_SA_DATASET=${GCP_SA_DATASET} \
  --build-arg GCP_SA_TABLE=${GCP_SA_TABLE} \
  -t bigquery-labs .
```

Read <a href="">here</a> for more information about run config CLI arguments.

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
  -Dtest=BigQueryAdminClientIntegrationTest#listDatasets \
  test -P integration \
  -DGCP_SA_KEY_PATH=$GCP_SA_KEY_PATH \
  -DGCP_ADC_ACCESS_TOKEN=$GCP_ADC_ACCESS_TOKEN \
  -DGCP_SA_ACCESS_TOKEN=$GCP_SA_ACCESS_TOKEN \
  -DGCP_DEFAULT_USER_PROJECT_ID=$GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET=$GCP_DEFAULT_USER_DATASET \
  -DGCP_DEFAULT_USER_TABLE=$GCP_DEFAULT_USER_TABLE
```

</details>


### 1b. Prepare the container environment

<details>
<summary>Expand</summary>

### Environment variables

The `GCP_SA_KEY_PATH` environment variable is used to store the path to the user's GCP service account key file.
The user's service account key file is mapped to the `/root/.config/gcloud` directory on the container instance.

```shell
export GCP_SA_KEY_PATH=/root/.config/gcloud/sa-private-key.json
```

The `GCP_ADC_ACCESS_TOKEN` environment variable is used to store an OAuth2 access token for reaching BigQuery RESTful services _using Application Default Credentials_ (ADC).

```shell
export GCP_ADC_ACCESS_TOKEN=$(gcloud auth application-default print-access-token)
```

The `GCP_SA_ACCESS_TOKEN` environment variable is used to store an OAuth2 access token for reaching BigQuery RESTful services _as a specific service account_.

```shell
export GCP_SA_ACCESS_TOKEN=$(gcloud auth print-access-token --impersonate-service-account="sa-developer@your-sa-name.iam.gserviceaccount.com")
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
  -e GCP_SA_KEY_PATH=$GCP_SA_KEY_PATH \
  -e GCP_ADC_ACCESS_TOKEN=$GCP_ADC_ACCESS_TOKEN \
  -e GCP_SA_ACCESS_TOKEN=$GCP_SA_ACCESS_TOKEN \
  -e GCP_DEFAULT_USER_PROJECT_ID=$GCP_DEFAULT_USER_PROJECT_ID \
  -e GCP_DEFAULT_USER_DATASET=$GCP_DEFAULT_USER_DATASET \
  -e GCP_DEFAULT_USER_TABLE=$GCP_DEFAULT_USER_TABLE \
  -e GCP_SA_PROJECT_ID=$GCP_SA_PROJECT_ID \
  -e GCP_SA_DATASET=$GCP_SA_DATASET \
  -e GCP_SA_TABLE=$GCP_SA_TABLE \
  -v ${LOCAL_GCLOUD_AUTH_DIRECTORY}:${CONTAINER_GCLOUD_AUTH_DIRECTORY} \
  -v ${LOCAL_MAVEN_REPOSITORY}:${CONTAINER_MAVEN_REPOSITORY} \
  bigquery-labs
```

Read <a href="">here</a> for more information about run config CLI arguments.

---

### `./run.sh` utility script

```shell
./run.sh \
  -dpid $GCP_DEFAULT_USER_PROJECT_ID \
  -sakp $HOME/.config/gcloud/sa-private-key.json \
  -saat access_token_placeholder \
  -adcat $(gcloud auth application-default print-access-token)
```

Or use long arguments:

```shell
./run.sh \
  --GCP_DEFAULT_USER_PROJECT_ID $GCP_DEFAULT_USER_PROJECT_ID \
  --GCP_SA_KEY_PATH $HOME/.config/gcloud/sa-private-key.json \
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

- `-i GCP_SA_EMAIL_ADDRESS`, `--impersonate GCP_SA_EMAIL_ADDRESS`: Impersonate a GCP service account.

  Replace `GCP_SA_EMAIL_ADDRESS` with the email address of the service account to impersonate.

  <details>
  <summary>Example</summary>

  ```shell
  ./run.sh --default -ci --impersonate-service-account GCP_SA_EMAIL_ADDRESS
  ```

  **Replace the following**:
  - `GCP_SA_EMAIL_ADDRESS`: the impersonated service account's email address.

  </details>

</details>


### Other examples

<details>
<summary>Build JAR, build image, & start an interactive container instance with default run environment settings</summary>

```shell
./run.sh --default --container-instance
```

```shell
./run.sh --default -ci
```

</details>


<details>
<summary>Build and run JAR with default run environment settings</summary>

```shell
./run.sh --default
```

```shell
./run.sh --default --no-container-instance
```

```shell
./run.sh --default -nci
```

</details>


</details>


### 3. Run the JAR

> Note: This section currently is only used for testing the main application entrypoint.
>
> This project currently invokes the BigQuery Java SDK (and later the BigQuery RESTful services) via the `mvn test` interface. Use that for now.
> 
> _**Refer to**_: <a href="https://github.com/squidmin/bigquery-labs/blob/main/src/test/README.md">`/src/test` README.md</a>.

<details>
<summary>Using "exec java" command. Specify a profile.</summary>

```shell
exec java -jar \
  -Dspring.profiles.active=$PROFILE \
  -DGCP_SA_KEY_PATH=$GCP_SA_KEY_PATH \
  -DGCP_ADC_ACCESS_TOKEN=$GCP_ADC_ACCESS_TOKEN \
  -DGCP_SA_ACCESS_TOKEN=$GCP_SA_ACCESS_TOKEN \
  -DGCP_DEFAULT_USER_PROJECT_ID=$GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_integration" \
  -DGCP_DEFAULT_USER_TABLE="test_table_integration_custom" \
  ./target/bigquery-labs-0.0.1-SNAPSHOT.jar
```

</details>


<details>
<summary>With Maven. Specify a profile.</summary>

```shell
mvn spring-boot:run \
  -Dspring-boot.run.profiles=$PROFILE \
  -DGCP_SA_KEY_PATH=$GCP_SA_KEY_PATH \
  -DGCP_ADC_ACCESS_TOKEN=$GCP_ADC_ACCESS_TOKEN \
  -DGCP_SA_ACCESS_TOKEN=$GCP_SA_ACCESS_TOKEN \
  -DGCP_DEFAULT_USER_PROJECT_ID=$GCP_DEFAULT_USER_PROJECT_ID \
  -DGCP_DEFAULT_USER_DATASET="test_dataset_integration" \
  -DGCP_DEFAULT_USER_TABLE="test_table_integration_custom"
```

</details>


---


## Table admin

### `bq` CLI

<details>
<summary>List datasets</summary>

```shell
bq ls --filter labels.key:value \
  --max_results INTEGER \
  --format=prettyjson \
  --project_id PROJECT_ID
```

**Replace the following**:
- `key:value`: a label key and value, if applicable.
- `INTEGER`: an integer representing the number of datasets to list.
- `PROJECT_ID`: the name of the GCP project containing the datasets to list.

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
  ${GCP_DEFAULT_USER_PROJECT_ID}:${GCP_DEFAULT_USER_DATASET}
```

The Cloud Key Management Service (KMS) key parameter (`KMS_KEY_NAME`) can be specified.
This parameter is used to pass the name of the default Cloud Key Management Service key used to protect newly created tables in this dataset.
You cannot create a Google-encrypted table in a dataset with this parameter set.

```shell
bq --location=us mk \
  --dataset \
  --default_kms_key=KMS_KEY_NAME \
  ... \
  ${GCP_DEFAULT_USER_PROJECT_ID}:${GCP_DEFAULT_USER_DATASET}
```

</details>


<details>
<summary>Delete a dataset</summary>

Refer to the <a href="https://cloud.google.com/bigquery/docs/managing-datasets#delete_a_dataset">GCP documentation for deleting a dataset</a>.

#### Examples:

Remove all tables in the dataset (`-r` flag):

```shell
bq rm -r -f -d ${GCP_DEFAULT_USER_PROJECT_ID}:${GCP_DEFAULT_USER_DATASET}
```

</details>


<details>
<summary>Create a table with a configured schema</summary>

**Create an empty table with an inline schema definition**

```shell
bq mk --table PROJECT_ID:DATASET.TABLE SCHEMA
```

**Replace the following**:
- `PROJECT_ID`: the name of the GCP project to target.
- `DATASET`: the name of the BigQuery dataset to target.
- `TABLE`: the name of the BigQuery table to target.
- `SCHEMA`: an inline schema definition.

Example:

```shell
bq mk --table \
  ${GCP_DEFAULT_USER_PROJECT_ID}:${GCP_DEFAULT_USER_DATASET}.test_table_name_lofty \
  id:STRING,creation_timestamp:DATETIME,last_update_timestamp:DATETIME,column_a:STRING,column_b:BOOL
```

### Specify the schema in a JSON schema file

For an example JSON schema file, refer to: `/schema/example.json`.

**Create an empty table**

```shell
bq mk --table \
  PROJECT_ID:DATASET.TABLE \
  ./path/to/schema/file.json
```

Example:

```shell
bq mk --table \
  ${GCP_DEFAULT_USER_PROJECT_ID}:${GCP_DEFAULT_USER_DATASET}.test_table_name_lofty \
  ./schema/example.json
```

**Create a table with CSV data**

```shell
bq --location=location load \
  --source_format=format \
  PROJECT_ID:DATASET.TABLE \
  ./path/to/data/file.csv \
  ./path/to/schema/file.json
```

Example:

```shell
bq --location=us load \
  --source_format=CSV \
  ${GCP_DEFAULT_USER_PROJECT_ID}:${GCP_DEFAULT_USER_DATASET}.test_table_name_lofty \
  ./csv/example.csv \
  ./schema/example.json
```

Refer to the BigQuery documentation: <a href="https://cloud.google.com/bigquery/docs/loading-data-cloud-storage-csv#details_of_loading_csv_data">Details of loading CSV data</a>.

</details>


<details>
<summary>Delete a table</summary>

```shell
bq rm --table ${GCP_DEFAULT_USER_PROJECT_ID}:${GCP_DEFAULT_USER_DATASET}.test_table_name_lofty
```

</details>


<details>
<summary>Show table schema</summary>

Example:

```shell
bq show \
  --schema \
  --format=prettyjson \
  ${GCP_DEFAULT_USER_PROJECT_ID}:${GCP_DEFAULT_USER_DATASET}.test_table_name_lofty
```

The table schema can be written to a file:

```shell
bq show \
  --schema \
  --format=prettyjson \
  ${GCP_DEFAULT_USER_PROJECT_ID}:${GCP_DEFAULT_USER_DATASET}.test_table_name_lofty \ > ./schema/example_show-write.json
```

</details>


<details>
<summary>Modify table schemas</summary>

```shell
bq update \
  ${GCP_DEFAULT_USER_PROJECT_ID}:${GCP_DEFAULT_USER_DATASET}.test_table_name_lofty \
  ./schema/example_update.json
```

Refer to the <a href="https://cloud.google.com/bigquery/docs/managing-table-schemas">GCP documentation on modifying table schemas.</a>.

</details>


<details>
<summary>Insert data into a table</summary>

**Examples**:

Insert for known values:

```shell
bq insert ${GCP_DEFAULT_USER_PROJECT_ID}:${GCP_DEFAULT_USER_DATASET}.test_table_name_lofty ./json/example.json
```

Specify a template suffix (`--template_suffix` or `-x`):

```shell
bq insert --ignore_unknown_values \
  --template_suffix=_insert \
  ${GCP_DEFAULT_USER_PROJECT_ID}:${GCP_DEFAULT_USER_DATASET}.test_table_name_lofty \
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
    id, column_b
  FROM
    `lofty-root-378503.test_dataset_name_lofty.test_table_name_lofty`
  LIMIT
    3;'
```

</details>
