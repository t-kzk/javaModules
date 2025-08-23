package org.kzk.jpa.converter;

import jakarta.persistence.AttributeConverter;

import java.sql.Date;
import java.util.Optional;

public class UpdatedConverter implements AttributeConverter<Updated, Date> {
    @Override
    public Date convertToDatabaseColumn(Updated attribute) {
        return Optional.ofNullable(attribute)
                .map(Updated::updatedDateTime)
                .map(Date::valueOf)
                .orElse(null);
    }

    @Override
    public Updated convertToEntityAttribute(Date dbData) {
        return Optional.ofNullable(dbData)
                .map(Date::toLocalDate)
                .map(Updated::new).orElse(null);
    }
}
