package com.thedavestack.productcatalog.converter;

import com.thedavestack.productcatalog.model.Category;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CategoryConverter implements AttributeConverter<Category, String> {

    @Override
    public String convertToDatabaseColumn(Category category) {
        return category != null ? category.getDisplayName() : null;
    }

    @Override
    public Category convertToEntityAttribute(String displayName) {
        return displayName != null ? Category.fromDisplayName(displayName) : null;
    }
}
