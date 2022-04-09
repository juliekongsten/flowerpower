package com.mygdx.game.Views;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.FlowerPowerGame;

public class GameView extends View{

    private boolean waiting = true;

    private final Texture pool;
    private Texture ready;
    private Texture op_board;
    private Texture my_board;
    private Texture my_turn;
    private Texture op_turn;



    protected GameView(ViewManager vm) {
        super(vm);
        pool = new Texture("pool.png");
        ready = new Texture("Button.png");
        op_board = new Texture("board.png");
        my_board = new Texture("board.png");
        my_turn = new Texture("my_turn.png");
        op_turn = new Texture("waiting.png");

    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        ScreenUtils.clear((float)254/255,(float)144/255,(float) 182/255,1);


        //her må vi ha noe opplegg at denne kun skal vises om man er i "plassere beds stadier"
        float ready_x = (float)(FlowerPowerGame.WIDTH/2-ready.getWidth()/2);
        sb.draw(ready,ready_x, -10);

        float board_x = (float) (FlowerPowerGame.WIDTH-op_board.getWidth())/2;
        float my_board_y = ready.getHeight()-13;
        sb.draw(my_board, board_x,my_board_y );

        float pool_x = (float) (FlowerPowerGame.WIDTH/2-pool.getWidth()/2);
        float pool_y = my_board_y+my_board.getHeight()+2;
        sb.draw(pool, pool_x ,pool_y);

        float op_board_y = pool_y+pool.getHeight()+2;
        sb.draw(op_board, board_x, op_board_y);

        float my_turn_x = pool_x+(pool.getWidth()/2)-my_turn.getWidth()/2;
        float my_turn_y = pool_y+(pool.getHeight()/2)-my_turn.getHeight()/2;

        float waiting_x = pool_x+(pool.getWidth()/2-op_turn.getWidth()/2);
        float waiting_y = pool_y+(pool.getHeight()/2)-op_turn.getHeight()/2;

        if (!waiting){
            sb.draw(my_turn, my_turn_x, my_turn_y);
        }
        else{
            sb.draw(op_turn, waiting_x, waiting_y);
        }

        sb.end();

    }
}
