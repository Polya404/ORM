package knubisoft.parsingStrategy;

import knubisoft.ORMInterface;
import knubisoft.Table;

public interface ParsingStrategy<T extends ORMInterface.DataInputSource> {
    Table parseToTable(T content);
}
