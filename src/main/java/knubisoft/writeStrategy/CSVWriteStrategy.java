package knubisoft.writeStrategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class CSVWriteStrategy implements WriteStrategy {
    private File file;

    @SneakyThrows
    @Override
    public void write(List<?> list) {
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(getPerson(list));
        Files.write(Path.of(file.getPath()), toCsvString(s).getBytes());
    }

    @SneakyThrows
    public List<String> getPerson(List<?> list) {
        Class cls = list.get(0).getClass();
        List<Field> fields = Arrays.asList(cls.getDeclaredFields());
        List<String> result = new ArrayList<>();
        result.add(convertToHeader(fields));
        result.addAll(list.stream().map(item -> transform(item, fields)).collect(Collectors.toList()));

        return result;
    }

    @SneakyThrows
    public String transform(Object o, List<Field> fields){
        String[] person = new String[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
                fields.get(i).setAccessible(true);
                String s = String.valueOf(fields.get(i).get(o));
                person[i] = s;
            }
        return convertToCSV(person);
    }

    public String convertToHeader(List<Field> fields){
        String[] str = new String[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            str[i] = fields.get(i).getName();
        }
        return convertToCSV(str);
    }

    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    public String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    public String toCsvString(String data){
        data = data.substring(2, data.length()-2);
        data= data.replaceAll("\",\"", ",\n");
        return data;
    }
}
