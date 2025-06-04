import java.util.ArrayList;

public class Czytelnik {
    public int id;
    public String imie_nazwisko;
    public int wiek;
    public ArrayList<Integer> wypozyczoneKsiazki;

    public Czytelnik(int id, String imie_nazwisko, int wiek) {
        this.id = id;
        this.imie_nazwisko = imie_nazwisko;
        this.wiek = wiek;
        this.wypozyczoneKsiazki = new ArrayList<>();
    }
    public Czytelnik(int id, String imie_nazwisko, int wiek, ArrayList wypozyczone) {
        this.id = id;
        this.imie_nazwisko = imie_nazwisko;
        this.wiek = wiek;
        this.wypozyczoneKsiazki = wypozyczone;
    }

    public String toString(){
        return this.id+";"+this.imie_nazwisko+";"+this.wiek+";"+this.wypozyczoneKsiazki.toString();
    }
}