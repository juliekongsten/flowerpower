package com.mygdx.game.Controller;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.mygdx.game.FlowerPowerGame;
import com.mygdx.game.Model.Button;

public class ButtonController {
    /**
     * Controller where buttons are initialized
     */
    private Button back;
    private Button playbook;
    private Button highscore;
    private final ImageButton highscoreButton;
    private final ImageButton playbookButton;
    private final ImageButton backButton;


    public ButtonController(){
        this.back = new Button("back.png", 20, FlowerPowerGame.HEIGHT - 20);
        this.backButton = back.getButton();
        this.playbook = new Button("playbook.png", 10, 15);
        this.playbookButton = playbook.getButton();;
        this.highscore = new Button("Highscore.png", FlowerPowerGame.WIDTH - 125, 15);
        this.highscoreButton = highscore.getButton();
    }

    /**
     * Getting the button for highScore
     * @return ImageButton
     */
    public ImageButton getHighscoreButton(){
        System.out.println("Dette er ImageButton : "+ highscore);

        return this.highscoreButton;
    }

    /**
     * Getting the button for the playbook
     * @return PlayBookButton
     */
    public ImageButton getPlaybookButton(){
        return this.playbookButton;
    }

    /**
     * Getting the button for going back
     * @return BackButton
     */
    public ImageButton getBackButton(){
        return this.backButton;
    }
}
