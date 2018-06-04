package com.isel.pg;

import isel.leic.pg.Console;

import java.awt.event.KeyEvent;

public class MasterMind {
    public static final int MAX_TRIES = 9;  // 9..20
    public static final int KEY_LENGTH = 4;  // 1..6
    public static final int MAX_COLORS = 5;  // KEY_LENGTH..8

    public static int[] currentTry = new int[KEY_LENGTH];  // Pins of current try
    private static int[] secretKey = new int[KEY_LENGTH];  // Secret key with the same size of current try
    private static int tryNum;      // Number of current try (1..MAX_TRIES)
    private static int pinNum = 0;  // Number of current pin in current try
    private static boolean terminate = false;
    public static int ColorSelected = 0; //Number of the current color
    private static int swap, exact;
    private static boolean keepColors = false;

    public static void main(String[] args) {
        Panel.init();
        TopScore.load();
        init();
        play();
        TopScore.save();
        //Panel.message("Game terminated;Bye");
        Panel.end();
    }

    private static void init() {
        TopScore.printFrame();
        Panel.printLegend();
        startGame();
    }

    private static void startGame() {
        tryNum=1;
        for (int i = 0; i <KEY_LENGTH; i++)
        {
            currentTry[i] = Panel.NO_COLOR;
            secretKey[i] = Panel.NO_COLOR;
        }
        Panel.generateSecretKey(secretKey, MAX_COLORS, keepColors);
        Panel.printBoard(keepColors);
        Panel.printEqualColors(keepColors);
        TopScore.printScores();
    }

    private static void play() {
        int key;
        do {
            key = Console.waitKeyPressed(1000);
            if (key>0) {
                processKey(key);
                Panel.printTryPins(tryNum, pinNum, currentTry);
                Panel.printRectColors(Panel.BAR_LINE, Panel.COLS+1, MAX_COLORS+2, 3, ColorSelected, currentTry, keepColors);
                Console.waitKeyReleased(key);
            }
        } while( !terminate );
    }

    private static void processKey(int key) {
        switch (key) {
            case KeyEvent.VK_ESCAPE:
                if(Panel.confirm("Exit game?")) terminate = true;
                break;
            case KeyEvent.VK_RIGHT:
                if (++pinNum == KEY_LENGTH) pinNum =0;
                break;
            case KeyEvent.VK_LEFT:
                if (--pinNum <0) pinNum = KEY_LENGTH-1;
                break;
            case KeyEvent.VK_DOWN:
                if(++ColorSelected > MAX_COLORS) ColorSelected = 0;
                break;
            case KeyEvent.VK_UP:
                if (--ColorSelected <0) ColorSelected = MAX_COLORS;
                break;
            case KeyEvent.VK_SPACE:
                if(checkCurrentColor(currentTry, ColorSelected, pinNum)) currentTry[pinNum] = ColorSelected;
                break;
            case KeyEvent.VK_DELETE:
                currentTry[pinNum] = Panel.NO_COLOR;
                break;
            case KeyEvent.VK_ENTER:
                if(checkCurrentTry(currentTry))
                {
                    validatePins();
                    if(exact == 4){
                        Panel.validateMove(tryNum, exact, swap, currentTry, true, secretKey, "You win", keepColors);
                        if(TopScore.checkResult(tryNum, 5)){
                            Score s = new Score(Panel.read("Well done;The score goes to top;Enter your name", 10), tryNum, 5);
                            TopScore.addScore(s);
                            TopScore.printScores();
                        }
                        if(Panel.confirm("New game")){
                            if(Panel.confirm("With repeated colors")){
                                keepColors = true;
                                init();
                            }
                            else{
                                keepColors = false;
                                init();
                            }
                        }
                        else
                            terminate = true;
                    }else if(tryNum != MAX_TRIES){
                        Panel.validateMove(tryNum, exact, swap, currentTry, false, secretKey, "", keepColors);
                        tryNum++;
                    }
                    else{
                        Panel.validateMove(tryNum, exact, swap, currentTry, true, secretKey, "You lose", keepColors);
                        if(Panel.confirm("New game"))
                            if(Panel.confirm("With repeated colors")){
                                keepColors = true;
                                init();
                            }
                            else{
                                keepColors = false;
                                init();
                            }
                        else
                            terminate = true;
                    }
                }
                break;
            case KeyEvent.VK_G:
                if(Panel.confirm("New game"))
                    if(Panel.confirm("With repeated colors")){
                        keepColors = true;
                        init();
                    }
                    else{
                        keepColors = false;
                        init();
                    }
                break;


        }
    }

    private static boolean checkCurrentColor(int[] currTry, int color, int pin){
        boolean res = false;
        if(!keepColors) {
            for (int i = 0; i < currTry.length; i++) {
                if (currTry[pin] == -1 && currTry[i] != color)
                    res = true;
                else {
                    res = false;
                    break;
                }
            }
        }
        else
            res = true;
        return res;
    }

    private static boolean checkCurrentTry(int[] currTry){
        boolean res = false;
        for( int i = 0; i < currTry.length; i++)
        {
            if(currTry[i] != Panel.NO_COLOR)
                res = true;
            else{
                res = false;
                break;
            }
        }
        return res;
    }

    private static void validatePins(){
        exact = 0;
        swap = 0;
        for(int i=0; i<secretKey.length; i++){
            for(int j=0; j<currentTry.length; j++){
                if (secretKey[i] == currentTry[j] && i == j)
                    exact++;
                else if(secretKey[i] == currentTry[j] && i != j)
                    swap++;
            }
        }
    }



 }
