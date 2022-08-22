package knubisoft;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ModelSQLHelper {
    private final List<String> availableFieldInDatabase;

    public String buildSQL(Object o){
        Class<? extends Object> cls = o.getClass();
        String tableName = getTableName(cls);
        String fields = getFields(cls);
        String arguments = getArguments(cls);
        return String.format("INSERT INTO %s (%s) VALUES (%s);",
                tableName, fields, arguments);
    }

    private String getArguments(Class<?> cls) {
        List<Field> fields = Arrays.asList(cls.getDeclaredFields());
        List<String> listFieldNames = fields.stream().map(Field::getName)
                .filter(availableFieldInDatabase::contains).map(field -> "?").
                collect(Collectors.toList());
        return String.join(",", listFieldNames);
    }

    private String getFields(Class<?> cls) {
        List<Field> fields = Arrays.asList(cls.getDeclaredFields());
        List<String> listFieldNames = fields.stream().map(Field::getName)
                .filter(availableFieldInDatabase::contains).
                collect(Collectors.toList());
        return String.join(",", listFieldNames);
    }

    private String getTableName(Class<?> cls) {
        return cls.getAnnotation(TableAnnotation.class).value();
    }

    @SneakyThrows
    public void bindArguments(Object o, PreparedStatement ps){
        int index = 1;
        for (Field field: o.getClass().getDeclaredFields()){
            if (availableFieldInDatabase.contains(field.getName())) {
                field.setAccessible(true);
                ps.setObject(index, field.get(o));
                index++;
            }
        }
    }
}
