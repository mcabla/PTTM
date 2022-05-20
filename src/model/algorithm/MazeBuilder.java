package model.algorithm;

import tools.DataSingleton;
import tools.Direction;

import java.util.*;


/**
 * A builder used by Maze for generating a maze as a matrix.
 */
public class MazeBuilder {
    private int x=0,y=0,dimX,dimY;
    private int endX,endY;
    private final Tile[][] tileMatrix;
    private final Stack<Tile> tileStack;
    private final Random random;
    private final Tile protoTile;
    private final List<Direction> directions;
    private final Map<Direction,Integer> dx, dy;
    private final Map<Direction,Direction> opposite;
    private int solutionLength = 0;

    /**
     *  Constructor that sets the dimensions of the maze in tiles.
     * @param dimX Amount of tiles on the X-axis.
     * @param dimY Amount of tiles on the Y-axis.
     */
    public MazeBuilder(int dimX, int dimY){
        this.dimX = dimX;
        this.dimY = dimY;
        this.endX = dimX-1;
        this.endY = dimY-1;
        tileMatrix = new Tile[dimY][dimX];
        tileStack = new Stack<>();
        protoTile = new Tile(-1,-1);
        directions = Arrays.asList(Direction.values());

        DataSingleton dataSingleton = DataSingleton.getInstance();
        random = dataSingleton.getRandom();
        dx = dataSingleton.getDX();
        dy = dataSingleton.getDy();
        opposite = dataSingleton.getOpposite();
    }

    /**
     * Enables Maze to set the size used by every cell.
     * Cannot be called after generation.
     * @param size double.
     */
    public void setSize(double size){
        if (tileMatrix[0][0] != null){
            throw new RuntimeException("Maze already generated");
        }

        int tileSize = (int) Math.floor(size/dimY);
        protoTile.setSize(tileSize);

    }

    /**
     * Enables Maze to disable the solution.
     * Cannot be called after generation.
     * @param showSolution boolean.
     */
    public void setShowSolution(boolean showSolution){
        if (tileMatrix[0][0] != null){
            throw  new RuntimeException("Maze already generated");
        }

        protoTile.setSolution(showSolution);

    }

    /**
     * Set the start coordinates.
     * @param x X-coordinate.
     * @param y Y-coordinate.
     */
    public void setStartCoordinates(int x, int y){
        if (x<0 || y<0 || x>=dimX || y>=dimY) {
            throw new RuntimeException("Incompatible coordinates");
        } else if (tileMatrix[0][0] != null){
            throw  new RuntimeException("Maze already generated");
        }
        this.x = x;
        this.y = y;
    }

    /**
     * Set the end coordinates.
     * @param x X-coordinate.
     * @param y Y-coordinate.
     */
    public void setEndCoordinates(int x, int y){
        if (x<0 || y<0 || x>=dimX || y>=dimY) {
            throw new RuntimeException("Incompatible coordinates");
        } else if (tileMatrix[0][0] != null){
            throw  new RuntimeException("Maze already generated");
        }
        this.endX = x;
        this.endY = y;
    }

    /**
     * Generates the maze.
     */
    public void generate(){
        if(tileMatrix[0][0] != null){
            throw  new RuntimeException("Maze already generated");
        }

        Tile currentTile = addTile(x, y);

        int n = dimX*dimY;
        int counter = 1;

        //Loop until all tiles are generated
        while (counter < n){
            boolean found = false;

            //chose random direction
            Collections.shuffle(directions, random);
            for(Direction dir: directions){
                //move to new tile
                int nx = x + dx.get(dir);
                int ny = y + dy.get(dir);
                if(checkNotVisited(nx,ny)){
                    found = true;

                    Tile newTile = addTile(nx, ny);
                    removeWalls(dir, currentTile, newTile);
                    currentTile = newTile;

                    if (checkIfEnd(nx, ny)){
                        protoTile.setSolution(false);
                        solutionLength = tileStack.size();
                    }

                    counter++;

                    break;
                }
            }

            //In case the branch is stuck, go back for a new try
            if(!found){
                if(protoTile.isSolution()) currentTile.setSolution(false);
                tileStack.pop();
                if(tileStack.size() >0) currentTile = tileStack.lastElement();

            }

            //save current position
            x = currentTile.getPosX();
            y = currentTile.getPosY();
        }
    }

    private void removeWalls(Direction dir, Tile currentTile, Tile newTile) {
        currentTile.removeWall(dir);
        newTile.removeWall(opposite.get(dir));
    }

    private Tile addTile(int x, int y) {
        Tile newTile = protoTile.clone();
        newTile.setPosX(x);
        newTile.setPosY(y);
        tileStack.push(newTile);
        tileMatrix[y][x] = newTile;
        return newTile;
    }

    private boolean checkNotVisited(int x, int y){
        return x >= 0 && y >= 0
                && x < dimX && y < dimY
                && tileMatrix[y][x] == null;
    }

    private boolean checkIfEnd(int x, int y){
        return x == endX && y == endY;
    }

    /**
     * Returns the generated maze.
     * @return Tile[][].
     */
    public Tile[][] getTileMatrix() {
        if(tileMatrix[0][0] == null) generate();
        return tileMatrix.clone();
    }

    public int getSolutionLength() {
        return solutionLength;
    }
}
