package knubisoft.parsingStrategy;

import knubisoft.ORMInterface;
import knubisoft.Table;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedHashMap;
import java.util.Map;

public class DatabaseParsingStrategy implements ParsingStrategy<ORMInterface.DatabaseInputSource> {

    @SneakyThrows
    private Map<Integer, Map<String, String>> buildTable(ResultSet rs) {
        ResultSetMetaData metadata = rs.getMetaData();

        Map<Integer, Map<String, String>> result = new LinkedHashMap<>();
        int rowId = 0;
        while (rs.next()) {
            Map<String, String> row = new LinkedHashMap<>();
            for (int index = 0; index < metadata.getColumnCount(); index++) {
                row.put(metadata.getColumnName(index), rs.getString(index));
            }
            result.put(rowId, row);
            rowId++;
        }

        return result;
    }

    @Override
    public Table parseToTable(ORMInterface.DatabaseInputSource content) {
        ResultSet rs = content.getResultSet();
        Map<Integer, Map<String, String>> result = buildTable(rs);
        return new Table(result);
    }
}
