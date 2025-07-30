#!/usr/bin/env node

const { execSync } = require("child_process");

console.log("🚀 Starting FINAL and CORRECTED task creation for the Helidon project...");

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
  // Task 1: Design Doc
  {
    cmd: `backlog task create "Create and Detail the Helidon API Design Document" \\
      --priority "critical" --labels "documentation,design,helidon"`,
    plan: `Implementation Plan:
1. Create a new Markdown file at './docs/api-design-product-catalog-helidon.md'.
2. Write the following complete and exact markdown content into the file:

# Product Catalog Service Design (Helidon)

## 1. Purpose

To create a robust and modern RESTful service for product catalog management using the Helidon MP framework, for direct comparison with its Spring Boot counterpart.

## 2. Key Decisions

- **Framework**: Helidon MP 4.x (latest stable version), using Jakarta EE & MicroProfile standards.
- **Database Access**: Helidon DB Client will be used for database interactions, implying manual, programmatic SQL query construction.
- **Schema Management**: A manual SQL script ('schema.sql') will be used to initialize the database schema, executed by the PostgreSQL container on startup.
- **Project Scaffolding**: The Helidon CLI ('helidon init') will be used to generate the initial project structure.
- **Data Model**: The 'Product' class will be a simple Java Record, without persistence annotations.
- **Health Checks**: The application will expose a '/health' endpoint using Helidon Health.
- **Containerization**: The application will be packaged as a GraalVM native executable inside a minimal Docker container.
- **SKU Logic**: SKUs will be auto-generated ('[NNN]-######') if not provided and validated against a specific format if they are.
- **Atomicity**: Bulk creation operations will be transactional.
- **E2E Testing**: A dedicated Maven module ('e2e-tests') will be used with Testcontainers and RestAssured to test the full 'docker-compose' stack.

## 3. Data Model

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

## 4. API Endpoints

| Method | Path                               | Description                           |
| :----- | :--------------------------------- | :------------------------------------ |
| POST   | /api/v1/products                   | Creates a single new product.         |
| POST   | /api/v1/products/batch-create      | Creates multiple products atomically. |
| GET    | /api/v1/products/{id}              | Retrieves a single product by its ID. |
| GET    | /api/v1/products                   | Retrieves a paginated list of products. |
| GET    | /api/v1/products/export?format=json| Exports all products to a JSON file.  |
| PUT    | /api/v1/products/{id}              | Updates an existing product.          |
| DELETE | /api/v1/products/{id}              | Deletes a product.                    |
`,
    ac: `"The Markdown document 'api-design-product-catalog-helidon.md' is created.,The document contains all four specified sections.,The content is exactly as defined in the plan.,Human review and approval of the final document."`
  },

  // Task 2: Project Setup
  {
    cmd: `backlog task create "Set Up Initial Helidon MP Project using a Non-Interactive Command" \\
      --priority "high" --labels "setup,helidon,cli,automation" --depends-on "task-1"`,
    plan: `Implementation Plan:
1. Ensure the Helidon CLI is installed and up-to-date.
2. Execute the single, non-interactive command to generate the project skeleton: 'helidon init --flavor mp --group com.thedavestack --artifact product-catalog-helidon --name product-catalog-helidon --features db-client,health'.
3. Once generated, navigate into its root directory.
4. Run 'mvn clean package' to verify the setup.`,
    ac: `"The Helidon MP project has been generated using the non-interactive 'helidon init' command.,The 'pom.xml' contains 'helidon-mp-db-client' and 'helidon-mp-health'.,'mvn clean package' completes without errors.,Human review and approval."`
  },

  // Task 3: Git Init
  {
    cmd: `backlog task create "Initialize Git Repository and Branches for Helidon Project" \\
      --priority "critical" --labels "setup,git,helidon" --depends-on "task-2"`,
    plan: `Implementation Plan:
1. Navigate into the generated project's root directory ('product-catalog-helidon').
2. Run 'git init'.
3. Create a '.gitignore' file if needed.
4. Stage all generated files ('git add .').
5. Make the first commit: 'git commit -m "chore: initial commit of Helidon project"'.
6. Ensure the primary branch is 'main' and create the 'develop' branch.`,
    ac: `"A '.git' directory exists in the project root.,The 'main' and 'develop' branches exist.,The initial commit with all generated files exists.,Human review and approval."`
  },

  // Task 4: Create GitHub Repo
  {
    cmd: `backlog task create "Create Remote GitHub Repository for Helidon Project" \\
      --priority "critical" --labels "setup,git,github,helidon" --depends-on "task-3"`,
    plan: `Implementation Plan:
1. Attempt to create a new public GitHub repository named 'product-catalog-helidon' using the 'github-mcp' or 'gh' CLI.
2. If both methods fail: change the task status to 'Blocked', add a note, reassign to 'David', and stop.
3. If successful, push 'main' and 'develop' branches to the remote.`,
    ac: `"A remote repository named 'product-catalog-helidon' exists on GitHub.,The local repository is linked to the remote 'origin'.,Both 'main' and 'develop' branches are pushed to the remote.,Human review and approval."`
  },

  // Task 5: DB Setup
  {
    cmd: `backlog task create "Set Up PostgreSQL with Docker Compose for Helidon" \\
      --priority "high" --labels "db,docker,postgres,helidon" --depends-on "task-2"`,
    plan: `Implementation Plan:
1. Create a 'docker-compose.yml' file with a 'postgres-db' service using the 'postgres:17.5-alpine' image.
2. Map port 5432:5432 and configure environment variables.
3. Define a named volume for persistence.
4. In 'src/main/resources/META-INF/microprofile-config.properties', add the DB Client connection properties ('db.connection.url', etc.).`,
    ac: `"'docker-compose.yml' exists and is correct.,'microprofile-config.properties' contains the correct DB connection properties.,'docker-compose up -d' starts the container successfully.,The Helidon application can connect to the database.,Human review and approval."`
  },

  // Task 6: Data Object
  {
    cmd: `backlog task create "Define the 'Product' Data Object (POJO/Record)" \\
      --priority "high" --labels "model,helidon" --depends-on "task-2"`,
    plan: `Implementation Plan:
1. Create package 'com.thedavestack.productcatalog.model'.
2. Create a Java 'record' named 'Product' with all the fields defined in the design doc (id, sku, name, price, etc.).
3. This record must not contain any JPA annotations.`,
    ac: `"The 'Product.java' record exists in the correct package.,It contains all specified fields with correct types.,It is a plain data carrier, free of persistence annotations.,Human review and approval."`
  },

  // Task 7: Schema Script
  {
    cmd: `backlog task create "Create Initial DB Schema Script" \\
      --priority "high" --labels "db,sql,schema,helidon" --depends-on "task-5,task-6"`,
    plan: `Implementation Plan:
1. Create a directory 'db-init' in the project root.
2. Inside, create a 'schema.sql' file.
3. Write the 'CREATE TABLE products (...)' SQL statement, matching the 'Product' record.
4. Modify 'docker-compose.yml' to mount './db-init' to '/docker-entrypoint-initdb.d' on the postgres container.`,
    ac: `"'schema.sql' exists and is correct.,'docker-compose.yml' is updated to mount the script.,On a clean start, the 'products' table is created in the database.,Human review and approval."`
  },

  // Task 8: Repository
  {
    cmd: `backlog task create "Implement the 'ProductRepository' using Helidon DB Client" \\
      --priority "high" --labels "db,repository,helidon,db-client" --depends-on "task-7"`,
    plan: `Implementation Plan:
1. Consult the official Helidon 4.x documentation for 'DbClient' best practices using your MCP tools.
2. Create the 'ProductRepository.java' class, annotate with @ApplicationScoped, and inject the 'DbClient'.
3. Implement methods for all CRUD operations by manually building and executing SQL queries.
4. Pay close attention to the 'Transactional Integrity' rule for batch operations, using 'dbClient.inTransaction(...)'.
5. Create a private helper method to map from 'DbRow' to the 'Product' record.`,
    ac: `"'ProductRepository.java' is a correct @ApplicationScoped CDI bean.,The implementation follows current best practices from Helidon documentation.,All CRUD methods are implemented with manual SQL.,Batch operations are transactional.,Human review and approval."`
  },

  // Task 9: Service
  {
    cmd: `backlog task create "Implement Business Logic in 'ProductService'" \\
      --priority "high" --labels "service,business-logic,helidon" --depends-on "task-8"`,
    plan: `Implementation Plan:
1. Create the 'ProductService.java' class as an @ApplicationScoped CDI bean.
2. Inject the 'ProductRepository'.
3. Implement all business logic (SKU generation, duplicate checks, etc.), delegating DB calls to the repository.
4. Handle not-found scenarios by throwing custom exceptions.`,
    ac: `"'ProductService.java' exists and is correct.,All business logic methods are implemented.,Creation logic handles SKU generation and validation.,Not-found scenarios are handled correctly.,Human review and approval."`
  },

  // Task 10: Resource
  {
    cmd: `backlog task create "Implement REST Endpoints in 'ProductResource'" \\
      --priority "high" --labels "api,resource,jax-rs,helidon" --depends-on "task-9"`,
    plan: `Implementation Plan:
1. Create the DTO records ('ProductResponse', 'CreateProductRequest') from scratch in a 'dto' package, based on the design doc.
2. Create the 'ProductResource.java' class as a JAX-RS resource (@Path(\\"/products\\"), @ApplicationScoped).
3. Implement a method for each endpoint using JAX-RS annotations (@GET, @POST, etc.).
4. Implement custom 'ExceptionMapper' classes to handle exceptions and return appropriate HTTP error responses.`,
    ac: `"'ProductResource.java' and DTOs have been created from scratch.,All MVP endpoints are implemented using JAX-RS annotations.,Custom 'ExceptionMapper' classes are implemented.,Human review and approval."`
  },

  // Task 11: Tests
  {
    cmd: `backlog task create "Add Unit and Integration Tests for Helidon Service" \\
      --priority "high" --labels "test,junit,mockito,helidon" --depends-on "task-10"`,
    plan: `Implementation Plan:
1. Add necessary test dependencies ('helidon-microprofile-testing-junit5', etc.).
2. Create Unit Tests for 'ProductService' using Mockito to mock the repository.
3. Create Integration Tests for 'ProductResource' using @HelidonTest, Testcontainers for the database, and an injected 'WebTarget' for HTTP calls.`,
    ac: `"Unit tests for 'ProductService' are implemented.,Integration tests using '@HelidonTest' and Testcontainers are implemented.,'mvn clean package' runs all tests successfully.,Human review and approval."`
  },

  // Task 12: Dockerfile
  {
    cmd: `backlog task create "Create and Verify Native Dockerfile for Helidon Application" \\
      --priority "medium" --labels "docker,deployment,native,graalvm,helidon" --depends-on "task-11"`,
    plan: `Implementation Plan:
1. Consult official Helidon and Docker Hub documentation for the latest best practices on building native images.
2. Create a multi-stage 'Dockerfile' that uses a GraalVM build image and a minimal runtime image (e.g., 'scratch').
3. The build stage should run 'mvn clean package -Pnative-image'.
4. Add a 'HEALTHCHECK' instruction pointing to the '/health' endpoint.
5. Verify the image by running it with '--rm', checking its health, and stopping it.`,
    ac: `"'Dockerfile' that builds a GraalVM native executable exists.,The implementation aligns with the latest documentation and uses current base images.,'docker build' completes successfully.,The verification process completes successfully.,Human review and approval."`
  },

  // Task 13: Documentation
  {
    cmd: `backlog task create "Update Final Project Documentation (README.md) for Helidon" \\
      --priority "low" --labels "documentation,helidon" --depends-on "task-12"`,
    plan: `Implementation Plan:
1. Edit 'README.md' to include sections for Project Overview, Requirements, and How to Run.
2. Update 'docker-compose.yml' to include the Helidon application service.
3. Provide the 'docker-compose up --build' command in the README.
4. Run 'backlog board export --readme' to embed the Kanban board.`,
    ac: `"'README.md' is updated and well-structured.,'docker-compose.yml' is updated to include the application service.,The Kanban board is embedded in the README.,Human review and approval."`
  },

  // Task 14: E2E Tests
  {
    cmd: `backlog task create "Create Java-based E2E Tests for Helidon Stack" \\
      --priority "medium" --labels "test,e2e,docker-compose,testcontainers,helidon" --depends-on "task-13"`,
    plan: `Implementation Plan:
1. Create a new Maven module named 'e2e-tests'.
2. Add dependencies for Testcontainers, RestAssured, and JUnit 5.
3. Create a test class that uses 'DockerComposeContainer' to manage the root 'docker-compose.yml'.
4. Implement @Test methods for each user story defined in the design document.`,
    ac: `"A new Maven module 'e2e-tests' exists.,The E2E test class uses 'DockerComposeContainer' correctly.,All user story tests are implemented and pass.,Human review and approval."`
  }
];

// Build and execute the full commands
tasks.forEach(task => {
  let fullCommand = task.cmd;
  if (task.plan) {
    const combinedPlan = `${goldenRule}\n\n${task.plan}`;
    const escapedPlan = JSON.stringify(combinedPlan);
    fullCommand += ` --plan ${escapedPlan}`;
  }
  if (task.ac) {
    const escapedAc = JSON.stringify(task.ac);
    fullCommand += ` --ac ${escapedAc}`;
  }
  
  runCommand(fullCommand);
});

console.log("\n🎉 Script finished. All 14 tasks for the Helidon project have been created in backlog.");