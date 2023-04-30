#!/bin/bash

# Globals.
LOCAL_GCLOUD_AUTH_DIRECTORY=$HOME/.config/gcloud
CONTAINER_GCLOUD_AUTH_DIRECTORY=/root/.config/gcloud
GCLOUD_SA_KEY_FILENAME=sa-private-key.json

POSITIONAL_ARGS=()

while [[ $# -gt 0 ]]; do
  case $1 in
    -gcppid|--GCP_PROJECT_ID)
      GCP_PROJECT_ID="$2"
      shift; # past argument
      shift; # past value
      ;;
    -gac|--GOOGLE_APPLICATION_CREDENTIALS)
      GOOGLE_APPLICATION_CREDENTIALS="$2"
      shift # past argument
      shift # past value
      ;;
    -adcat|--GCP_ADC_ACCESS_TOKEN)
      GCP_ADC_ACCESS_TOKEN="$2"
      shift # past argument
      shift # past value
      ;;
    -saat|--GCP_SA_ACCESS_TOKEN)
      GCP_SA_ACCESS_TOKEN="$2"
      shift # past argument
      shift # past value
      ;;
    -i|--impersonate)
      IMPERSONATED_SERVICE_ACCOUNT="$2"
      echo "Attempting impersonation of service account: $2"
      GCP_SA_ACCESS_TOKEN=$(gcloud auth print-access-token --impersonate-service-account="${IMPERSONATED_SERVICE_ACCOUNT}")
      shift # past argument
      shift # past value
      ;;
    --default)
      DEFAULT=YES
      echo "Building and running the application with default settings."
      GOOGLE_APPLICATION_CREDENTIALS=${LOCAL_GCLOUD_AUTH_DIRECTORY}/${GCLOUD_SA_KEY_FILENAME}
      GCP_ADC_ACCESS_TOKEN=$(gcloud auth application-default print-access-token)
      GCP_SA_ACCESS_TOKEN=sa_access_token_placeholder
      shift # past argument
      ;;
    -ci|--container-instance)
      CONTAINER_INSTANCE=YES
      GOOGLE_APPLICATION_CREDENTIALS=${CONTAINER_GCLOUD_AUTH_DIRECTORY}/${GCLOUD_SA_KEY_FILENAME}
      shift # past argument
      ;;
    -nci|--no-container-instance)
      CONTAINER_INSTANCE=NO
      GOOGLE_APPLICATION_CREDENTIALS=${LOCAL_GCLOUD_AUTH_DIRECTORY}/${GCLOUD_SA_KEY_FILENAME}
      ;;
    -*|--*)
      echo "Unknown option $1"
      exit 1
      ;;
    *)
      POSITIONAL_ARGS+=("$1") # save positional arg
      shift # past argument
      ;;
  esac
done

set -- "${POSITIONAL_ARGS[@]}" # Restore CLI positional parameters.

echo "GCP_PROJECT_ID                  = ${GCP_PROJECT_ID}"
echo "LOCAL_GCLOUD_AUTH_DIRECTORY     = ${LOCAL_GCLOUD_AUTH_DIRECTORY}"
echo "GCLOUD_SA_KEY_FILENAME          = ${GCLOUD_SA_KEY_FILENAME}"
echo "GOOGLE_APPLICATION_CREDENTIALS  = ${GOOGLE_APPLICATION_CREDENTIALS}"
echo "GCP_ADC_ACCESS_TOKEN            = ${GCP_ADC_ACCESS_TOKEN}"
echo "GCP_SA_ACCESS_TOKEN             = ${GCP_SA_ACCESS_TOKEN}"
echo "IMPERSONATED_SERVICE_ACCOUNT    = ${IMPERSONATED_SERVICE_ACCOUNT}"
echo "DEFAULT                         = ${DEFAULT}"
echo "CONTAINER_INSTANCE              = ${CONTAINER_INSTANCE}"

if [[ -n $1 ]]; then
  echo "Last line of file specified as non-opt/last argument:"
  tail -1 "$1"
fi

chmod +x ./bash/build_jar.sh
chmod +x ./bash/build_image.sh
chmod +x ./bash/run_container.sh

./mvnw clean package

if [ -z "$GOOGLE_APPLICATION_CREDENTIALS" ]
then
  echo "GOOGLE_APPLICATION_CREDENTIALS not set. Will attempt GCP authentication with Application Default Credentials (ADC) access token."
  GOOGLE_APPLICATION_CREDENTIALS=""
fi

if [ -z "$CONTAINER_INSTANCE" ] || [ "$CONTAINER_INSTANCE" != "YES" ]
then
  echo "TODO: Run application with no container instance."
else
  if [ "$CONTAINER_INSTANCE" == "YES" ]
  then
    echo "Building container image."
    docker build \
      --build-arg GCP_PROJECT_ID=$GCP_PROJECT_ID \
      -t bigquery-labs .

    echo "Starting container instance."
    docker run --rm -it \
      -e GOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS \
      -e GCP_SA_ACCESS_TOKEN=$GCP_SA_ACCESS_TOKEN \
      -e GCP_ADC_ACCESS_TOKEN=$GCP_ADC_ACCESS_TOKEN \
      -v ${LOCAL_GCLOUD_AUTH_DIRECTORY}:${CONTAINER_GCLOUD_AUTH_DIRECTORY} \
      -v $HOME/.m2:/root/.m2 \
      bigquery-labs
  else
    echo "Something went wrong."
  fi
fi
