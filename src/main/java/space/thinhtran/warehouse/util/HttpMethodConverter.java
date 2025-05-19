package space.thinhtran.warehouse.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class HttpMethodConverter implements AttributeConverter<Set<String>, String> {

    private static final String DELIMITER = ",";
    private static final String EMPTY_STRING = "";
    private static final Set<String> EMPTY_SET = Collections.emptySet();

    @Override
    public String convertToDatabaseColumn(Set<String> attribute) {
        return attribute != null ? String.join(DELIMITER, attribute) : EMPTY_STRING;
    }

    @Override
    public Set<String> convertToEntityAttribute(String dbData) {
        return dbData != null && !dbData.isEmpty()
                ? Arrays.stream(dbData.split(DELIMITER))
                .map(String::trim)
                .map(String::toUpperCase)
                .collect(Collectors.toSet())
                : EMPTY_SET;
    }
}
