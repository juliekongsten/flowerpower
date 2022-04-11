package com.mygdx.game.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Controllers.GameController;
import com.mygdx.game.FlowerPowerGame;
import com.mygdx.game.Models.Square;

import java.util.ArrayList;
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




    public GameView(ViewManager vm) {
        super(vm);
        controller = new GameController();
        pool = new Texture("pool.png");
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
        findStaticCoordinates();
        opBoard = controller.getOpBoard();
        myBoard = controller.getMyBoard();
    }

    @Override
    protected void handleInput() {
        //obs!! må sjekke tilstand til spillet, er man i waiting mode skal det ikke skje noe forskjell om man trykker på opponent sitt board
        if (Gdx.input.justTouched()) {
            Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            //Check if any of opponents squares is pressed
            //Shouldn't do anything if opponents squares is pushed while waiting as it isn't your turn:)
            if (!waiting){
                for (Square square : opBoard){
                    if (square.getBounds().contains(pos.x,pos.y)){
                        //Make controller check the square and update the values
                        //Controller gives feedback of if it was a hit/miss or if you pressed square already is pressed before (then nothing will happen)
                        //View should give feedback to user if this was hit/miss
                        //Do not need to do changes in spritebatch here, since we update square it will be taken care of in render


                        //Temporarily: sets the square to hit immediately without checking, checking should probably be done in controller
                        boolean flower = controller.hitSquare(square);
                        if (flower){
                            //TODO: give visual feedback to user
                            System.out.println("Hit!");

                        } else {
                            //TODO: give visual feedback to user
                            System.out.println("Miss!");
                        }
                        //TODO: Give feedback to controller so that the other player also is notified (or implement squarelistener in some way)
                        System.out.println("Opponents square was pressed: ["+square.getBounds().x+","+square.getBounds().y+"]");
                    }
                }
            }

        }
    }

    protected void receiveOpMove(Square square){
        //Should only be called when the opponent has made a move
        //TODO: Give feedback to user that your square has been hit/miss
        //Do not draw the flower/miss as this is done in render


    }

    @Override
    public void update(float dt) {
        handleInput();
        if (waiting){
            //TODO: Find way to get square from controller
            //TODO: Find out if we should implement this as squarelistener instead and how
            Square square = new Square(1,1,1); //should get this from controller
            if (square != null){
                receiveOpMove(square);
            }

        }
    }

    private void drawSquares(SpriteBatch sb){
        //Draw opponents board
        //Goes through the list of squares in opponents board and draws the grass plus flower/miss(if hit) on given coordinates
        //Should also make frame around bed if the whole bed is hit! Maybe iterate through opponents beds and for any full bed draw the frame for that bed?
        //TODO: Find out how to frame fully hit beds
        for (Square square : opBoard){
            int x = (int) square.getBounds().x;
            int y = (int) square.getBounds().y;
            sb.draw(opGrass, x, y);
            sb.draw(opFrame, x, y);
            if (square.isHit()){
                if (square.hasFlower()){
                    sb.draw(flower,x,y);
                }
                else{
                    sb.draw(miss,x,y);
                }
            }
        }

        //Draw my board

        //Goes through the list of squares in my board and draws the grass plus flower/miss(if hit) on given coordinates
        for (Square square : myBoard){
            int x = (int) square.getBounds().x;
            int y = (int) square.getBounds().y;
            sb.draw(myGrass, x, y);
            if (square.isHit()){
                if (square.hasFlower()){
                    sb.draw(flower,x,y);
                }
                else{
                    sb.draw(miss,x,y);
                }
            }
        }

    }

    private void drawBeds(SpriteBatch sb){
        //TODO: Fill with the same as in PlaceBedsView
        //this should maybe be AFTER drawing square but BEFORE drawing miss/flower... Check this and if needed split "drawSquares" into "drawSquares" and "drawHits"
        //My beds should always be drawn in their placements on my board
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

    public List<Float> getMyBoardCoords(){
        List<Float> list = new ArrayList<>();
        list.add(board_x);
        list.add(my_board_y);
        return list;
    }

    public List<Float> getOpBoardCoords(){
        List<Float> list = new ArrayList<>();
        list.add(board_x);
        list.add(op_board_y);
        return list;
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

        sb.end();

    }
}
