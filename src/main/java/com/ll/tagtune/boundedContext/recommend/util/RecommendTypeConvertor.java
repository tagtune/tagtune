package com.ll.tagtune.boundedContext.recommend.util;

import com.ll.tagtune.boundedContext.recommend.entity.RecommendType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RecommendTypeConvertor implements AttributeConverter<RecommendType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(RecommendType attribute) {
        return attribute.getCode();
    }

    @Override
    public RecommendType convertToEntityAttribute(Integer dbData) {
        return RecommendType.codeOf(dbData);
    }
}
