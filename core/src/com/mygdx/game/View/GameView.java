package com.mygdx.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Controller.GameController;
import com.mygdx.game.FlowerPowerGame;
import com.mygdx.game.Model.Bed;
import com.mygdx.game.Model.Square;

import java.util.List;

public class GameView extends View{

    private boolean waiting = false;

    private final Texture pool;
    private Texture ready;
    private Texture op_board;
    private Texture my_board;
    private Texture my_turn;
    private Texture op_turn;
    private Texture myGrass;
    private Texture opGrass;
    private Texture opFrame; //midlertidig? er fordi opGrass bare er grønt og uten svart ramme
    private Texture flower;
    private Texture miss;
    private Texture back;
    private Texture waiting_black;
    private Texture sure;
    private Texture no;
    private Texture yes;

    private boolean goBack = false;
    private boolean gameOver = false;

    private GameController controller;

    private float board_x;
    private float my_board_y;
    private float pool_x;
    private float pool_y;
    private float op_board_y;
    private float my_turn_x;
    private float my_turn_y;
    private float waiting_x;
    private float waiting_y;
    private List<Square> opBoard;
    private List<Square> myBoard;
    private List<Bed> myBeds;
    private List<Bed> opBeds;




    public GameView(ViewManager vm) {
        super(vm);
        controller = vm.getController();
        pool = new Texture("pool.png");
        createTextures();
        findStaticCoordinates();
        myBeds = controller.getMyBeds();
        myBoard = controller.getMyBoard();
        controller.receiveOpBeds();
        opBeds = controller.getOpBeds();
        opBoard = controller.getOpBoard();

    }

    /**
     * Prepares textures for parts of the view
     */
    private void createTextures(){
        ready = new Texture("Button.png");
        op_board = new Texture("board.png");
        my_board = new Texture("board.png");
        my_turn = new Texture("my_turn.png");
        op_turn = new Texture("waiting.png");
        myGrass = new Texture("mysquare.png");
        opGrass = new Texture("opsquare.png");
        opFrame = new Texture("opframe.png");
        flower = new Texture("flower.png");
        miss = new Texture("miss.png");
        back = new Texture("back.png");
        waiting_black = new Texture("waiting_black.png");
        sure = new Texture("sure.png");
        no = new Texture("no.png");
        yes = new Texture("yes.png");
    }

    /**
     * Handles input from user
     */
    @Override
    protected void handleInput() {
        //TODO: Handle state of game - do not handle playing input if not in playing state (partially implemented)
        if (Gdx.input.justTouched()) {
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            //If player is not waiting on opponents move we check if player presses any of opponents
            //squares and act accordingly
            if (!waiting){
                for (Square square : opBoard){
                    if (square.getBounds().contains(pos.x,pos.y)){
                        //Lets controller know a square was hit, gets feedback from controller of if it was a hit/miss or if you pressed square already is pressed before (then nothing will happen)
                        //Gives feedback to user if this was hit/miss

                        boolean flower = controller.hitSquare(square);
                        //TODO: (low priority) give visual feedback ("Hit!"/"Miss")
                        if (flower){
                            System.out.println("Hit!");
                        } else {
                            System.out.println("Miss!");
                        }
                        //TODO: Give feedback to controller so that the other player also is notified (or implement squarelistener in some way)
                    }
                }

            }
            Rectangle backBounds = new Rectangle(10, FlowerPowerGame.HEIGHT-20, back.getWidth()+3, back.getHeight()+3);
            Rectangle noBounds = new Rectangle(FlowerPowerGame.WIDTH/2-no.getWidth()-5,FlowerPowerGame.HEIGHT/2-100,no.getWidth(),no.getHeight());
            Rectangle yesBounds = new Rectangle(FlowerPowerGame.WIDTH/2+yes.getWidth()/8,FlowerPowerGame.HEIGHT/2 -100,yes.getWidth(),yes.getHeight());
            if (backBounds.contains(pos.x, pos.y)) {
                goBack = true;
            }

            if(goBack){
                if(noBounds.contains(pos.x,pos.y)){
                    goBack = false;
                }
                if(yesBounds.contains(pos.x,pos.y)){
                    vm.set(new ExitView(vm, false));
                }
            }

        }
    }

    /**
     *
     * @param square
     */
    protected void receiveOpMove(Square square){
        //Should only be called when the opponent has made a move
        //TODO: Give feedback to user that your square has been hit/miss
        //Do not draw the flower/miss as this is done in render


    }

    @Override
    public void update(float dt) {
        handleInput();

        //Checks if the game is over and takes player to ExitView
        if (gameOver){
            boolean won = controller.getWinner();
            vm.set(new ExitView(vm, won));
        }
        //If waiting we check if the opponent has made a move so we can give give feedback
        if (waiting){
            //TODO: Find way to get square from controller
            //TODO: Find out if we should implement this as squarelistener instead and how
            Square square = new Square(1,1,1); //should get this from controller
            if (square != null){
                receiveOpMove(square);
            }

        }
    }

    /**
     * Draws the graphics of squares to the boards
     * @param sb
     */
    private void drawSquares(SpriteBatch sb){
        //Draw opponents board
        for (Square square : opBoard){
            int x = (int) square.getBounds().x;
            int y = (int) square.getBounds().y;
            sb.draw(opGrass, x, y);
            sb.draw(opFrame, x, y);

        }

        //Draw my board
        for (Square square : myBoard){
            int x = (int) square.getBounds().x;
            int y = (int) square.getBounds().y;
            sb.draw(myGrass, x, y);
        }

    }

    /**
     * Draw flower/miss on the squares that are hit
     * @param sb
     */
    //TODO: (low priority) Consider implementing a list "hitSquares" that all hit squares are added to so that we could iterate through only these
    private void drawHits(SpriteBatch sb) {
        for (Square square : opBoard) {
            int x = (int) square.getBounds().x;
            int y = (int) square.getBounds().y;
            if (square.isHit()) {
                if (square.hasFlower()) {
                    sb.draw(flower, x, y);
                } else {
                    sb.draw(miss, x, y);
                }
            }
        }

        for (Square square : myBoard) {
            int x = (int) square.getBounds().x;
            int y = (int) square.getBounds().y;
            if (square.isHit()) {
                if (square.hasFlower()) {
                    sb.draw(flower, x, y);
                } else {
                    sb.draw(miss, x, y);
                }
            }
        }

    }


    /**
     * Draws all of own beds, and fully hit beds on opponents board
     * @param sb
     */
    private void drawBeds(SpriteBatch sb){
        for (Bed bed : myBeds){
            sb.draw(bed.getTexture(),bed.getPos_x(),bed.getPos_y());
        }

        //Iterate through beds - only draw if fully hit
        for (Bed bed : opBeds){
            if(bed.isFullyHit(opBoard)){
                sb.draw(bed.getTexture(),bed.getPos_x(),bed.getPos_y());
            }
        }
    }

    /**
     * Help method: Finds the coordinates to where ready button, boards, pool and messages should be placed
     */
    private void findStaticCoordinates(){
        board_x = (float) (FlowerPowerGame.WIDTH-op_board.getWidth())/2;
        my_board_y = ready.getHeight()-13;
        pool_x = (float) (FlowerPowerGame.WIDTH/2-pool.getWidth()/2);
        pool_y = my_board_y+my_board.getHeight()+2;
        op_board_y = pool_y+pool.getHeight()+2;
        my_turn_x = pool_x+(pool.getWidth()/2)-my_turn.getWidth()/2;
        my_turn_y = pool_y+(pool.getHeight()/2)-my_turn.getHeight()/2;
        waiting_x = pool_x+(pool.getWidth()/2-op_turn.getWidth()/2);
        waiting_y = pool_y+(pool.getHeight()/2)-op_turn.getHeight()/2;
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        ScreenUtils.clear((float)254/255,(float)144/255,(float) 182/255,1);

        //Draws the background of "my board"
        sb.draw(my_board, board_x,my_board_y );

        //Draws the pool in the middle
        sb.draw(pool, pool_x ,pool_y);

        //Draws the background of "opponents board"
        sb.draw(op_board, board_x, op_board_y);

        //Draws message (your turn/waiting) in the pool
        if (!waiting){
            sb.draw(my_turn, my_turn_x, my_turn_y);
        }
        else{
            sb.draw(op_turn, waiting_x, waiting_y);
        }

        drawSquares(sb);
        drawBeds(sb);
        drawHits(sb);

        //draws Back button, if it isnt touched
        if(!goBack){
            sb.draw(back, 10, FlowerPowerGame.HEIGHT-20);
        }
        else{
            sb.draw(waiting_black,0,0);
            sb.draw(sure,FlowerPowerGame.WIDTH/2-sure.getWidth()/2,FlowerPowerGame.HEIGHT/2);
            sb.draw(no, FlowerPowerGame.WIDTH/2-no.getWidth()-5,FlowerPowerGame.HEIGHT/2-100);
            sb.draw(yes,FlowerPowerGame.WIDTH/2+yes.getWidth()/8,FlowerPowerGame.HEIGHT/2 -100);
        }
        gameOver = controller.getGameOver();

        sb.end();

    }
}
