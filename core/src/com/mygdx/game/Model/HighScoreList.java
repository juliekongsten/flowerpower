package com.mygdx.game.Model;

import com.mygdx.game.Controller.GameController;
import com.mygdx.game.FireBaseInterface;
import com.mygdx.game.FlowerPowerGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class HighScoreList {
    private ArrayList<Integer> scoreList;
    private HashMap< String, Integer> scoreMap;
    private LinkedHashMap<String, Integer> topFive;
    private FireBaseInterface _FBIC;



    public HighScoreList(){
        this._FBIC = FlowerPowerGame.getFBIC();
        scoreMap = new HashMap<>();
        topFive = new LinkedHashMap<>();
        scoreList = new ArrayList<>();
        // Getting the ScoreList from db
        // All userIDs

        ArrayList<String> userIDList =  _FBIC.getUserIDs();

        for(int i =0; i< userIDList.size(); i++){
            System.out.println("Kommer inn i for løkken");
            int currentScore = _FBIC.getScore(userIDList.get(i));
            System.out.println("Dette er currentScore: " + currentScore);
            String currentName = _FBIC.getNameFromUID(userIDList.get(i));
            scoreMap.put(currentName, currentScore);
            scoreList.add(currentScore);
        }
        Collections.sort(scoreList);
        Collections.reverse(scoreList);
        for(int i = 0; i < 5; i++){
            int score = scoreList.get(i);
            Object key ="";
            // String name = scoreMap.get(score);

            for(Map.Entry entry: scoreMap.entrySet()){
                if(score == (Integer) entry.getValue()){
                    key = entry.getKey();
                    break; //breaking because its one to one map
                }
            }
            String name = key.toString();


            scoreMap.remove(name);
            if(!topFive.containsKey(name)) {
                topFive.put(name, score);

            }

        }
    }

    public ArrayList<Integer> getScoreList(){
        return this.scoreList;
    }
    public HashMap<String, Integer> getFullMap(){
        return this.scoreMap;
    }
    public HashMap<String, Integer> getTopFive(){
        return this.topFive;
    }




    /*
    TODO: Må hente ut allle spillere og sjekke scoren
     */
}
