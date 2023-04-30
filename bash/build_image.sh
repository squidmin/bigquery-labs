#!/bin/bash
docker build \
  --build-arg GCP_PROJECT_ID=PROJECT_ID \
  -t bigquery-labs .