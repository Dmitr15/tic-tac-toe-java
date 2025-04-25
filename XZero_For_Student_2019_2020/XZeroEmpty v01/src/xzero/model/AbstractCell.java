package xzero.model;

import java.awt.*;

public abstract class AbstractCell {

    private Point _position;

    void setPosition(Point pos){
        _position = pos;
    }

    public Point position(){
        return (Point)_position.clone();
    }

    private GameField _field;

    void setField(GameField f){
        _field = f;
    }

    private Label label;// = new Label();

    public Label label() {
        //label.
        return label;
        //return null;
    }

    public boolean isEmpty(){
        return label==null;
    }

    public void setLabel(Label l){
        this.label = l;
        l.setCell(this);
    }

}
