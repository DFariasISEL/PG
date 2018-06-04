package com.isel.pg;

import isel.leic.pg.Console;

import java.io.*;
import java.util.Arrays;

public class TopScore {
    public static final int MAX_SCORES = 8;     // Maximum of lines of score
    public static final int LINE = Panel.BAR_LINE, COL = Panel.COLS+5;  // View position.
    public static final int MAX_LINE_COLS = 16; // Maximum width of each line
    public static final int COLS = MAX_LINE_COLS + 2;

    private static Score[] top = new Score[MAX_SCORES]; // Top scores stored
    private static int size = 0;    // Number of scores stored
    private static int positionToAddInTop = 0;

    /**
     * Print border frame, title and header of columns (Name, Number of tries, minutes)
     */
    public static void printFrame() {
        Console.color(Console.GRAY, Console.BLACK);
        Panel.clearRect(LINE, COL, MAX_SCORES + 3, COLS, Console.LIGHT_GRAY);
        Console.color(Console.CYAN, Console.BLACK);
        Console.cursor(LINE, COL + 4);
        Console.print("TOP SCORE");         // Title
        Console.cursor(LINE + 1, COL + 1);
        Console.color(Console.WHITE,Console.DARK_GRAY);
        Console.print("Name       n min");  // Header line
    }

    /**
     * Print lines of top score
     * ATENÇÃO: Não devem alterar este método
     */
    public static void printScores() {
        Console.color(Console.BLACK, Console.LIGHT_GRAY);
        for(int i=0 ; i< size; ++i) {
            Console.cursor(LINE+2+i,COL+1);
            Console.print( top[i].score );
        }
    }

    // Name of text file to store top score
    // O conteúdo do ficheiro pode ser observado e alterado com qualquer editor de texto.
    private static final String FILE_NAME = "TopScore.txt";

    /**
     * Save top score in file
     * ATENÇÃO: Este método usa código com conceitos que só serão tratados em POO.
     *          Não devem alterar este método
     */
    public static void save() {
        try (PrintStream out = new PrintStream(FILE_NAME)) {
            for (int i = 0; i < size; i++)
                out.println(top[i].toString());
        } catch (IOException e) {
            System.out.println("Error saving file "+FILE_NAME+"\n"+e);
        }
    }

    /**
     * Load top score from file
     * ATENÇÃO: Este método usa código com conceitos que só serão tratados em POO.
     *          Não devem alterar este método
     */
    public static void load() {
        String line = null;
        try (BufferedReader in = new BufferedReader(new FileReader(FILE_NAME))) {
            String name;
            int minutes, tries, idxBar, idxSpace;
            for(size=0 ; (line=in.readLine()) !=null ; ++size) {
                idxBar = line.indexOf('-');
                name = line.substring(0,idxBar).trim();
                int idx = idxBar+1;
                while(line.charAt(idx)==' ') ++idx;
                idxSpace = line.indexOf(' ',idx+1);
                tries = Integer.parseInt(line.substring(idx,idxSpace));
                minutes = Integer.parseInt(line.substring(idxSpace+1).trim());
                top[size] = new Score(name,tries,minutes);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File "+FILE_NAME+" not found. Top score empty");
            size = 0;
        } catch (Exception e) {
            System.out.println("Error loading file " + FILE_NAME + "\n in line: " + line);
        }
    }


    public static void addScore(Score s){
        for(int i = top.length -1; i > positionToAddInTop; i--){
            top[i] = top[i - 1];
        }
        top[positionToAddInTop] = s;
    }

    public static boolean checkResult(int numTries, int minutes){
       boolean toAdd = false;
       positionToAddInTop = 0;
       while(positionToAddInTop < top.length){
           if(numTries <= top[positionToAddInTop].tries && minutes < top[positionToAddInTop].minutes){
               toAdd = true;
               break;
           }
        positionToAddInTop++;
       }
       return toAdd;
    }

}
