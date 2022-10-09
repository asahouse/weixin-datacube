package com.andc.amway.datacubecatcher.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by benjaminkc on 17/7/10.
 */
@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
        return (attribute == null ? null :
                Timestamp.from(attribute.atZone(ZoneId.systemDefault()).toInstant()));
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp dbData) {
        return (dbData == null ? null :
                dbData.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    }
}
