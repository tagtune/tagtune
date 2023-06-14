package com.ll.tagtune.boundedContext.lyric.ut;

import com.ll.tagtune.boundedContext.lyric.entity.Language;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class LanguageConvertor implements AttributeConverter<Language, String> {

    @Override
    public String convertToDatabaseColumn(final Language attribute) {
        return attribute.getName();
    }

    @Override
    public Language convertToEntityAttribute(final String dbData) {
        return Language.nameOf(dbData);
    }
}
