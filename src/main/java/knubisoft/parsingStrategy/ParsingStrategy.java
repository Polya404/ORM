package knubisoft.parsingStrategy;

import knubisoft.DataReadWriteSource;
import knubisoft.ORMInterface;
import knubisoft.Table;

public interface ParsingStrategy<T extends DataReadWriteSource> {
    Table parseToTable(T content);
}
