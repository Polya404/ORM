package knubisoft.parsingStrategy;

import knubisoft.readWriteSources.ConnectionReadWriteSource;
import knubisoft.Table;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedHashMap;
import java.util.Map;

public class DatabaseParsingStrategy implements ParsingStrategy<ConnectionReadWriteSource> {
    @Override
    public Table parseToTable(ConnectionReadWriteSource content) {
        ResultSet rs = content.getContent(); // get data from data base
        Map<Integer, Map<String, String>> result = buildTable(rs);
        return new Table(result);
    }

    @SneakyThrows
    private Map<Integer, Map<String, String>> buildTable(ResultSet rs) {
        ResultSetMetaData metaData = rs.getMetaData();

        Map<Integer, Map<String, String>> result = new LinkedHashMap<>();
        int rowId = 0;
        while (rs.next()) {
            Map<String, String> row = new LinkedHashMap<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                row.put(metaData.getCatalogName(i), rs.getString(i));
            }
            result.put(rowId, row);
            rowId++;
        }
        return result;
    }
}