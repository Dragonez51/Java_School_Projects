import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputHandler extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JSlider slider1;
    private JLabel SliderValue;
    private int value;

    public InputHandler() {
        initialize();
    }

    private void initialize(){
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                SliderValue.setText(String.valueOf(slider1.getValue()));
            }
        });

        setSize(300,150);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void onOK() {
        // add your code here
        value = slider1.getValue();
        dispose();
    }

    public int getValue(){
        return value;
    }
}
