package com.thedavestack.productcatalog.validation;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SkuValidator implements ConstraintValidator<ValidSku, String> {

    private static final Pattern SKU_PATTERN = Pattern.compile("^[A-Za-z0-9_-]{3,20}$");

    @Override
    public void initialize(ValidSku constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String sku, ConstraintValidatorContext context) {
        if (sku == null || sku.trim().isEmpty()) {
            return true; // Let @NotBlank handle empty values
        }
        return SKU_PATTERN.matcher(sku.trim()).matches();
    }
}
