package knubisoft.writeStrategy;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class CSVWriteStrategy implements WriteStrategy {
    private File file;

    @SneakyThrows
    @Override
    public void write(List<?> list) {
        Path path = Path.of(file.getPath());
        Files.delete(Path.of(file.getPath()));
        Files.createFile(path);
        try (PrintWriter pw = new PrintWriter(file)) {
            getPerson(list).stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
    }

    @SneakyThrows
    public List<String[]> getPerson(List<?> list) {
        List<String[]> res = new ArrayList<>();
        Class cls = list.get(0).getClass();
        Field[] fields = cls.getDeclaredFields();
        String[] first = new String[fields.length];

        for (int i = 0; i < fields.length; i++) {
            first[i] = fields[i].getName();
        }
        res.add(first);
        String[] person = new String[fields.length];
        for (Object o : list) {
            int i = 0;
            for (Field f : fields) {
                f.setAccessible(true);
                String s = String.valueOf(f.get(o));
                person[i] = s;
                i++;
            }
            res.add(person.clone());
        }
        return res;
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
}
