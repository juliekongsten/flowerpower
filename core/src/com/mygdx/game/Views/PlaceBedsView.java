package com.mygdx.game.Views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Controllers.GameController;
import com.mygdx.game.FlowerPowerGame;
import com.mygdx.game.Models.Bed;
import com.mygdx.game.Models.Square;

import java.util.List;

public class PlaceBedsView extends View{
    private boolean isReady = false; //if the player is ready

    private final Texture pool;
    private Texture ready;
    private Texture op_board;
    private Texture my_board;
    private Texture myGrass;
    private Texture opGrass;
    private Texture opFrame;
    private Texture your_beds;
    private Texture waiting_black;
    private Texture waiting_text;

    private GameController controller;

    private float ready_x;
    private float ready_y = -10;
    private float board_x;
    private float my_board_y;
    private float pool_x;
    private float pool_y;
    private float op_board_y;
    private List<Square> opBoard;
    private List<Square> myBoard;
    private List<Bed> beds;


    public PlaceBedsView(ViewManager vm){
        super(vm);
        controller = new GameController();
        pool = new Texture("bedpool.png");
        ready = new Texture("Button.png");
        op_board = new Texture("board.png");
        my_board = new Texture("board.png");
        myGrass = new Texture("mysquare.png");
        opGrass = new Texture("opsquare.png");
        opFrame = new Texture("opframe.png");
        your_beds = new Texture("your_beds.png");
        waiting_black = new Texture("waiting_black.png");
        waiting_text = new Texture("waiting_text.png");

        findStaticCoordinates();
        opBoard = controller.getOpBoard();
        myBoard = controller.getMyBoard();
        beds = controller.getMyBeds();


    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            //Check if ready-button is pushed
            Rectangle readyBounds = new Rectangle(ready_x, ready_y, ready.getWidth(), ready.getHeight());
            if (readyBounds.contains(pos.x, pos.y)) {
                System.out.println("READY PRESSED");
                isReady = true; //sets to isReady, so that in render you will be sent to GameView if other player is ready
            }

            //TODO: Handle drag-and-drop of beds

        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    public void findStaticCoordinates(){
        ready_x = (float)(FlowerPowerGame.WIDTH/2-ready.getWidth()/2);
        board_x = (float) (FlowerPowerGame.WIDTH-op_board.getWidth())/2;
        my_board_y = ready.getHeight()-13;
        pool_x = (float) (FlowerPowerGame.WIDTH/2-pool.getWidth()/2);
        pool_y = my_board_y+my_board.getHeight()+2;
        op_board_y = pool_y+pool.getHeight()+2;
    }

    /**
     * Only draw the static squares
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
     * Draw my beds based on their placements
     * Should start with the given beds in the pool, don't know where this logic should be (controller?)
     * @param sb
     */
    private void drawBeds(SpriteBatch sb){
        //TODO: find out how to do this
        //if we find out logic for beds somewhere else we can probably just iterate through list of beds and draw it

    }

    /**
     * Checks if the other player is ready and sends player to gameview if both players are ready
     * @param sb
     */
    private void checkOtherPlayer(SpriteBatch sb){

        //TODO: check if the other player is ready
        boolean opReady = false; //set to true now, so that we get to next view, should be actual check here

        if (opReady){
            vm.set(new GameView(vm));
        } else{
            //draw seethrough black background color
            sb.draw(waiting_black,0,0);
            //draw text "Waiting for other player to get ready"
            sb.draw(waiting_text,FlowerPowerGame.WIDTH/2-waiting_text.getWidth()/2,FlowerPowerGame.HEIGHT/2);

            return;
        }

    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        ScreenUtils.clear((float)254/255,(float)144/255,(float) 182/255,1);

        //Draws ready button
        sb.draw(ready,ready_x, ready_y);

        //Draws the background of "my board"
        sb.draw(my_board, board_x,my_board_y );

        //Draws the pool in the middle
        sb.draw(pool, pool_x ,pool_y);

        //Draw text "Your beds:"
        sb.draw(your_beds,pool_x+10,pool_y+pool.getHeight()-20);
        

        //Draws the background of "opponents board"
        sb.draw(op_board, board_x, op_board_y);

        //Draw the squares
        drawSquares(sb);

        //Draw the beds
        drawBeds(sb);

        if(isReady){
            checkOtherPlayer(sb);
        }

        sb.end();


    }
}
