   import java.lang.Math;
   import java.io.*;

    public class Game2048 
   {
      final public static int LEFT_INPUT  = 0;
      final public static int DOWN_INPUT  = 1;
      final public static int RIGHT_INPUT = 2;
      final public static int UP_INPUT  = 3;
   
      final public static int VALUE_GRID_SETTING  = 0;
      final public static int INDEX_GRID_SETTING = 1;
   
      private String GAME_CONFIG_FILE = "game_config.txt";
   
      private Game2048GUI gui;
   
   /* position [0][0] represents the Top-Left corner and
   * position [max][max] represents the Bottom-Right corner */
      private int grid [][];
      private final int EMPTY_SLOT = -1;
   
      private int winningLevel;  
      private long currentScore;
      private int currentLevel;
     
     //declaring constant for size of grid
      final int MAX = 4;
     //declaring boolean variable for whether or not a move has occured
      private boolean move = false;
   
   /**
   * Constructs Game2048 object.
   *
   * @param gameGUI The GUI object that will be used by this class.
   */   
       public Game2048(Game2048GUI gameGUI)
      {
         gui = gameGUI;
       
         try {
           //creating BufferedReader in to read from text file "game_config.txt"
            BufferedReader in = new BufferedReader(new FileReader(GAME_CONFIG_FILE));
           //declaring size of grid array
            grid = new int[MAX][MAX];
            in.readLine();
           //initializing the winning level from the text file
            winningLevel = Integer.parseInt(in.readLine());
           //initializing the current level to the 2 tile
            currentLevel = 2;
           //initializing the current score to 0
            currentScore = 0;
            in.close();
          
           //initializing the grid to empty slots
            for (int i = 0; i < MAX; i ++) {
               for (int j = 0; j < MAX; j++) {
                  grid[i][j] = EMPTY_SLOT;
               }
            }
          
           //printing two random tiles
            move = true;
            newSlot();
            newSlot();
          
         }  //catching any thrown exception
             catch (IOException iox) {
               System.out.println("Error accessing file.");
            }
      }
   
   /**
   * Place a new number tile on a random slot on the grid.
   * This method is called every time a key is released.
   */  
       public void newSlot()
      {
        //proceeds to print a new slot if a move has occured
         if (move) {
           //declaring and initializing a boolean variable to check if the generated slot is empty
            boolean empty = true;
           
           //proceeds to print a new slot if the generated slot is empty
            while (empty) {
              //randomly generating the coordinates for a slot
               int row = (int)(Math.random()*4);
               int col = (int)(Math.random()*4);
              //randomly generating a new tile
               int newTile = genTile();
              //places the tile on the board and updates the grid array if the generated slot is empty
               if (grid[row][col] == EMPTY_SLOT) {
                  grid[row][col] = newTile;
                  gui.setNewSlotBySlotValue(row, col, newTile);
                 //exists the loop if a slot has been placed
                  empty = false;
               }
            }
         }
        
        //checks if the player has lost. The player has lost when board is full and there's no move possible
         if (boardFull() && !movePossibleFull()) {
           //displays the game over window
            gui.showGameOver();
         }
      
      }
   
   /**
   * Plays the game by the direction specified by the user.     
   * This method is called every time a button is pressed
   */  
       public void play(int direction)
      {
         //decides which actions to take based on the direction pressed
         switch (direction) {
          //variable move is assigned false until a movement has occured
    //for each possible direction, their respective combine methods are called, then their shift methods
    
            case UP_INPUT:
               move = false;
               combineUp();
               shiftUp(); 
               break;
         
            case DOWN_INPUT:
               move = false;
               combineDown();
               shiftDown();
               break;
         
            case RIGHT_INPUT:
               move = false;
               combineRight();
               shiftRight();  
               break;
         
            case LEFT_INPUT:
               move = false;
               combineLeft();
               shiftLeft();
         }
        
        //updating the score
         gui.setScore(currentScore);
        //updating the game board
         gui.setGridByValue(grid);

         if (checkWin()) {
            gui.showGameWon();
         }
      
      }
      
   /* Method name: shiftUp
    Method parameters: none
    Method action: Shifts all the tiles on the board up 
    Method returns: nothing
   */
       public void shiftUp() {
         for (int col = 0; col < MAX; col++) { 
            for (int row = 1; row < MAX; row++) {
               if (grid[row][col] != EMPTY_SLOT) {
                  int value = grid[row][col];
                  //goes through the column of the tile and shifts it up as far as possible
                  for (int r = row-1; r >= 0; r--) {
                    //keeps shifting until there's not more empty slots above it
                     if (grid[r][col] == EMPTY_SLOT) {
                        grid[r][col] = value;
                        //clearing the old slot
                        grid[r+1][col] = EMPTY_SLOT;
                        move = true;
                     }
                  }
               
               }
            }
         }
      
      }
   
   /* Method name: shiftDown
    Method parameters: none
    Method action: Shifts all the tiles on the board down 
    Method returns: nothing
   */
   //similar to shiftUp, except this method checks from row 3 and goes up
       public void shiftDown() {
      
         for (int col = 0; col < MAX; col++) { 
            for (int row = MAX-1; row >= 0; row--) {
               if (grid[row][col] != EMPTY_SLOT) {
                  int value = grid[row][col];
                  for (int r = row+1; r < MAX; r++) {
                     if (grid[r][col] == EMPTY_SLOT) {
                        grid[r][col] = value;
                        grid[r-1][col] = EMPTY_SLOT;
                        move = true;
                     }
                  }
               }
            }
         }
      }
   
   /* Method name: shiftRight
    Method parameters: none
    Method action: Shifts all the tiles on the board right
    Method returns: nothing
   */   
   //is similar to shiftUp, except this method checks from column 3 and goes left
       public void shiftRight() {
      
         for (int row = 0; row < MAX; row++) {
            for (int col = MAX-1; col >= 0; col--) { 
               if (grid[row][col] != EMPTY_SLOT) {
                  int value = grid[row][col];
                  for (int c = col+1; c < MAX; c++) {
                     if (grid[row][c] == EMPTY_SLOT) {
                        grid[row][c] = value;
                        grid[row][c-1] = EMPTY_SLOT;
                        move = true;
                     }
                  }
               }      
            }
         }
      }
   
   /* Method name: shiftLeft
    Method parameters: none
    Method action: Shifts all the tiles on the board left 
    Method returns: nothing
   */   
   //is similar to shiftUp, except this method checks from column 1 and goes left
       public void shiftLeft() {
      
         for (int row = 0; row < MAX; row++) {
            for (int col = 1; col < MAX; col++) { 
               if (grid[row][col] != EMPTY_SLOT) {
                  int value = grid[row][col];
                  for (int c = col-1; c >= 0; c--) {
                     if (grid[row][c] == EMPTY_SLOT) {
                        grid[row][c] = value;
                        grid[row][c+1] = EMPTY_SLOT;
                        move = true;
                     }
                  }
               }
            }
         }
      }
   
   /* Method name: combineUp
    Method parameters: none
    Method action: Goes through the board and combines tiles with the tile above it if they have the same value
    Method returns: nothing
   */   
       public void combineUp() {
         for (int col = 0; col < MAX; col++) {
            for (int row = 0; row < MAX; row++) {
               if (grid[row][col] != EMPTY_SLOT) {
                  boolean merge = false;
                  for (int r = row+1; r < MAX && merge == false; r++ ) {
                     if (grid[row][col] != grid[r][col] && grid[r][col] != EMPTY_SLOT) {
                        merge = true;
                     }
                     //combines the tiles if the tile under it has the same value
                     else if (grid[row][col] == grid[r][col] && grid[r][col] != EMPTY_SLOT) {         
                        grid[row][col] *= 2;
                        scoreUpdate(grid[row][col]);  
                        grid[r][col] = EMPTY_SLOT;
                        merge = true;
                        move = true; 
                     }
                  }
               }
            }
         }
      }
   
   /* Method name: combineDown
    Method parameters: none
    Method action: Goes through the board and combines tiles with the tile below it if they have the same value
    Method returns: nothing
   */   
   //simiar to combineUp, except this method checks from the bottom of the board up
       public void combineDown() {
      
         for (int col = 0; col < MAX; col++) {
            for (int row = MAX-1; row >= 0; row--) {
               if (grid[row][col] != EMPTY_SLOT) {
                  boolean merge = false;
                  for (int r = row-1; r >= 0 && merge == false; r-- ) {
                     if (grid[row][col] != grid[r][col] && grid[r][col] != EMPTY_SLOT) {
                        merge = true;
                     }
                     else if (grid[row][col] == grid[r][col] && grid[r][col] != EMPTY_SLOT) { 
                        grid[row][col] *= 2;
                        scoreUpdate(grid[row][col]);  
                        grid[r][col] = EMPTY_SLOT;
                        merge = true;
                        move = true;
                     }
                  }
               }
            }
         }
      }
   
   /* Method name: combineRight
    Method parameters: none
    Method action: Goes through the board and combines tiles with the tile to the right of it if they have the same value
    Method returns: nothing
   */   
   //simiar to combineUp, except this method checks from the right of the board left
       public void combineRight() {
      
         for (int row = 0; row < MAX; row++) {
            for (int col = MAX-1; col >= 0; col--) {
               if (grid[row][col] != EMPTY_SLOT) {
                  boolean merge = false;
                  for (int c = col-1; c >= 0 && merge == false; c--) {
                     if (grid[row][col] != grid[row][c] && grid[row][c] != EMPTY_SLOT) {
                        merge = true;
                     }
                     else if (grid[row][col] == grid[row][c] && grid[row][c] != EMPTY_SLOT) {         
                        grid[row][col] *= 2;
                        scoreUpdate(grid[row][col]); 
                        grid[row][c] = EMPTY_SLOT;
                        merge = true;
                        move = true;
                     }
                  }
               }
            }
         }
      }
   
   /* Method name: combineLeft
    Method parameters: none
    Method action: Goes through the board and combines tiles with the tile to the left of it if they have the same value
    Method returns: nothing
   */   
   //simiar to combineUp, except this method checks from the left of the board right
       public void combineLeft() {
      
         for (int row = 0; row < MAX; row++) {
            for (int col = 0; col < MAX; col++) {
               if (grid[row][col] != EMPTY_SLOT) {
                  boolean merge = false;
                  for (int c = col+1; c < MAX && merge == false; c++) {
                     if (grid[row][col] != grid[row][c] && grid[row][c] != EMPTY_SLOT) {
                        merge = true;
                     }
                     else if (grid[row][col] == grid[row][c] && grid[row][c] != EMPTY_SLOT) {  
                        grid[row][col] *= 2;
                        scoreUpdate(grid[row][col]); 
                        grid[row][c] = EMPTY_SLOT;
                        merge = true;
                        move = true;
                     }
                  }
               }
            }
         }
      }
   
   /* Method name: genTile
    Method parameters: none
    Method action: Generates a random tile. Generates a either a 0 or 1 at random and returns the value 2 if a 0 is generated and 4 if a 1 is generated
    Method returns: integer. Either 2 or 4 at random
   */   
       public static int genTile() {
         //declaring a temporary integer variable called temp and initializing it to either a 0 or 1 at random
         int temp = (int)(Math.random()*2);
        
        //returns 2 if temp is equal to 0 and 4 if temp is equal to 1
         if (temp == 0) {
            return 2;
         }
         else {
            return 4;
         }
      }
   
   /*  Method name: scoreUpdate
    Method parameters: newValue (integer, the value of new tile to be placed with two tiles combine)
    Method action: Calculates the new score by adding the value of the new tile after a combination to the old score
    Method returns: nothing
   */   
       public void scoreUpdate (int newValue) {
         currentScore += newValue;
      } 
   
   /* Method name: checkWin
    Method parameters: none  
    Method action: Checks if the player has won. Goes through the board and checks if any tile has the value of the winningLevel
    Method returns: boolean. True if the player has won, false if they haven't
   */    
       public boolean checkWin() {
         for (int row = 0; row < MAX; row++) {
            for (int col = 0; col < MAX; col++) {
              //returns true if any tile on the board has the value of the winningLevel
               if (grid[row][col] == winningLevel) {
                  return true;
               }
            }
         }
        //returns false if the winningLevel isn't present
         return false;
      }
   
   /* Method name: boardFull
    Method parameters: none  
    Method action: Checks if the board is full/No empty slots available. This method is called along with movePossibleFull to check if the player has lost
    Method returns: boolean. True if the board is full, false if it isn't
   */ 
       public boolean boardFull() {
         for (int row = 0; row < MAX; row++) {
            for (int col = 0; col < MAX; col++) {
               if (grid[row][col] == EMPTY_SLOT) {
                  return false;
               }
            }
         }
         return true;
      }   
   
   /* Method name: movePossibleFull
    Method parameters: none  
    Method action: Checks if any moves are possible when the board is full. This method is called along with boardFull. If board is full and no move is possible, the player has lost
    Method returns: boolean. True if any moves are possible, false if there isn't
   */   
       public boolean movePossibleFull() {
        //this method assumes that the board is full and sliding into empty slots is not possible 
        //checks if the board can combine up
         for (int row = 1; row < MAX; row++) {
            for (int col = 0; col < MAX; col++) {          
               if (grid[row][col] == grid[row-1][col]) {
                  return true; 
               }
            }
         }
        //checkis if the board can combine down
         for (int row = 0; row < MAX-1; row++) {
            for (int col = 0; col < MAX; col++) {          
               if (grid[row][col] == grid[row+1][col]) {
                  return true; 
               }
            }
         }
        //checks is the board can combine left
         for (int row = 0; row < MAX; row++) {
            for (int col = 1; col < MAX; col++) {           
               if (grid[row][col] == grid[row][col-1]) {
                  return true; 
               }
            }
         }
        //checks is the board can combine right
         for (int row = 0; row < MAX; row++) {
            for (int col = 0; col < MAX-1; col++) {
               if (grid[row][col] == grid[row][col+1]) {
                  return true; 
               }
            }
         }
        //returns false if no moves are possible
         return false;
      
      }
   
   }