package model.algorithm;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import tools.Direction;
import model.Drawable;
import model.image.ImageCache;
import tools.UI;

/**
 * An object used to generate a maze and store it's information.
 */
public class Maze extends Drawable {
    private final int dimX;
    private final int dimY;
    private int endX;
    private int endY;
    private Tile[][] tileMatrix;
    private boolean showSolution = true;
    private int solutionLength = 0;

    /**
     * Construct the object with the given dimensions.
     * @param dimX The width of the maze in cells.
     * @param dimY The height of the maze in cells.
     */
    public Maze(int dimX, int dimY) {
        super(0,0);
        this.dimX = dimX;
        this.dimY = dimY;
    }

    /**
     * Enables or disables the solution for the next maze generation.
     * @param showSolution boolean
     */
    public void setShowSolution(boolean showSolution){
        this.showSolution = showSolution;
    }

    /**
     * Generate the maze with the top left corner as starting point
     * and the bottom right corner as the finish.
     */
    public void generate(){
        generate(0,0, dimX -1, dimY -1);
    }

    /**
     * Generate the maze
     * @param startX X-coordinate of the start position.
     * @param startY Y-coordinate of the start position.
     * @param endX X-coordinate of the end position.
     * @param endY Y-coordinate of the end position.
     */
    public void generate(int startX,int startY,int endX, int endY){
        this.endX = endX;
        this.endY = endY;
        MazeBuilder mazeBuilder = new MazeBuilder(dimX, dimY);
        mazeBuilder.setStartCoordinates(startX,startY);
        mazeBuilder.setEndCoordinates(endX,endY);

        double size = (UI.getMaxHeight() < 900 )? UI.getMaxHeight()*.7:UI.getMaxHeight() *.75;
        if(size > UI.getMaxWidth()/2) size = UI.getMaxWidth()/3;

        mazeBuilder.setSize(size);
        mazeBuilder.setShowSolution(showSolution);
        tileMatrix = mazeBuilder.getTileMatrix();
        solutionLength = mazeBuilder.getSolutionLength();
    }

    public boolean checkFinished(int x, int y){
        return endX == x && endY == y;
    }

    /**
     * Checks if the player can go in a certain direction on the given coordinates.
     * @param x X-coordinate
     * @param y Y-coordinate
     * @param dir The direction the player wants to go.
     * @return Boolean.
     */
    public boolean canGoDirection(int x, int y, Direction dir){
        return tileMatrix[y] != null && tileMatrix[y][x] != null && !tileMatrix[y][x].hasWall(dir);
    }

    /**
     * Returns the size of one cell.
     * @return int.
     */
    public double getTileSize(){
        return tileMatrix[0][0].getSize();
    }

    /**
     * Places every tile in a GridPane.
     * Adds a moneybag on the finish.
     * @return GridPane.
     */
    @Override
    public GridPane getNode() {
        GridPane gridPane = new GridPane();

        for(int y = 0; y< dimY; y++){
            for(int x = 0; x< dimX; x++){
                gridPane.add(tileMatrix[y][x].getNode(),x,y);
            }
        }

        addMoneyBag(gridPane);

        return gridPane;
    }

    private void addMoneyBag(GridPane gridPane) {
        Image image = ImageCache.getImage("money.png");
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(tileMatrix[endY][endX].getSize());

        gridPane.add(imageView,endX,endY);
    }

    public int getSolutionLength() {
        return solutionLength;
    }
}
