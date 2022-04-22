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

import java.util.ArrayList;
import java.util.List;

public class PlaceBedsView extends View{
    private boolean isReady = false; //if the player is ready

    private final Texture pool;
    private final Texture ready;
    private final Texture op_board;
    private final Texture my_board;
    private final Texture myGrass;
    private final Texture opGrass;
    private final Texture opFrame;
    private final Texture your_beds;
    private final Texture waiting_black;
    private final Texture waiting_text;
    private final Texture overlapping_text;
    private final Texture replace;
    private final Texture beds_outside_board;
    private final Texture back;
    private final Texture opponent_exited_text;
    private final Texture go_to_menu;
    private final Texture sure;
    private final Texture no;
    private final Texture yes;
    private final Texture pop_up;

    private GameController gameController;

    private float ready_x;
    private final float ready_y = -10;
    private float board_x;
    private float my_board_y;
    private float pool_x;
    private float pool_y;
    private float op_board_y;
    private final List<Square> opBoard;
    private final List<Square> myBoard;
    private List<Bed> beds;
    private boolean overlappingBeds = false;
    private boolean bedsOutsideBoard = false;
    private boolean goBack;
    private boolean opponent_exited = false; //If the opponent exited the game before it started.


    public PlaceBedsView(ViewManager vm){
        super(vm);
        this.gameController = vm.getController();
        //Prepares textures for parts of the view
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
        overlapping_text = new Texture("overlapping_text.png");
        replace = new Texture("replace.png");
        beds_outside_board = new Texture("beds_outside_board.png");
        back = new Texture("back.png");
        opponent_exited_text = new Texture("opponent_exited.png");
        go_to_menu = new Texture("go_to_menu.png");
        sure = new Texture("sure_pbw.png");
        no = new Texture("no.png");
        yes = new Texture("yes.png");
        pop_up = new Texture("POP-UP.png");


        findStaticCoordinates();
        opBoard = gameController.getOpBoard();
        myBoard = gameController.getMyBoard();
        beds = gameController.getMyBeds();

    }

    /**
     * Handles input from user
     */
    @Override
    protected void handleInput() {
        //TODO: (low priority) Handle no switch of touched bed: you should move one bed until you drop it
        if (Gdx.input.isTouched()) {
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            Bed touchedBed = new Bed(0, true, "flowerbed_1.png");
            //Update position to bed touched
            for (Bed bed : beds) {
                if (bed.getBounds().contains(pos.x, pos.y)) {
                    touchedBed = bed;
                }
            }
            touchedBed.updatePosition(pos.x-touchedBed.getTexture().getWidth()/2, pos.y - (touchedBed.getTexture().getHeight()/2));
        }

        if (Gdx.input.justTouched()) {
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            Rectangle readyBounds = new Rectangle(ready_x, ready_y, ready.getWidth(), ready.getHeight());

            //Check if ready-button is pressed
            if (readyBounds.contains(pos.x, pos.y)) {
                for (Bed bed : beds){
                    //Makes sure bed is placed on squares
                    bed.moveToNearestSquares(myBoard);

                    //Checks if given bed is inside the board (my board)
                    List<Square> squares = bed.getSquares(myBoard);
                    if (squares.size() < bed.getSize()){ //means that some part of the bed is outside the board
                        bedsOutsideBoard = true;
                    }
                }

                gameController.setMyBeds(beds);
                overlappingBeds = checkOverlappingBeds();
                if(!overlappingBeds & !bedsOutsideBoard){
                    //Set isReady to true so render will act accordingly
                    isReady = true;
                    gameController.setPlayerReady();
                }
            }
            //Checks if replace-button is pressed
            Rectangle replaceBounds = new Rectangle(FlowerPowerGame.WIDTH/2-replace.getWidth()/2,FlowerPowerGame.HEIGHT-150,replace.getWidth(),replace.getHeight());
            if(replaceBounds.contains(pos.x,pos.y)){
                System.out.println("REPLACE TOUCHED");
                overlappingBeds = false;
                bedsOutsideBoard = false;
            }
            Rectangle backBounds = new Rectangle(10, FlowerPowerGame.HEIGHT-20, back.getWidth(), back.getHeight());
            if (backBounds.contains(pos.x, pos.y)) {
                goBack = true;
            }
            Rectangle noBounds = new Rectangle(FlowerPowerGame.WIDTH/2-no.getWidth()-5,FlowerPowerGame.HEIGHT/2-100,no.getWidth(),no.getHeight());
            Rectangle yesBounds = new Rectangle(FlowerPowerGame.WIDTH/2+yes.getWidth()/8,FlowerPowerGame.HEIGHT/2 -100,yes.getWidth(),yes.getHeight());
            if(goBack){
                if(noBounds.contains(pos.x,pos.y)){
                    goBack = false;
                }
                if(yesBounds.contains(pos.x,pos.y)){
                    //TODO update that the user has pressed "go back" and yes, and exited the game DB
                    vm.set(new MenuView(vm));
                }
            }
            Rectangle go_to_menuBounds = new Rectangle(FlowerPowerGame.WIDTH/2-go_to_menu.getWidth()/2,FlowerPowerGame.HEIGHT/2+120,go_to_menu.getWidth(),go_to_menu.getHeight());
            if (go_to_menuBounds.contains(pos.x,pos.y)&&opponent_exited){
                vm.set(new MenuView(vm));
            }
        }

    }

    /**
     * Check position to beds to see if any are overlapping
     * @return True if there are any overlapping beds, false if not
     */
    private boolean checkOverlappingBeds(){
        List<Square> tmp = new ArrayList<>();
        for(Bed bed : beds){
            for(Square square: bed.getSquares(myBoard)){
                if(tmp.contains(square)){
                    return true;
                }
            }
            tmp.addAll(bed.getSquares(myBoard));
        }
        return false;
    }



    @Override
    public void update(float dt) {
        handleInput();
    }

    /**
     * Finds coordinates to boards, button and pool based on the size of the game
     */
    public void findStaticCoordinates(){
        ready_x = (float)(FlowerPowerGame.WIDTH/2-ready.getWidth()/2);
        board_x = (float) (FlowerPowerGame.WIDTH-op_board.getWidth())/2;
        my_board_y = ready.getHeight()-13;
        pool_x = (float) (FlowerPowerGame.WIDTH/2-pool.getWidth()/2);
        pool_y = my_board_y+my_board.getHeight()+2;
        op_board_y = pool_y+pool.getHeight()+2;
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
     * Draw my beds based on their placements
     * Should start with the given beds in the pool
     * @param sb
     */
    private void drawBeds(SpriteBatch sb){
        //Set the placement to inside pool IF they're not moved yet
        if ((beds.get(0).getPos_x() == 0 && beds.get(0).getPos_y() == 0) || beds.size()==0) {
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
        for (Bed bed : beds){
            sb.draw(bed.getTexture(), bed.getPos_x(), bed.getPos_y());
        }
    }

    /**
     * Checks if the other player is ready and sends player to gameview if both players are ready
     * @param sb
     */
    private void checkOtherPlayer(SpriteBatch sb){
        boolean opReady = gameController.getOpReady();

        if (opReady){
            gameController.sendMyBeds(beds);
            vm.setController(gameController);
            vm.set(new GameView(vm, gameController));
        } else{
            //Draw waiting-graphics
            sb.draw(waiting_black,0,0);
            sb.draw(waiting_text,FlowerPowerGame.WIDTH/2-waiting_text.getWidth()/2,FlowerPowerGame.HEIGHT/2);
        }

    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        ScreenUtils.clear((float)254/255,(float)144/255,(float) 182/255,1);

        //Draws the textures
        sb.draw(ready,ready_x, ready_y);
        sb.draw(my_board, board_x,my_board_y );
        sb.draw(pool, pool_x ,pool_y);
        sb.draw(your_beds,pool_x+10,pool_y+pool.getHeight()-20);
        sb.draw(op_board, board_x, op_board_y);
        drawSquares(sb);
        drawBeds(sb);
        sb.draw(pop_up,pool_x+150,pool_y+pool.getHeight()-20);

        //Draws message and replace button if there are overlapping beds or beds outside board
        if (bedsOutsideBoard){
            sb.draw(waiting_black,0,0);
            sb.draw(beds_outside_board,FlowerPowerGame.WIDTH/2-beds_outside_board.getWidth()/2,FlowerPowerGame.HEIGHT-50);
            sb.draw(replace,FlowerPowerGame.WIDTH/2-replace.getWidth()/2,FlowerPowerGame.HEIGHT-150);
        }
        else if(overlappingBeds){
            sb.draw(waiting_black,0,0);
            sb.draw(overlapping_text,FlowerPowerGame.WIDTH/2-overlapping_text.getWidth()/2,FlowerPowerGame.HEIGHT-50);
            sb.draw(replace,FlowerPowerGame.WIDTH/2-replace.getWidth()/2,FlowerPowerGame.HEIGHT-150);
        }

        if(!goBack){
            sb.draw(back, 10, FlowerPowerGame.HEIGHT-20);
        }
        else{
            sb.draw(waiting_black,0,0);
            sb.draw(sure,FlowerPowerGame.WIDTH/2-sure.getWidth()/2,FlowerPowerGame.HEIGHT/2);
            sb.draw(no, FlowerPowerGame.WIDTH/2-no.getWidth()-5,FlowerPowerGame.HEIGHT/2-100);
            sb.draw(yes,FlowerPowerGame.WIDTH/2+yes.getWidth()/8,FlowerPowerGame.HEIGHT/2 -100);
        }
        //Checks if the opponent exited the game
        opponent_exited = gameController.getOpExited();
        if(opponent_exited){
            sb.draw(waiting_black,0,0);
            sb.draw(opponent_exited_text,FlowerPowerGame.WIDTH/2-opponent_exited_text.getWidth()/2,FlowerPowerGame.HEIGHT-130); //vil ikke tegnes
            sb.draw(go_to_menu,FlowerPowerGame.WIDTH/2-go_to_menu.getWidth()/2,FlowerPowerGame.HEIGHT/2+120);
        }
        if(isReady){
            checkOtherPlayer(sb);
        }
        sb.end();


    }
}
