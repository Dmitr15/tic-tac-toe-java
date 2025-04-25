package xzero.model.navigation;

/**
 * Смещение в декартовой системе координат
 */
public class Shift {
    
    private int _horizontal;
    private int _vertical;

    public Shift(int horiz, int vert){
        _horizontal = horiz;
        _vertical = vert;
    }    
        
    public int byHorizontal(){
        return _horizontal;
    }
    
    public int byVertical(){
        return _vertical;
    }    
}
