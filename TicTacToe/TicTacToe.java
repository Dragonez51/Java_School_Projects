import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class TicTacToe {
    private JPanel panel1;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    private JButton button8;
    private JButton button9;

    private JFrame frameReference;

    private JButton[][] buttons;
    private int[][] table;
    private int clickCounter;

    private int tempi, tempj;
    private boolean turn;
    private boolean win;

    public TicTacToe(String gameMode){
        turn = true;
        win = false;
        clickCounter = 0;
        buttons = new JButton[][]{{button1, button2, button3},{button4, button5, button6},{button7, button8, button9}};

        table = new int[][]{
                {0,0,0},
                {0,0,0},
                {0,0,0},
        };

        initializeButtons(gameMode);
    }

    private void initializeButtons(String mode){
        String modeLowerCase = mode.toLowerCase();
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                tempi = i;
                tempj = j;
                switch(modeLowerCase){
                    case "multiplayer":
                        buttons[i][j].addActionListener(new ActionListener() {
                            int temp2i = tempi;
                            int temp2j = tempj;
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                multiplayerAction(temp2i, temp2j);
                            }
                        });
                        break;
                    case "singleplayer":
                        buttons[i][j].addActionListener(new ActionListener() {
                            int temp2i = tempi;
                            int temp2j = tempj;
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                playerAction(temp2i, temp2j);
                                if(win){
                                    return;
                                }
                                botTurn();
                            }
                        });
                        break;
                }
            }
        }
    }

    private void multiplayerAction(int i, int j){
        buttons[i][j].setBackground(turn ? Color.GREEN : Color.RED);
        buttons[i][j].setEnabled(false);
        table[i][j] = turn ? 1 : 10;
        turn = !turn;
        clickCounter++;
        checkMultiplayerWinner();
    }

    private void playerAction(int i, int j){
        buttons[i][j].setBackground(Color.GREEN);
        buttons[i][j].setEnabled(false);
        table[i][j] = 1;
        clickCounter+=2;
        checkSingleplayerWinner();
    }

    private void botTurn(){
        if(botAttack()){
           return;
        }
        if(botDefend()){
            return;
        }
        botRandom();
    }

    private void botRandom(){
        if(clickCounter == 2){
            int[] indexes = botCheckFirstMove();
            if(indexes != null){
                botAction(indexes[0], indexes[1]);
                return;
            }
        }
        if(clickCounter == 4){
            int[] indexes = botCheckSecondMove();
            if(indexes != null){
                botAction(indexes[0], indexes[1]);
                return;
            }
        }

        Random rand = new Random();
        int i = rand.nextInt(0,3);
        int j = rand.nextInt(0,3);
        while(table[i][j] != 0){
            i = rand.nextInt(0,3);
            j = rand.nextInt(0,3);
        }
        botAction(i, j);
    }

    private int[] botCheckFirstMove(){
        if(table[1][1] == 1){
            Random rand = new Random();
            int[][] tempCorner = {{0,0}, {0,2}, {0,2}, {2,2}};
            return tempCorner[rand.nextInt(0, tempCorner.length)];
        }
        if(table[0][0] == 1 || table[0][2] == 1 || table[2][0] == 1 || table[2][2] == 1){
            return new int[]{1, 1};
        }

        return null;
    }

    private int[] botCheckSecondMove(){
        if(table[1][1] == 1){
            Random rand = new Random();
            if((table[0][0] + table[2][2]) == 11){
                int[][] corners = new int[][]{{0,2}, {2,0}};
                return corners[rand.nextInt(0,2)];
            }
            if((table[2][0] + table[0][2] == 11)){
                int[][] corners = new int[][]{{0,0},{2,2}};
                return corners[rand.nextInt(0,2)];
            }
        }
        return null;
    }

    private boolean botAttack(){
        for(int i=0; i<3; i++) {
            int rowSum = 0;
            int colSum = 0;
            for (int j = 0; j < 3; j++) {
                rowSum += table[i][j];
                colSum += table[j][i];
            }

            if (rowSum == 20) {
                botAction(i, getFreeCellIndexInRow(i));
                return true;
            }
            if (colSum == 20) {
                botAction(getFreeCellIndexInCollumn(i), i);
                return true;
            }
        }
        int[] cross = getCrossSums();
        if(cross[0] == 20){
            int[] temp = getCrossLFreeIndex();
            botAction(temp[0], temp[1]);
            return true;
        }
        if(cross[1] == 20){
            int[] temp = getCrossRFreeIndex();
            botAction(temp[0], temp[1]);
            return true;
        }

        return false;
    }

    private boolean botDefend(){
        for(int i=0; i<3; i++) {
            int rowSum = 0;
            int colSum = 0;
            for (int j = 0; j < 3; j++) {
                rowSum += table[i][j];
                colSum += table[j][i];
            }

            if (rowSum == 2) {
                botAction(i, getFreeCellIndexInRow(i));
                return true;
            }
            if (colSum == 2) {
                botAction(getFreeCellIndexInCollumn(i), i);
                return true;
            }
        }
        int[] cross = getCrossSums();
        if(cross[0] == 2){
            int[] temp = getCrossLFreeIndex();
            botAction(temp[0], temp[1]);
            return true;
        }
        if(cross[1] == 2){
            int[] temp = getCrossRFreeIndex();
            botAction(temp[0], temp[1]);
            return true;
        }

        return false;
    }

    private void botAction(int i, int j){
        table[i][j] = 10;
        buttons[i][j].setEnabled(false);
        buttons[i][j].setBackground(Color.RED);
        checkSingleplayerWinner();
    }

    private int[] getCrossSums(){
        int[] temp = new int[2];
        temp[0] = table[0][0] + table[1][1] + table[2][2];
        temp[1] = table[0][2] + table[1][1] + table[2][0];
        return temp;
    }
    private int[] getCrossLFreeIndex(){
        for(int i=0; i<3; i++){
            if(table[i][i] == 0){
                return new int[]{i, i};
            }
        }
        return null;
    }

    private int[] getCrossRFreeIndex(){
        for(int i=0; i<3; i++){
            for(int j=2; j>-1; j--){
                if(table[i][j] == 0){
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private int getFreeCellIndexInRow(int row){
        for(int i=0; i<3; i++){
            if(table[row][i] == 0){
                return i;
            }
        }
        return -1;
    }

    private int getFreeCellIndexInCollumn(int collumn){
        for(int i=0; i<3; i++){
            if(table[i][collumn] == 0){
                return i;
            }
        }
        return -1;
    }

    private void checkSingleplayerWinner(){
        Alert alert = null;
        switch (checkTurn()){
            case 0:
                if(clickCounter>=9){
                    alert = new Alert("Tie!");
                    win = true;
                }
                break;
            case 1:
                alert = new Alert("You win!");
                disableAllButtons();
                win = true;
                break;
            case 2:
                alert = new Alert("Computer wins!");
                disableAllButtons();
                win = true;
                break;
        }
        if(alert != null){
            playAgain(alert.getPlayAgain(), "singleplayer");
        }
    }

    private void checkMultiplayerWinner(){
        Alert alert = null;
        switch (checkTurn()){
            case 0:
                if(clickCounter>=9){
                    alert = new Alert("Tie!");
                }
                break;
            case 1:
                alert = new Alert("Green wins!");
                disableAllButtons();
                break;
            case 2:
                alert = new Alert("Red wins!");
                disableAllButtons();
                break;
        }
        if(alert != null){
            playAgain(alert.getPlayAgain(), "multiplayer");
        }
    }

    private int checkTurn(){
        //check horizontally and vertically:
        for(int i=0; i<3; i++){
            int rowSum = 0;
            int collSum = 0;
            for(int j=0; j<3; j++){
                rowSum += table[i][j];
                collSum += table[j][i];
            }
            if(rowSum == 3 || collSum == 3){
                return 1;
            }
            if(rowSum == 30 || collSum == 30){
                return 2;
            }
        }
        //check cross:
        int crossL = table[0][0] + table[1][1] + table[2][2];
        int crossR = table[0][2] + table[1][1] + table[2][0];
        if(crossL == 3 || crossR == 3){
            return 1;
        }
        if(crossL == 30 || crossR == 30){
            return 2;
        }
        return 0;
    }

    private void disableAllButtons(){
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                buttons[i][j].setEnabled(false);
            }
        }
    }

    public void setFrameReference(JFrame frameReference){
        this.frameReference = frameReference;
    }

    private void playAgain(boolean playAgain, String gameMode){
        if(playAgain){
            this.frameReference.dispose();
            setGame(gameMode);
        }
    }

    public static void setGame(String gameMode){
        JFrame frame = new JFrame("TicTacToe");
        TicTacToe game = new TicTacToe(gameMode);
        game.setFrameReference(frame);
        frame.setContentPane(game.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(600,600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        TicTacToe.setGame("singleplayer");
    }
}
