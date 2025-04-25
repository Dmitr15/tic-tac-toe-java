package xzero.model.navigation;

/*
 * Direction - абстракция направления в системе координат "север-юг-восток-запад"; 
 * позволяет сравнивать направления и порождать новые направления относительно 
 * текущего
 */
public class Direction {
    
//--------- Определяем направление как угол в градусах от 0 до 360 ------------
    private int _angle = 90;

    private Direction(int angle) {
        // Приводим заданный угол к допустимому диапазону 
        angle = angle%360;
        if(angle < 0)    angle += 360;
        
        this._angle = angle;
    }
    
// --------------------------- Возможные направления --------------------------
    
    public static Direction north()
    { return new Direction(90); }
    
    public static Direction south()
    { return new Direction(270); }

    public static Direction east()
    { return new Direction(0); }

    public static Direction west()
    { return new Direction(180); }
    
    public static Direction northEast()
    { return new Direction(45); }

    public static Direction northWest()
    { return new Direction(135); }

    public static Direction southEast()
    { return new Direction(-45); }

    public static Direction southWest()
    { return new Direction(-135); }
    
// ---------------- Смещения в декартовой системе координат  ------------------

    public Shift shift(){
        
        int vertShift = 0, horizShift = 0;
        
        if(_angle == 90 || _angle == 270)
        { horizShift = 0; }
        else if(_angle < 90 || _angle > 270)
        { horizShift = 1; }
        else if(_angle > 90 && _angle < 270)
        { horizShift = -1; }
        else
        { /* TODO породить исключение*/ throw new RuntimeException(); }
        
        if(_angle == 0 || _angle == 180)
        { vertShift = 0; }
        else if(_angle > 0 && _angle < 180)
        { vertShift = -1; }
        else if(_angle > 180 && _angle < 360)
        { vertShift = 1; }
        else
        { /* TODO породить исключение*/ throw new RuntimeException(); }
        
        return new Shift(horizShift, vertShift);
    }
    
// ---------------------------- Новые направления -----------------------------
    
    private static int ANGLE_STEP = 45;
    
    @Override
    public Direction clone(){ 
        return new Direction(this._angle); 
    }
  
    public Direction clockwise() { 
        return new Direction(this._angle-ANGLE_STEP); 
    }
    
    public Direction anticlockwise() { 
        return new Direction(this._angle+ANGLE_STEP); 
    }
    
    public Direction opposite() { 
        return new Direction(this._angle+180); 
    }
    
    public Direction rightword()  { 
        return clockwise(); 
    }
    
    public Direction leftword()  { 
        return anticlockwise(); 
    }
    
// --------------------------- Сравнить направления ---------------------------
    
    @Override
    public boolean equals(Object other) {

        if(other instanceof Direction) {
            // Типы совместимы, можно провести преобразование
            Direction otherDirect = (Direction)other;
            // Возвращаем результат сравнения углов
            return  _angle == otherDirect._angle;
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        return this._angle;
    }
    
    public boolean isOpposite(Direction other) {
        return this.opposite().equals(other);
    }
}