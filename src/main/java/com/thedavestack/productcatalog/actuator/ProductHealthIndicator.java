package com.thedavestack.productcatalog.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.thedavestack.productcatalog.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductHealthIndicator implements HealthIndicator {

    private final ProductRepository productRepository;

    @Override
    public Health health() {
        try {
            long totalProducts = productRepository.count();
            long activeProducts = productRepository.findByActive(true, null).getTotalElements();

            if (totalProducts == 0) {
                return Health.down()
                        .withDetail("message", "No products found in database")
                        .withDetail("totalProducts", totalProducts)
                        .build();
            }

            return Health.up()
                    .withDetail("totalProducts", totalProducts)
                    .withDetail("activeProducts", activeProducts)
                    .withDetail("inactiveProducts", totalProducts - activeProducts)
                    .build();

        } catch (Exception e) {
            log.error("Product health check failed", e);
            return Health.down().withDetail("error", e.getMessage()).build();
        }
    }
}
