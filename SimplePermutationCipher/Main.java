import java.io.*;
import java.util.ArrayList;

public class Main {
    private static int[] key = {3, 4, 6, 5, 2, 1};

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

    public static String encrypt(String ciphroText){
        String encryptText = "";
        int count = 0;
        char[] arr = ciphroText.toCharArray();
        ArrayList<Character> chars = new ArrayList<>();
        for (int i = 0; i<arr.length; i++){
            chars.add(arr[i]);
        }
        while (chars.size() % key.length != 0){
            chars.add(arr[count]);
            count++;
        }

        for (int i = 0; i < chars.size(); i+= key.length){
            char[] transposition = new char[key.length];
            for (int j = 0; j < key.length; j++){
                transposition[key[j] - 1] = chars.get(i + j);
            }
            for (int j = 0; j < key.length; j++){
                encryptText += transposition[j];
            }
        }
        return encryptText;
    }

    public static String decrypt(String ciphroText){
        String decryptText = "";

        char[] arr = ciphroText.toCharArray();
        ArrayList<Character> chars = new ArrayList<>();
        for (int i = 0; i<arr.length; i++){
            chars.add(arr[i]);
        }

        for (int i = 0; i < chars.size(); i+= key.length){
            char[] transposition = new char[key.length];

            for (int j = 0; j < key.length; j++)
                transposition[j] = chars.get(i + key[j] - 1);

            for (int j = 0; j < key.length; j++)
                decryptText += transposition[j];
        }

        return decryptText;
    }
}
