package model;
import javafx.scene.Node;

public abstract class Drawable {

    private int posX;
    private int posY;

    public Drawable(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
    }

    public int getPosX(){
        return posX;
    }

    public void setPosX(int x){
        posX = x;
    }

    public int getPosY(){
        return posY;
    }

    public void setPosY(int y){
        posY = y;
    }

    public abstract Node getNode();

}
