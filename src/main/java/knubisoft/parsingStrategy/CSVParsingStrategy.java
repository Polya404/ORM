package knubisoft.parsingStrategy;

import knubisoft.readWriteSources.FileReadWriteSource;
import knubisoft.Table;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CSVParsingStrategy implements ParsingStrategy<FileReadWriteSource> {
    public static final String DELIMITER = ",";
    public static final String COMMENT = "--";
    @SneakyThrows
    @Override
    public Table parseToTable(FileReadWriteSource content) {
        List<String> lines = Arrays.asList(content.getContent().split(System.lineSeparator()));
        Map<Integer, String> mapping = buildMapping(lines.get(0));
        Map<Integer, Map<String,String>> result = buildTable(lines.subList(1, lines.size()), mapping);
        return new Table(result);
    }

    private Map<Integer, Map<String, String>> buildTable(List<String> lines, Map<Integer, String> mapping) {
        Map<Integer, Map<String,String>> result = new LinkedHashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            result.put(i, buildRow(mapping, line));
        }
        return result;
    }

    private Map<String, String> buildRow(Map<Integer, String> mapping, String line) {
        Map<String,String> nameToValueMap = new LinkedHashMap<>();
        String[] rowItems = splitLine(line);
        for (int rowIndex = 0; rowIndex < rowItems.length; rowIndex++) {
            String value = rowItems[rowIndex];
            nameToValueMap.put(mapping.get(rowIndex),value);
        }
        return nameToValueMap;
    }

    private Map<Integer, String> buildMapping(String firstLine) {
        Map<Integer,String> map = new LinkedHashMap<>();
        String[] array = splitLine(firstLine);
        for (int i = 0; i < array.length; i++) {
            String value = array[i];
            if (value.contains(COMMENT)){
                value = value.split(COMMENT)[0];
            }
            map.put(i,value.trim());
        }
        return map;
    }

    private String[] splitLine(String line) {
        return line.split(DELIMITER);
    }
}