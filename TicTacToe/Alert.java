import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Alert extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel Message;
    private JButton NOButton;
    private boolean playAgain;

    public Alert(String message) {
        Message.setText(message);
        initialize(message.length()*30 + 10);
    }

    private void initialize(int width) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onYES();
            }
        });

        NOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNO();
            }
        });

        setSize(width, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public boolean getPlayAgain(){
        return this.playAgain;
    }

    private void onYES() {
        // add your code here
        playAgain = true;
        dispose();
    }

    private void onNO(){
        playAgain = false;
        dispose();
    }
}
