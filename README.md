# AI-Driven Development: A Spring Boot vs. Helidon Comparative Study

## 🚀 Concept

This repository serves as a practical experiment in modern Java development and AI-assisted programming. It contains two functionally identical implementations of a **Product Catalog REST API**, each built with a different technology stack.

The primary goals of this project are:
1.  **To conduct a real-world comparison** between the developer experience, performance, and core philosophies of **Spring Boot** and **Helidon MP**.
2.  **To serve as a testbed** for executing a complete development lifecycle using an **AI Developer Assistant**. The entire project, from setup to E2E testing, is broken down into a highly detailed backlog of tasks designed to be executed by an AI.
3.  **To generate content and insights** for blog posts and portfolio pieces for **[The Dave Stack](https://www.thedavestack.com)**.

---

## 🔬 The Core Comparison

This repository contrasts two distinct approaches to building cloud-native Java applications.

### Spring Boot Approach
- **High Abstraction & Productivity**: Leverages the full Spring ecosystem, including **Spring Data JPA** for a highly abstracted data access layer.
- **"Batteries-Included"**: Relies on Spring's auto-configuration and vast library support for rapid development.
- **Runtime**: Packaged as a standard executable JAR running on the JVM.

### Helidon MP Approach
- **Low Abstraction & Control**: Uses the **Helidon DB Client**, which requires manual SQL query construction, offering fine-grained control and a reactive programming model.
- **Standards-Based**: Adheres to Jakarta EE and MicroProfile standards, promoting interoperability.
- **Runtime**: Packaged as a **GraalVM native executable**, resulting in an extremely small memory footprint and near-instantaneous startup times.

---

## 📂 Projects

This repository contains the following two projects:

* ### [**product-catalog-spring**](./spring/)
    The implementation of the Product Catalog service using the Spring Boot stack.

* ### [**product-catalog-helidon**](./helidon/)
    The implementation of the Product Catalog service using the Helidon MP stack.

---

## 🤖 The AI-Driven Workflow

A unique aspect of this project is its development process. The entire lifecycle is managed by a custom CLI tool, **`backlog.md`**, and is designed to be executed by an AI assistant.

This simulation requires the AI to behave like a human developer, following a strict workflow for each task that includes:
- **Task State Management**: Changing status from `To Do` -> `In Progress` -> `Done`.
- **Time Tracking**: Logging the time spent on each task.
- **Developer Journaling**: Keeping a timestamped log of progress, questions, and blockers.
- **Git & PR Process**: Creating feature branches and preparing Pull Request templates upon completion.

---

## ▶️ How to Run

For detailed instructions on how to build, test, and run each service, please refer to the `README.md` file within its respective project subdirectory:

*   [**product-catalog-spring**](./spring/README.md)
*   [**product-catalog-helidon**](./helidon/README.md)

---

Created by **David** for **The Dave Stack**.
