import java.io.*;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Path;


public class Solver {

    private int rows, columns; //height and width of the board
    private HashSet<Board> visitedBoards; // keeps track of all previous board configurations
    private Board initialBoard; // initial state of the board
    private Board finalBoard; // final state of the board as in we need to have this at the end
    private HashSet<coordinates> emptyspaces; //to see where the empty spaces are

    private HashSet<Block> blocks = new HashSet<>();

    /**
     *
     * read the file line by line , make all the blocks and throw them in to intial configuration,
     * and take the line where it represents the goal and put that in a end goal configuration
     * figure out where the empty spaces are and throw them into a hashset as well
     * @param initFile
     * @param goalFile
     * @throws Exception
     */
    public Solver(File initFile , File goalFile) throws Exception {
        HashSet<Block> goalBlocks = new HashSet<>();
        visitedBoards = new HashSet<>();
        emptyspaces = new HashSet<>();

        blocks.addAll(readFileAndInitialize(initFile, "initial"));
        emptyspaces.addAll(calculateEmptySpaces(blocks));
        initialBoard = new Board(blocks, emptyspaces, new coordinates(this.rows,this.columns));
        
        goalBlocks.addAll(readFileAndInitialize(goalFile, "goal"));
        finalBoard = new Board(goalBlocks,calculateEmptySpaces(goalBlocks),new coordinates(this.rows,this.columns)); 
    }

    /**
     *
     * @param file
     * @param type
     * @return
     * @throws Exception
     */
    //initialize the height and width of the board(s) , set up all the blocks and throw them in a hashset of blocks
    public HashSet<Block> readFileAndInitialize(File file , String type) throws Exception {
        HashSet<Block> blocks = new HashSet<>();
        Scanner scan = new Scanner(file);
        Block block;
        int count = 1;
        
        type.toUpperCase();
        if (type.equals("initial")) {
            String [] fline = scan.nextLine().split(" ");
            if (fline.length != 2 ) throw new Exception("Board Size ....");
            this.rows = Integer.parseInt(fline[0]);
            this.columns = Integer.parseInt(fline[1]);
        }

        while (scan.hasNext()) {
            String [] line = scan.nextLine().split(" ");
            if (line.length != 4) throw new Exception("File Err ...");
            
            block = new Block(new coordinates(Integer.parseInt(line[0]), Integer.parseInt(line[1])) , new coordinates(Integer.parseInt(line[2]), Integer.parseInt(line[3])) 
            , count , new coordinates(this.rows,this.columns));
            count++;

            blocks.add(block.deepClone());
        }

        return blocks;
    }

    /**
     *
     * @param currentBoard
     * @return
     */
    public HashSet<coordinates> calculateEmptySpaces(HashSet<Block> currentBoard){
    HashSet<coordinates> empty = new HashSet<>();

    boolean [][] taken = new boolean[rows][columns];
        Iterator iterator = currentBoard.iterator();
        while (iterator.hasNext()){
            Block block = (Block)iterator.next();
            for (int i = 0  ; i < block.getSize().getRow(); i++){
                for (int j = 0 ; j < block.getSize().getCol(); j++){
                    taken[ block.getUpperLeft().getRow() + i][block.getUpperLeft().getCol() + j] = true;
                }
            }
        }
        for (int i = 0 ; i < rows ; i++){
            for (int j = 0 ; j < columns ; j++){
                if(taken[i][j] == false){
                    empty.add(new coordinates(i , j));
                }
            }
        }
    return empty;
    }

   //use this for depth first search
    public Board solve() throws Exception{
        int i = 0 ;
        Board b = DepthFirstSearch(this.initialBoard,i );
        if (b == null){ 
            System.out.println("SOLUTION NOT FOUND"); 
        return null;
    }
        System.out.println("SOLUTION FOUND");
        printPath(b, 0);
        return b;
    }

   
    public Board DepthFirstSearch(Board currentBoard  , int recursiveLevel) throws  Exception{
        if  ( currentBoard != null && currentBoard.isSolved(this.finalBoard)) return currentBoard;
        if  ( currentBoard == null  ) return null;

        Iterator<Block> it = currentBoard.getBlocks().iterator();

        while (it.hasNext()) {
            Block block = (Block) it.next();
            Board nextBoard = currentBoard.moveOneBlock(block, block.oneUp());
            if (nextBoard != null && !visitedBoards.contains(nextBoard)) {
                visitedBoards.add(nextBoard);
                nextBoard = DepthFirstSearch(nextBoard , recursiveLevel + 1 );
                if  ( nextBoard != null && nextBoard.isSolved(this.finalBoard)) return nextBoard;
            }
            nextBoard = currentBoard.moveOneBlock(block, block.oneDown());
            if (nextBoard != null && !visitedBoards.contains(nextBoard)) {
                visitedBoards.add(nextBoard);
                nextBoard = DepthFirstSearch(nextBoard , recursiveLevel + 1 );
                if  ( nextBoard != null && nextBoard.isSolved(this.finalBoard)) return nextBoard;
            }
            nextBoard = currentBoard.moveOneBlock(block, block.oneRight());
            if (nextBoard != null && !visitedBoards.contains(nextBoard)) {
                visitedBoards.add(nextBoard);
                nextBoard = DepthFirstSearch(nextBoard , recursiveLevel + 1 );
                if  ( nextBoard != null && nextBoard.isSolved(this.finalBoard)) return nextBoard;
            }
            nextBoard = currentBoard.moveOneBlock(block, block.oneLeft());
            if (nextBoard != null && !visitedBoards.contains(nextBoard)) {
                visitedBoards.add(nextBoard);
                nextBoard = DepthFirstSearch(nextBoard , recursiveLevel + 1 );
                if  ( nextBoard != null && nextBoard.isSolved(this.finalBoard)) return nextBoard;
            }
            
        }
        return null;
    }

    //use this for breadth first search
    // public Board Solve() throws Exception{
     
    // }

    public void printPath(Board b , long time){
        boolean debug = false;
        int count = 0 ;
        System.out.println("PRINTING PATH OF SOLVED BOARD");
        if(b != null){
            while (b.getParent() != null){
                if(debug) System.out.println("AT STEP: " + count);
                b.printBoard();
                System.out.println("====================");
                b = b.getParent();
                count++;
            }
            if(debug) System.out.println("AT STEP: " + count);
            b.printBoard();
            count++;
            System.out.println("====================");
            System.out.println(count + " STEPS");
            System.out.println("TIME ELAPSED TO FIND SOLUTION:" + time + " milliseconds");
            System.out.println("DONE");
        }
    }

    // public static void printDataOnBoard(Board board){
    // }

    // public Board BreadthFirstSearch(Board init) throws Exception{
    // }

    public static void main(String args[]) throws Exception{
        // File init = new File("/home/neckron/Dev/Tumo/Java/1x1");
        // File goal = new File("/home/neckron/Dev/Tumo/Java/1x1.goal");
        File goal = new File("/Users/admin/Desktop/Tumo_Bug/easy.goal");
        File init = new File("/Users/admin/Desktop/Tumo_Bug/easy");
        
        Solver solver = new Solver(init, goal);

        solver.solve();
    }
}