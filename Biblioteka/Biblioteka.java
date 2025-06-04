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
    private JLabel Label1;
    private JLabel Label2;
    private JLabel Label3;

    private ArrayList<Ksiazka> ksiazki;
    private ArrayList<Czytelnik> czytelnicy;

    private int currentModel; // 0 - ksiazki | 1 - czytelnicy

    public Biblioteka(){

//      ||=================================Tabela=================================||
        ksiazki = new ArrayList<>();
        czytelnicy = new ArrayList<>();

        DefaultTableModel modelKsiazek = new DefaultTableModel(new Object[]{"ID", "Tytuł", "Autor", "Rok", "Status"}, 0);
        DefaultTableModel modelCzytelnikow = new DefaultTableModel(new Object[]{"ID", "Imie Nazwisko", "Wiek", "Wypożyczone książki"}, 0);

        table1.setModel(modelKsiazek);
        currentModel = 0;

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
                if(wczytajZPliku(fc.getSelectedFile())){
                    Label1.setText("Tytuł:");
                    Label2.setText("Autor:");
                    Label3.setText("Rok:");
                    table1.setModel(modelKsiazek);
                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    for(int i=0; i<ksiazki.size(); i++){
                        model.addRow(new Object[]{ksiazki.get(i).id, ksiazki.get(i).tytul, ksiazki.get(i).autor, ksiazki.get(i).rok, ksiazki.get(i).status ? "dostępny" : "wypożyczony"});
                    }
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

//      ------------------------------------Menu 2--------------------------------------------

        JMenu menu2 = new JMenu("Zakładki");
        menuItem = new JMenuItem("Książki");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTable(table1);
                
                Label1.setText("Tytuł:");
                Label2.setText("Autor:");
                Label3.setText("Rok:");
                
                table1.setModel(modelKsiazek);
                currentModel = 0;

                for(int i=0; i<ksiazki.size(); i++){
                    Ksiazka kTemp = ksiazki.get(i);
                    modelKsiazek.addRow(new Object[]{kTemp.id, kTemp.tytul, kTemp.autor, kTemp.rok, kTemp.status ? "dostępna":"wypożyczona"});
                }
            }
        });
        menu2.add(menuItem);

        menuItem = new JMenuItem("Czytelnicy");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTable(table1);

                Label1.setText("Imie:");
                Label2.setText("Nazwisko:");
                Label3.setText("Wiek:");

                table1.setModel(modelCzytelnikow);
                currentModel = 1;

                for(int i=0; i< czytelnicy.size(); i++){
                    Czytelnik cTemp = czytelnicy.get(i);
                    modelCzytelnikow.addRow(new Object[]{cTemp.id, cTemp.imie_nazwisko, cTemp.wiek, cTemp.wypozyczoneKsiazki.toString().substring(1, cTemp.wypozyczoneKsiazki.toString().length()-1)});
                }
            }
        });
        menu2.add(menuItem);

        menuBar.add(menu2);

        frame.setJMenuBar(menuBar);
//      ||=================================Dodawanie Książek=================================||

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!Tytul.getText().isEmpty() && !Autor.getText().isEmpty() && !Rok.getText().isEmpty()){
                    switch(currentModel){
                        case 0:
                            int nextID = 0;
                            if(!ksiazki.isEmpty()){
                                nextID = ksiazki.get(ksiazki.size()-1).id+1;
                            }
                            Ksiazka temp = new Ksiazka(nextID, Tytul.getText(), Autor.getText(), Rok.getText(), true);
                            ksiazki.add(temp);
                            DefaultTableModel model = (DefaultTableModel) table1.getModel();
                            model.addRow(new Object[]{nextID, Tytul.getText(), Autor.getText(), Rok.getText(), "dostępny"});
                            break;
                        case 1:
                            nextID=0;
                            if(!czytelnicy.isEmpty()){
                                nextID = czytelnicy.get(czytelnicy.size()-1).id+1;
                            }
                            Czytelnik cTemp = new Czytelnik(nextID, Tytul.getText()+" "+Autor.getText(), Integer.parseInt(Rok.getText()));
                            czytelnicy.add(cTemp);
                            DefaultTableModel model2 = (DefaultTableModel) table1.getModel();
                            model2.addRow(new Object[]{nextID, Tytul.getText()+" "+Autor.getText(), Rok.getText(), ""});
                            break;
                        default:
                            new ErrorDialog("currentModel is out of bounds!");
                            break;
                    }
                }else{
                    new ErrorDialog("Występują puste pola!");
                }
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(table1.getSelectedRow() != -1){
                    switch(currentModel){
                        case 0:
                            ksiazki.remove(table1.convertRowIndexToModel(table1.getSelectedRow()));
                            DefaultTableModel model = (DefaultTableModel) table1.getModel();
                            model.removeRow(table1.convertRowIndexToModel(table1.getSelectedRow()));
                            break;
                        case 1:
                            czytelnicy.remove(table1.convertRowIndexToModel(table1.getSelectedRow()));
                            DefaultTableModel model2 = (DefaultTableModel) table1.getModel();
                            model2.removeRow(table1.convertRowIndexToModel(table1.getSelectedRow()));
                            break;
                    }
                }else{
                    new ErrorDialog("Nie wybrano rekordu!");
                }
            }
        });

        rent_return.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(table1.getSelectedRow() != -1){
                    ksiazki.get(table1.convertRowIndexToModel(table1.getSelectedRow())).changeStatus();
                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    model.setValueAt(ksiazki.get(table1.convertRowIndexToModel(table1.getSelectedRow())).status ? "dostępna" : "wypożyczona", table1.convertRowIndexToModel(table1.getSelectedRow()), 4);
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

    private void clearTable(JTable table){
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        for(int i=model.getRowCount()-1; i>=0; i--){
            model.removeRow(i);
        }
    }

    private boolean wczytajZPliku(File file){
        try {
            Scanner sc = new Scanner(new File(file.getParent()+"/Ksiazki.csv"));

            while(sc.hasNextLine()){
                String[] line = sc.nextLine().split(";");
                boolean status;
                if(line[4].equals("true")){
                    status = true;
                }else{
                    status = false;
                }
                this.ksiazki.add(new Ksiazka(Integer.parseInt(line[0]), line[1], line[2],line[3], status));
            }

            sc = new Scanner(new File(file.getParent()+"/Czytelnicy.csv"));

            while(sc.hasNextLine()){
                String[] line = sc.nextLine().split(";");

                ArrayList<Integer> wypozyczone = new ArrayList<>();

                line[3] = line[3].substring(1, line[3].length()-1);
                if(!line[3].isBlank()){
                    String[] tab = line[3].split(",");
                    for(String t : tab){
                        wypozyczone.add(Integer.parseInt(t));
                    }
                }

                this.czytelnicy.add(new Czytelnik(Integer.parseInt(line[0]), line[1], Integer.parseInt(line[2]), wypozyczone));

            }
            return true;
        }catch(FileNotFoundException e){
            new ErrorDialog("Nie znaleziono pliku!");
            return false;
        }
    }

    private void zapiszDoPliku(File file){
        try{
            PrintWriter pw = new PrintWriter(file.getParent()+"/Ksiazki.csv");
            for(int i=0; i<ksiazki.size(); i++){
                pw.println(ksiazki.get(i).toString());
            }
            pw.close();
            pw = new PrintWriter(file.getParent()+"/Czytelnicy.csv");
            for(Czytelnik c : czytelnicy){
                pw.println(c.toString());
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
