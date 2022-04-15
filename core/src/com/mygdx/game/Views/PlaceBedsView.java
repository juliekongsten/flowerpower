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

import java.util.ArrayList;
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
        for (Square square : opBoard){
            int x = (int) square.getBounds().x;
            int y = (int) square.getBounds().y;

            System.out.println("OpSquare: "+x+","+y);
        }
        //Draw my board
        for (Square square : myBoard){
            int x = (int) square.getBounds().x;
            int y = (int) square.getBounds().y;

            System.out.println("MySquare: "+x+","+y);
        }
    }

    @Override
    protected void handleInput() {
        //TODO: Handle no switch of touched bed: you should move one bed until you drop it
        if (Gdx.input.isTouched()) {
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            Bed touchedBed = new Bed(0, true, "flowerbed_1.png");
            for (Bed bed : beds) {
                if (bed.getBounds().contains(pos.x, pos.y)) {
                    touchedBed = bed;
                }
            }
            touchedBed.updatePosition(pos.x-touchedBed.getTexture().getWidth()/2, pos.y - (touchedBed.getTexture().getHeight()/2));
        }

        if (Gdx.input.justTouched()) {
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            //Check if ready-button is pushed
            Rectangle readyBounds = new Rectangle(ready_x, ready_y, ready.getWidth(), ready.getHeight());
            if (readyBounds.contains(pos.x, pos.y)) {
                for (Square square : myBoard) {
                    // TODO: implement check that all beds are inside the board, give feedback to keep moving if no
                    //Move beds to actual squares if they are between
                    for (Bed bed : beds) {
                        if (!bed.isHorizontal()) {
                            if (square.getBounds().contains(bed.getPos_x() + bed.getTexture().getWidth()/2, bed.getPos_y()) && !square.hasFlower()) {
                                bed.updatePosition(square.getBounds().getX(), square.getBounds().getY());
                                //bed.addSquare(square);
                                //square.setHasFlower(true);
                                System.out.println("Squareobject: "+square);
                                System.out.println("Vertical bed moved");
                        }
                        } else {
                            if (square.getBounds().contains(bed.getPos_x(), bed.getPos_y()+bed.getTexture().getHeight()/2) && !square.hasFlower()) {
                                bed.updatePosition(square.getBounds().getX(), square.getBounds().getY());
                                //bed.addSquare(square);
                                //square.setHasFlower(true);
                                System.out.println("Squareobject: "+square);
                                System.out.println("Horizontal bed moved");
                            }
                        }
                    }
                }

                //addAdjacentSquares();

                controller.setMyBeds(beds);
                controller.setMyBoard(myBoard);
                controller.setOpBoard(opBoard);


                isReady = true; //sets to isReady, so that in render you will be sent to GameView if other player is ready
            }
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
     * Should start with the given beds in the pool
     * @param sb
     */
    private void drawBeds(SpriteBatch sb){

        if (beds.get(0).getPos_x() == 0 && beds.get(0).getPos_y() == 0) {
            float bed1_x = pool.getWidth()/2;
            float bed1_y = pool_y+pool.getHeight()/2-20;
            float bed2_y = bed1_y - beds.get(1).getTexture().getHeight()-8;
            float bed3_x = pool_x + 20;
            float bed3_y = pool_y + 10;
            float bed4_x = bed1_x - beds.get(3).getTexture().getWidth() - 20;
            float bed5_y = bed1_y + beds.get(4).getTexture().getHeight()+8;
            beds.get(0).updatePosition(bed1_x, bed1_y);
            beds.get(1).updatePosition(bed1_x, bed2_y);
            beds.get(2).updatePosition(bed3_x, bed3_y);
            beds.get(3).updatePosition(bed4_x, bed3_y);
            beds.get(4).updatePosition(bed1_x, bed5_y);
        }
        sb.draw(beds.get(0).getTexture(), beds.get(0).getPos_x(), beds.get(0).getPos_y());
        sb.draw(beds.get(1).getTexture(), beds.get(1).getPos_x(), beds.get(1).getPos_y());
        sb.draw(beds.get(2).getTexture(), beds.get(2).getPos_x(), beds.get(2).getPos_y());
        sb.draw(beds.get(3).getTexture(), beds.get(3).getPos_x(), beds.get(3).getPos_y());
        sb.draw(beds.get(4).getTexture(), beds.get(4).getPos_x(), beds.get(4).getPos_y());
    }

    /**
     *  Adds adjecent squares to each bed
     *  Have to add 2 to the coordinates, to only get the square inside the bed
     */
    /*
    private void addAdjacentSquares(){
        for (Bed bed : beds) {
            for (Square square : myBoard){
                if(bed.getBounds().contains(square.getBounds().x+2,square.getBounds().y+2) && !bed.getSquares().contains(square)){
                    //bed.addSquare(square);
                    square.setHasFlower(true);
                }
            }
        }
    }
    */

    /**
     * Checks if the other player is ready and sends player to gameview if both players are ready
     * @param sb
     */
    private void checkOtherPlayer(SpriteBatch sb){

        //TODO: check if the other player is ready
        boolean opReady = true; //set to true now, so that we get to next view, should be actual check here
        //changed^ to false, to check the waiting text and color.

        if (opReady){
            vm.setController(controller);
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
