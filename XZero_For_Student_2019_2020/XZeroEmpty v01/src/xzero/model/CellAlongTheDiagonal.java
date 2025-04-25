package xzero.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class CellAlongTheDiagonal extends AbstractCell{
    private static ArrayList<Point> peaksForX = new ArrayList<Point>();
    private static ArrayList<Point> peaksForO = new ArrayList<Point>();

    public static void clearXO(){
        peaksForX.clear();
        peaksForO.clear();
    }

    public void findingCellsAlongDiagonals(Point p, String player ){
        Point peakedPoint;
        // Проверяем точки по левой верхней диагонали

        peakedPoint = p;
        while (peakedPoint.getX() > 1 && peakedPoint.getY() > 1){
            peakedPoint = new Point((int) (peakedPoint.getX() - 1), (int) (peakedPoint.getY() - 1));
            if (Objects.equals(player, "X") && !peaksForX.contains(peakedPoint)) {
                peaksForX.add(peakedPoint);
            }
            else if(Objects.equals(player, "O") && !peaksForO.contains(peakedPoint)) {
                peaksForO.add(peakedPoint);
            }
        }

        // Проверяем точки по правой верхней диагонали
        peakedPoint = p;
        while (peakedPoint.getX() < 5 && peakedPoint.getY() > 1){
            peakedPoint = new Point((int) (peakedPoint.getX() + 1), (int) (peakedPoint.getY() - 1));
            if (Objects.equals(player, "X") && !peaksForX.contains(peakedPoint)) {
                peaksForX.add(peakedPoint);
            }
            else if(Objects.equals(player, "O") && !peaksForO.contains(peakedPoint)) {
                peaksForO.add(peakedPoint);
            }
        }

        // Проверяем точки по левой нижней диагонали
        peakedPoint = p;
        while (peakedPoint.getX() > 1 && peakedPoint.getY() < 5){
            peakedPoint = new Point((int) (peakedPoint.getX() - 1), (int) (peakedPoint.getY() + 1));
            if (Objects.equals(player, "X") && !peaksForX.contains(peakedPoint)) {
                peaksForX.add(peakedPoint);
            }
            else if(Objects.equals(player, "O") && !peaksForO.contains(peakedPoint)) {
                peaksForO.add(peakedPoint);
            }
        }

        // Проверяем точки по правой нижней диагонали
        peakedPoint = p;
        while (peakedPoint.getX() < 5 && peakedPoint.getY() < 5){
            peakedPoint = new Point((int) (peakedPoint.getX() + 1), (int) (peakedPoint.getY() + 1));
            if (Objects.equals(player, "X") && !peaksForX.contains(peakedPoint)) {
                peaksForX.add(peakedPoint);
            }
            else if(Objects.equals(player, "O") && !peaksForO.contains(peakedPoint)) {
                peaksForO.add(peakedPoint);
            }
        }
    }


    public ArrayList<Point> changeCell(String name){
        if (name.equals("X")) {
            return peaksForX;
        }
        return peaksForO;
    }

    public boolean absenceOfCellDuplication(Point p, String name){
        boolean blockedButton = false;
        if(Objects.equals(name, "X")){
            for(Point i: peaksForX){
                if (i.equals(p)) {
                    blockedButton = true;
                    break;
                }
            }
        }else{
            for(Point i: peaksForO){
                if (i.equals(p)) {
                    blockedButton = true;
                    break;
                }
            }
        }
        return blockedButton;
    }
}