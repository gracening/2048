   import java.awt.event.*;


//Game2048Listener manages user inputs. It implements KeyListener class

    public class Game2048Listener implements KeyListener
   {
      private Game2048GUI gui;
      private Game2048 game;
   
//Constructs the Game2048Listener object by taking in the game and the gui
       public Game2048Listener (Game2048 _game, Game2048GUI _gui)
      {
         game =_game;
         gui = _gui;
         gui.addListener (this);
      }
   
//Overriding the keyPressed method from KeyListener class
       public void keyPressed(KeyEvent e)
      {
         int direction = -1;
         int key = e.getKeyCode();
        
         if( key == KeyEvent.VK_LEFT)
         {
            direction = Game2048.LEFT_INPUT; 
         }
         else if( key == KeyEvent.VK_DOWN)
         {
            direction = Game2048.DOWN_INPUT; 
         }
         else if( key == KeyEvent.VK_RIGHT)
         {
            direction = Game2048.RIGHT_INPUT; 
         }
         else if( key == KeyEvent.VK_UP)
         {
            direction = Game2048.UP_INPUT; 
         }
        
         game.play(direction); 
      
      }
   
//Overriding the keyReleased method from KeyListener class
       public void keyReleased(KeyEvent e)
      {
         game.newSlot();
      
      }
   
//Overriding the keyTyped method from KeyListener class
       public void keyTyped(KeyEvent e)
      {
      }
   }
