import javax.swing.*;
import java.awt.event.*;

public class Formularz extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField Imie;
    private JTextField Sprzet;
    private JTextField Dni;
    private JTextField Stawka;
    private Wypozyczenie wypozyczenie;

    public Formularz() {
        initialize();
    }

    public Formularz(Wypozyczenie wypozyczenie){
        initialize();
        this.Imie.setText(wypozyczenie.getImieNazwisko());
        this.Sprzet.setText(wypozyczenie.getNazwaSprzetu());
        this.Dni.setText(String.valueOf(wypozyczenie.getLiczbaDni()));
        this.Stawka.setText(String.valueOf(wypozyczenie.getStawkaZaDzien()));
    }

    private void onOK() {
        // add your code here
        this.wypozyczenie = new Wypozyczenie(this.Imie.getText(), this.Sprzet.getText(), Integer.valueOf(this.Dni.getText()), Double.valueOf(this.Stawka.getText()));
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public Wypozyczenie getWypozyczenie(){
        return this.wypozyczenie;
    }

    private void initialize(){
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        pack();
        setSize(400, 250);
        setLocationRelativeTo(null);
        setModal(true);
        setContentPane(contentPane);
        setVisible(true);
    }
}
