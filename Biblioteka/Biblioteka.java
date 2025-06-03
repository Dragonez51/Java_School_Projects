import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Biblioteka {
    public static JFrame frame;
    private JPanel MainPanel;
    private JPanel Top;
    private JPanel Mid;
    private JPanel Bottom;
    private JButton Clear;
    private JButton Search;
    private JTable table1;
    private JTextField Tytul;
    private JTextField Autor;
    private JTextField Rok;
    private JButton add;
    private JButton rent_return;
    private JButton delete;
    private JTextField SearchBar;

    private ArrayList<Ksiazka> lista;

    public Biblioteka(){

//      ||=================================Tabela=================================||
        lista = new ArrayList<Ksiazka>();

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Tytuł", "Autor", "Rok", "Status"}, 0);

        table1.setModel(model);

//      ||=================================Menu=================================||
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Plik");
        JMenuItem menuItem = new JMenuItem("Otwórz");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new FileNameExtensionFilter("pliki CSV (.csv)", "csv"));
                fc.showOpenDialog(frame);
                lista = wczytajZPliku(fc.getSelectedFile());
                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                for(int i=0; i<lista.size(); i++){
                    model.addRow(new Object[]{lista.get(i).tytul, lista.get(i).autor, lista.get(i).rok, lista.get(i).status ? "dostępny" : "wypożyczony"});
                }
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Zapisz");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new FileNameExtensionFilter("pliki CSV (.csv)", "csv"));
                fc.showSaveDialog(frame);
                zapiszDoPliku(fc.getSelectedFile());
            }
        });
        menu.add(menuItem);
        menu.addSeparator();

        menuItem = new JMenuItem("Zamknij");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Biblioteka.frame.dispose();
            }
        });
        menu.add(menuItem);

        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

//      ||=================================Dodawanie Książek=================================||

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!Tytul.getText().isEmpty() && !Autor.getText().isEmpty() && !Rok.getText().isEmpty()){
                    Ksiazka temp = new Ksiazka(Tytul.getText(), Autor.getText(), Rok.getText(), true);
                    lista.add(temp);
                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    model.addRow(new Object[]{Tytul.getText(), Autor.getText(), Rok.getText(), "dostępny"});
                }else{
                    new ErrorDialog("Występują puste pola!");
                }
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(table1.getSelectedRow() != -1){
                    lista.remove(table1.convertRowIndexToModel(table1.getSelectedRow()));
                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    model.removeRow(table1.convertRowIndexToModel(table1.getSelectedRow()));
                }else{
                    new ErrorDialog("Nie wybrano rekordu!");
                }
            }
        });

        rent_return.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(table1.getSelectedRow() != -1){
                    lista.get(table1.convertRowIndexToModel(table1.getSelectedRow())).changeStatus();
                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    model.setValueAt(lista.get(table1.convertRowIndexToModel(table1.getSelectedRow())).status ? "dostępna" : "wypożyczona", table1.convertRowIndexToModel(table1.getSelectedRow()), 3);
//                    table1.getRowSorter()
                }else{
                    new ErrorDialog("Nie wybrano rekordu!");
                }
            }
        });

        Search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(((DefaultTableModel) table1.getModel()));
                String regex = SearchBar.getText();

                regex = regex.replace("?", "[a-zA-Z0-9 -/]?");
                regex = regex.replace("*", "[a-zA-Z0-9 -/]*");

                if(SearchBar.getText().charAt(0)!='*'){
                    regex="^"+regex;
                }
                if(SearchBar.getText().charAt(SearchBar.getText().length()-1)!='*'){
                    regex+="&";
                }

                System.out.println(regex);

                sorter.setRowFilter(RowFilter.regexFilter(regex));

                table1.setRowSorter(sorter);
            }
        });

        Clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchBar.setText("");

                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(((DefaultTableModel) table1.getModel()));
                sorter.setRowFilter(RowFilter.regexFilter(SearchBar.getText()));

                table1.setRowSorter(sorter);
            }
        });
    }

    public ArrayList<Ksiazka> wczytajZPliku(File file){
        try {
            ArrayList<Ksiazka> lista = new ArrayList<>();
            Scanner sc = new Scanner(file);

            while(sc.hasNextLine()){
                String[] line = sc.nextLine().split(";");
                if(line[3].equals("true")){
                    lista.add(new Ksiazka(line[0], line[1], line[2], true));
                }else{
                    lista.add(new Ksiazka(line[0], line[1], line[2], false));
                }
            }
            return lista;

        }catch(FileNotFoundException e){
            new ErrorDialog("Nie znaleziono pliku!");
        }

        return null;
    }

    public void zapiszDoPliku(File file){
        try{
            PrintWriter pw = new PrintWriter(file+".csv");
            for(int i=0; i<lista.size(); i++){
                pw.println(lista.get(i).toString());
            }
            pw.close();

        }catch(FileNotFoundException e){
            try{
                file.createNewFile();
                zapiszDoPliku(file);
            }catch(IOException io){
                new ErrorDialog("Unexpected error!");
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Biblioteka");
        Biblioteka.frame = frame;
        frame.setContentPane(new Biblioteka().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
