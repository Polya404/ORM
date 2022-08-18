package knubisoft;

import knubisoft.parsingStrategy.*;
import knubisoft.readWriteSources.ConnectionReadWriteSource;
import knubisoft.readWriteSources.DataReadWriteSource;
import knubisoft.readWriteSources.FileReadWriteSource;
import knubisoft.writeStrategy.*;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ORM implements ORMInterface {
    @Override
    public <T> List<T> readAll(DataReadWriteSource<?> inputSource, Class<T> cls) {
        Table table = convertToTable(inputSource);
        return convertTableToListOfClasses(table, cls);
    }

    private <T> List<T> convertTableToListOfClasses(Table table, Class<T> cls) {
        List<T> result = new ArrayList<>();
        for (int i = 0; i < table.size(); i++) {
            Map<String, String> row = table.getTableRowByIndex(i);
            T instance = reflectTableRowToClass(row, cls);
            result.add(instance);
        }
        return result;
    }

    @SneakyThrows
    private <T> T reflectTableRowToClass(Map<String, String> row, Class<T> cls) {
        T instance = cls.getDeclaredConstructor().newInstance();
        for (Field each : cls.getDeclaredFields()) {
            each.setAccessible(true);
            String value = row.get(each.getName());
            if (value != null) {
                each.set(instance, transformValueToFieldType(each, value));
            }
        }
        return instance;
    }

    private Object transformValueToFieldType(Field field, String value) {
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

    private Table convertToTable(DataReadWriteSource<?> inputSource) {
        if (inputSource instanceof ConnectionReadWriteSource) {
            ConnectionReadWriteSource databaseSource = (ConnectionReadWriteSource) inputSource;
            return new DatabaseParsingStrategy().parseToTable(databaseSource);
        } else if (inputSource instanceof FileReadWriteSource) {
            FileReadWriteSource fileSource = (FileReadWriteSource) inputSource;
            return getStringParsingStrategy(fileSource).parseToTable(fileSource);
        } else {
            throw new UnsupportedOperationException("Unknown DataInputSource " + inputSource);
        }
    }

    @Override
    public <T> void writeAll(DataReadWriteSource target, List<T> object) {
        if (target instanceof FileReadWriteSource) {
            WriteStrategy strategy = getWriteStrategy(target);
            writeToFile(strategy, object);
        }
        if (target instanceof ConnectionReadWriteSource){
            DataBaseWriteStrategy strategy = new DataBaseWriteStrategy();
            strategy.write(object);
        }
    }

    private void writeToFile(WriteStrategy strategy, List<?> object) {
        strategy.write(object);
    }

    private WriteStrategy getWriteStrategy(DataReadWriteSource target) {
        String ext = FilenameUtils.getExtension(((FileReadWriteSource) target).getSource().getName());
        if (ext.equals("json")) {
            return new JSONWriteStrategy(((FileReadWriteSource) target).getSource());
        }else if(ext.equals("xml")){
            return new XMLWriteStrategy(((FileReadWriteSource) target).getSource());
        }else {
            return new CSVWriteStrategy(((FileReadWriteSource) target).getSource());
        }
    }

    private ParsingStrategy<FileReadWriteSource> getStringParsingStrategy(FileReadWriteSource inputSource) {
        String content = inputSource.getContent();
        char firstChar = content.charAt(0);
        return switch (firstChar) {
            case '{', '[' -> new JSONParsingStrategy();
            case '<' -> new XMLParsingStrategy();
            default -> new CSVParsingStrategy();
        };
    }
}