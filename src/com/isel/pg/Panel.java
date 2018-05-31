package com.isel.pg;

import isel.leic.pg.Console;

import static isel.leic.pg.Console.*;

public class Panel {
    private static final int TRIES = MasterMind.MAX_TRIES;
    private static final int KEY_LEN = MasterMind.KEY_LENGTH;
    private static final int NUM_SPACES = 4; // Space used to show the try number

    // Colors used for pins [0..7]
    public static final int[] COLORS = { RED, GREEN, BLUE, YELLOW, MAGENTA, CYAN, ORANGE, PINK };

    public static final char PIN = '\u25CF'; // Character to print a pin (ball)
    private static final char EQUALS = '=', DIFFERENT = '\u2260'; // Repeat equal colors or different colors

    public static final int LINES = TRIES *2+3;
    public static final int COLS = KEY_LEN*3+NUM_SPACES+3;
    public static final int BAR_LINE = 9;

    public static void init() {
        fontSize(20);
        scaleFactor(1.1, 0.9);
        open("Master Mind", LINES, COLS + TopScore.COLS + 6);
    }

    public static void end() {
        close();
    }

    /**
     * Draws a rectangle only with spaces with a background color.
     * @param lin Top lin of the rectangle.
     * @param col Left column of the rectangle.
     * @param height Number of lines.
     * @param width Number of columns.
     * @param color background color of rectangle.
     */
    public static void clearRect(int lin, int col, int height, int width, int color) {
        setBackground(color);
        for(int i=0 ; i<height ; ++i) {
            cursor(lin + i, col);
            printRepeat(' ',width);
        }
    }

    /**
     * Draws a rectangle with the available colors.
     * @param lin Top lin of the rectangle.
     * @param col Left column of the rectangle.
     * @param height Number of lines.
     * @param width Number of columns.
     * @param color background color of rectangle to be selected.
     */
    public static void printRectColors(int lin, int col, int height, int width, int color, int pinSelected, int[] currentTry) {
        setBackground(BROWN);
        for(int i=0 ; i<=height ; ++i) {
            cursor(lin + i, col);
            if(i == 0 || i == height)
                printRepeat(' ', width);
            else
                printRepeatWithColors(i, width, color, pinSelected, currentTry);
        }
    }

    /**
     * Draws the basics of board. Call only once per game.
     */
    public static void printBoard() {
        clearRect(0,0,LINES,COLS,BROWN);
        printSecretLine();
        for (int i = TRIES; i > 0; i--)
            printTryLine(i);
        //clearRect(BAR_LINE, COLS+1, MasterMind.MAX_COLORS+2, 3, BROWN);
        //To print Rect with Colors
        printRectColors(BAR_LINE, COLS+1, MasterMind.MAX_COLORS+2, 3, NO_COLOR, MasterMind.pinNum, MasterMind.currentTry);
    }

    private static void printTryLine(int n) {
        cursor(LINES-n*2,1);
        color(WHITE, BROWN);
        if (n<10) print(' ');
        print(n + ":");
        color(WHITE, DARK_GRAY);
        printRepeat(' ', KEY_LEN * 2 + 1);
        setBackground(BROWN);
        print(' ');
        setBackground(DARK_GRAY);
        printRepeat('o', KEY_LEN);
        printTryPins(n, n==1 ? 0 : NO_PIN, null);
    }

    public static void printSecretLine() {
        color(BLACK, WHITE);
        for (int i = 0; i < KEY_LEN; i++) {
            cursor(1, NUM_SPACES +1+i*2);
            print('?');
        }
    }

    private static void printRepeat(char c, int times) {
        for (; times>0 ; times--) print(c);
    }

    private static void printRepeatWithColors(int line, int times, int color, int pinSelected, int[] currentTry) {
        for (; times>0 ; times--) {
            if (times == 2) {
                setBackground((line == 1 && color == NO_COLOR) || (line == color+1) ? LIGHT_GRAY : BROWN);
                if(isColorFilled(currentTry, line))
                {
                    setForeground(WHITE);
                    print('O');
                }
                else
                {
                    setForeground(COLORS[line - 1]);
                    print(PIN);
                }
            } else {
                setBackground(BROWN);
                print(' ');
            }
        }
    }

    public static boolean isColorFilled(int[] currentTry, int line)
    {
        boolean result = false;
        for(int i=0;i<currentTry.length;i++)
        {
            if(currentTry[i] != NO_COLOR && currentTry[i] == line-1) result=true;
        }
        return result;
    }

    public static final int NO_COLOR = -1;
    public static final int NO_PIN = -1;

    /**
     * Print pins of current try
     * @param tryNum Number of try line to put pins. [1..MAX_TRIES]
     * @param pinSelected Number of pin selected. [0..KEYLEN-1] or -1(NO_PIN)
     * @param pinColors Array of pin colors. [0..7] or -1(NO_COLOR) if no pin put
     */
    public static void printTryPins(int tryNum, int pinSelected, int[] pinColors) {
        for (int i = 0; i < KEY_LEN; i++) {
            cursor(LINES - tryNum * 2, NUM_SPACES + 1 + i * 2);
            setBackground(pinSelected == i ? LIGHT_GRAY : DARK_GRAY);
            if (pinColors == null || pinColors[i] == NO_COLOR) {
                setForeground(WHITE);
                print('O');
            } else {
                setForeground(COLORS[pinColors[i]]);
                print(PIN);
            }
        }
    }

    /**
     * Show the secret key
     * @param secretKey Array of pin colors [0..MAX_COLORS]
     */
    public static void showKey(int[] secretKey) {
        for (int i = 0; i < KEY_LEN; i++) {
            cursor(1, NUM_SPACES + 1 + i * 2);
            color(COLORS[secretKey[i]],BLACK);
            print(PIN);
        }
    }

    /**
     * Print result pins (white pins - exacts, black pins - swaps)
     * @param tryNum Number of try line to put result pins. [1..MAX_TRIES]
     * @param exact Number of white pins to put. [0..KEY_LEN]
     * @param swap Number of black pins to put. [0..KEY_LEN]
     */
    public static void printResult(int tryNum, int exact, int swap) {
        setBackground(DARK_GRAY);
        for (int i=0 ; exact>0 || swap>0 ; i++) {
            cursor(LINES - tryNum * 2, NUM_SPACES + KEY_LEN * 2 + 2 +i);
            if (exact>0) {
                setForeground(WHITE);
                --exact;
            } else {
                setForeground(BLACK);
                --swap;
            }
            print(PIN);
        }
    }

    /**
     * Write an equal if the colors can be equals.
     * Write a different if the colors must be different.
     * @param eq if colors can be repeated
     */
    public static void printEqualColors(boolean eq) {
        color(WHITE,BROWN);
        cursor(1, NUM_SPACES +KEY_LEN*3+1);
        print(eq?EQUALS:DIFFERENT);
    }

    private static final int LINE_MESSAGE = 1;
    private static final int COL_MESSAGE = COLS + 2;

    private static void clearArea() {
        clearRect(LINE_MESSAGE,COL_MESSAGE-1,7,23,BLACK);
    }

    public static void printLegend() {
        clearArea();
        color(WHITE,BLACK);
        legend(1," \u2BC7 \u2BC8 ","Select pin");
        legend(2," \u2BC5 \u2BC6 ","Select color");
        legend(3,"Space","Put pin");
        legend(4," Del ","Remove pin");
        legend(5,"Enter","Next move");
        legend(6,"   G ","New game");
        legend(7," Esc ","Exit");
    }

    private static void legend(int line, String keys, String msg) {
        setForeground(YELLOW);
        cursor(line,COLS+1);
        print(keys);
        cursor(line,COLS+6);
        setForeground(WHITE);
        print(" - "+msg);
    }

    private static void printMessage(String msg) {
        int key;
        while ((key=Console.getKeyPressed())!=Console.NO_KEY) {
            if (key >= 0) Console.waitKeyReleased(key);
            else Console.getMouseEvent();
        }
        clearArea();
        setForeground(YELLOW);
        String[] lines = msg.split(";");
        for (int l = 0; l < lines.length; l++) {
            cursor(LINE_MESSAGE + l, COL_MESSAGE);
            print(lines[l]);
        }
        cursor(LINE_MESSAGE+lines.length,COL_MESSAGE);
    }

    /**
     * Write a messsage in legend area and waits a confirmation.
     * Wait for the user to type 'Y' or 'N' and return true if is 'Y'.
     * @param msg The message to write. Each ';' forces a change of line.
     * @return true if the response is 'Y'
     */
    public static boolean confirm(String msg) {
        printMessage(msg);
        setForeground(WHITE);
        print("Y/N ? ");
        cursor(true);
        Console.clearAllChars();
        int key;
        do {
            key = Console.waitKeyPressed(0);
            Console.waitKeyReleased(key);
        } while ("yYnN".indexOf(key)<0);
        cursor(false);
        printLegend();
        return key=='Y' || key=='y';
    }

    /**
     * Write a messsage in legend area.
     * The message remains for 4 seconds or until a key is pressed.
     * @param msg The message to write. Each ';' forces a change of line.
     */
    public static void message(String msg) {
        printMessage(msg);
        int key;
        do {
            key = Console.waitKeyPressed(4000);
            if (key >= 0) Console.waitKeyReleased(key);
            Console.getMouseEvent();
        } while(key == MOUSE_EVENT );
        printLegend();
    }

    /**
     * Write a messsage in legend area and waits a user input.
     * Wait for the user to type text until enter.
     * @param msg The message to write. Each ';' forces a change of line.
     * @param maxField Maximum length of string readed.
     * @return The text readed or null if none.
     */
    public static String read(String msg, int maxField) {
        printMessage(msg);
        Console.clearAllChars();
        color(WHITE,DARK_GRAY);
        String value = Console.nextLine(maxField);
        printLegend();
        return value;
    }

}
