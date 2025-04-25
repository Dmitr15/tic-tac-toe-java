package xzero.model.events;

import java.util.EventObject;
import xzero.model.Label;
import xzero.model.Player;

/*
 * Событие, связанное с любой деятельностью робота
 */
public class PlayerActionEvent extends EventObject {
    
// -------------------------------- Игрок --------------------------------------    
    Player _player;
    
    public void setPlayer(Player p){
        _player = p;
    }
    
    public Player player(){
        return _player;
    }

// ------------------------- Активная метка ------------------------------
    
    Label _label;
    
    public void setLabel(Label l){
        _label = l;
    }
    
    public Label label(){
        return _label;
    }

    public PlayerActionEvent(Object source) { 
        super(source); 
    } 
} 
