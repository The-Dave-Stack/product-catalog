/**
 * DatabaseConfig.java
 *
 * <p>Design Doc: ./docs/product-repository-design.md
 *
 * <p>Purpose: - Provides CDI configuration for Helidon DB Client - Sets up database connection pool
 * and JDBC configuration - Centralizes database connectivity configuration
 *
 * <p>Logic Overview: 1. Uses CDI @Produces to create DbClient bean 2. Configures JDBC connection
 * with PostgreSQL driver 3. Sets up connection pooling for performance 4. Reads configuration from
 * MicroProfile Config
 *
 * <p>Last Updated: 2025-08-06 by Cline (Model: claude-3-opus, Task: Implementing ProductRepository)
 */
package com.thedavestack.productcatalog.config;

import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.dbclient.DbClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

@ApplicationScoped
public class DatabaseConfig {

    @Produces
    @Singleton
    public DbClient createDbClient(
            @ConfigProperty(name = "db.connection.url") String url,
            @ConfigProperty(name = "db.connection.username") String username,
            @ConfigProperty(name = "db.connection.password") String password) {

        Config dbConfig =
                Config.builder()
                        .addSource(
                                ConfigSources.create(
                                        Map.of(
                                                "source", "jdbc",
                                                "connection.url", url,
                                                "connection.username", username,
                                                "connection.password", password,
                                                "connection.poolName", "product-catalog-pool",
                                                "connection.initializationFailTimeout", "60000",
                                                "connection.connectionTimeout", "30000",
                                                "connection.maxLifetime", "1800000",
                                                "connection.maximumPoolSize", "20",
                                                "connection.minimumIdle", "5")))
                        .build();

        return DbClient.create(dbConfig);
    }
}
