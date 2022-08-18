package knubisoft.writeStrategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CSVWriteStrategy implements WriteStrategy {
    private File file;

    @SneakyThrows
    @Override
    public void write(List<?> list) {
        FileUtils.writeStringToFile(file, String.join(System.lineSeparator(), getPerson(list)), StandardCharsets.UTF_8);
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
        List<String> person = new ArrayList<>();
        for (Field f: fields){
            f.setAccessible(true);
            String s = String.valueOf(f.get(o));
            person.add(s);
        }
        return String.join(",", person);
    }

    public String convertToHeader(List<Field> fields){
        List<String> str = new ArrayList<>();
        for (Field f:fields){
            str.add(f.getName());
        }
        return String.join(",", str);
    }

}
