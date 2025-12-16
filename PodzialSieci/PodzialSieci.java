import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PodzialSieci {
    private JPanel MainPanel;
    private JButton CheckButton;
    private JTextField Ad1;
    private JTextField Ad2;
    private JTextField Ad3;
    private JTextField Ad4;
    private JTextField Ms4;
    private JTextField Ms3;
    private JTextField Ms2;
    private JTextField Ms1;
    private JTextField Rz1;
    private JTextField Rz2;
    private JTextField Rz3;
    private JTextField Rz4;
    private JTextField Min;
    private JTextField Max;
    private JComboBox DividePicker;

    private JTextField[] Adres;
    private JTextField[] Maska;
    private JTextField[] Broadcast;

    private int firstZeroInMaskIndex = -1;

    private byte[] adresByte;
    private byte[] maskaByte;
    private byte[] broadcastByte;

    public PodzialSieci(){
        adresByte = new byte[32];
        maskaByte = new byte[32];
        broadcastByte = new byte[32];

        Adres = new JTextField[] {Ad1, Ad2, Ad3, Ad4};
        Maska = new JTextField[] {Ms1, Ms2, Ms3, Ms4};
        Broadcast = new JTextField[] {Rz1, Rz2, Rz3, Rz4};

        CheckButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                check();
            }
        });
    }

    private void check(){
        switch(areNumbersFromRange()){
            case -1:
                new Alert("Należy wprowadzić jedynie liczby!");
                break;
            case -2:
                new Alert("Liczby muszą być z zakresu 0-255!");
                break;
            case -3:
                new Alert("Pierwsza liczba adresu sieci ma być większa od 0!");
                break;
            default:
                fillAdres();
                if(!checkMaska()){
                    new Alert("Niepoprawna Maska! - Poziom sprawdzania czy liczby są pomieszane.");
                    return;
                }
                if(!checkSameZeros()){
                    new Alert("Niepoprawny Adres! - Poziom sprawdzania czy zera są na tym samym miejscu.");
                    return;
                }
//                new Alert("Poprawna maska!");
                calculateBroadcast();
                calculateMinMax();
                fillDividePicker();
                break;
        }
    }

    private int areNumbersFromRange(){
        int[] AdresInt = new int[4];
        int[] MaskaInt = new int[4];
        try{
            for(int i=0; i<Adres.length; i++){
                AdresInt[i] = Integer.parseInt(Adres[i].getText());
                if(AdresInt[i] < 0 || AdresInt[i] > 255) return -2;
                MaskaInt[i] = Integer.parseInt(Adres[i].getText());
                if(MaskaInt[i] < 0 || MaskaInt[i] > 255) return -2;
            }
        }catch(NumberFormatException e){
            return -1;
        }
        if(AdresInt[0] == 0){
            return -3;
        }

        return 0;
    }

    private void fillAdres(){
        for(int i=0; i<4; i++){
            convertToBinary(Integer.parseInt(Adres[i].getText()), i*8, adresByte);
            convertToBinary(Integer.parseInt(Maska[i].getText()), i*8, maskaByte);
        }
    }

    private boolean checkMaska(){
        boolean isZero = false;
        for(int i=0; i<32; i++){
            if(maskaByte[i] == 0){
                isZero = true;
            }

            if(isZero && maskaByte[i] == 1){
                return false;
            }
        }
        return true;
    }

    private boolean checkSameZeros(){
        boolean wasFirstZero = false;
        for(int i=0; i<32; i++){

            if(maskaByte[i] == 0){
                if(!wasFirstZero){
                    firstZeroInMaskIndex = i;
                    wasFirstZero = true;
                }
                if(adresByte[i] != 0) return false;
            }
        }
        return true;
    }

    private void convertToBinary(int l, int p, byte[] t){
        for(int i=0; i<8; i++){
            t[p+7] = (byte)(l%2);
            l/=2;
            p--;
        }
    }

    private int convertToHex(byte[] t, int p){
        int sum = 0;
        int power = 1;
        for(int i=(t.length-1)-(p*8); i>=(t.length)-(p*8)-8; i--){
            sum += t[i]*power;
            power*=2;
        }
        return sum;
    }

    private void calculateBroadcast(){
        for(int i=0; i<32; i++){
            broadcastByte[i] = adresByte[i];
            if(maskaByte[i] == 0){
                broadcastByte[i] = 1;
            }
        }

        int j=3;
        for(int i=0; i<4; i++){
            Broadcast[j].setText(String.valueOf(convertToHex(broadcastByte, i)));
            j--;
        }
    }

    private void calculateMinMax(){
        byte[] minByte = new byte[32];
        byte[] maxByte = new byte[32];
        for(int i=0; i<32; i++){
            minByte[i] = adresByte[i];
            maxByte[i] = broadcastByte[i];
        }
        minByte[31] = 1;
        maxByte[31] = 0;

        Min.setText(convertByteToString(minByte));
        Max.setText(convertByteToString(maxByte));
    }

    private String convertByteToString(byte[] t){
        String result = "";
        for(int i=0; i<4; i++){
            result = convertToHex(t, i) + result;
            if(i<3){
                result = "." + result;
            }
        }
        return result;
    }

    private int getMaskZeroCount(){
        return 32 - firstZeroInMaskIndex;
    }

    private void fillDividePicker(){
        DividePicker.removeAllItems();
        int zeroCount = getMaskZeroCount();
        for(int i=1; i<zeroCount-2; i++){
            DividePicker.addItem((int)Math.pow(2, i));
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("PodzialSieci");
        frame.setContentPane(new PodzialSieci().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(600, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
