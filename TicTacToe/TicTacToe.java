import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class TicTacToe {
    private JPanel panel1;

    private JFrame frameReference;

    private JButton[][] buttons;
    private int[][] table;
    private int clickCounter;

    private int tempi, tempj;
    private boolean turn;
    private boolean win;

    private int boardSize;

    private static final int  WINDOW_SIZE = 1000;

    public TicTacToe(){
//        InputHandler input = new InputHandler();
//        System.out.println(input.getValue());
//        new TicTacToe(input.getValue());
        new TicTacToe(3);
    }

    private TicTacToe(int boardSize){
        turn = true;
        win = false;
        clickCounter = 0;

        initializeButtons2(boardSize);
    }

    private void initializeButtons2(int boardSize){
        this.boardSize = boardSize;
        panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.PAGE_AXIS));
        table = new int[boardSize][boardSize];
        buttons = new JButton[boardSize][boardSize];

        for(int i=0; i<boardSize; i++){
            JPanel row = new JPanel();
//            row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
            for(int j=0; j<boardSize; j++){
                tempi = i;
                tempj = j;
//                JPanel wrapper = new JPanel();
                JButton button = new JButton();
                int buttonSize = WINDOW_SIZE / boardSize;
                button.setPreferredSize(new Dimension(buttonSize - 20,buttonSize - 20));
                button.addActionListener(new ActionListener() {
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
                table[i][j] = 0;
                buttons[i][j] = button;
//                wrapper.add(button);
//                row.add(wrapper);
                row.add(button);
            }
            panel1.add(row);
        }
    }

//    private void multiplayerAction(int i, int j){
//        buttons[i][j].setBackground(turn ? Color.GREEN : Color.RED);
//        buttons[i][j].setEnabled(false);
//        table[i][j] = turn ? 1 : 10;
//        turn = !turn;
//        clickCounter++;
//        checkMultiplayerWinner();
//    }

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
        if(boardSize == 3){
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
        }

        Random rand = new Random();
        int i = rand.nextInt(0,boardSize);
        int j = rand.nextInt(0,boardSize);
        while(table[i][j] != 0){
            i = rand.nextInt(0,boardSize);
            j = rand.nextInt(0,boardSize);
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
        int missingOne = (boardSize*10) - 10;
        for(int i=0; i<boardSize; i++) {
            int rowSum = 0;
            int colSum = 0;
            for (int j = 0; j < boardSize; j++) {
                rowSum += table[i][j];
                colSum += table[j][i];
            }

            if (rowSum == missingOne) {
                botAction(i, getFreeCellIndexInRow(i));
                return true;
            }
            if (colSum == missingOne) {
                botAction(getFreeCellIndexInColumn(i), i);
                return true;
            }
        }
        int[] cross = getCrossSums();
        if(cross[0] == missingOne){
            int[] temp = getCrossLFreeIndex();
            botAction(temp[0], temp[1]);
            return true;
        }
        if(cross[1] == missingOne){
            int[] temp = getCrossRFreeIndex();
            botAction(temp[0], temp[1]);
            return true;
        }

        return false;
    }

    private boolean botDefend(){
        int missingOne = boardSize - 1;
        for(int i=0; i<boardSize; i++) {
            int rowSum = 0;
            int colSum = 0;
            for (int j = 0; j < boardSize; j++) {
                rowSum += table[i][j];
                colSum += table[j][i];
            }

            if (rowSum == missingOne) {
                botAction(i, getFreeCellIndexInRow(i));
                return true;
            }
            if (colSum == missingOne) {
                botAction(getFreeCellIndexInColumn(i), i);
                return true;
            }
        }
        int[] cross = getCrossSums();
        if(cross[0] == missingOne){
            int[] temp = getCrossLFreeIndex();
            botAction(temp[0], temp[1]);
            return true;
        }
        if(cross[1] == missingOne){
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
        int crossLSum = 0;
        for(int i=0; i<boardSize; i++){
            crossLSum+=table[i][i];
        }
        int crossRSum = 0;
        int j=boardSize-1;
        for(int i=0; i<boardSize; i++){
            crossRSum+=table[i][j--];
        }
        return new int[]{crossLSum, crossRSum};
    }
    private int[] getCrossLFreeIndex(){
        for(int i=0; i<boardSize; i++){
            if(table[i][i] == 0){
                return new int[]{i, i};
            }
        }
        return null;
    }

    private int[] getCrossRFreeIndex(){
        int j=boardSize-1;
        for(int i=0; i<boardSize; i++) {
            System.out.println(j);
            if (table[i][j] == 0) {
                return new int[]{i, j};
            }
            j--;
        }
        return null;
    }

    private int getFreeCellIndexInRow(int row){
        for(int i=0; i<boardSize; i++){
            if(table[row][i] == 0){
                return i;
            }
        }
        return -1;
    }

    private int getFreeCellIndexInColumn(int column){
        for(int i=0; i<boardSize; i++){
            if(table[i][column] == 0){
                return i;
            }
        }
        return -1;
    }

    private void checkSingleplayerWinner(){
        Alert alert = null;
        switch (checkTurn()){
            case 0:
                if(clickCounter>=boardSize*boardSize){
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
            playAgain(alert.getPlayAgain());
        }
    }

//    private void checkMultiplayerWinner(){
//        Alert alert = null;
//        switch (checkTurn()){
//            case 0:
//                if(clickCounter>=boardSize*boardSize){
//                    alert = new Alert("Tie!");
//                }
//                break;
//            case 1:
//                alert = new Alert("Green wins!");
//                disableAllButtons();
//                break;
//            case 2:
//                alert = new Alert("Red wins!");
//                disableAllButtons();
//                break;
//        }
//        if(alert != null){
//            playAgain(alert.getPlayAgain(), "multiplayer");
//        }
//    }

    private int checkTurn(){
        //check horizontally and vertically:
        for(int i=0; i<boardSize; i++){
            int rowSum = 0;
            int collSum = 0;
            for(int j=0; j<boardSize; j++){
                rowSum += table[i][j];
                collSum += table[j][i];
            }
            if(rowSum == boardSize || collSum == boardSize){
                return 1;
            }
            if(rowSum == boardSize*10 || collSum == boardSize*10){
                return 2;
            }
        }
        //check cross:
        int[] crossSums = getCrossSums();
        int crossL = crossSums[0];
        int crossR = crossSums[1];
        if(crossL == boardSize || crossR == boardSize){
            return 1;
        }
        if(crossL == boardSize*10 || crossR == boardSize*10){
            return 2;
        }
        return 0;
    }

    private void disableAllButtons(){
        for(int i=0; i<boardSize; i++){
            for(int j=0; j<boardSize; j++){
                buttons[i][j].setEnabled(false);
            }
        }
    }

    public void setFrameReference(JFrame frameReference){
        this.frameReference = frameReference;
    }

    private void playAgain(boolean playAgain){
        if(playAgain){
            this.frameReference.dispose();
            setGame(boardSize);
        }
    }

    public static void setGame(int boardSize){
        JFrame frame = new JFrame("TicTacToe");
        TicTacToe game = new TicTacToe(boardSize);
        game.setFrameReference(frame);
        frame.setContentPane(game.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(WINDOW_SIZE,WINDOW_SIZE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void printTable(){
        for(int i=0; i<boardSize; i++){
            for(int j=0; j<boardSize; j++){
                System.out.print(table[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        InputHandler input = new InputHandler();
//        TicTacToe.setGame(3);
        TicTacToe.setGame(input.getValue());
    }
}
