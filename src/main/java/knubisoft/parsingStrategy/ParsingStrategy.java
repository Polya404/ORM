package knubisoft.parsingStrategy;

import knubisoft.Table;

public interface ParsingStrategy {
    Table parseToTable(String content);
}
