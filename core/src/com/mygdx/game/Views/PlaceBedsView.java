package com.mygdx.game.Views;

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
    private final Texture pool;
    private Texture ready;
    private Texture op_board;
    private Texture my_board;
    private Texture my_turn;
    private Texture op_turn;
    private Texture myGrass;
    private Texture opGrass;
    private Texture opFrame;

    private GameController controller;

    private float ready_x;
    private float ready_y = -10;
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
    private List<Bed> beds;


    public PlaceBedsView(ViewManager vm){
        super(vm);
        controller = new GameController();
        pool = new Texture("pool.png");
        ready = new Texture("Button.png");
        op_board = new Texture("board.png");
        my_board = new Texture("board.png");
        myGrass = new Texture("mysquare.png");
        opGrass = new Texture("opsquare.png");
        opFrame = new Texture("opframe.png");

        findStaticCoordinates();
        opBoard = controller.getOpBoard();
        myBoard = controller.getMyBoard();

    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            //Check if ready-button is pushed
            Rectangle readyBounds = new Rectangle(ready_x, ready_y, ready.getWidth(), ready.getHeight());
            if (readyBounds.contains(pos.x, pos.y)) {
                //some logic
                //send to waitingside
                vm.set(new GameView(vm)); //må forandres senere, må jo vente på at neste spiller er ready
                System.out.println("READY PRESSED");

            }

            //Handle drag-and-drop of beds

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
            /*if (square.isHit()){
                if (square.hasFlower()){
                    sb.draw(flower,x,y);
                }
                else{
                    sb.draw(miss,x,y);
                }
            }*/ //probably doesn't need this as no squares will be hit at this point!
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
     * Should start with the given beds in the pool, don't know where this logic should be
     * @param sb
     */
    private void drawBeds(SpriteBatch sb){


    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        ScreenUtils.clear((float)254/255,(float)144/255,(float) 182/255,1);

        //Draws ready button
        //her må vi ha noe opplegg at denne kun skal vises om man er i "plassere beds stadier"
        sb.draw(ready,ready_x, ready_y);

        //Draws the background of "my board"
        sb.draw(my_board, board_x,my_board_y );

        //Draws the pool in the middle
        sb.draw(pool, pool_x ,pool_y);

        //Draws the background of "opponents board"
        sb.draw(op_board, board_x, op_board_y);

        //Draw the squares
        drawSquares(sb);

        //Draw the beds
        drawBeds(sb);

        sb.end();


    }
}
