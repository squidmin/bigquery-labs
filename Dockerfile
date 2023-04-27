FROM eclipse-temurin:11-jdk-alpine

### CLI arguments ###
ARG GCP_PROJECT_ID
ARG GCP_ACCESS_TOKEN
###

### Environment variables ###
# OS
ENV APP_DIR=/usr/local/app
# JVM arguments.
ENV GCP_PROJECT_ID=$GCP_PROJECT_ID
ENV GOOGLE_APPLICATION_CREDENTIALS=$GCP_ACCESS_TOKEN
###

# Copy the project into the container.
ADD . ${APP_DIR}

ENTRYPOINT ["sh", "-c", "cd /usr/local/app && sh"]
