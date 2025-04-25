package xzero.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import xzero.model.events.GameEvent;
import xzero.model.events.GameListener;
import xzero.model.events.PlayerActionEvent;
import xzero.model.events.PlayerActionListener;
import xzero.model.factory.CellFactory;
import xzero.model.factory.LabelFactory;
import xzero.model.navigation.Direction;

/**
/* Aбстракция всей игры; генерирует стартовую обстановку; поочередно передает 
* ход игрокам, задавая им метку для установки на поле; следит за игроками с 
* целью определения конца игры
*/

public class GameModel {

// -------------------------------- Поле -------------------------------------
    
   // !!!
    private GameField sizeOfField = new GameField();
    
    public GameField field(){
       // !!!
        return sizeOfField;
        //return null;
    }
    
// -------------------------------- Игроки -----------------------------------
    
    private ArrayList<Player> _playerList = new ArrayList<>();
    private int _activePlayer;

    public Player activePlayer(){
        return _playerList.get(_activePlayer);
    }

    public Player getActivePlayer(int num){
        return _playerList.get(num);
    }
    
    public GameModel(){
        // Задаем размеры поля по умолчанию
        field().setSize(5, 5);
        
        // Создаем двух игроков
        Player p;
        PlayerObserver observer = new PlayerObserver();
        
        p = new Player(field(), "X");
        //!!!0 // "Следим" за игроком
        p.addPlayerActionListener(observer);
        _playerList.add(p);
        _activePlayer = 0;
        
        p = new Player(field(), "O");
        //!!!0 // "Следим" за игроком
        p.addPlayerActionListener(observer);
        _playerList.add(p);
    }

    public void cellsAlong(Point pos, String name){
        AbstractCell cell = sizeOfField.getCell(pos);
        CellAlongTheDiagonal diagonalCell = new CellAlongTheDiagonal();

        if(cell instanceof CellAlongTheDiagonal){
             diagonalCell = (CellAlongTheDiagonal)cell;
        }

        diagonalCell.findingCellsAlongDiagonals(pos, name);
    }

    public boolean absenceOfDuplication(Point p, String name){
        AbstractCell cell = sizeOfField.getCell(p);
        CellAlongTheDiagonal diagonalCell = new CellAlongTheDiagonal();

        if(cell instanceof CellAlongTheDiagonal){
            diagonalCell = (CellAlongTheDiagonal)cell;
        }

         return diagonalCell.absenceOfCellDuplication(p, name);
    }

    public void change(String name, Point point){
        AbstractCell cell = sizeOfField.getCell(point);
        CellAlongTheDiagonal diagonalCell = new CellAlongTheDiagonal();

        if(cell instanceof CellAlongTheDiagonal){
            diagonalCell = (CellAlongTheDiagonal)cell;
        }

        ArrayList<Point> array = diagonalCell.changeCell(name);

        for (Point p: array) {
            field().setCell(p, _cellFactory.createcellAlongTheDiagonal());
        }
    }


    // ---------------------- Пропуск хода ---------------------

    public void skipATurn(){
        exchangePlayer();
    }

    // ---------------------- Порождение обстановки на поле ---------------------

    private CellFactory _cellFactory = new CellFactory();

    public CellFactory getCellFactory(){
        return _cellFactory;
    }

    private void generateField(){

        field().clear();
        field().setSize(5, 5);
        for(int row = 1; row <= field().height(); row++)
        {
            for(int col = 1; col <= field().width(); col++)
            {
                field().setCell(new Point(col, row), _cellFactory.createCell());
            }
        }
    }

// ----------------------------- Игровой процесс ----------------------------
    
    public void start(){
        
        // Генерируем поле
        generateField();
        
        // Передаем ход первому игроку
        _activePlayer = _playerList.size()-1;
        exchangePlayer();
    }

    public LabelFactory getLabelFactory() {
        return _labelFactory;
    }

    private LabelFactory _labelFactory = new LabelFactory();

    public void exchangePlayer(){

        // Сменить игрока
        _activePlayer++;
        if(_activePlayer >= _playerList.size())     _activePlayer = 0;
        
        // Выбрать для него метку
        Label newLabel = _labelFactory.createLabel();
        activePlayer().setActiveLabel(newLabel);
        
        // Генерируем событие
        firePlayerExchanged(activePlayer());
    }
    
    
    private static int WINNER_LINE_LENGTH = 5;
    
    private Player determineWinner(){
    
        for(int row = 1; row <= field().height(); row++)
        {
            for(int col = 1; col <= field().width(); col++)
            {
                Point pos = new Point(col, row);
                Direction direct = Direction.north();
                for(int  i = 1; i <= 8; i++)
                {
                    direct = direct.rightword();

                    List<Label> line = field().labelLine(pos, direct);

                    if(line.size() >= WINNER_LINE_LENGTH)
                    {
                        return line.get(0).player();
                    }
                }
            }
        }
        
        return null;
    }

    // ------------------------- Реагируем на действия игрока ------------------

    private class PlayerObserver implements PlayerActionListener{

        @Override
        public void labelisPlaced(PlayerActionEvent e) {
            
            //  Транслируем событие дальше для активного игрока
            if(e.player() == activePlayer()){
                fireLabelIsPlaced(e);
            }
            
            // Определяем победителя
            Player winner = determineWinner();
            
            // Меняем игрока, если игра продолжается

            if(winner == null)
            {
                exchangePlayer();
            }
            else
            { 
                // Генерируем событие
                fireGameFinished(winner);
            }
        }

        @Override
        public void labelIsReceived(PlayerActionEvent e) {
            //  Транслируем событие дальше для активного игрока
            if(e.player() == activePlayer()){
                fireLabelIsRecived(e);
            }
        }
    }
    
// ------------------------ Порождает события игры ----------------------------
    
    // Список слушателей
    private ArrayList _listenerList = new ArrayList(); 
 
    // Присоединяет слушателя
    public void addGameListener(GameListener l) { 
        _listenerList.add(l); 
    }
    
    // Отсоединяет слушателя
    public void removeGameListener(GameListener l) { 
        _listenerList.remove(l); 
    } 
    
    // Оповещает слушателей о событии
    protected void fireGameFinished(Player winner) {
        
        GameEvent event = new GameEvent(this);
        event.setPlayer(winner);
        for (Object listner : _listenerList)
        {
            ((GameListener)listner).gameFinished(event);
        }
    }     

    protected void firePlayerExchanged(Player p) {
        
        GameEvent event = new GameEvent(this);
        event.setPlayer(p);
        for (Object listner : _listenerList)
        {
            ((GameListener)listner).playerExchanged(event);
        }
    }     
    
// --------------------- Порождает события, связанные с игроками -------------
    
    // Список слушателей
    private ArrayList _playerListenerList = new ArrayList(); 
 
    // Присоединяет слушателя
    public void addPlayerActionListener(PlayerActionListener l) { 
        _playerListenerList.add(l); 
    }
    
    // Отсоединяет слушателя
    public void removePlayerActionListener(PlayerActionListener l) { 
        _playerListenerList.remove(l); 
    } 
    
    // Оповещает слушателей о событии
    protected void fireLabelIsPlaced(PlayerActionEvent e) {
        
        for (Object listner : _playerListenerList)
        {
            ((PlayerActionListener)listner).labelisPlaced(e);
        }
    }     
    
    protected void fireLabelIsRecived(PlayerActionEvent e) {
        
        for (Object listner : _playerListenerList)
        {
            ((PlayerActionListener)listner).labelIsReceived(e);
        }
    }         
}
