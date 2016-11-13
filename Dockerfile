FROM sportchef/ping
COPY build/libs/sportchef-events.war ${DEPLOYMENT_DIR}
ENV ARCHIVE_NAME sportchef-events.war
