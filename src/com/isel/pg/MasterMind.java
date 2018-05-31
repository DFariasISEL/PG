package com.isel.pg;

import isel.leic.pg.Console;
import javafx.scene.layout.Pane;

import java.awt.event.KeyEvent;

public class MasterMind {
    public static final int MAX_TRIES = 9;  // 9..20
    public static final int KEY_LENGTH = 4;  // 1..6
    public static final int MAX_COLORS = 5;  // KEY_LENGTH..8

    public static int[] currentTry = new int[KEY_LENGTH];  // Pins of current try
    private static int tryNum;      // Number of current try (1..MAX_TRIES)
    public static int pinNum = 0;  // Number of current pin in current try
    private static boolean terminate = false;
    private static int ColorSelected = 0; //Number of the current color

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
            currentTry[i] = Panel.NO_COLOR;
        Panel.printBoard();
        Panel.printEqualColors(false);
        TopScore.printScores();
    }

    private static void play() {
        int key;
        do {
            key = Console.waitKeyPressed(1000);
            if (key>0) {
                processKey(key);
                Panel.printTryPins(tryNum, pinNum, currentTry);
                Panel.printRectColors(Panel.BAR_LINE, Panel.COLS+1, MAX_COLORS+2, 3, ColorSelected, pinNum, currentTry);
                Console.waitKeyReleased(key);
            }
        } while( !terminate );
    }

    private static void processKey(int key) {
        switch (key) {
            case KeyEvent.VK_ESCAPE:
                if(Panel.confirm("Exit game?")){
                    terminate = true;
                    break;
                }
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
                if(currentTry[pinNum] == -1 &&
                        currentTry[0] != ColorSelected &&
                        currentTry[1] != ColorSelected &&
                        currentTry[2] != ColorSelected &&
                        currentTry[3] != ColorSelected)
                    currentTry[pinNum] = ColorSelected;
                break;
            case KeyEvent.VK_DELETE:
                currentTry[pinNum] = Panel.NO_COLOR;
        }
    }

 }
