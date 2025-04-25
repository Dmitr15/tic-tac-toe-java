package xzero.model;

import xzero.model.events.PlayerActionEvent;
import xzero.model.events.PlayerActionListener;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xzero.model.navigation.Direction;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;


 class XZeroTests {
    private static class XZeroListener implements PlayerActionListener {
        boolean labelPlaced = false;
        boolean labelReceived = false;

        @Override
        public void labelisPlaced(PlayerActionEvent e) {
            labelPlaced = true;
        }

        @Override
        public void labelIsReceived(PlayerActionEvent e) {
            labelReceived = true;
        }
    }

    // ------------------------------ Метки ---------------------------------------
    @Test
    public void LabelIsPlacedAndReceived() {
        GameModel model = new GameModel();
        XZeroListener playerListener = new XZeroListener();

        model.activePlayer().addPlayerActionListener(playerListener);
        model.start();
        model.activePlayer().setActiveLabel(new Label());
        model.activePlayer().setLabelTo(new Point(1, 1));

        Assertions.assertTrue(playerListener.labelPlaced);
        Assertions.assertTrue(playerListener.labelReceived);
    }

    @Test
    public void PlayerIsActivated() {
        GameModel model = new GameModel();
        Player activePlayer = model.activePlayer();

        Assertions.assertEquals(5, model.field().width());
        Assertions.assertEquals(5, model.field().height());
        Assertions.assertEquals("X", activePlayer.name());
    }

    @Test
    public void SetCellOnGameField() {
        GameField field = new GameField();
        Cell cell = new Cell();
        Point position = new Point(1, 1);

        field.setCell(position, cell);

        Assertions.assertEquals(cell, field.cell(position));
    }

    @Test
    public void SetLabelOnGameField() {
        GameField field = new GameField();
        Cell cell = new Cell();
        Point position = new Point(1, 1);
        Label label = new Label();

        field.setCell(position, cell);

        field.setLabel(position, label);
        Assertions.assertEquals(label, field.label(position));
    }

    @Test
    public void GameFieldContainsLabels() {
        GameField field = new GameField();

        Cell first_cell = new Cell();
        Cell second_cell = new Cell();
        field.setCell(new Point(1, 1), first_cell);
        field.setCell(new Point(2, 2), second_cell);

        Label label1 = new Label();
        Label label2 = new Label();
        Label label3 = new Label();
        field.setLabel(new Point(1, 1), label1);
        field.setLabel(new Point(2, 2), label2);

        List<Label> labels = field.labels();

        Assertions.assertEquals(2, labels.size());
        Assertions.assertFalse(labels.contains(label3));
        Assertions.assertTrue(labels.contains(label1));
        Assertions.assertTrue(labels.contains(label2));
    }

    @Test
    public void GameFieldIsCleared() {
        GameField field = new GameField();
        Cell cell = new Cell();
        field.setCell(new Point(2, 2), cell);

        field.clear();

        Assertions.assertNull(field.cell(new Point(2, 2)));
    }


    @Test
    public void ShiftOnEastLabelOnOneLine() {
        GameField field = new GameField();
        Player player = new Player(field, "X");
        Direction direction = Direction.east();

        Cell first_cell = new Cell();
        Cell second_cell = new Cell();
        Label first_label = new Label();
        Label second_label = new Label();

        field.setCell(new Point(1, 1), first_cell);
        field.setCell(new Point(2, 1), second_cell);
        field.setLabel(new Point(1, 1), first_label);
        field.setLabel(new Point(2, 1), second_label);

        first_label.setPlayer(player);
        second_label.setPlayer(player);

        List<Label> line = field.labelLine(new Point(1, 1), direction);

        Assertions.assertEquals(2, line.size());
        Assertions.assertTrue(line.contains(first_label));
        Assertions.assertTrue(line.contains(second_label));
    }

     @Test
     public void ShiftOnWestLabelOnOneLine() {
         GameField field = new GameField();
         Player player = new Player(field, "X");
         Direction direction = Direction.west();

         Cell first_cell = new Cell();
         Cell second_cell = new Cell();
         Label first_label = new Label();
         Label second_label = new Label();

         field.setCell(new Point(1, 1), first_cell);
         field.setCell(new Point(2, 1), second_cell);
         field.setLabel(new Point(1, 1), first_label);
         field.setLabel(new Point(2, 1), second_label);

         first_label.setPlayer(player);
         second_label.setPlayer(player);

         List<Label> line = field.labelLine(new Point(1, 1), direction);

         Assertions.assertEquals(1, line.size());
         Assertions.assertTrue(line.contains(first_label));
         Assertions.assertFalse(line.contains(second_label));
     }

     @Test
     public void IsShiftOpposite() {
         Direction direction = Direction.south();
         direction = direction.leftword();

         Assertions.assertFalse(direction.isOpposite(Direction.west()));

         direction = direction.leftword();

         Assertions.assertTrue(direction.isOpposite(Direction.west()));
     }



    @Test
    public void IsGameFieldContainsOfCells() {
        GameField field = new GameField();
        Cell first_cell = new Cell();
        Cell second_cell = new Cell();
        Cell third_cell = new Cell();

        field.setCell(new Point(1, 1), first_cell);
        field.setCell(new Point(1, 2), second_cell);

        List<AbstractCell> cells = field.cells();

        Assertions.assertEquals(2, cells.size());
        Assertions.assertTrue(cells.contains(first_cell));
        Assertions.assertTrue(cells.contains(second_cell));
        Assertions.assertFalse(cells.contains(third_cell));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> cells.add(new Cell()));
    }

    @Test
    public void SizeOfField() {
        GameModel model = new GameModel();

        model.field().setSize(5, 5);

        Assertions.assertEquals(5, model.field().width());
        Assertions.assertEquals(5, model.field().height());
        Assertions.assertTrue(model.field().containsRange(new Point(1, 1)));
        Assertions.assertFalse(model.field().containsRange(new Point(7, 8)));
    }

    @Test
    public void UnsetPlayer() {
        GameField field = new GameField();
        Player player = new Player(field, "X");
        Cell cell = new Cell();
        Label label = new Label();

        field.setCell(new Point(1, 1), cell);
        field.setLabel(new Point(1, 1), label);

        label.setPlayer(player);


        Assertions.assertEquals("X", player.name());

        label.unsetPlayer();

        Assertions.assertNull(player.activeLabel());
    }

    @Test
    public void PositionOfCell() {
        Cell cell = new Cell();
        Point position = new Point(1, 1);

        cell.setPosition(position);

        Assertions.assertEquals(position, cell.position());
    }

    @Test
    public void CellsHasNoLabel() {
        GameField field = new GameField();
        Cell cell1 = new Cell();

        field.setCell(new Point(1, 1), cell1);

        Assertions.assertTrue(cell1.isEmpty());
        Assertions.assertTrue(cell1.isEmpty());
    }

    //
    @Test
    public void playersOnAlongDiagonalCell(){
        GameModel model = new GameModel();
        XZeroListener playerListener = new XZeroListener();
        Point p1 = new Point(3,3);
        Point p2 = new Point(4,4);

        model.activePlayer().addPlayerActionListener(playerListener);
        model.field().clear();
        model.field().setSize(5, 5);
        for(int row = 1; row <= model.field().height(); row++)
        {
            for(int col = 1; col <= model.field().width(); col++)
            {
                model.field().setCell(new Point(col, row), model.getCellFactory().createCell());
            }
        }

        Player pl1 = model.getActivePlayer(0);
        model.cellsAlong(p1, "X");
        model.change("X", p1);
        Label newLabel = model.getLabelFactory().createLabel();
        pl1.setActiveLabel(newLabel);
        model.activePlayer().setLabelTo(p1);


        Player pl2 = model.getActivePlayer(0);
        //pl2.setName("X");
        model.cellsAlong(p2, "X");
        //model.change("X", p2);
        Label newLabel2 = model.getLabelFactory().createLabel();
        pl2.setActiveLabel(newLabel2);
        model.activePlayer().setLabelTo(p2);

        Assertions.assertSame(model.field().cell(p1).label().player(), pl1);
        Assertions.assertNotSame(model.field().cell(p2).label().player(), pl2);
        //Assertions.assertNull(pl2._label);
    }

     @Test
     public void numOfAlongDiagonalCells(){
         int numOfDiagonalCells = 8;
         int sizeDiagonal =0;

         GameModel model = new GameModel();
         XZeroListener playerListener = new XZeroListener();
         Point p1 = new Point(3,3);

         model.activePlayer().addPlayerActionListener(playerListener);
         model.field().clear();
         model.field().setSize(5, 5);
         for(int row = 1; row <= model.field().height(); row++)
         {
             for(int col = 1; col <= model.field().width(); col++)
             {
                 model.field().setCell(new Point(col, row), model.getCellFactory().createCell());
             }
         }

         Player pl1 = model.getActivePlayer(0);
         model.cellsAlong(p1, "X");
         model.change("X", p1);
         Label newLabel = model.getLabelFactory().createLabel();
         pl1.setActiveLabel(newLabel);
         model.activePlayer().setLabelTo(p1);

         List<AbstractCell> arr = model.field().cells();

         for (AbstractCell cell : arr) {
             if (cell instanceof CellAlongTheDiagonal) {
                 sizeDiagonal++;
             }
         }

         Assertions.assertTrue(numOfDiagonalCells ==sizeDiagonal);
     }

     @Test
     public void differentPlayers(){
         GameModel model = new GameModel();
         XZeroListener playerListener = new XZeroListener();
         Point p1 = new Point(3,3);
         Point p2 = new Point(3,4);

         model.activePlayer().addPlayerActionListener(playerListener);
         model.field().clear();
         model.field().setSize(5, 5);
         for(int row = 1; row <= model.field().height(); row++)
         {
             for(int col = 1; col <= model.field().width(); col++)
             {
                 model.field().setCell(new Point(col, row), model.getCellFactory().createCell());
             }
         }

         Player pl1 = model.getActivePlayer(0);
         model.cellsAlong(p1, "X");
         model.change("X", p1);
         Label newLabel = model.getLabelFactory().createLabel();
         pl1.setActiveLabel(newLabel);
         model.activePlayer().setLabelTo(p1);


         Player pl2 = model.getActivePlayer(1);
         model.cellsAlong(p2, "O");
         model.change("O", p2);
         Label newLabel1 = model.getLabelFactory().createLabel();
         pl2.setActiveLabel(newLabel1);
         model.activePlayer().setLabelTo(p2);

         Assertions.assertSame(model.field().cell(p1).label().player(), pl1);
         Assertions.assertSame(model.field().cell(p2).label().player(), pl2);
     }

     @Test
     public void differentPlayersObDiagonal(){
         GameModel model = new GameModel();
         XZeroListener playerListener = new XZeroListener();
         Point p1 = new Point(2,2);
         Point p2 = new Point(4,4);

         model.activePlayer().addPlayerActionListener(playerListener);
         model.field().clear();
         model.field().setSize(5, 5);
         for(int row = 1; row <= model.field().height(); row++)
         {
             for(int col = 1; col <= model.field().width(); col++)
             {
                 model.field().setCell(new Point(col, row), model.getCellFactory().createCell());
             }
         }

         Player pl1 = model.getActivePlayer(0);
         model.cellsAlong(p1, "X");
         model.change("X", p1);
         Label newLabel = model.getLabelFactory().createLabel();
         pl1.setActiveLabel(newLabel);
         model.activePlayer().setLabelTo(p1);

         Assertions.assertSame(model.field().cell(p1).label().player(), pl1);

         Player pl2 = model.getActivePlayer(1);
         model.cellsAlong(p2, "O");
         model.change("O", p2);
         Label newLabel1 = model.getLabelFactory().createLabel();
         pl2.setActiveLabel(newLabel1);
         model.activePlayer().setLabelTo(p2);

         Assertions.assertSame(model.field().cell(p2).label().player(), pl2);
     }



}