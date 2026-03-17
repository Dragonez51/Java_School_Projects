import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:mysql://localhost:3306/javabiblioteka";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Połączono z bazą danych!");

            boolean end = false;
            while(!end){
                System.out.println("\nMenu: \n1. Wyświetl wszystkie rekordy \n2. Dodaj książkę \n3. Usuń książkę");
                Scanner sc = new Scanner(System.in);
                switch(sc.nextLine()){
                    case "1":
                        wypisz(connection);
                        break;
                    case "2":
                        dodaj(connection);
                        break;
                    case "3":
                        usun(connection);
                        break;
                    default:
                        end = true;
                        break;
                }
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void wypisz(Connection conn) throws SQLException{
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ksiazki");
        System.out.println("\nID | Tytuł | Autor | Rok | ISBN");
        while(rs.next()){
            int id = rs.getInt("id");
            String tytul = rs.getString("tytul");
            String autor = rs.getString("autor");
            int rok = rs.getInt("rok");
            String isbn = rs.getString("isbn");
            System.out.println(id+" "+tytul+" "+autor+" "+rok+" "+isbn);
        }
    }

    public static void dodaj(Connection conn)throws SQLException{
        Scanner sc = new Scanner(System.in);
        System.out.print("Podaj tytuł książki: ");
        String tytul = sc.nextLine();
        System.out.print("Podaj autora książki: ");
        String autor = sc.nextLine();
        System.out.print("Podaj rok wydania książki: ");
        int rok = Integer.parseInt(sc.nextLine());
        System.out.print("Podaj nr. ISBN: ");
        String isbn = sc.nextLine();


        PreparedStatement ps = conn.prepareStatement("insert into ksiazki values (Null,?,?,?,?)");
        ps.setString(1, tytul);
        ps.setString(2, autor);
        ps.setInt(3, rok);
        ps.setString(4, isbn);
        ps.addBatch();

        ps.executeBatch();
    }

    public static void usun(Connection conn)throws SQLException{
        Scanner sc = new Scanner(System.in);
        System.out.print("Podaj id książki do usunięcia: ");
        int id = sc.nextInt();

        PreparedStatement ps = conn.prepareStatement("DELETE FROM ksiazki WHERE id="+id);
        ps.execute();
    }
}