package xzero.model.events;

import java.util.EventListener;

/*
 * Слушатель состояния игры
 */
public interface GameListener extends EventListener {
    public void gameFinished(GameEvent e);
    public void playerExchanged(GameEvent e);
}
