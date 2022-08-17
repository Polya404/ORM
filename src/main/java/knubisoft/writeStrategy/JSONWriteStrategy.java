package knubisoft.writeStrategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@AllArgsConstructor
public class JSONWriteStrategy implements WriteStrategy {
    private File file;

    @SneakyThrows
    public void write(List<?> list) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "";
        for (Object o : list) {
            if (o.equals(list.get(0))) {
                jsonString = "[" + objectMapper.writeValueAsString(o) + ",";
            }
            else if (o.equals(list.get(list.size() - 1))) {
                jsonString = objectMapper.writeValueAsString(o) + "]";
            } else {
                jsonString = objectMapper.writeValueAsString(o) + ",";
            }
            Files.write(Path.of(file.getPath()), jsonString.getBytes(), StandardOpenOption.APPEND);
        }
    }
}
