package knubisoft.parsingStrategy;

import knubisoft.readWriteSources.DataReadWriteSource;
import knubisoft.Table;

public interface ParsingStrategy<T extends DataReadWriteSource> {
    Table parseToTable(T content);
}
