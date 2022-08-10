package knubisoft.parsingStrategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import knubisoft.Table;
import lombok.SneakyThrows;


public class XMLParsingStrategy implements ParsingStrategy {
    JSONParsingStrategy jsonParsingStrategy = new JSONParsingStrategy();

    @SneakyThrows
    @Override
    public Table parseToTable(String content) {
        // TODO
        XmlMapper xmlMapper = new XmlMapper();
        JsonNode tree = xmlMapper.readTree(content);
        int i = String.valueOf(tree.fields().next()).indexOf("[");
        String str = String.valueOf(tree.fields().next()).substring(i);

        return jsonParsingStrategy.parseToTable(str);
    }

}