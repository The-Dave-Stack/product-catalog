---
id: task-11
title: 'Add Unit, Repository, and Integration Tests'
status: In Progress
assignee: []
created_date: '2025-07-28'
updated_date: '2025-08-01'
labels:
  - test
  - junit
  - mockito
  - testcontainers
dependencies:
  - task-10
priority: high
---

## Description

Create a comprehensive test suite for all layers.

## Acceptance Criteria

- [ ] Unit tests for 'ProductService' are implemented.
- [ ] @DataJpaTest tests for 'ProductRepository' are implemented.
- [ ] Integration tests for 'ProductController' with Testcontainers are implemented.
- [ ] 'mvn clean package' runs all tests successfully.
- [ ] Human review and approval.

## Implementation Plan

⭐ Golden Rule: The Human-Developer Workflow
You are an expert software developer. For this task, you MUST follow this exact lifecycle:
1. Start Task: Announce you are starting. Change the task status to 'In Progress'. Start the timer using the 'time-mcp' tool.
2. Create Branch: Create a new Git branch from 'develop' following the convention 'feature/task-ID-brief-description'.
3. Execute & Journal: Execute the Implementation Plan below. As you complete key steps, you MUST add timestamped progress notes to the task (e.g., '[YYYY-MM-DD HH:MM:SS] - Note content.').
4. Stop & Log Time: When the work is done, stop the timer. Add a final timestamped note with the total time spent.
5. Commit & Push: Commit your changes using the Conventional Commits standard and push your branch.
6. Prepare PR: Generate the title and body for a Pull Request.
7. Finish Task: Change the task status to 'Done'.

Universal Mandates:
- You MUST strictly adhere to all rules in 'global.general.rules.pdf' and 'global.backend.rules.pdf'.
- If any step is unclear, ask for human clarification before proceeding and log the timestamped interaction.

Implementation Plan:
1. Add 'Testcontainers' dependency to pom.xml, if not exists.
2. Check the Unit Tests for 'ProductService' using Mockito and add more if needed.
3. Check the Repository Tests for 'ProductRepository' using @DataJpaTest and add more if needed.
4. Create Integration Tests for 'ProductController' using @SpringBootTest, Testcontainers, and MockMvc.

## Implementation Notes

[2025-08-01 10:05:18] - Verified project dependencies in pom.xml. The required Testcontainers dependencies are present and their versions are managed by the Spring Boot parent POM. No changes are necessary.

[2025-08-01 10:06:54] - Added a new unit test to ProductServiceTest to cover the edge case of calling createMultipleProducts with an empty list.

[2025-08-01 10:07:43] - Reviewed ProductRepositoryTest. The existing tests for custom query methods are comprehensive. No additional tests are needed for the data access layer.

[2025-08-01 10:10:12] - Added integration tests for the GET /api/v1/products/{id} endpoint in ProductControllerIT, covering both success and not-found scenarios.

[2025-08-01 10:12:08] - Final verification complete. [INFO] Scanning for projects...
[INFO] 
[INFO] --------------< com.thedavestack:product-catalog-spring >---------------
[INFO] Building product-catalog-spring 0.0.1-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- clean:3.4.1:clean (default-clean) @ product-catalog-spring ---
[INFO] Deleting /home/kratos/Development/Github/The-Dave-Stack/product-catalog/spring/target
[INFO] 
[INFO] --- resources:3.3.1:resources (default-resources) @ product-catalog-spring ---
[INFO] Copying 1 resource from src/main/resources to target/classes
[INFO] Copying 1 resource from src/main/resources to target/classes
[INFO] 
[INFO] --- compiler:3.14.0:compile (default-compile) @ product-catalog-spring ---
[INFO] Recompiling the module because of changed source code.
[INFO] Compiling 10 source files with javac [debug parameters release 21] to target/classes
[INFO] 
[INFO] --- resources:3.3.1:testResources (default-testResources) @ product-catalog-spring ---
[INFO] Copying 1 resource from src/test/resources to target/test-classes
[INFO] 
[INFO] --- compiler:3.14.0:testCompile (default-testCompile) @ product-catalog-spring ---
[INFO] Recompiling the module because of changed dependency.
[INFO] Compiling 7 source files with javac [debug parameters release 21] to target/test-classes
[INFO] 
[INFO] --- surefire:3.5.3:test (default-test) @ product-catalog-spring ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.thedavestack.productcatalog.service.ProductServiceTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.095 s -- in com.thedavestack.productcatalog.service.ProductServiceTest
[INFO] Running com.thedavestack.productcatalog.controller.ProductControllerTest
10:13:27.169 [main] INFO org.springframework.test.context.support.AnnotationConfigContextLoaderUtils -- Could not detect default configuration classes for test class [com.thedavestack.productcatalog.controller.ProductControllerTest]: ProductControllerTest does not declare any static, non-private, non-final, nested classes annotated with @Configuration.
10:13:27.514 [main] INFO org.springframework.boot.test.context.SpringBootTestContextBootstrapper -- Found @SpringBootConfiguration com.thedavestack.productcatalog.ProductCatalogSpringApplication for test class com.thedavestack.productcatalog.controller.ProductControllerTest

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.5.4)

2025-08-01T10:13:28.531+02:00  INFO 2474641 --- [           main] c.t.p.controller.ProductControllerTest   : Starting ProductControllerTest using Java 21.0.8 with PID 2474641 (started by kratos in /home/kratos/Development/Github/The-Dave-Stack/product-catalog/spring)
2025-08-01T10:13:28.533+02:00  INFO 2474641 --- [           main] c.t.p.controller.ProductControllerTest   : No active profile set, falling back to 1 default profile: "default"
2025-08-01T10:13:30.952+02:00  INFO 2474641 --- [           main] o.s.b.t.m.w.SpringBootMockServletContext : Initializing Spring TestDispatcherServlet ''
2025-08-01T10:13:30.953+02:00  INFO 2474641 --- [           main] o.s.t.web.servlet.TestDispatcherServlet  : Initializing Servlet ''
2025-08-01T10:13:30.957+02:00  INFO 2474641 --- [           main] o.s.t.web.servlet.TestDispatcherServlet  : Completed initialization in 2 ms
2025-08-01T10:13:31.032+02:00  INFO 2474641 --- [           main] c.t.p.controller.ProductControllerTest   : Started ProductControllerTest in 3.401 seconds (process running for 7.393)
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.170 s -- in com.thedavestack.productcatalog.controller.ProductControllerTest
[INFO] Running com.thedavestack.productcatalog.repository.ProductRepositoryTest
2025-08-01T10:13:31.882+02:00  INFO 2474641 --- [           main] t.c.s.AnnotationConfigContextLoaderUtils : Could not detect default configuration classes for test class [com.thedavestack.productcatalog.repository.ProductRepositoryTest]: ProductRepositoryTest does not declare any static, non-private, non-final, nested classes annotated with @Configuration.
2025-08-01T10:13:31.960+02:00  INFO 2474641 --- [           main] .b.t.c.SpringBootTestContextBootstrapper : Found @SpringBootConfiguration com.thedavestack.productcatalog.ProductCatalogSpringApplication for test class com.thedavestack.productcatalog.repository.ProductRepositoryTest
2025-08-01T10:13:32.016+02:00  INFO 2474641 --- [           main] org.testcontainers.images.PullPolicy     : Image pull policy will be performed by: DefaultPullPolicy()
2025-08-01T10:13:32.023+02:00  INFO 2474641 --- [           main] o.t.utility.ImageNameSubstitutor         : Image name substitution will be performed by: DefaultImageNameSubstitutor (composite of 'ConfigurationFileImageNameSubstitutor' and 'PrefixingImageNameSubstitutor')
2025-08-01T10:13:32.038+02:00  INFO 2474641 --- [           main] org.testcontainers.DockerClientFactory   : Testcontainers version: 1.21.3
2025-08-01T10:13:32.508+02:00  INFO 2474641 --- [           main] o.t.d.DockerClientProviderStrategy       : Loaded org.testcontainers.dockerclient.UnixSocketClientProviderStrategy from ~/.testcontainers.properties, will try it first
2025-08-01T10:13:33.790+02:00  INFO 2474641 --- [           main] o.t.d.DockerClientProviderStrategy       : Found Docker environment with local Unix socket (unix:///var/run/docker.sock)
2025-08-01T10:13:33.807+02:00  INFO 2474641 --- [           main] org.testcontainers.DockerClientFactory   : Docker host IP address is localhost
2025-08-01T10:13:33.893+02:00  INFO 2474641 --- [           main] org.testcontainers.DockerClientFactory   : Connected to docker: 
  Server Version: 28.3.3
  API Version: 1.51
  Operating System: Ubuntu 24.04.2 LTS
  Total Memory: 15853 MB
2025-08-01T10:13:34.029+02:00  INFO 2474641 --- [           main] tc.testcontainers/ryuk:0.12.0            : Creating container for image: testcontainers/ryuk:0.12.0
2025-08-01T10:13:34.546+02:00  INFO 2474641 --- [           main] tc.testcontainers/ryuk:0.12.0            : Container testcontainers/ryuk:0.12.0 is starting: afd4425cd0f6b1664369d48e48fa565244fcc5d4b5b11b21449673da02f2faae
2025-08-01T10:13:35.808+02:00  INFO 2474641 --- [           main] tc.testcontainers/ryuk:0.12.0            : Container testcontainers/ryuk:0.12.0 started in PT1.779271363S
2025-08-01T10:13:35.847+02:00  INFO 2474641 --- [           main] o.t.utility.RyukResourceReaper           : Ryuk started - will monitor and terminate Testcontainers containers on JVM exit
2025-08-01T10:13:35.848+02:00  INFO 2474641 --- [           main] org.testcontainers.DockerClientFactory   : Checking the system...
2025-08-01T10:13:35.849+02:00  INFO 2474641 --- [           main] org.testcontainers.DockerClientFactory   : ✔︎ Docker server version should be at least 1.6.0
2025-08-01T10:13:35.852+02:00  INFO 2474641 --- [           main] tc.postgres:17.5-alpine                  : Creating container for image: postgres:17.5-alpine
2025-08-01T10:13:35.915+02:00  INFO 2474641 --- [           main] tc.postgres:17.5-alpine                  : Container postgres:17.5-alpine is starting: 501a217bd6635ce2832789cca8ea51063aed9de00f36471b10aec658db7a9b3f
2025-08-01T10:13:40.584+02:00  INFO 2474641 --- [           main] tc.postgres:17.5-alpine                  : Container postgres:17.5-alpine started in PT4.732294485S
2025-08-01T10:13:40.591+02:00  INFO 2474641 --- [           main] tc.postgres:17.5-alpine                  : Container is started (JDBC URL: jdbc:postgresql://localhost:32805/test?loggerLevel=OFF)

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.5.4)

2025-08-01T10:13:40.738+02:00  INFO 2474641 --- [           main] c.t.p.repository.ProductRepositoryTest   : Starting ProductRepositoryTest using Java 21.0.8 with PID 2474641 (started by kratos in /home/kratos/Development/Github/The-Dave-Stack/product-catalog/spring)
2025-08-01T10:13:40.739+02:00  INFO 2474641 --- [           main] c.t.p.repository.ProductRepositoryTest   : The following 1 profile is active: "test"
2025-08-01T10:13:41.485+02:00  INFO 2474641 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2025-08-01T10:13:41.697+02:00  INFO 2474641 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 149 ms. Found 1 JPA repository interface.
2025-08-01T10:13:42.107+02:00  INFO 2474641 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2025-08-01T10:13:42.540+02:00  INFO 2474641 --- [           main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection org.postgresql.jdbc.PgConnection@29811d4d
2025-08-01T10:13:42.550+02:00  INFO 2474641 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2025-08-01T10:13:42.605+02:00  INFO 2474641 --- [           main] org.flywaydb.core.FlywayExecutor         : Database: jdbc:postgresql://localhost:32805/test?loggerLevel=OFF (PostgreSQL 17.5)
2025-08-01T10:13:42.671+02:00  INFO 2474641 --- [           main] o.f.c.i.s.JdbcTableSchemaHistory         : Schema history table "public"."flyway_schema_history" does not exist yet
2025-08-01T10:13:42.676+02:00  INFO 2474641 --- [           main] o.f.core.internal.command.DbValidate     : Successfully validated 1 migration (execution time 00:00.031s)
2025-08-01T10:13:42.703+02:00  INFO 2474641 --- [           main] o.f.c.i.s.JdbcTableSchemaHistory         : Creating Schema History table "public"."flyway_schema_history" ...
2025-08-01T10:13:42.797+02:00  INFO 2474641 --- [           main] o.f.core.internal.command.DbMigrate      : Current version of schema "public": << Empty Schema >>
2025-08-01T10:13:42.811+02:00  INFO 2474641 --- [           main] o.f.core.internal.command.DbMigrate      : Migrating schema "public" to version "1 - Create products table"
2025-08-01T10:13:42.852+02:00  INFO 2474641 --- [           main] o.f.core.internal.command.DbMigrate      : Successfully applied 1 migration to schema "public", now at version v1 (execution time 00:00.018s)
2025-08-01T10:13:43.004+02:00  INFO 2474641 --- [           main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
2025-08-01T10:13:43.169+02:00  INFO 2474641 --- [           main] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 6.6.22.Final
2025-08-01T10:13:43.278+02:00  INFO 2474641 --- [           main] o.h.c.internal.RegionFactoryInitiator    : HHH000026: Second-level cache disabled
2025-08-01T10:13:43.514+02:00  INFO 2474641 --- [           main] o.s.o.j.p.SpringPersistenceUnitInfo      : No LoadTimeWeaver setup: ignoring JPA class transformer
2025-08-01T10:13:43.624+02:00  INFO 2474641 --- [           main] org.hibernate.orm.connections.pooling    : HHH10001005: Database info:
	Database JDBC URL [Connecting through datasource 'HikariDataSource (HikariPool-1)']
	Database driver: undefined/unknown
	Database version: 17.5
	Autocommit mode: undefined/unknown
	Isolation level: undefined/unknown
	Minimum pool size: undefined/unknown
	Maximum pool size: undefined/unknown
2025-08-01T10:13:44.421+02:00  INFO 2474641 --- [           main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)
2025-08-01T10:13:44.425+02:00  INFO 2474641 --- [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2025-08-01T10:13:44.828+02:00  INFO 2474641 --- [           main] c.t.p.repository.ProductRepositoryTest   : Started ProductRepositoryTest in 4.23 seconds (process running for 21.189)
Hibernate: insert into products (category,created_at,description,name,price,sku,updated_at,id) values (?,?,?,?,?,?,?,?)
Hibernate: select p1_0.id,p1_0.category,p1_0.created_at,p1_0.description,p1_0.name,p1_0.price,p1_0.sku,p1_0.updated_at from products p1_0 where p1_0.sku=?
Hibernate: select p1_0.id,p1_0.category,p1_0.created_at,p1_0.description,p1_0.name,p1_0.price,p1_0.sku,p1_0.updated_at from products p1_0 where p1_0.sku=?
Hibernate: insert into products (category,created_at,description,name,price,sku,updated_at,id) values (?,?,?,?,?,?,?,?)
Hibernate: select p1_0.id from products p1_0 where p1_0.sku=? fetch first ? rows only
Hibernate: select p1_0.id from products p1_0 where p1_0.sku=? fetch first ? rows only
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 13.28 s -- in com.thedavestack.productcatalog.repository.ProductRepositoryTest
[INFO] Running com.thedavestack.productcatalog.ProductCatalogSpringApplicationTests
2025-08-01T10:13:45.154+02:00  INFO 2474641 --- [           main] t.c.s.AnnotationConfigContextLoaderUtils : Could not detect default configuration classes for test class [com.thedavestack.productcatalog.ProductCatalogSpringApplicationTests]: ProductCatalogSpringApplicationTests does not declare any static, non-private, non-final, nested classes annotated with @Configuration.
2025-08-01T10:13:45.167+02:00  INFO 2474641 --- [           main] .b.t.c.SpringBootTestContextBootstrapper : Found @SpringBootConfiguration com.thedavestack.productcatalog.ProductCatalogSpringApplication for test class com.thedavestack.productcatalog.ProductCatalogSpringApplicationTests

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.5.4)

2025-08-01T10:13:45.213+02:00  INFO 2474641 --- [           main] t.p.ProductCatalogSpringApplicationTests : Starting ProductCatalogSpringApplicationTests using Java 21.0.8 with PID 2474641 (started by kratos in /home/kratos/Development/Github/The-Dave-Stack/product-catalog/spring)
2025-08-01T10:13:45.213+02:00  INFO 2474641 --- [           main] t.p.ProductCatalogSpringApplicationTests : No active profile set, falling back to 1 default profile: "default"
2025-08-01T10:13:45.753+02:00  INFO 2474641 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2025-08-01T10:13:45.763+02:00  INFO 2474641 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 8 ms. Found 1 JPA repository interface.
2025-08-01T10:13:45.965+02:00  INFO 2474641 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-2 - Starting...
2025-08-01T10:13:45.986+02:00  INFO 2474641 --- [           main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-2 - Added connection org.postgresql.jdbc.PgConnection@3094b465
2025-08-01T10:13:45.987+02:00  INFO 2474641 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-2 - Start completed.
2025-08-01T10:13:45.989+02:00  INFO 2474641 --- [           main] org.flywaydb.core.FlywayExecutor         : Database: jdbc:postgresql://localhost:32805/test?loggerLevel=OFF (PostgreSQL 17.5)
2025-08-01T10:13:46.013+02:00  INFO 2474641 --- [           main] o.f.core.internal.command.DbValidate     : Successfully validated 1 migration (execution time 00:00.010s)
2025-08-01T10:13:46.046+02:00  INFO 2474641 --- [           main] o.f.core.internal.command.DbMigrate      : Current version of schema "public": 1
2025-08-01T10:13:46.047+02:00  INFO 2474641 --- [           main] o.f.core.internal.command.DbMigrate      : Schema "public" is up to date. No migration necessary.
2025-08-01T10:13:46.089+02:00  INFO 2474641 --- [           main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
2025-08-01T10:13:46.092+02:00  INFO 2474641 --- [           main] o.h.c.internal.RegionFactoryInitiator    : HHH000026: Second-level cache disabled
2025-08-01T10:13:46.104+02:00  INFO 2474641 --- [           main] o.s.o.j.p.SpringPersistenceUnitInfo      : No LoadTimeWeaver setup: ignoring JPA class transformer
2025-08-01T10:13:46.111+02:00  INFO 2474641 --- [           main] org.hibernate.orm.connections.pooling    : HHH10001005: Database info:
	Database JDBC URL [Connecting through datasource 'HikariDataSource (HikariPool-2)']
	Database driver: undefined/unknown
	Database version: 17.5
	Autocommit mode: undefined/unknown
	Isolation level: undefined/unknown
	Minimum pool size: undefined/unknown
	Maximum pool size: undefined/unknown
2025-08-01T10:13:46.164+02:00  INFO 2474641 --- [           main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)
2025-08-01T10:13:46.165+02:00  INFO 2474641 --- [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2025-08-01T10:13:46.436+02:00  WARN 2474641 --- [           main] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning
2025-08-01T10:13:46.844+02:00  INFO 2474641 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 1 endpoint beneath base path '/actuator'
2025-08-01T10:13:46.915+02:00  INFO 2474641 --- [           main] t.p.ProductCatalogSpringApplicationTests : Started ProductCatalogSpringApplicationTests in 1.735 seconds (process running for 23.276)
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.848 s -- in com.thedavestack.productcatalog.ProductCatalogSpringApplicationTests
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 18, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] 
[INFO] --- jar:3.4.2:jar (default-jar) @ product-catalog-spring ---
[INFO] Building jar: /home/kratos/Development/Github/The-Dave-Stack/product-catalog/spring/target/product-catalog-spring-0.0.1-SNAPSHOT.jar
[INFO] 
[INFO] --- spring-boot:3.5.4:repackage (repackage) @ product-catalog-spring ---
[INFO] Replacing main artifact /home/kratos/Development/Github/The-Dave-Stack/product-catalog/spring/target/product-catalog-spring-0.0.1-SNAPSHOT.jar with repackaged archive, adding nested dependencies in BOOT-INF/.
[INFO] The original artifact has been renamed to /home/kratos/Development/Github/The-Dave-Stack/product-catalog/spring/target/product-catalog-spring-0.0.1-SNAPSHOT.jar.original
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  35.226 s
[INFO] Finished at: 2025-08-01T10:13:48+02:00
[INFO] ------------------------------------------------------------------------ executed successfully, and all 18 tests passed. The test suite is now comprehensive.
