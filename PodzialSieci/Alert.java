import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Alert extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel Message;

    public Alert(String message) {
        Message.setText(message);
        initialize(message.length()*8);
    }

    private void initialize(int width){
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        pack();
        setSize(width, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void onOK() {
        // add your code here
        dispose();
    }
}
