import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static int[][] s_block = {
            { 13,	2,	8,	4,	6,	15,	11,	1,	10,	9,	3,	14,	5,	0,	12,	7 },
            { 1, 15, 13, 8,	10,	3,	7,	4,	12,	5,	6,	11,	0,	14,	9,	2 },
            {7,	11,	4,	1,	9,	12,	14,	2,	0,	6,	10,	13,	15,	3,	5,	8},
            { 2, 1,	14,	7,	4,	10,	8,	13,	15,	12,	9,	0,	3,	5,	6,	11 }
    };

    private static int a = 2;
    private static int b = 4;

    public static void main(String[] args){
        String ciphroText = "";

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
        System.out.println(encrypt(ciphroText));
        System.out.println("------------------------------------------");
        System.out.println("Decrypted message: ");
        System.out.println(decrypt(encrypt(ciphroText)));
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

    public static String swap(String str, int a, int b){
        String newStr = "";
        String[] arr = new String[6];

        arr[a-1] = str.substring(0, 1);
        arr[b-1] = str.substring(1,2);

        if (a > b){
            int d = a;
            a = b;
            b = d;
        }

        if (a == 1 && b == 6){
            int d = 2;
            for (int i = a; i < b-1; i++){
                arr[i] = str.substring(d, d+1);
                d++;
            }
        }

        if (a != 1 && b == 6){
            int d = 2;
            for (int i = 0; i < a-1; i++){
                arr[i] = str.substring(d, d+1);
                d++;
            }
            for (int i = a; i < b-1; i++){
                arr[i] = str.substring(d, d+1);
                d++;
            }
        }

        if (a == 1 && b != 6){
            int d = 2;
            for (int i = a; i < b-1; i++){
                arr[i] = str.substring(d, d+1);
                d++;
            }
            for (int i = b; i < 6; i++){
                arr[i] = str.substring(d, d+1);
                d++;
            }
        }

        if (a != 1 && b != 6){
            int d = 2;
            for (int i = 0; i < a-1; i++){
                arr[i] = str.substring(d, d+1);
                d++;
            }
            for (int i = a; i < b-1; i++){
                arr[i] = str.substring(d, d+1);
                d++;
            }
            for (int i = b; i < 6; i++){
                arr[i] = str.substring(d, d+1);
                d++;
            }
        }

        for (int i = 0; i < arr.length; i ++){
            newStr += arr[i];
        }

        return newStr;
    }

    public static String encrypt(String ciphroText){
        List<String> buffer;
        String binaryStr = "";
        String encryptText = "";
        char[] arr = ciphroText.toCharArray();

        for (int i = 0; i < arr.length; i++){
            String result = Integer.toBinaryString((int) arr[i]);
            binaryStr += String.format("%8s", result).replaceAll(" ", "0");
        }
        buffer = splitEqually(binaryStr, 6);
        binaryStr = "";
        String str = buffer.get(buffer.size() - 1);
        while (buffer.get(buffer.size() - 1).length() != 6){
            str += '0';
            buffer.set(buffer.size() - 1, str);
        }
        for (String buf : buffer){
            int row = Integer.parseInt(buf.substring(a-1, a) + buf.substring(b-1, b), 2);
            int column = Integer.parseInt(buf.substring(0,a-1) + buf.substring(a,b-1) + buf.substring(b,6), 2);
            int encryptedColumn = s_block[row][column];
            binaryStr += String.format("%2s", Integer.toBinaryString(row)).replaceAll(" ", "0") + String.format("%4s",  Integer.toBinaryString(encryptedColumn)).replaceAll(" ", "0");
        }
        buffer = splitEqually(binaryStr, 8);
        str = buffer.get(buffer.size() - 1);
        while (buffer.get(buffer.size() - 1).length() != 8){
            str += '0';
            buffer.set(buffer.size() - 1, str);
        }


        for (String buf : buffer){
            encryptText += Character.toString((char)Integer.parseInt(buf,2));
        }
        return encryptText;
    }

    public static String decrypt(String ciphroText){
        String decryptText = "";
        String binaryStr = "";
        List<String> buffer;
        int decryptedColumn = 0;

        char[] arr = ciphroText.toCharArray();
        for (int i = 0; i < arr.length; i++){
            String result = Integer.toBinaryString((int) arr[i]);
            binaryStr += String.format("%8s", result).replaceAll(" ", "0");
        }
        buffer = splitEqually(binaryStr, 6);
        if (buffer.get(buffer.size() - 1).length() != 6){
            buffer.remove(buffer.size() - 1);
        }
        binaryStr = "";
        for (String buf : buffer){
            int row = Integer.parseInt(buf.substring(0, 2), 2);
            int column = Integer.parseInt(buf.substring(2,6), 2);
            for (int i = 0; i < 16; i++){
                if (s_block[row][i] == column){
                    decryptedColumn = i;
                    break;
                }
            }
            binaryStr += swap(String.format("%2s", Integer.toBinaryString(row)).replaceAll(" ", "0") + String.format("%4s",  Integer.toBinaryString(decryptedColumn)).replaceAll(" ", "0"), a, b);
        }
        buffer = splitEqually(binaryStr, 8);
        if (buffer.get(buffer.size() - 1).length() != 8){
            buffer.remove(buffer.size() - 1);
        }

        for (String buf : buffer){
            decryptText += Character.toString((char)Integer.parseInt(buf,2));
        }
        return decryptText;
    }
}