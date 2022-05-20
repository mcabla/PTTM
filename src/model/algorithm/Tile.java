package model.algorithm;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import tools.Direction;
import model.Drawable;
import model.image.ImageCache;
import tools.UI;

import java.util.Objects;

/**
 *  A square cell, when correctly placed in a grid with other cells, it forms a maze.
 *  Every cell can have 0 to 4 walls. One in each direction.
 *  These walls are stored in one byte to minimise memory usage and ease development.
 */
public class Tile extends Drawable implements Cloneable{
    private byte wall;
    private boolean solution;
    private int size = 10;

    /**
     * Generate the tile.
     * @param posX The X-coordinate of this tile in the grid.
     * @param posY The Y-coordinate of this tile in the grid.
     */
    public Tile(int posX, int posY) {
        super(posX, posY);

        wall = 0;
        for(Direction dir: Direction.values()){
            addWall(dir);
        }

        solution = true;

    }

    /**
     * Flips the bit of the given direction to 0 if it is 1, otherwise it stays 0. (XOR-
     * @param dir the bit it needs to set to 0.
     */
    public void removeWall(Direction dir){
        wall ^= dir.getValue();
    }

    /**
     * Flips the bit of the given direction to 1 if it is 0, otherwise it stays 1. (OR)
     * @param dir the bit it needs to set to 1.
     */
    public void addWall(Direction dir){
        wall |= dir.getValue();
    }

    /**
     * Checks if there is a wall in the given direction. (AND)
     * When the bit is not 0. This is true.
     * @param dir The bit it needs to check.
     * @return Boolean that informs the requesting object of the state of the given wall.
     */
    public boolean hasWall(Direction dir){
        return (wall & dir.getValue()) != 0;
    }

    /**
     * Checks if the tile is part of the solution.
     * @return Boolean that informs the requesting object of the state of the given wall.
     */
    public boolean isSolution() {
        return solution;
    }

    /**
     * Setter used to define if the tile is part of the solution.
     * @param solution Boolean.
     */
    public void setSolution(boolean solution) {
        this.solution = solution;
    }

    /**
     * The size of the tile.
     * @return double.
     */
    public double getSize() {
        return size;
    }

    /**
     * Setter for the size of the tile.
     * @param size int.
     */
    public void setSize(int size){
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return getPosX() == tile.getPosX() &&
                getPosY() == getPosY() &&
                wall == tile.wall &&
                solution == tile.solution;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosX(),getPosY(),wall, solution);
    }

    /**
     * Returns a custom node that contains an image of the walls and possibly the solution.
     * @return
     */
    @Override
    public StackPane getNode() {
        StackPane stackPane = new StackPane(getImageView());

        addSolution(stackPane);

        UI.addClassName("maze-tile",stackPane);

        return stackPane;
    }

    private void addSolution(StackPane stackPane) {
        if(solution){
            Rectangle rectangle = new Rectangle();
            rectangle.setHeight(size/5.0);
            rectangle.setWidth(size/5.0);
            UI.addClassName("maze-tile-solution",rectangle);
            stackPane.getChildren().add(rectangle);
        }
    }

    /**
     * Returns an image with the requested walls.
     * @return Image.
     */
    private ImageView getImageView(){
        Image image = ImageCache.getImage(String.format("tiles/%d.png",wall));
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(size);
        return imageView;
    }


    @Override
    public Tile clone(){
        try{
            return (Tile) super.clone();
        } catch (CloneNotSupportedException e){
            return null;
        }
    }
}
