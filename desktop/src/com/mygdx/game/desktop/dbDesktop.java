package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.db;

public class dbDesktop implements db {

    @Override
    public void writeToDb(String message) {
        Gdx.app.log("dbDesktop", "would write" + message );
    }
}
