import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ErrorDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel ErrorMessage;

    public ErrorDialog(){
        new ErrorDialog("Unexpected Error!");
    }

    public ErrorDialog(String errorMessage) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        ErrorMessage.setText(errorMessage);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void onOK() {
        // add your code here
        dispose();
    }
}
