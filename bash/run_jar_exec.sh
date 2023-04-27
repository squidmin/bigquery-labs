#!/bin/bash
exec java -jar \
  -Dspring.profiles.active=local \
  -DGOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS \
  ./build/libs/bigquery-labs-1.0.0-SNAPSHOT.jar
