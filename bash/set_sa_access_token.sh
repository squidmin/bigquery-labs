#!/bin/bash
export GCP_SA_ACCESS_TOKEN=$(gcloud auth print-access-token --impersonate-service-account='9644524330-compute@developer.gserviceaccount.com')
