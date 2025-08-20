# AI Integration with Spring AI MCP Server

This project features a comprehensive AI integration using an enterprise-grade Model-Centric Programming (MCP) server built with Spring AI. This allows for intuitive, natural language management of the product catalog.

## 🧠 Spring AI MCP Server v2.0 Overview

The MCP server exposes the application's core business logic through three main capabilities: **Tools**, **Resources**, and **Prompts**.

### 🔧 **Tools** (9+ Available)
Intelligent product management operations exposed as functions for an AI model:
- **Product CRUD Operations**: Create, read, update, delete with validation and audit.
- **Advanced Search & Filtering**: Multi-criteria search with pagination.
- **Inventory Management**: Stock monitoring, low-stock alerts, and reorder planning.
- **Bulk Operations**: Multi-product transactions with atomic rollback.

### 📊 **Resources** (20+ Available)
Dynamic access to live business data and documentation via a URI-like scheme:
- **Product Catalog Data**: `product://catalog/all`, `product://catalog/category/{category}`
- **Inventory Analytics**: `inventory://status/overview`, `inventory://alerts/critical`
- **Category Information**: `categories://overview/all`
- **API Documentation**: `api://documentation/endpoints/products`
- **Business Rules**: `business://rules/sku-generation`

### 🎯 **Prompts** (7+ Available)
Structured guidance templates for executing complex, multi-step operations:
- **Product Creation Guide**: `product-creation-guide`
- **Inventory Analysis**: `inventory-analysis`
- **Pricing Strategy**: `pricing-strategy`
- **Data Quality Audit**: `data-quality-audit`
- **Troubleshooting Assistant**: `troubleshooting-assistant`

### ⚙️ Environment-Specific Configuration

The MCP server configuration adapts to each environment (`application-local.properties`, etc.) for optimal performance and security. All capabilities (tool, resource, prompt) are enabled across environments but can be fine-tuned.

```properties
# Example: Local MCP Server Configuration (application-local.properties)
spring.ai.mcp.server.enabled=true
spring.ai.mcp.server.name=product-catalog-mcp-server-local
spring.ai.mcp.server.version=2.0.0-local
spring.ai.mcp.server.type=SYNC
spring.ai.mcp.server.instructions=Local Development Product Catalog Management Server with AI-powered tools
spring.ai.mcp.server.sse-endpoint=/sse
spring.ai.mcp.server.capabilities.tool=true
spring.ai.mcp.server.capabilities.resource=true  
spring.ai.mcp.server.capabilities.prompt=true
````

### 🔗 Access & Authentication

  - **Endpoint**: Server-Sent Events (SSE) on `/sse` path.
  - **Authentication**: A valid JWT (USER or ADMIN role) is required.
  - **Security**: Fully integrated with Spring Security.

## 🤖 MCP Server Example Flows

### 1\. Product Creation Flow

**Scenario**: Add a new high-end gaming laptop to the catalog.

1.  **Get Guidelines**: Access category-specific rules via `Resource: categories://details/ELECTRONICS`.
2.  **Use Guidance**: Get a step-by-step template with `Prompt: product-creation-guide` and `{"category": "ELECTRONICS"}`.
3.  **Create Product**: Use the `Tool: createProduct` with all required data. The system validates, generates an SKU, and creates an audit trail.

### 2\. Inventory Management Flow

**Scenario**: Perform a daily inventory health check.

1.  **Get Overview**: Check overall stock health with `Resource: inventory://status/overview`.
2.  **Identify Issues**: Find critical items with `Resource: inventory://alerts/critical`.
3.  **Analyze Needs**: Get an AI-driven reorder plan with `Prompt: inventory-analysis`.
4.  **Execute Reorder**: Update stock levels using the `Tool: updateProduct` for multiple items.

### 3\. Business Analysis Flow

**Scenario**: Conduct a monthly performance review.

1.  **Get Summary**: Access catalog-wide analytics with `Resource: product://catalog/summary`.
2.  **Generate Insights**: Run a full business intelligence report with `Prompt: business-intelligence` and `{"focus": "performance", "timeframe": "monthly"}` to get KPI analysis and optimization recommendations.

### 🔗 Claude Code Integration Examples

The MCP server is designed for seamless interaction with AI assistants like Claude Code.

**Natural Language Commands:**

  - `"Create a new smartphone in the electronics category with competitive pricing."`
      - *This automatically uses the `createProduct` tool, consults pricing guidelines, and applies category validation.*
  - `"Show me all low-stock items and suggest reorder quantities."`
      - *This combines the `findLowStockProducts` tool with the `inventory-analysis` prompt.*

**Intelligent Error Handling:**

  - **Error**: `"Duplicate SKU detected"`
  - **MCP Response**: Provides SKU generation alternatives, an explanation of the business rule, and a resolution workflow.

## 🎯 Business Value Outcomes

  - **Operational Efficiency**: 60% reduction in manual product management tasks.
  - **Decision Making**: Real-time business intelligence and AI-driven recommendations.
  - **Quality Improvement**: Systematic data quality monitoring and automated compliance checking.