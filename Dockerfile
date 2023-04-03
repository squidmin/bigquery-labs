FROM eclipse-temurin:11-jdk-alpine

# CLI arguments.
ARG GCP_PROJECT_ID

### Environment variables ###
# OS
ENV APP_DIR=/usr/local/app
# gcloud CLI
ENV GCLOUD_CLI_BASE_URL=https://dl.google.com/dl/cloudsdk/channels/rapid/downloads/
ENV GCLOUD_CLI_TARGET=google-cloud-cli-387.0.0-linux-x86_64
ENV GOOGLE_APPLICATION_CREDENTIALS=/root/.config/gcloud/sa-private-key.json
ENV GCLOUD=/usr/local/google-cloud-sdk/bin/gcloud
# Maven
ENV MAVEN_ARCHIVE=apache-maven-3.9.1-bin.tar.gz
ENV M2_HOME=/usr/local/apache-maven/apache-maven-3.9.1
ENV M2=$M2_HOME/bin
ENV MAVEN_OPTS="-Xms256m -Xmx512m"
# Python
ENV PYTHONUNBUFFERED=1
###

# Install Python 3 and pip. (Required for gcloud SDK.)
RUN apk add --update --no-cache python3 && ln -sf python3 /usr/bin/python
RUN python3 -m ensurepip
RUN pip3 install --no-cache --upgrade pip setuptools

# Install gcloud SDK.
RUN apk --no-cache add curl
RUN curl -O ${GCLOUD_CLI_BASE_URL}${GCLOUD_CLI_TARGET}.tar.gz && \
    mv ${GCLOUD_CLI_TARGET}.tar.gz /usr/local
RUN cd /usr/local && \
    tar -xf ${GCLOUD_CLI_TARGET}.tar.gz && \
    ./google-cloud-sdk/install.sh
RUN ${GCLOUD} components update

# Copy the project into the container.
ADD . ${APP_DIR}

# Download and install build dependencies.
#RUN cd /usr/local/app && ./mvnw clean install

ENTRYPOINT ["sh", "-c", "cd /usr/local/app && sh"]
