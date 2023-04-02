# bigquery-labs

GCP BigQuery labs using the Java SDK.


### Using Application Default Credentials (ADC)

If you haven't done so already, install the `gcloud` SDK.

After installing the `gcloud` SDK, run the following commands:

```shell
gcloud auth login
gcloud auth application-default login
```


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
