import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Wypozyczalnia {
    private JPanel MainPanel;
    private JTable Tabela;
    private JButton Add;
    private JButton Edit;
    private JButton Delete;
    private JButton Save;
    private JButton Load;
    private JPanel TablePanel;
    private JPanel ButtonPanel;

    private List<Wypozyczenie> Lista;

    public Wypozyczalnia(){
        Lista = new ArrayList<>();

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Imię i nazwisko", "Sprzęt", "Dni", "Stawka", "Koszt"}, 0);
        Tabela.setModel(model);

        Add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dodaj();
            }
        });
    }

    private void refresh(){
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Imię i nazwisko", "Sprzęt", "Dni", "Stawka", "Koszt"}, 0);
        for(int i=0; i<Lista.size(); i++){
            model.addRow(new Object[]{Lista.get(i).getImieNazwisko(), Lista.get(i).getNazwaSprzetu(), Lista.get(i).getLiczbaDni(), Lista.get(i).getStawkaZaDzien(), Lista.get(i).koszt()});
        }
        Tabela.setModel(model);
    }

    private void dodaj(){
        Formularz form = new Formularz();
        Lista.add(form.getWypozyczenie());
        refresh();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Wypozyczalnia");
        frame.setContentPane(new Wypozyczalnia().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
