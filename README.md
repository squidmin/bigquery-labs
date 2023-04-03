# bigquery-labs

GCP BigQuery labs using the Java SDK.


### Quickstart

<details>
<summary>Create and store a service account key</summary>

**On macOS**, store the service account key at the location:

`/Users/USERNAME/.config/gcloud`

Replace the following:

- `USERNAME`: the current user's username

The `Dockerfile` will map the above directory to a volume on the container instance so that the **_service account key_** and **_application default credentials_** are available to the container instance.

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
-e GCP_PROJECT_ID=lofty-root-378503 \
-v $HOME/.config/gcloud:/root/.config/gcloud \
bigquery-labs
```

</details>


<details>
<summary>Activate GCP service account</summary>

```shell
/usr/local/google-cloud-sdk/bin/gcloud auth activate-service-account --key-file=${GOOGLE_APPLICATION_CREDENTIALS}
```

`GOOGLE_APPLICATION_CREDENTIALS` will be stored as an environment variable in the container instance.

</details>


<details>
<summary>Set the active GCP project</summary>

```shell
/usr/local/google-cloud-sdk/bin/gcloud config set project ${ENV_GCP_PROJECT_ID}
```

`ENV_GCP_PROJECT_ID` is the same project ID passed via the `docker run` command when starting the container.

</details>


<details>
<summary>List available gcloud components</summary>

```shell
/usr/local/google-cloud-sdk/bin/gcloud components list
```

</details>


<details>
<summary>Application Default Credentials (ADC) usage</summary>

In an interactive container instance, run the following commands:

```shell
/usr/local/google-cloud-sdk/bin/gcloud auth login
/usr/local/google-cloud-sdk/bin/gcloud auth application-default login
```

</details>


### Table admin

<details>
<summary>List BigQuery resource metadata configured for a particular Spring profile</summary>

Assuming the name of the profile to activate is `integration`:

```shell
mvn \
  -Dtest=BigQueryAdminClientIntegrationTest#echoDefaultBigQueryResourceMetadata \
  test -P integration \
  -Dschema="name_1,datatype_1;name_2,datatype_2;[...];name_n,datatype_n"
```

or

```shell
mvn \
  -DargLine="-Dspring.profiles.active=integration" \
  -Dtest=BigQueryAdminClientIntegrationTest#echoDefaultBigQueryResourceMetadata \
  test \
  -Dschema="name_1,datatype_1;name_2,datatype_2;[...];name_n,datatype_n"
```

</details>


<details>
<summary>List datasets</summary>

```shell
mvn \
  -Dtest=BigQueryAdminClientIntegrationTest#listDatasets \
  test -P integration
```

</details>


<details>
<summary>Create a dataset</summary>

```shell
mvn \
  -Dtest=BigQueryAdminClientIntegrationTest#createDataset \
  test -P integration \
  -DprojectId="project-id" \
  -DdatasetName="dataset_name"
```

</details>


<details>
<summary>Create a table with the configured default schema</summary>

```shell
mvn \
  -Dtest=BigQueryAdminClientIntegrationTest#createTableWithDefaultSchema \
  test -P integration \
  -DprojectId="project-id" \
  -DdatasetName="dataset_name" \
  -DtableName="table_name"
```

</details>


<details>
<summary>Create a table with a custom schema</summary>

```shell
mvn \
  -Dtest=BigQueryAdminClientIntegrationTest#createTableWithCustomSchema \
  test -P integration \
  -DprojectId="project-id" \
  -DdatasetName="dataset_name" \
  -DtableName="table_name" \
  -Dschema="name_1,datatype_1;name_2,datatype_2;[...];name_n,datatype_n"
```

</details>


### Run the tests

<details>
<summary>Run all test cases</summary>

```shell
mvn test
```

</details>


<details>
<summary>Run a single test class</summary>

```shell
mvn -Dtest=ClassTest1 test
```

</details>


<details>
<summary>Run multiple test classes</summary>

```shell
mvn -Dtest=ClassTest1,ClassTest2 test
```

</details>


<details>
<summary>Run a single test method from a test class</summary>

```shell
mvn -Dtest=ClassTest1#methodName test
```

</details>


<details>
<summary>Run a single test method from a test class with parameters</summary>

```shell
mvn -Dtest=ClassTest1#methodName test -key="value"
```

</details>


<details>
<summary>Run all test methods that match pattern "testHello*" from a test class</summary>

```shell
mvn -Dtest=ClassTest1#testHello* test
```

</details>


<details>
<summary>Run all test methods matching pattern "testHello*" and "testMagic*" from a test class</summary>

```shell
mvn -Dtest=ClassTest1#testHello*+testMagic* test
```

</details>


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
