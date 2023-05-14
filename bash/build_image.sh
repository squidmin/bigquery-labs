#!/bin/bash
docker build \
  --build-arg GCP_SA_KEY_PATH=${GCP_SA_KEY_PATH} \
  --build-arg GCP_DEFAULT_USER_PROJECT_ID=${GCP_DEFAULT_USER_PROJECT_ID} \
  --build-arg GCP_DEFAULT_USER_DATASET=${GCP_DEFAULT_USER_DATASET} \
  --build-arg GCP_DEFAULT_USER_TABLE=${GCP_DEFAULT_USER_TABLE} \
  --build-arg GCP_SA_PROJECT_ID=${GCP_SA_PROJECT_ID} \
  --build-arg GCP_SA_DATASET=${GCP_SA_DATASET} \
  --build-arg GCP_SA_TABLE=${GCP_SA_TABLE} \
  -t bigquery-labs .