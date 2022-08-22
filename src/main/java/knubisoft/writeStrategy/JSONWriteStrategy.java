package knubisoft.writeStrategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
        objectMapper.registerModule(new JavaTimeModule());
        String jsonString = objectMapper.writeValueAsString(list);
        Files.write(Path.of(file.getPath()), jsonString.getBytes());
    }
}
