# Tech Context

## Technologies used
- **Backend**: Helidon MP 4.x (Java, Jakarta EE, MicroProfile)
- **Database**: PostgreSQL
- **Containerization**: Docker, GraalVM
- **Testing**: JUnit, Mockito, Testcontainers, RestAssured
- **Build Tool**: Maven

## Development setup
- Java Development Kit (JDK) 21+
- Maven 3.8+
- Docker Desktop (for PostgreSQL and containerization)
- Git

## Technical constraints
- Must be compatible with Helidon MP 4.x.
- Must run as a GraalVM native executable.
- Database interactions must be through Helidon DB Client.

## Dependencies
- `io.helidon.microprofile.server:helidon-microprofile-server`
- `io.helidon.microprofile.config:helidon-microprofile-config`
- `io.helidon.microprofile.health:helidon-microprofile-health`
- `io.helidon.dbclient:helidon-dbclient-jdbc`
- `org.postgresql:postgresql`
- `org.testcontainers:postgresql` (for testing)
- `io.rest-assured:rest-assured` (for testing)

## Tool usage patterns
- `mvn clean install`: To build the project.
- `mvn helidon:build-native`: To build the native executable.
- `docker-compose up`: To start the database and application containers.
- `git`: For version control, following Git Flow.

## Last Updated
2025-08-06 by Cline (Model: claude-3-opus, Task: Initialized memory bank)
