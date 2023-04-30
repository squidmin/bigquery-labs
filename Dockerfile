FROM eclipse-temurin:11-jdk-alpine

### Build arguments ###
ARG GCP_PROJECT_ID
###

### Environment variables ###
# OS
ENV APP_DIR=/usr/local/app
# JVM arguments.
ENV GCP_PROJECT_ID=$GCP_PROJECT_ID
###

# Copy the project into the container.
ADD . ${APP_DIR}

ENTRYPOINT ["sh", "-c", "cd /usr/local/app && sh"]
