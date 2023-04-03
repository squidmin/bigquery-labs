# bigquery-labs

GCP BigQuery labs using the Java SDK.

Made with:
- **IntelliJ IDEA 2022.2 (Community Edition)**
- **openjdk 11.0.17**


---


### Quickstart

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
<summary>Create and store a service account key</summary>

The `Dockerfile` of this project will map the directory:

`/Users/USERNAME/.config/gcloud`

to a volume on the container instance: 

`/root/.config/gcloud`

so that the **_service account key_** and **_application default credentials_** used locally are also available for the container instance.

Read <a href="https://cloud.google.com/iam/docs/keys-create-delete#iam-service-account-keys-create-gcloud">here</a> for more information about creating service account keys.

### macOS

Store the service account key at the location:

`/Users/USERNAME/.config/gcloud`

**Replace the following**:

- `USERNAME`: the current user's username

e.g.,

`/Users/squidmin/.config/gcloud/sa-private-key.json`

### Windows

TBD

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
<summary>Build the container image</summary>

```shell
docker build -t bigquery-labs .
```

</details>


<details>
<summary>Start a container instance</summary>

```shell
docker run \
  --rm -it \
  -e GCP_PROJECT_ID=PROJECT_ID \
  -v $HOME/.config/gcloud:/root/.config/gcloud \
  -v $HOME/.m2:/root/.m2 \
  bigquery-labs
```

**Replace the following**:
- `PROJECT_ID`: the project ID for the target GCP project.

Example with `lofty-root-305785` as the `GCP_PROJECT_ID`:

```shell
docker run \
  --rm -it \
  -e GCP_PROJECT_ID=lofty-root-305785 \
  -v $HOME/.config/gcloud:/root/.config/gcloud \
  -v $HOME/.m2:/root/.m2 \
  bigquery-labs
```

</details>


---


### `gcloud` usage in instances of the container

<details>
<summary>Using the gcloud executable</summary>

<blockquote>
<strong><i>Note</i></strong>: In the container image, the absolute path of the <code>gcloud</code> executable is assigned to the <code>$GCLOUD</code> environment variable.
<br/>
The below example demonstrates how to use this environment variable to execute <code>gcloud</code> commands:
<br/><br/>
<code>$GCLOUD auth activate-service-account --key-file=${GOOGLE_APPLICATION_CREDENTIALS}</code>
<br/><br/>
Instead of typing the absolute path, as in
<br/><br/>
<code>/usr/local/google-cloud-sdk/bin/gcloud</code>
<br/><br/>
run <code>gcloud</code> commands using this environment variable.
</blockquote>

</details>


<details>
<summary>Activate GCP service account</summary>

The container image stores the user's service account key in the `GOOGLE_APPLICATION_CREDENTIALS` environment variable, so the user can activate their service account using the below command:

```shell
$GCLOUD auth activate-service-account --key-file=${GOOGLE_APPLICATION_CREDENTIALS}
```

</details>


<details>
<summary>Set the active GCP project</summary>

```shell
$GCLOUD config set project ${GCP_PROJECT_ID}
```

`GCP_PROJECT_ID` is the same project ID passed via the `docker run` command when starting the container.

</details>


<details>
<summary>List available gcloud SDK components</summary>

```shell
$GCLOUD components list
```

</details>


<details>
<summary>Update gcloud SDK components</summary>

```shell
$GCLOUD components update
```

</details>


<details>
<summary>Application Default Credentials (ADC) usage</summary>

In an interactive container instance, run the following commands:

```shell
$GCLOUD auth login
$GCLOUD auth application-default login
```

</details>


---


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
- `PROJECT_ID`: the project ID of the GCP project to target.
- `PROFILE_NAME`: the name of the profile to activate.

Example:

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#listDatasets \
  test -P integration \
  -DprojectId="lofty-root-378503"
```

</details>


<details>
<summary>Create a dataset</summary>

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#createDataset \
  test -P integration \
  -DprojectId="PROJECT_ID" \
  -DdatasetName="DATASET_NAME"
```

**Replace the following**:
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
- `PROFILE_NAME`: the name of the profile to activate for the method execution.
- `PROJECT_ID`: the name of the GCP project ID to target.
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
- `PROFILE_NAME`: the name of the profile to activate for the method execution.
- `PROJECT_ID`: the name of the GCP project ID to target.
- `DATASET_NAME`: the name of the BigQuery dataset to target.
- `TABLE_NAME`: the name of the BigQuery table to target.

Example using the `integration` profile:

```shell
./mvnw \
  -Dtest=BigQueryAdminClientIntegrationTest#createTableWithCustomSchema \
  test -P integration \
  -DprojectId="lofty-root-378503" \
  -DdatasetName="test_dataset_123" \
  -DtableName="test_table_321" \
  -Dschema="id,string;client_name,string;active,bool;creation_timestamp,datetime;last_update_timestamp,datetime"
```

</details>


---


### Run the jar

<details>
<summary>Specify no profile</summary>

```shell
mvn spring-boot:run
```

</details>


<details>
<summary>Specify a profile</summary>

```shell
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

</details>
