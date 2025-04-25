package xzero.view;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import xzero.model.GameModel;
import xzero.model.Label;
import xzero.model.Player;
import xzero.model.CellAlongTheDiagonal;
import xzero.model.events.GameEvent;
import xzero.model.events.GameListener;
import xzero.model.events.PlayerActionEvent;
import xzero.model.events.PlayerActionListener;

public class GamePanel extends JFrame {
    
    private JPanel fieldPanel = new JPanel();
    
    private JPanel infoPanel = new JPanel();
    private JButton labelInfo = new JButton();
    private JLabel playerInfo = new JLabel();


    private JMenuBar menu = null;
    private final String[] fileItems = new String []{"New", "Exit", "Skip"};
    private final int CELL_SIZE = 50;
    private final int TITLE_HEIGHT = 40;
    
    private GameModel _model = new GameModel();

    public GamePanel() {
        super();

        this.setTitle("Крестики-нолики NEXT");
        
        // Представление должно реагировать на изменение состояния модели
        _model.addGameListener(new GameObserver());
        _model.addPlayerActionListener(new PlayerObserver());

        // Меню
        createMenu();
        setJMenuBar(menu);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Box mainBox = Box.createVerticalBox();

        // Информационная панель
        mainBox.add(Box.createVerticalStrut(10));
        mainBox.add(createInfoPanel());

        // Игровое поле
        mainBox.add(Box.createVerticalStrut(10));
        fieldPanel.setDoubleBuffered(true);
        //_model.start();
        createField();
        setEnabledField(false);
        mainBox.add(fieldPanel);
        
        setContentPane(mainBox);
        pack();
        setResizable(false);
       // _model.start();
    }
    
// ---------------------- Создаем информационную панель -----------------------
    
    private Box createInfoPanel() {
        
        Box box = Box.createHorizontalBox();
        
        box.add(Box.createHorizontalStrut(10));
        
        box.add(new JLabel("Игрок :"));
        playerInfo.setText("?");
        box.add(Box.createHorizontalStrut(10));
        box.add(playerInfo);
        
        box.add(Box.createHorizontalStrut(20));

        box.add(new JLabel("Метка :"));
        box.add(Box.createHorizontalStrut(10));
        
        labelInfo.setEnabled(false);
        labelInfo.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        labelInfo.setMinimumSize(new Dimension(CELL_SIZE, CELL_SIZE));
        labelInfo.setMaximumSize(new Dimension(CELL_SIZE, CELL_SIZE));
        box.add(labelInfo);

        box.add(Box.createHorizontalStrut(10));
        
        return box;
    }
        
// --------------------------- Отрисовываем поле ------------------------------    
    
    private void createField(){
        
        fieldPanel.setDoubleBuffered(true);
        fieldPanel.setLayout(new GridLayout(_model.field().height(), _model.field().width()));
        
        Dimension fieldDimension = new Dimension(CELL_SIZE*_model.field().height(), CELL_SIZE*_model.field().width());
        
        fieldPanel.setPreferredSize(fieldDimension);
        fieldPanel.setMinimumSize(fieldDimension);
        fieldPanel.setMaximumSize(fieldDimension);
        
        repaintField();
    }
    
    public void repaintField() {
        
        fieldPanel.removeAll();

        for (int row = 1; row <= _model.field().height(); row++) 
        {
            for (int col = 1; col <= _model.field().width(); col++) 
            {
                JButton button = new JButton("");
                button.setFocusable(false);
                fieldPanel.add(button);
                button.addActionListener(new ClickListener());
            }
        }

        fieldPanel.validate();
    }
    
    private Point buttonPosition(JButton btn){
        
        int index = 0;
        for(Component widget: fieldPanel.getComponents())
        {
            if(widget instanceof JButton)
            {
                if(btn.equals((JButton)widget))
                {
                    break;
                }
                index++;
            }
         }
        int fieldWidth = _model.field().width();
        return new Point(index%fieldWidth + 1, index/fieldWidth + 1);
    }
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////
   private JButton getButton(Point pos) {

       int index = _model.field().width()*(pos.y-1) + (pos.x-1);
       
        for(Component widget: fieldPanel.getComponents())
        {
            if(widget instanceof JButton)
            {
                if(index == 0)
                {
                    return (JButton)widget;
                }
                index--;
            }
         }
        
        return null;
    }

    private void drawLabelOnField(Label l){ 
        
        JButton btn = getButton(l.cell().position());
        btn.setText(l.player().name());
    }
    
    private void drawLabelOnInfoPanel(Label l){ 
        
        labelInfo.setText(l.player().name());
    }   

    private void drawPlayerOnInfoPanel(Player p){ 
        
        playerInfo.setText(p.name());
    }
    
    private void setEnabledField(boolean on){

        Component comp[] = fieldPanel.getComponents();
        for(Component c : comp)
        {    c.setEnabled(on);   }
    }
    
// ----------------------------- Создаем меню ----------------------------------  
    
    private void createMenu() {
 
        menu = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        for (int i = 0; i < fileItems.length; i++) {
           
            JMenuItem item = new JMenuItem(fileItems[i]);
            item.setActionCommand(fileItems[i].toLowerCase());
            item.addActionListener(new NewMenuListener());
            fileMenu.add(item);
        }
        fileMenu.insertSeparator(1);

        menu.add(fileMenu);
    }

    public class NewMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if ("exit".equals(command)) {
                System.exit(0);
            }
            if ("new".equals(command)) {
                CellAlongTheDiagonal.clearXO();
                _model.start();
                createField();
            }
            if("skip".equals(command)){
                _model.skipATurn();
            }
        }
    }
    
// ------------------------- Реагируем на действия игрока ----------------------
    
    private class ClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            button.setEnabled(false);
            // Ставим на поле метку текущего игрока
            Point p = buttonPosition(button);
            boolean isOk = _model.absenceOfDuplication(p, _model.activePlayer().name());
            if(!isOk){
                _model.cellsAlong(p, _model.activePlayer().name());
                _model.change(_model.activePlayer().name(), p);
                _model.activePlayer().setLabelTo(p);
            } else {
                button.setEnabled(true);
            }
        }
    }


    private class PlayerObserver implements PlayerActionListener{

        @Override
        public void labelisPlaced(PlayerActionEvent e) {
            
            drawLabelOnField(e.label());
        }

        @Override
        public void labelIsReceived(PlayerActionEvent e) {
            drawLabelOnInfoPanel(e.label());
        }
    }
    
    private class GameObserver implements GameListener{

        @Override
        public void gameFinished(GameEvent e){

            // Если победитель найден - выдаём сообщение и закрываем все кнопки
            if(e.player() != null)
            {
                String str = "Победил игрок '" + e.player().name() + "' !!!";
                
                JOptionPane.showMessageDialog(null, str, "Победа!", JOptionPane.INFORMATION_MESSAGE);
            
                setEnabledField(false);
            }
        }

        @Override
        public void playerExchanged(GameEvent e) {
            drawPlayerOnInfoPanel(e.player());
        }
    }   
}
