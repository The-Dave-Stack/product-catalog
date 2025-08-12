#!/usr/bin/env node

const { execSync } = require("child_process");

console.log("🚀 Starting FINAL and CORRECTED task creation for the Spring Boot project...");

const runCommand = (command) => {
  try {
    const cmdParts = command.split(" --");
    console.log(`\n🔵 Executing: ${cmdParts[0]}...`);
    execSync(command, { stdio: "inherit" });
    console.log("✅ Task created successfully.");
  } catch (error) {
    console.error(`❌ Task creation failed.`);
    console.error(error.message);
    process.exit(1);
  }
};

const goldenRule = `⭐ Golden Rule: The Human-Developer Workflow
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
- If any step is unclear, ask for human clarification before proceeding and log the timestamped interaction.`;

const tasks = [
  // Task 1: Create and Detail the API Design Document
  {
    cmd: `backlog task create "Create and Detail the API Design Document" \\
      --priority "critical" --labels "documentation,design"`,
    plan: `Implementation Plan:
1. Create a new Markdown file at './docs/api-design-product-catalog.md'.
2. Populate the file with the following sections and content.
3. **Section 1: Purpose**
   - Add a brief description: "To create a robust and modern RESTful service for product catalog management, serving as a portfolio piece and a baseline for framework comparisons."
4. **Section 2: Key Decisions**
   - Document the following decisions:
     - **Technology Stack**: Java 21 (LTS), Spring Boot 3.x, Maven.
     - **Database**: PostgreSQL (image 'postgres:17.5-alpine'), managed via Docker Compose.
     - **Schema Management**: Flyway will be used. Hibernate's 'ddl-auto' will be set to 'validate'.
     - **Entity IDs**: Primary keys will be UUIDs.
     - **SKU Logic**: Auto-generated ('[NNN]-######') if not provided; validated against a format if provided.
     - **Atomicity**: Bulk creation will be transactional.
     - **Health Checks**: A '/actuator/health' endpoint will be exposed.
     - **E2E Testing**: A dedicated Maven module will test the full 'docker-compose' stack with Testcontainers.
5. **Section 3: Data Model**
   - Add the following Markdown table defining the 'Product' entity:
| Field       | Data Type      | Constraints                  | Description                                 |
| :---------- | :------------- | :--------------------------- | :------------------------------------------ |
| id          | String         | Primary Key, UUID            | Unique identifier for the product.          |
| sku         | String         | Not Null, Unique             | Stock Keeping Unit. Auto-generated if null. |
| name        | String         | Not Null                     | Product's name.                             |
| description | String         |                              | Detailed product description.               |
| price       | BigDecimal     | Not Null                     | Price of the product.                       |
| category    | String         |                              | Simple text-based product category.         |
| createdAt   | Instant        |                              | Timestamp of creation (auto-managed).     |
| updatedAt   | Instant        |                              | Timestamp of last update (auto-managed).  |
6. **Section 4: API Endpoints**
   - Add the following Markdown table summarizing the API contract:
| Method | Path                               | Description                           |
| :----- | :--------------------------------- | :------------------------------------ |
| POST   | /api/v1/products                   | Creates a single new product.         |
| POST   | /api/v1/products/batch-create      | Creates multiple products atomically. |
| GET    | /api/v1/products/{id}              | Retrieves a single product by its ID. |
| GET    | /api/v1/products                   | Retrieves a paginated list of products. |
| GET    | /api/v1/products/export?format=json| Exports all products to a JSON file.  |
| PUT    | /api/v1/products/{id}              | Updates an existing product.          |
| DELETE | /api/v1/products/{id}              | Deletes a product.                    |`,
    ac: `The Markdown document 'api-design-product-catalog.md' is created in the ./docs directory.,The document contains all four specified sections (Purpose, Key Decisions, Data Model, API Endpoints).,The content accurately and completely reflects all the details defined in the plan.,Human review and approval of the final document.`
  },

  // Task 2: Project Setup
  {
    cmd: `backlog task create "Set Up Initial Spring Boot Project using MCP Tool" \\
      --priority "high" --labels "setup,spring-boot,mcp" --depends-on "task-1" \\
      --desc "Use the springinitializr-mcp tool to generate the base project structure."`,
    plan: `Implementation Plan:
1. Use the 'springinitializr-mcp' tool to generate the project with dependencies: 'web', 'data-jpa', 'postgresql', 'flyway-migration', 'validation', 'lombok', 'actuator'.
2. Once generated, navigate into its root directory.
3. In 'src/main/resources/application.properties', set 'spring.jpa.hibernate.ddl-auto=validate'.
4. Run 'mvn clean package' to verify the setup.`,
    ac: `The project has been generated using the mcp tool.,The 'pom.xml' contains 'flyway-core' and 'spring-boot-starter-actuator'.,'application.properties' contains 'spring.jpa.hibernate.ddl-auto=validate'.,'mvn clean package' completes without errors.,Human review and approval.`
  },

  // Task 3: Git Init
  {
    cmd: `backlog task create "Initialize Git Repository and Branches" \\
      --priority "critical" --labels "setup,git" --depends-on "task-2" \\
      --desc "Initialize a Git repository within the newly created project directory."`,
    plan: `Implementation Plan:
1. Navigate into the generated project's root directory ('product-catalog-spring').
2. Run 'git init'.
3. Create a '.gitignore' file if one doesn't exist.
4. Stage all generated files ('git add .').
5. Make the first commit: 'git commit -m \\"chore: initial commit of Spring Boot project\\"'.
6. Rename the current branch to 'main' if needed.
7. Create the 'develop' branch from 'main'.`,
    ac: `A '.git' directory exists in the project root.,The 'main' and 'develop' branches exist.,The initial commit with all generated files exists.,Human review and approval.`
  },

  // Task 4: Create GitHub Repo
  {
    cmd: `backlog task create "Create Remote GitHub Repository" \\
      --priority "critical" --labels "setup,git,github" --depends-on "task-3" \\
      --desc "Create a remote repository on GitHub and link the local repository to it."`,
    plan: `Implementation Plan:
1. Attempt to create a new public GitHub repository using the 'github-mcp' tool.
2. If the MCP tool fails, attempt to use the GitHub CLI ('gh repo create ...').
3. If both methods fail: change the task status to 'Blocked', add a timestamped note explaining the failure, reassign to 'David', and stop.
4. If successful, push the 'main' and 'develop' branches to the remote origin.`,
    ac: `A remote repository exists on GitHub.,The local repository is linked to the remote 'origin'.,Both 'main' and 'develop' branches are pushed to the remote.,Human review and approval.`
  },

  // Task 5: DB Setup
  {
    cmd: `backlog task create "Set Up PostgreSQL with Docker Compose" \\
      --priority "high" --labels "db,docker,postgres" --depends-on "task-2" \\
      --desc "Create a reproducible PostgreSQL database environment using Docker Compose."`,
    plan: `Implementation Plan:
1. In the project's root directory, create 'docker-compose.yml'.
2. Define a service 'postgres-db' using the image 'postgres:17.5-alpine'.
3. Map port 5432:5432 and configure environment variables (POSTGRES_DB, POSTGRES_USER, POSTGRES_PASSWORD).
4. Define a named volume for data persistence.
5. In 'application.properties', add the datasource properties to connect to the Docker container.`,
    ac: `'docker-compose.yml' exists and defines the 'postgres-db' service.,'application.properties' has correct datasource properties.,'docker-compose up -d' starts the container successfully.,The Spring Boot application connects to the database.,Human review and approval.`
  },

  // Task 6: Product Entity
  {
    cmd: `backlog task create "Define the 'Product' JPA Entity" \\
      --priority "high" --labels "db,jpa,entity" --depends-on "task-2" \\
      --desc "Create the 'Product' entity class."`,
    plan: `Implementation Plan:
1. Create package 'com.thedavestack.productcatalog.model'.
2. Create class 'Product.java'.
3. Use @Data, @NoArgsConstructor, @AllArgsConstructor, @Entity, @Table(name = \\"products\\").
4. Define fields: id (String), sku (String), name (String), description (String), price (BigDecimal), category (String), createdAt (Instant), updatedAt (Instant).
5. Annotate 'id' with @Id and @GeneratedValue(strategy = GenerationType.UUID).
6. Add @Column constraints to sku, name, and price.
7. Use @CreationTimestamp and @UpdateTimestamp for automatic date management.`,
    ac: `'Product.java' exists in the correct package.,Class is correctly annotated.,Fields use 'BigDecimal' and 'Instant'.,'id' is the UUID primary key.,Human review and approval.`
  },

  // Task 7: Flyway Migration
  {
    cmd: `backlog task create "Create Initial DB Schema with Flyway" \\
      --priority "high" --labels "db,migration,flyway" --depends-on "task-5,task-6" \\
      --desc "Create the first Flyway migration script for the 'products' table."`,
    plan: `Implementation Plan:
1. In 'src/main/resources/db/migration', create a new SQL file named 'V1__Create_products_table.sql'.
2. Write the 'CREATE TABLE products (...)' SQL statement in this file.
3. The table schema must match the fields defined in the 'Product' entity, including types and constraints.`,
    ac: `A SQL migration file exists in 'src/main/resources/db/migration'.,The SQL script contains the correct 'CREATE TABLE' statement.,Flyway successfully applies the migration on application startup.,Human review and approval.`
  },

  // Task 8: Repository
  {
    cmd: `backlog task create "Create the 'ProductRepository' Interface" \\
      --priority "high" --labels "db,jpa,repository" --depends-on "task-6" \\
      --desc "Create the Spring Data JPA repository for the 'Product' entity."`,
    plan: `Implementation Plan:
1. Create package 'com.thedavestack.productcatalog.repository'.
2. Create interface 'ProductRepository.java'.
3. Extend 'JpaRepository<Product, String>'.
4. Add method signatures: 'Optional<Product> findBySku(String sku);' and 'boolean existsBySku(String sku);'.`,
    ac: `'ProductRepository.java' exists.,It extends 'JpaRepository<Product, String>'.,It includes 'findBySku' and 'existsBySku' methods.,Human review and approval.`
  },

  // Task 9: Service
  {
    cmd: `backlog task create "Implement Business Logic in 'ProductService'" \\
      --priority "high" --labels "service,business-logic" --depends-on "task-8" \\
      --desc "Create the service layer to orchestrate product management logic."`,
    plan: `Implementation Plan:
1. Create package 'com.thedavestack.productcatalog.service' and class 'ProductService.java'.
2. Annotate with @Service and inject 'ProductRepository'.
3. Implement 'createProduct', including SKU generation and duplicate validation logic.
4. Implement 'createMultipleProducts' with @Transactional annotation.
5. Implement all other CRUD methods (findById, findAll, update, delete), handling not-found scenarios with exceptions.`,
    ac: `'ProductService.java' exists and is annotated with @Service.,All CRUD and batch-create methods are implemented.,SKU logic is correctly handled.,Batch creation is @Transactional.,Human review and approval.`
  },

  // Task 10: Controller
  {
    cmd: `backlog task create "Implement REST Endpoints in 'ProductController'" \\
      --priority "high" --labels "api,controller,rest" --depends-on "task-9" \\
      --desc "Expose business logic via RESTful endpoints, using DTOs and a global exception handler."`,
    plan: `Implementation Plan:
1. Create 'com.thedavestack.productcatalog.dto' package and define DTOs ('ProductResponse', 'CreateProductRequest') with validation annotations.
2. Create 'com.thedavestack.productcatalog.controller' package and class 'ProductController.java'.
3. Annotate with @RestController and @RequestMapping(\\"/api/v1/products\\").
4. Implement methods for all MVP endpoints, using DTOs for request and response bodies.
5. Implement a mapper component to convert between entities and DTOs.
6. Create a @ControllerAdvice class to handle custom exceptions and return appropriate HTTP errors.`,
    ac: `'ProductController.java' and DTOs exist.,All MVP endpoints are implemented.,A @ControllerAdvice exception handler is implemented.,API uses DTOs exclusively.,Human review and approval.`
  },

  // Task 11: Tests
  {
    cmd: `backlog task create "Add Unit, Repository, and Integration Tests" \\
      --priority "high" --labels "test,junit,mockito,testcontainers" --depends-on "task-10" \\
      --desc "Create a comprehensive test suite for all layers."`,
    plan: `Implementation Plan:
1. Add 'Testcontainers' dependency to pom.xml.
2. Create Unit Tests for 'ProductService' using Mockito.
3. Create Repository Tests for 'ProductRepository' using @DataJpaTest.
4. Create Integration Tests for 'ProductController' using @SpringBootTest, Testcontainers, and MockMvc.`,
    ac: `Unit tests for 'ProductService' are implemented.,@DataJpaTest tests for 'ProductRepository' are implemented.,Integration tests for 'ProductController' with Testcontainers are implemented.,'mvn clean package' runs all tests successfully.,Human review and approval.`
  },

  // Task 12: Dockerfile
  {
    cmd: `backlog task create "Create and Verify Application Dockerfile" \\
      --priority "medium" --labels "docker,deployment,healthcheck" --depends-on "task-11" \\
      --desc "Create a multi-stage Dockerfile with a HEALTHCHECK and verification process."`,
    plan: `Implementation Plan:
1. Create a 'Dockerfile' in the project root.
2. Implement a multi-stage build (build stage with Maven, runtime stage with JRE).
3. Add a 'HEALTHCHECK' instruction pointing to '/actuator/health'.
4. In 'application.properties', ensure the health endpoint is exposed.
5. Verify the image by running it with '--rm', checking its health, and stopping it.`,
    ac: `'Dockerfile' with multi-stage build and HEALTHCHECK exists.,'docker build' completes successfully.,The verification process (run, check health, stop) completes successfully.,Human review and approval.`
  },

  // Task 13: Documentation
  {
    cmd: `backlog task create "Update Final Project Documentation (README.md)" \\
      --priority "low" --labels "documentation" --depends-on "task-12" \\
      --desc "Create clear documentation on how to set up, build, and run the project."`,
    plan: `Implementation Plan:
1. Edit 'README.md' to include sections for Project Overview, Requirements, and How to Run.
2. Update 'docker-compose.yml' to include the application service, allowing the full stack to be run with 'docker-compose up'.
3. Add a summary table of API Endpoints.
4. Run 'backlog board export --readme' to embed the Kanban board.`,
    ac: `'README.md' is updated and well-structured.,'docker-compose.yml' is updated to include the application service.,The Kanban board is embedded in the README.,Human review and approval.`
  },

  // Task 14: E2E Tests
  {
    cmd: `backlog task create "Create Java-based E2E Tests using Testcontainers" \\
      --priority "medium" --labels "test,e2e,docker-compose,testcontainers" --depends-on "task-13" \\
      --desc "Create a dedicated Maven module for E2E tests using Testcontainers to manage the full docker-compose stack."`,
    plan: `Implementation Plan:
1. Create a new Maven module named 'e2e-tests'.
2. Add dependencies for Testcontainers, RestAssured, and JUnit 5.
3. Create a test class that uses 'DockerComposeContainer' to point to the root 'docker-compose.yml'.
4. Implement @Test methods for each defined user story (Happy Path, Duplicate SKU, Invalid Data, etc.).
5. Use RestAssured to make HTTP calls and assert responses.`,
    ac: `A new Maven module 'e2e-tests' exists.,The E2E test class uses 'DockerComposeContainer'.,Test methods covering all user stories are implemented.,All E2E tests pass.,Human review and approval.`
  }
];

// Build and execute the full commands
tasks.forEach(task => {
  let fullCommand = task.cmd;
  if (task.plan) {
    // Combine the Golden Rule and the specific plan into a single --plan argument
    const combinedPlan = `${goldenRule}\n\n${task.plan}`;
    // Escape the combined plan for shell execution. Using JSON.stringify is a robust way to handle quotes and special characters.
    const escapedPlan = JSON.stringify(combinedPlan);
    fullCommand += ` --plan ${escapedPlan}`;
  }
  if (task.ac) {
    const escapedAc = JSON.stringify(task.ac);
    fullCommand += ` --ac ${escapedAc}`;
  }
  runCommand(fullCommand);
});

console.log("\n🎉 Script finished. All 14 tasks have been created in backlog.");