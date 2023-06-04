#!/bin/bash

# Globals.
DEFAULT_GCP_PROJECT_ID=lofty-root-378503                 # Default user GCP project ID.
LOCAL_GCLOUD_AUTH_DIRECTORY=$HOME/.config/gcloud         # Local system directory containing GCP service account key file.
CONTAINER_GCLOUD_AUTH_DIRECTORY=/root/.config/gcloud     # Container directory containing GDP service account key file.
GCLOUD_SA_KEY_FILENAME=sa-private-key.json               # Name of GCP service account key file.
LOCAL_MAVEN_REPOSITORY=$HOME/.m2                         # Maven repository local system directory.
CONTAINER_MAVEN_REPOSITORY=/root/.m2                     # Maven repository container directory.

POSITIONAL_ARGS=()

while [[ $# -gt 0 ]]; do
  case $1 in
    -dpid|--GCP_DEFAULT_USER_PROJECT_ID)
      GCP_DEFAULT_USER_PROJECT_ID="$2"
      shift; # past argument
      shift; # past value
      ;;
    -sakp|--GCP_SA_KEY_PATH)
      GCP_SA_KEY_PATH="$2"
      shift; # past argument
      shift; # past value
      ;;
    -adcat|--GCP_ADC_ACCESS_TOKEN)
      GCP_ADC_ACCESS_TOKEN="$2"
      shift; # past argument
      shift; # past value
      ;;
    -saat|--GCP_SA_ACCESS_TOKEN)
      GCP_SA_ACCESS_TOKEN="$2"
      shift; # past argument
      shift; # past value
      ;;
    -i|--impersonate)
      IMPERSONATED_SERVICE_ACCOUNT="$2"
      echo "Attempting impersonation of service account: $2"
      GCP_SA_ACCESS_TOKEN=$(gcloud auth print-access-token --impersonate-service-account="${IMPERSONATED_SERVICE_ACCOUNT}")
      shift; # past argument
      shift; # past value
      ;;
    --default)
      DEFAULT=YES
      echo "Building and running the application with default settings."
      if [ -z "$GCP_DEFAULT_USER_PROJECT_ID" ]
      then
        echo "GCP_DEFAULT_USER_PROJECT_ID not set. Using default: ${DEFAULT_GCP_PROJECT_ID}."
        GCP_DEFAULT_USER_PROJECT_ID=$DEFAULT_GCP_PROJECT_ID
      fi
      GCP_SA_KEY_PATH=${LOCAL_GCLOUD_AUTH_DIRECTORY}/${GCLOUD_SA_KEY_FILENAME}
      GCP_ADC_ACCESS_TOKEN=$(gcloud auth application-default print-access-token)
      GCP_SA_ACCESS_TOKEN=sa_access_token_placeholder
      shift; # past argument
      ;;
    -ci|--container-instance)
      CONTAINER_INSTANCE=YES
      GCP_SA_KEY_PATH=${CONTAINER_GCLOUD_AUTH_DIRECTORY}/${GCLOUD_SA_KEY_FILENAME}
      shift; # past argument
      ;;
    -nci|--no-container-instance)
      CONTAINER_INSTANCE=NO
      GCP_SA_KEY_PATH=${LOCAL_GCLOUD_AUTH_DIRECTORY}/${GCLOUD_SA_KEY_FILENAME}
      shift; # past argument
      ;;
    -*|--*)
      echo "Unknown option $1"
      exit 1
      ;;
    *)
      POSITIONAL_ARGS+=("$1") # save positional arg
      shift; # past argument
      ;;
  esac
done

set -- "${POSITIONAL_ARGS[@]}" # Restore CLI positional parameters.

echo "DEFAULT                          = ${DEFAULT}"
echo "CONTAINER_INSTANCE               = ${CONTAINER_INSTANCE}"
echo "GCP_SA_KEY_PATH                  = ${GCP_SA_KEY_PATH}"
echo "GCLOUD_SA_KEY_FILENAME           = ${GCLOUD_SA_KEY_FILENAME}"
echo "IMPERSONATED_SERVICE_ACCOUNT     = ${IMPERSONATED_SERVICE_ACCOUNT}"
echo "GCP_ADC_ACCESS_TOKEN             = ${GCP_ADC_ACCESS_TOKEN}"
echo "GCP_SA_ACCESS_TOKEN              = ${GCP_SA_ACCESS_TOKEN}"
echo "GCP_DEFAULT_USER_PROJECT_ID      = ${GCP_DEFAULT_USER_PROJECT_ID}"
echo "GCP_DEFAULT_USER_DATASET         = ${GCP_DEFAULT_USER_DATASET}"
echo "GCP_DEFAULT_USER_TABLE           = ${GCP_DEFAULT_USER_TABLE}"
echo "GCP_SA_PROJECT_ID                = ${GCP_SA__PROJECT_ID}"
echo "GCP_SA_DATASET                   = ${GCP_SA_DATASET}"
echo "GCP_SA_TABLE                     = ${GCP_SA_TABLE}"
echo "LOCAL_GCLOUD_AUTH_DIRECTORY      = ${LOCAL_GCLOUD_AUTH_DIRECTORY}"
echo "CONTAINER_GCLOUD_AUTH_DIRECTORY  = ${CONTAINER_GCLOUD_AUTH_DIRECTORY}"
echo "LOCAL_MAVEN_REPOSITORY           = ${LOCAL_MAVEN_REPOSITORY}"
echo "CONTAINER_MAVEN_REPOSITORY       = ${CONTAINER_MAVEN_REPOSITORY}"

if [[ -n $1 ]]; then
  echo "Last line of file specified as non-opt/last argument:"
  tail -1 "$1"
fi

chmod +x ./bash/build_jar.sh
chmod +x ./bash/build_image.sh
chmod +x ./bash/run_container.sh

./mvnw clean package -P integration \
  -DGCP_SA_KEY_PATH=${GCP_SA_KEY_PATH} \
  -DGCP_ADC_ACCESS_TOKEN=${GCP_ADC_ACCESS_TOKEN} \
  -DGCP_SA_ACCESS_TOKEN=${GCP_SA_ACCESS_TOKEN} \
  -DGCP_DEFAULT_USER_PROJECT_ID=${GCP_DEFAULT_USER_PROJECT_ID} \
  -DGCP_DEFAULT_USER_DATASET=${GCP_DEFAULT_USER_DATASET} \
  -DGCP_DEFAULT_USER_TABLE=${GCP_DEFAULT_USER_TABLE} \
  -DGCP_SA_PROJECT_ID=${GCP_SA_PROJECT_ID} \
  -DGCP_SA_DATASET=${GCP_SA_DATASET} \
  -DGCP_SA_TABLE=${GCP_SA_TABLE}

if [ -z "$GCP_SA_KEY_PATH" ]
then
  echo "GCP_SA_KEY_PATH not set. Will attempt GCP authentication with Application Default Credentials (ADC) access token."
  GCP_SA_KEY_PATH=""
fi

if [ -z "${CONTAINER_INSTANCE}" ] || [ "${CONTAINER_INSTANCE}" != "YES" ] || [ "${CONTAINER_INSTANCE}" == "NO" ]
then
  echo "Running JAR on host system."
  echo "TODO: Run application with no container instance."
else
  if [ "${CONTAINER_INSTANCE}" == "YES" ]
  then
    echo "Building container image."
    docker build \
      --build-arg GCP_SA_KEY_PATH=${GCP_SA_KEY_PATH} \
      --build-arg GCP_DEFAULT_USER_PROJECT_ID=${GCP_DEFAULT_USER_PROJECT_ID} \
      --build-arg GCP_DEFAULT_USER_DATASET=${GCP_DEFAULT_USER_DATASET} \
      --build-arg GCP_DEFAULT_USER_TABLE=${GCP_DEFAULT_USER_TABLE} \
      --build-arg GCP_SA_PROJECT_ID=${GCP_SA_PROJECT_ID} \
      --build-arg GCP_SA_DATASET=${GCP_SA_DATASET} \
      --build-arg GCP_SA_TABLE=${GCP_SA_TABLE} \
      -t bigquery-labs .

    echo "Starting container instance."
    docker run --rm -it \
      -e GCP_SA_KEY_PATH=${GCP_SA_KEY_PATH} \
      -e GCP_ADC_ACCESS_TOKEN=${GCP_ADC_ACCESS_TOKEN} \
      -e GCP_SA_ACCESS_TOKEN=${GCP_SA_ACCESS_TOKEN} \
      -e GCP_DEFAULT_USER_PROJECT_ID=${GCP_DEFAULT_USER_PROJECT_ID} \
      -e GCP_DEFAULT_USER_DATASET=${GCP_DEFAULT_USER_DATASET} \
      -e GCP_DEFAULT_USER_TABLE=${GCP_DEFAULT_USER_TABLE} \
      -e GCP_SA_PROJECT_ID=${GCP_SA_PROJECT_ID} \
      -e GCP_SA_DATASET=${GCP_SA_DATASET} \
      -e GCP_SA_TABLE=${GCP_SA_TABLE} \
      -v ${LOCAL_GCLOUD_AUTH_DIRECTORY}:${CONTAINER_GCLOUD_AUTH_DIRECTORY} \
      -v ${LOCAL_MAVEN_REPOSITORY}:${CONTAINER_MAVEN_REPOSITORY} \
      bigquery-labs
  else
    echo "Invalid run configuration. Please review parameters and options passed to the 'run.sh' script."
  fi
fi
