package xzero.model;

/**
 * Метка, которую можно поместить на поле
 */
public class Label {
    
// --------------- Ячейка, которой прнадлежит метка. Задает сама ячейка -------
    //!!!
    private AbstractCell cell = new Cell();
    
    public AbstractCell cell(){
        //!!!
        return cell;
        //return null;
    }

    public void setCell(AbstractCell c){
        cell = c;
    }

// - Игрок, которому прнадлежит метка. Метка может быть нейтральной (не принадлежать никому) -
    
    private Player _player = null;
    
    void setPlayer(Player p){
        _player = p;
    }

    void unsetPlayer(){
        _player = null;
    }
    
    public Player player(){
        return _player;
    } 
}
