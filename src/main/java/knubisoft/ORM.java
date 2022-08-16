package knubisoft;

import knubisoft.parsingStrategy.*;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

public class ORM {

    @SneakyThrows
    public <T> List<T> transform(ORMInterface.DataInputSource inputSource, Class<T> cls) {
        Table table = convertToTable(inputSource);
        return convertTableToListOfClasses(table, cls);
    }

    private <T> List<T> convertTableToListOfClasses(Table table, Class<T> cls) {
        List<T> result = new ArrayList<>();
        for (int index = 0; index < table.size(); index++) {
            Map<String, String> row = table.getTableRowByIndex(index);
            T instance = reflectTableRowToClass(row, cls);
            result.add(instance);
        }
        return result;
    }

    @SneakyThrows
    private <T> T reflectTableRowToClass(Map<String, String> row, Class<T> cls) {
        T instance = cls.getDeclaredConstructor().newInstance();
        for (Field each: cls.getDeclaredFields()) {
            each.setAccessible(true);
            String value = row.get(each.getName());
            if (value != null) {
                each.set(instance, transformValueToFieldType(each, value));
            }
        }
        return instance;
    }

    private static Object transformValueToFieldType(Field field, String value) {
        Map<Class<?>, Function<String, Object>> typeToFunction = new LinkedHashMap<>();
        typeToFunction.put(String.class, s -> s);
        typeToFunction.put(int.class, Integer::parseInt);
        typeToFunction.put(Float.class, Float::parseFloat);
        typeToFunction.put(LocalDate.class, LocalDate::parse);
        typeToFunction.put(LocalDateTime.class, LocalDate::parse);
        typeToFunction.put(Long.class, Long::parseLong);
        typeToFunction.put(BigInteger.class, BigInteger::new);

        return typeToFunction.getOrDefault(field.getType(), type -> {
            throw new UnsupportedOperationException("Type is not supported by parser " + type);
        }).apply(value);
    }

    private Table convertToTable(ORMInterface.DataInputSource dataInputSource) {
        if (dataInputSource instanceof ORMInterface.DatabaseInputSource) {
            return new DatabaseParsingStrategy().
                    parseToTable((ORMInterface.DatabaseInputSource) dataInputSource);
        } else if (dataInputSource instanceof ORMInterface.StringInputSource) {
            return getStringParsingStrategy((ORMInterface.StringInputSource) dataInputSource).
                    parseToTable((ORMInterface.StringInputSource) dataInputSource);
        } else {
            throw new UnsupportedOperationException("Unknown DataInputSource " + dataInputSource);
        }
    }

    private ParsingStrategy<ORMInterface.StringInputSource> getStringParsingStrategy(ORMInterface.StringInputSource inputSource) {
        String content = inputSource.getContent();
        char firstChar = content.charAt(0);
        return switch (firstChar) {
            case '{', '[' -> new JSONParsingStrategy();
            case '<' -> new XMLParsingStrategy();
            default -> new CSVParsingStrategy();
        };
    }


}
