package xzero.model.events;

import java.util.EventObject;
import xzero.model.Player;

/*
 * Событие, связанное с изменением состояния игры
 */
public class GameEvent extends EventObject {
    
    Player _player;
    
    public void setPlayer(Player p) {
        _player = p;
    }
    
    public Player player(){
        return _player;
    }
    
    public GameEvent(Object source) { 
        super(source); 
    } 
} 
