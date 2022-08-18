package knubisoft.parsingStrategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import knubisoft.readWriteSources.FileReadWriteSource;
import knubisoft.Table;
import lombok.SneakyThrows;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class XMLParsingStrategy implements ParsingStrategy<FileReadWriteSource> {
    JSONParsingStrategy jsonParsingStrategy = new JSONParsingStrategy();

    @SneakyThrows
    @Override
    public Table parseToTable(FileReadWriteSource content) {
        XmlMapper xmlMapper = new XmlMapper();
        JsonNode tree = xmlMapper.readTree(content.getContent());
        Map<Integer, Map<String, String>> result = buildTable(tree);
        return new Table(result);
    }

    private Map<Integer, Map<String, String>> buildTable(JsonNode tree) {
        Map<Integer, Map<String, String>> map = new LinkedHashMap<>();
        int index = 0;
        for (JsonNode each : tree) {
            for (JsonNode node : each) {
                Map<String, String> item = buildRow(node);
                map.put(index, item);
                index++;
            }
        }
        return map;
    }

    private Map<String, String> buildRow(JsonNode each) {
        Map<String, String> item = new LinkedHashMap<>();
        Iterator<Map.Entry<String, JsonNode>> itr = each.fields();
        while (itr.hasNext()) {
            Map.Entry<String, JsonNode> next = itr.next();
            item.put(next.getKey(), next.getValue().textValue());
        }
        return item;
    }

}