FROM airhacks/java
RUN mkdir -p /opt/sportchef-events
COPY ./build/libs/sportchef-events-2.0-SNAPSHOT-all.jar /opt/sportchef-events
WORKDIR "/opt/sportchef-events"
CMD ["java", "-jar", "/opt/sportchef-events/sportchef-events-2.0-SNAPSHOT-all.jar"]
HEALTHCHECK CMD curl --fail http://localhost:8080/ping || exit 1
EXPOSE 8080
