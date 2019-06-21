import java.util.*;

// import static java.lang.System.exit;
// import static java.lang.System.setSecurityManager;

public class Board implements Comparable<Board> {
    //should have multiple blocks in itself , contain the, some way
    //should have a reference to previous board
    private static final int UP = 1;
    private static final int RIGHT = 2;
    private static final int DOWN = 3;
    private static final int LEFT = 4;
    static Board goal = null;

    private Block blockMoved; // reference to block that was last moved
    private coordinates size; // size of the overall board
    private Board parent; //previous Board in configuration
    private coordinates blockMovedDir; //indicates which direction the last block was moved in
    private HashMap<Integer, Block> AllBlocks; //a collection of all the blocks that have a unique number
    private HashSet<Block> blocks; // Hash set of blocks coming in from solver
    private HashSet<coordinates> emptyspaces;
    private int priority;

    public int getPrior(){ 
        return  priority;
    }

    public Board getParent() {
        return parent;
    }

    public coordinates getBlockMovedDir() {
        return blockMovedDir;
    }

    public Block getBlockMoved() {
        return blockMoved;
    }

    public HashSet<coordinates> getEmptyspaces() {
        return emptyspaces;
    }

    public HashMap<Integer, Block> getAllBlocks() {
        return AllBlocks;
    }

    public HashSet<Block> getBlocks() {
        return blocks;
    }

    public coordinates getSize() {
        return size;
    }

    /**
     * general constructor of board. Allocates all needed information in memory and copies over from hashSets to
     * private variable hashSets.
     * @param configuration the given hashSet of blocks that represent the blocks in the board
     * @param empty the given hashSet of coordinates that represent all the empty spaces in the board
     * @param size represents the size of the board
     * @throws Exception
     */
    public Board(HashSet<Block> configuration, HashSet<coordinates> empty ,  coordinates size ) throws Exception {
        if (!size.isValidSize()) throw new Exception ( "Board Size" );
        if (configuration.size() == 0) throw new Exception( "Board Blocks" );
        if (empty.size() == 0 ) throw new Exception( "Board Empty spaces" );
        
        AllBlocks = new HashMap<>();
        blocks = new HashSet<>();
        emptyspaces = new HashSet<>();

        blocks.addAll(configuration);
        emptyspaces.addAll(empty);
        this.size = size;


        Iterator<Block> it = blocks.iterator();

        while (it.hasNext()) {
            Block next = it.next();
            AllBlocks.put(next.hashCode(), next);
        }
    }

    /**
     * iterates through the blocks of the goal board and checks to see if the current board contains every single
     * block of the goal board
     * @param goalBoard it is the final configuration board , the goal board
     * @return true if the current board equals goal board , false otherwise
     */
    public boolean isSolved(Board goalBoard) {
        Iterator<Block> it = goalBoard.blocks.iterator();

        while (it.hasNext()) {
            if (!this.blocks.contains(it.next())) return false;
        }

        return true;
    }

    /**
     *
     * @param prev reference to the previous board
     * @param moved reference to the newly moved board
     * @param direction reference to the direction that the last block was moved
     */
    //sets info to this if was not there , only fires if it wasnt previously set
    public void previousInstance(Board prev, Block moved, coordinates direction) {
      if ( parent == null && blockMoved == null && moved == null ) {
            parent = prev;
            blockMoved = moved;
            blockMovedDir = direction;
      }
    }

    /**
     * find the block and take the desired destination and move the block in that direction.update empty spaces and
     * blocks if movement is successful. validate all positions and destination position before moving
     * @param findMe this is the block that will be used in the moving process
     * @param destination this is the destination coordinate where we want to move our block
     * @return new board if movement is successful , null otherwise
     * @throws Exception
     */
    public Board moveOneBlock(Block findMe, coordinates destination) throws Exception {
        if ( !destination.isValidPosition() || destination.getRow() >= this.size.getRow() || destination.getCol() >= this.size.getCol() ) return null;
        
        int des = 0 ;
        if (destination.equals(findMe.oneUp())) {
            des = UP;
        } else if (destination.equals(findMe.oneDown())){
            des = DOWN;
        } else if (destination.equals(findMe.oneRight())) {
            des = RIGHT;
        } else if  (destination.equals(findMe.oneLeft())) {
            des = LEFT;  
        }

        Board clone = this.deepClone();
        if (!this.equals(clone))  throw new Exception("clone doesnt equal original!!!!!!!!!!!!!!!!");  

        Block b = clone.getBlock(findMe.hashCode());
        
        if (b != null) { 
            boolean result = b.moveBlock(des, clone.blocks, clone.emptyspaces, clone.AllBlocks);
            if (result) {
                previousInstance(this, findMe, destination);
                return clone;
            }
        }

        return null;
    }

    /**
     * uses the info of the block and looks for the Block in the Board. MyNumber only used , the rest are not used
     * @param myNumber the number that the block was assigned in the HashMap
     * @return the block if its is found , null otherwise
     * @throws Exception
     */
    public Block getBlock(int myNumber) throws Exception {
        return AllBlocks.get(myNumber);
    }

    /**
     * print the board in a double for loop and a 2 dimensional array. Each block coordinate will print a number
     * if there is an empty space ,  print null. Use Block's number that was assigned to print in each box of the array
     */
    public void printBoard() {
        String[][] board = new String[size.getRow()][size.getCol()];
        Iterator<Block> iterator = this.blocks.iterator();

        while (iterator.hasNext()) {
            Block currentBlock = (Block) iterator.next();
            int currentNumber = currentBlock.getMyNumber();

            for (int i = currentBlock.getUpperLeft().getRow(); i < currentBlock.getBottomRight().getRow(); i++) {
                for (int j = currentBlock.getUpperLeft().getCol(); j < currentBlock.getBottomRight().getCol(); j++) {
                    board[i][j] = currentNumber + "";
                    if (currentNumber % 10 == 1 && currentNumber % 100 != 11) board[i][j] += "st";
                    else if (currentNumber % 10 == 2) board[i][j] += "nd";
                    else if (currentNumber % 10 == 3) board[i][j] += "rd";
                    else board[i][j] += "th";
                }
            }
        }
        for (int i = 0; i < this.size.getRow(); i++) {
            for (int j = 0; j < this.size.getCol(); j++) {
                System.out.print("[" + board[i][j] + "] ");
            }
            System.out.println();
        }
    }

    //we have to check if boards are the same. we check the size and we check if all the blocks in there are in the
    //same spots

    /**
     * equals compares every single Hashable and potentially dynamic part of 2 Boards to ensure that the two are
     * identical if the method returns true.
     * @param other is a Board that will be compared with this Board
     * @return true if this Board is equal to other Board , false otherwise
     */
    public boolean equals(Object other) {
        if  ( other instanceof Board) {
            Board eBoard = (Board) other;
            Iterator<Integer> AllBlockIt = eBoard.AllBlocks.keySet().iterator();
            Iterator<Block> BlockIt  = eBoard.blocks.iterator();
            Iterator<coordinates> EmptyIt = eBoard.emptyspaces.iterator();
            
            while (AllBlockIt.hasNext()) if(!this.AllBlocks.containsKey(AllBlockIt.next())) return false;
            while (BlockIt.hasNext()) if(!this.blocks.contains(BlockIt.next())) return false;
            while (EmptyIt.hasNext()) if (!this.emptyspaces.contains(EmptyIt.next())) return false;

            if (this.hashCode() != eBoard.hashCode()) return false; 
        }
        return true;
    }

    /**
     * makes sure to copy every single detail over to a new Board with memory allocated in a new address.
     * @return a Board that is identical to this Board
     * @throws Exception
     */
    public Board deepClone() throws Exception {
        HashSet<Block> b = new HashSet<>();
        HashSet<coordinates> e = new HashSet<>();

        Iterator<Block> BIT = this.blocks.iterator();
        Iterator<coordinates> EIT = this.emptyspaces.iterator();

        while (BIT.hasNext()) {
            b.add(BIT.next());
        }
        while (EIT.hasNext()) e.add(EIT.next());

        return new Board(b, e, size);
    }

    /**
     * HashCode is the multiplication of all the HashCodes of the Blocks in this Board
     * @return HashCode of this Block
     */
    @Override
    public int hashCode() {
        Iterator<Block> ITBlocks = blocks.iterator();
        int BlocksHashSum = 0;

        while (ITBlocks.hasNext()) BlocksHashSum += ITBlocks.next().hashCode();
        
        return (int) Math.abs(
            Math.pow(size.getRow(), 3) + BlocksHashSum  - Math.pow(size.getCol(), 5));
    }

    /**
     * if priority isn't 0 to begin with , update it then return it
     * @return the cost/priority from current Board to goal Board
     */
    public int getPriority(){
        if(this.priority != 0 ) {
            this.getCost(goal);
        }
        return this.priority;
    }

    /**
     * this method is necessary in order to be able to sort Boards in the priority queue based on their priority
     * @param other Board that is being compared with this
     * @return the difference between the priorities
     */
    @Override
    public int compareTo(Board other){
        return this.getPriority() - other.getPriority();
    }

    //the lower priority the better

    /**
     *
     * @param goal
     */
    public void getCost(Board goal){
        if(this.priority != 0 ) System.out.println("somethings wrong , priority was not 0");
        int temp = 0;
        LinkedList<Block> checkMe = new LinkedList<>();    //changes maybe needed
        for( Block current : this.blocks){
            checkMe.add(current);
        }

        for(Block current: goal.getBlocks()){
            if(this.blocks.contains(current))
                checkMe.remove(current);
            else
                temp += this.Cost(checkMe, current);
        }
        priority = temp;
    }

    /**
     *
     * @param checkMe
     * @param other
     * @return
     */
    private int Cost(LinkedList<Block> checkMe, Block other) {
        int returnMe = Integer.MAX_VALUE;
        int currentMin;

        Block closest = null;
        for (Block b : checkMe) {
            if (b.getSize().getRow() != other.getSize().getRow() || b.getSize().getCol() != other.getSize().getCol())
                continue;

            currentMin = other.getUpperLeft().manhattanDist(b.getUpperLeft(), other.getUpperLeft());

            if (currentMin < returnMe) {
                returnMe = currentMin;
                closest = b;
            }
        }
        if (returnMe == Integer.MAX_VALUE)
            System.out.println("returning max value , goal board doesnt exist");
        checkMe.remove(closest);
        return returnMe;
    }
}