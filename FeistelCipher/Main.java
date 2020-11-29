import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Main {
    public static void main(String[] args){
        String ciphroText = "";
        String key = "10101010";
        int rounds = 3;

        try {
            ciphroText = readFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("------------------------------------------");
        System.out.println("message: ");
        System.out.println(ciphroText);
        System.out.println("------------------------------------------");
        System.out.println("Encrypted message: ");
        String encrypted = encrypt(ciphroText, key, rounds);
        System.out.println(encrypted);
        System.out.println("------------------------------------------");
        System.out.println("Decrypted message: ");
        System.out.println(decrypt(encrypted, key, rounds));
    }

    private static byte right(byte a, byte s) {
        int x = a & 0xFF;
        int y = s % 8;
        return  (byte)((x >> y) | (x << (8 - y)));
    }

    private static byte left(byte a, byte s) {
        s %= 8;
        return  (byte)((a << s) | (a >> (8 - s)));
    }

    public static String readFile() throws IOException {
        String message = "";
        File file = new File("/Users/annamyamikova/Documents/Криптографические методы защиты информации/Lab2/text.txt");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        while (line != null) {
            message = message + line + "\n";
            line = reader.readLine();
        }
        return message;
    }

    public static List<String> splitEqually(String text, int size) {
        List<String> ret = new ArrayList<String>((text.length() + size - 1) / size);

        for (int start = 0; start < text.length(); start += size) {
            ret.add(text.substring(start, Math.min(text.length(), start + size)));
        }
        return ret;
    }


    public static String encrypt(String ciphroText, String key, int rounds){
        String encryptText = "";
        String binaryStr = "";
        List<String> buffer;

        char[] arr = ciphroText.toCharArray();
        for (int i = 0; i < arr.length; i++){
            String result = String.format("%8s", Integer.toBinaryString((int) arr[i])).replaceAll(" ", "0");
            String leftPart = result.substring(0, 4);
            String rightPart = result.substring(4);
            int LEP = Integer.parseInt(leftPart, 2);
            int REP = Integer.parseInt(rightPart, 2);
            int k = Integer.parseInt(key, 2);
            int left_temp, right_temp;
            for (int j = 0; j < rounds; j++){
                left_temp =  REP;
                right_temp =  (LEP ^ left((byte) REP, (byte) 2) ^ left( (byte) REP, (byte) 4) ^ k);
                LEP = left_temp;
                if (right_temp < 0){
                    REP = (byte) -(256 -  right_temp);
                } else REP = right_temp;
            }
            binaryStr += String.format("%8s", Integer.toBinaryString(LEP)).replaceAll(" ", "0") + String.format("%8s", Integer.toBinaryString(REP)).replaceAll(" ", "0") ;
        }
        buffer = splitEqually(binaryStr, 8);
        for (String buf : buffer){
            encryptText += Character.toString((char)Integer.parseInt(buf,2));
        }
        return encryptText;
    }

    public static String decrypt(String ciphroText, String key, int rounds){
        String binaryStr = "";
        List<String> buffer;
        String decryptText = "";
        char[] arr = ciphroText.toCharArray();
        for (int i = 0; i< arr.length; i++){
            binaryStr += String.format("%8s", Integer.toBinaryString((int) arr[i])).replaceAll(" ", "0");
        }
        buffer = splitEqually(binaryStr, 16);
        for (String buf : buffer){
            String rightPart = buf.substring(0, 8);
            String leftPart = buf.substring(8);
            int LEP = Integer.parseInt(leftPart, 2);
            int REP = Integer.parseInt(rightPart, 2);
            int k = Integer.parseInt(key, 2);
            int left_temp, right_temp;
            for (int j = 0; j < rounds; j++){
                left_temp =  REP;
                right_temp =  (LEP ^ left((byte) REP, (byte) 2) ^ left( (byte) REP, (byte) 4) ^ k);
                LEP = left_temp;
                if (right_temp < 0){
                    REP = (byte) -(256 -  right_temp);
                } else REP = right_temp;
            }
            decryptText += Character.toString((char)Integer.parseInt(String.format("%4s", Integer.toBinaryString(REP)).replaceAll(" ", "0") + String.format("%4s", Integer.toBinaryString(LEP)).replaceAll(" ", "0")
,2));
        }
        return decryptText;
    }
}
