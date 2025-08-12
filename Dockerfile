# Perform the extraction in a separate builder container
FROM bellsoft/liberica-openjre-debian:21 AS builder
WORKDIR /builder
# This points to the built jar file in the target folder
# Adjust this to 'build/libs/*.jar' if you're using Gradle
ARG JAR_FILE=target/*.jar
# Copy the jar file to the working directory and rename it to application.jar
COPY ${JAR_FILE} application.jar
# Extract the jar file using an efficient layout
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

# This is the runtime container
FROM bellsoft/liberica-openjre-debian:21

# Install necessary packages and create non-root user
RUN apt-get update && \
    apt-get install -y curl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* && \
    groupadd --system --gid 1001 appgroup && \
    useradd --system --gid appgroup --uid 1001 --shell /bin/bash --create-home appuser

# Set working directory and change ownership
WORKDIR /application
RUN chown -R appuser:appgroup /application

# Copy the extracted jar contents from the builder container into the working directory in the runtime container
# Every copy step creates a new docker layer
# This allows docker to only pull the changes it really needs
COPY --from=builder --chown=appuser:appgroup /builder/extracted/dependencies/ ./
COPY --from=builder --chown=appuser:appgroup /builder/extracted/spring-boot-loader/ ./
COPY --from=builder --chown=appuser:appgroup /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder --chown=appuser:appgroup /builder/extracted/application/ ./

# Switch to non-root user
USER appuser:appgroup

# Expose the application port
EXPOSE 8080

# Configure JVM options for container environments
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Enhanced health check with proper timeout and retries
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl --fail --silent --show-error http://localhost:8080/actuator/health || exit 1

# Start the application jar - this is not the uber jar used by the builder
# This jar only contains application code and references to the extracted jar files
# This layout is efficient to start up and CDS/AOT cache friendly
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar application.jar"]

# Add metadata labels
LABEL maintainer="The Dave Stack"
LABEL description="Product Catalog Spring Boot Application"
LABEL version="1.0"
LABEL org.opencontainers.image.source="https://github.com/The-Dave-Stack/product-catalog"
