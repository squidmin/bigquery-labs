#!/bin/bash
mvn spring-boot:run \
  -Dspring-boot.run.profiles=local \
  -DGOOGLE_APPLICATION_CREDENTIALS=$DGOOGLE_APPLICATION_CREDENTIALS