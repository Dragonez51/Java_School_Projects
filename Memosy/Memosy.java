import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Memosy {
    public static JFrame frame;
    private static final int WINDOW_SIZE = 600;
    private int[][] values;
    private int selectedY = -1;
    private int selectedX;
    private int clickCount = 0;
    private int elapsedSeconds = 0;
    private int guessedPairs = 0;
    private int pairs;

    private Image[] icons;

    private JPanel mainPanel;

    private JButton[][] buttons;

    private JLabel clickCounter;
    private JLabel elapsedTimer;

    private ArrayList<Integer> tempList;

    private Timer timer;
    private Timer globalTimer;

    public Memosy(){
        initIcons();
        initArrayList(4);
        initializeLayout(4);
        setTimer();
        startTimer();
    }

    private void initIcons(){
        int size = 64;
        try{
            icons = new Image[]{
                    ImageIO.read(new File("src/arr.png")).getScaledInstance(size,size,Image.SCALE_SMOOTH),
                    ImageIO.read(new File("src/bok.png")).getScaledInstance(size,size,Image.SCALE_SMOOTH),
                    ImageIO.read(new File("src/kng.png")).getScaledInstance(size,size,Image.SCALE_SMOOTH),
                    ImageIO.read(new File("src/ogr.png")).getScaledInstance(size,size,Image.SCALE_SMOOTH),
                    ImageIO.read(new File("src/pot.jpg")).getScaledInstance(size,size,Image.SCALE_SMOOTH),
                    ImageIO.read(new File("src/skl.png")).getScaledInstance(size,size,Image.SCALE_SMOOTH),
                    ImageIO.read(new File("src/quest.png")).getScaledInstance(size,size,Image.SCALE_SMOOTH),
                    ImageIO.read(new File("src/wep.png")).getScaledInstance(size,size,Image.SCALE_SMOOTH),
            };
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void initArrayList(int size){
        this.tempList = new ArrayList<>();
        int listSize = (size*size)/2;
        pairs = listSize;
        for(int i=0; i<listSize; i++){
            for(int j=0; j<2; j++){
                tempList.add(i);
            }
        }
    }

    private void initializeLayout(int size){
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        buttons = new JButton[size][size];
        values = new int[size][size];

        JPanel menuPanel = new JPanel();
        JPanel cardsPanel = new JPanel();

        Random rand = new Random();
        for(int i=0; i<size; i++){
            JPanel row = new JPanel();

            for(int j=0; j<size; j++){
                int tempI = i;
                int tempJ = j;

                int randIndex = rand.nextInt(tempList.size());
                JButton button = new JButton();
                values[i][j] = tempList.get(randIndex);
                tempList.remove(randIndex);

                int buttonSize = WINDOW_SIZE / size;
                button.setPreferredSize(new Dimension(buttonSize - 20,buttonSize - 20));

                button.addActionListener(new ActionListener() {
                    int y = tempI;
                    int x = tempJ;
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        buttons[y][x].setIcon(new ImageIcon(icons[values[y][x]]));
                        updateClickCounter();
                        checkMatch(y, x);
                        checkWin();
                    }
                });

                buttons[i][j] = button;
                row.add(button);
            }
            cardsPanel.add(row);
        }
        clickCounter = new JLabel("Liczba kliknięć: 0");
        menuPanel.add(clickCounter);
        elapsedTimer = new JLabel("Czas: 0s");
        menuPanel.add(elapsedTimer);
        menuPanel.setMaximumSize(new Dimension(WINDOW_SIZE, 150));
        cardsPanel.setMaximumSize(new Dimension(WINDOW_SIZE, 700));

        mainPanel.add(menuPanel);
        mainPanel.add(cardsPanel);
    }

    private void checkWin(){
        if(guessedPairs == pairs){
            stopTimer();
        }
    }

    private void checkMatch(int y, int x){
        if(selectedY == -1 || (selectedY == y && selectedX == x)){
            selectedY = y;
            selectedX = x;
            return;
        }

        if(values[selectedY][selectedX] == values[y][x]){
            buttons[selectedY][selectedX].setEnabled(false);
            buttons[y][x].setEnabled(false);
            selectedY = -1;
            selectedX = -1;
            guessedPairs++;
        }else {
            ActionListener taskPerformer = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttons[selectedY][selectedX].setIcon(null);
                    buttons[y][x].setIcon(null);
                    timer.stop();
                    selectedY = -1;
                    selectedX = -1;
                    Memosy.frame.setEnabled(true);
                }
            };
            timer = new Timer(1000, taskPerformer);
            timer.start();
            Memosy.frame.setEnabled(false);
        }
    }

    private void updateClickCounter(){
        clickCount++;
        clickCounter.setText("Liczba kliknięć: "+clickCount);
    }

    private void setTimer(){
        ActionListener update = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedSeconds++;
                elapsedTimer.setText("Czas: "+elapsedSeconds+"s");
            }
        };
        globalTimer = new Timer(1000, update);
    }

    private void startTimer(){
        globalTimer.start();
    }

    private void stopTimer(){
        globalTimer.stop();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Memosy");
        frame.setContentPane(new Memosy().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(WINDOW_SIZE, WINDOW_SIZE+200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Memosy.frame = frame;
    }
}