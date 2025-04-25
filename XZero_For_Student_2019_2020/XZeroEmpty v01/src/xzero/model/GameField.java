package xzero.model;

import java.util.List;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import xzero.model.navigation.Direction;
import xzero.model.navigation.Shift;

/**
 *  Прямоугольное поле, состоящее из ячеек
 */
public class GameField {
    
// ----------------- Контейнер ячеек, а в конечном счете, и меток ---------------
// Позиции ячеек и меток  задаются строками и столбцами в диапазоне [1, height] и
// [1, width] соответсвенно
    
// ------------------------------ Ячейки ---------------------------------------    
    private ArrayList<AbstractCell> _cellPool = new ArrayList();

    AbstractCell cell(Point pos){

        for(AbstractCell obj : _cellPool)
        {
            if(obj.position().equals(pos))     
            { return obj; }
        }
            
        return null;
    }
    
    void setCell(Point pos, Cell cell){
        // Удаляем старую ячейку
        removeCell(pos);
        
        // Связываем ячейку с полем
        cell.setField(this);
        cell.setPosition(pos);
        
        // Добавляем новую ячейку
        _cellPool.add(cell);
    }


    void setCell(Point pos, CellAlongTheDiagonal cell){
        // Удаляем старую ячейку
        removeCell(pos);

        // Связываем ячейку с полем
        cell.setField(this);
        cell.setPosition(pos);

        // Добавляем новую ячейку
        _cellPool.add(cell);
    }


    public AbstractCell getCell(Point pos){
        return cell(pos);
    }

    //!!!
    public List<AbstractCell> cells(){
        return Collections.unmodifiableList(_cellPool);
    }

    //!!!
    public void clear(){
        _cellPool.clear();
    }    
    
    private void removeCell(Point pos){

        AbstractCell obj = cell(pos);
        if(obj != null)     _cellPool.remove(obj);
    }
    
// ------------------------------ Метки ---------------------------------------    
    
    public Label label(Point pos){

        AbstractCell obj = cell(pos);
        if(obj != null)     return obj.label();
            
        return null;
    }

    public void setLabel(Point pos, Label label){

        AbstractCell obj = cell(pos);
        if(obj != null)  obj.setLabel(label);  // !!!
    }

    private ArrayList<Label> _labelPool = new ArrayList(); 
    
    public List<Label> labels(){
        _labelPool.clear();
        
        for(AbstractCell obj : _cellPool)
        { 
            Label l = obj.label();
            if(l != null)
            {
                _labelPool.add(obj.label()); 
            }
        }
            
        return Collections.unmodifiableList(_labelPool);
    }
    
    // Возвращает ряд меток, принадлежащих одному игроку
    public List<Label> labelLine(Point start, Direction direct){
        
        ArrayList<Label> line = new ArrayList<>();
        boolean isLineFinished = false;
        Player startPlayer = null;
        
        // Определяем первую метку и соответствующего ей игрока
        Point pos = new Point(start);
        Label l = label(pos);
        
        isLineFinished = (l == null);
        if(!isLineFinished)
        {
            line.add(l);
            startPlayer = line.get(0).player();
        }

        Shift shift = direct.shift();
        pos.translate(shift.byHorizontal(), shift.byVertical());
        while(!isLineFinished && containsRange(pos))
        {
            l = label(pos);
            isLineFinished = (l == null || !l.player().equals(startPlayer));
            
            if(!isLineFinished)
            {
                line.add(l); 
            }

            pos.translate(shift.byHorizontal(), shift.byVertical());
        }
        
        return line;
    }    
    
// ----------------------- Ширина и высота поля ------------------------------
    private int _width;
    private int _height;

    public void setSize(int width, int height) {
 
        _width = width;
        _height = height;
        
        // Удаляем все ячейки находящиеся вне поля
        for (AbstractCell obj : _cellPool)
        {
            if(!containsRange(obj.position()) )  
            { 
                _cellPool.remove(obj);
            }
        }
    }

    public int width() {
        return _width;
    }

    public int height() {
        return _height;
    }
    
    public boolean containsRange(Point p){
        return p.getX() >= 1 && p.getX() <= _width && p.getY() >= 1 && p.getY() <= _height ;
    }
    
// ----------------------------------------------------------------------------    
    public GameField() {
        
        setSize(10, 10);
    }
}
