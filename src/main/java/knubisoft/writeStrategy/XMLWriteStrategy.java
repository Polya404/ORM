package knubisoft.writeStrategy;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@AllArgsConstructor
public class XMLWriteStrategy implements WriteStrategy {
    private File file;

    @SneakyThrows
    @Override
    public void write(List<?> list) {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JavaTimeModule());
        String xml = xmlMapper.writeValueAsString(list);
        Files.write(Path.of(file.getPath()), xml.getBytes());
    }
}
