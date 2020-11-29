import java.io.*;

public class Main {

        public static void main(String[] args){
            System.out.println("Вариант 12");
            int m = 256;
            int a = 27;
            int b = 43;
            String ciphroText = "";

            try {
                ciphroText = readFile();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("message: ");
            System.out.println(ciphroText);

            String encryptMessage = encrypt(ciphroText, m, a, b);
            System.out.println("encryptMessage: ");
            System.out.println(encryptMessage);

            String decryptMessage = decrypt(encryptMessage, m, a, b);
            System.out.println("decryptMessage: ");
            System.out.println(decryptMessage);
        }

        public static int[] extended_euclid(int a, int b){
            int q, r, x1, x2, y1, y2;
            int d = 0, x = 0, y = 0;
            if (b == 0) {
                d = a;
                x = 1;
                y = 0;
                return new int[]{d, x, y};
            }
            x2 = 1;
            x1 = 0;
            y2 = 0;
            y1 = 1;

            while (b > 0) {
            q = a / b;
            r = a - q * b;
            x = x2 - q * x1;
            y = y2 - q * y1;
            a = b;
            b = r;

            x2 = x1; x1 = x; y2 = y1; y1 = y;
            d = a;
            x = x2;
            y = y2;

            }

            return new int[]{d, x, y};
        }


        public static int reverse(int a, int n){
            int[] mas = extended_euclid(a, n);
            if (mas[0] == 1){
                return mas[1];
            }
            return 0;
        }

        public static String encrypt(String ciphroText, int m, int a, int b) {
                int Ek;
                String encryptText = "";
                char[] chars = ciphroText.toCharArray();
                for (int i =0; i < chars.length; i++){
                    Ek = (a * (int) chars[i] + b) % m;
                    encryptText += (char)Ek;
                }
                return encryptText;
        }

        public static String readFile() throws IOException {
            String message = "";
                File file = new File("/Users/annamyamikova/Documents/Криптографические методы защиты информации/Lab1/text.txt");
                FileReader fr = new FileReader(file);
                BufferedReader reader = new BufferedReader(fr);
                String line = reader.readLine();
                while (line != null) {
                    message = message + line + "\n";
                    line = reader.readLine();
                }
                return message;
        }

        public static String decrypt(String ciphroText, int m, int a, int b){
            int Dk;
            String decryptText = "";
            char[] chars = ciphroText.toCharArray();
            System.out.println("-----------------------------");
            for (int i =0; i < chars.length; i++){
                    Dk = (reverse(a, m) * ((int) chars[i] - b)) % m;
                    if (Dk < 0){
                        Dk += 256;
                    }
                    decryptText += (char) Dk;
            }
            return decryptText;
        }
}

