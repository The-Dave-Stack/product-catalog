package com.thedavestack.productcatalog.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = SkuValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSku {
    String message() default
            "SKU must be 3-20 characters, contain only alphanumeric characters, hyphens, and underscores";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
