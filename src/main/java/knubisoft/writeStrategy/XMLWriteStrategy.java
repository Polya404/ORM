package knubisoft.writeStrategy;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@AllArgsConstructor
public class XMLWriteStrategy implements WriteStrategy {
    private File file;

    @SneakyThrows
    @Override
    public void write(List<?> list) {
        Path path = Path.of(file.getPath());
        Files.delete(Path.of(file.getPath()));
        Files.createFile(path);
        XmlMapper xmlMapper = new XmlMapper();
        String xml = "";
        for (Object o : list) {
            if (o.equals(list.get(0))) {
                xml = "<?xml version='1.0' encoding='UTF-8'?>\n<dataset>\n" + xmlMapper.writeValueAsString(o);
            } else if (o.equals(list.get(list.size() - 1))) {
                xml = xmlMapper.writeValueAsString(o) + "\n</dataset>";
            } else {
                xml = xmlMapper.writeValueAsString(o);
            }
            Files.write(Path.of(file.getPath()), xml.getBytes(), StandardOpenOption.APPEND);
        }
    }
}
