package xzero.model.factory;

import xzero.model.AbstractCell;
import xzero.model.Cell;
import xzero.model.CellAlongTheDiagonal;

/**
 * Фабрика, порождающая возможные виды ячеек. Реализует самую простую стратегию
 */
public class CellFactory {
    
    public Cell createCell(){
       return new Cell(); 
    }

    public CellAlongTheDiagonal createcellAlongTheDiagonal(){
        return new CellAlongTheDiagonal();
    }
}
