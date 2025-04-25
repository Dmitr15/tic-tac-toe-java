package xzero.model.events;

import java.util.EventListener;

/*
 * Слушатель действий игрока
 */
public interface PlayerActionListener extends EventListener {
    void labelisPlaced(PlayerActionEvent e);
    
    void labelIsReceived(PlayerActionEvent e);
}
