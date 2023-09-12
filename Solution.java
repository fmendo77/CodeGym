package es.codegym.task.pro.task1730;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Solution {
    public static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.,\":-!? ";
    //public static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String consonant = "BCDFGHJKLMNPQRSTVWXYZbcdfghjklmnpqrstvwxyz";
    public static final String vocal = "AEIOUaeiou";

    public static void main(String[] args) {
        boolean outMenu = false;
        int option = 0;
        while (!outMenu) {
        try {
            option = optionMenu();
        }
        catch (Exception ex) {
            System.out.println("*** Opción inválida.\n\n\n");
        }
        outMenu = executeOption(option);
        }
    }
    public static boolean executeOption(int option) {
        boolean outMenu = false;
        switch (option) {
            case 1: option_1(); break;
            case 2: option_2(); break;
            case 3: option_3(); break;
            case 4: break;
            case 5: outMenu = true; break;
        }
        return outMenu;
    }
    public static int optionMenu() {
       Scanner console = new Scanner(System.in);
       int option = 0;
       System.out.println("Elije una opción\n");
       System.out.println("  1.- Cifrar.");
       System.out.println("  2.- Descifrar.");
       System.out.println("  3.- Descifrar por fuerza bruta.");
       System.out.println("  4.- Descifrar por estadística.");
       System.out.println("  5.- Salir.");
       while (option < 1 || option > 5) {
               System.out.print("       -> ");
               option = console.nextInt();
       }
       return option;
    }

    public static void option_1() {
        String inFile = "archivo.txt";
        String outFile = "salida.cipher";

        var chars = new HashMap<Character, Character>();
        Scanner console = new Scanner(System.in);
        System.out.println("\n\n\n1.- Cifrar archivo.");
        System.out.print("Captura la ruta de archivo en claro: ");
        Path pathIn = Path.of(console.nextLine(), inFile);
        int key = 0;

        while (key <= 0 || key > alphabet.length()) {
          try {
                System.out.print("Captura la llave: ");
                key = console.nextInt();
          } catch (InputMismatchException e) {
                    System.out.println("*** Debe ser un número entero.");
                    String absorbEnter = console.nextLine();
          }
        }

        String absorbEnter = console.nextLine();
        System.out.print("Captura la ruta de salida: ");
        Path pathOut = Path.of(console.nextLine(), outFile);

        try (FileReader in = new FileReader(pathIn.toFile());
            BufferedReader reader = new BufferedReader(in);
            FileWriter writer = new FileWriter(pathOut.toFile())) {

            System.out.print("\nEncriptando...");
            chars = scroll(chars, key);
            while (reader.ready()) {
                String line = reader.readLine();
                String word = encrypt(chars, line);
                writer.write(word);
            }
            System.out.print(" encriptado exitoso!");

        } catch (FileNotFoundException e) {
            System.out.print("*** Ruta incorrecta!");
        } catch (Exception e) {
            System.out.println("Algo salió mal : " + e);
        }
        String enter = console.nextLine();
        System.out.println("\n\n\n");
    }

    public static void option_2() {
        //String inFile = "archivo.in";
        String inFile = "salida.cipher";
        String outFile = "salida.clear";

        var chars = new HashMap<Character, Character>();
        Scanner console = new Scanner(System.in);
        System.out.println("\n\n\n2.- Descifrar archivo.");
        System.out.print("Captura la ruta de archivo encriptado: ");
        Path pathIn = Path.of(console.nextLine(), inFile);
        int key = 0;

        while (key <= 0 || key > alphabet.length()) {
            try {
                System.out.print("Captura la llave: ");
                key = console.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("*** Debe ser un número entero.");
                String absorbEnter = console.nextLine();
            }
        }

        String absorbEnter = console.nextLine();
        System.out.print("Captura la ruta del archivo clear: ");
        Path pathOut = Path.of(console.nextLine(), outFile);

        try (FileReader in = new FileReader(pathIn.toFile());
             BufferedReader reader = new BufferedReader(in);
             FileWriter writer = new FileWriter(pathOut.toFile())) {

            System.out.print("\nDesencriptando...");
            chars = scrollInverse(chars, key);
            while (reader.ready()) {
                String line = reader.readLine();
                String word = encrypt(chars, line);
                writer.write(word);
            }
            System.out.print(" desencriptado realizado");

        } catch (FileNotFoundException e) {
            System.out.print("*** Ruta incorrecta!");
        } catch (Exception e) {
            System.out.println("Algo salió mal : " + e);
        }
        String enter = console.nextLine();
        System.out.println("\n\n\n");
    }

    public static void option_3() {
        String inFile = "salida.cipher";
        String outFile = "salida.clear";

        var chars = new HashMap<Character, Character>();
        Scanner console = new Scanner(System.in);
        System.out.println("\n\n\n3.- Descifrar por fuerza bruta.");
        System.out.print("Captura la ruta de archivo encriptado: ");
        Path pathIn = Path.of(console.nextLine(), inFile);

        System.out.print("Captura la ruta del archivo clear: ");
        Path pathOut = Path.of(console.nextLine(), outFile);

        try (FileReader in = new FileReader(pathIn.toFile());
            BufferedReader reader = new BufferedReader(in);
            FileWriter writer = new FileWriter(pathOut.toFile(), true);
            BufferedWriter bw = new BufferedWriter(writer)) {
            System.out.print("\nDesencriptando...");

            String line = null;
            while (reader.ready()) {
                line = reader.readLine();

              for (int key = 0; key < alphabet.length(); key++) {
                chars = scrollInverse(chars, key);
                String word = encrypt(chars, line);
                if (wordRules(word)) {
                    bw.write(key + "] " + word);
                    bw.newLine();
                }
              }
            }
            System.out.print(" desencriptado realizado");
        } catch (FileNotFoundException e) {
            System.out.print("*** Ruta incorrecta!");
        } catch (Exception e) {
            System.out.println("Algo salió mal : " + e);
        }
        String enter = console.nextLine();
        System.out.println("\n\n\n");
    }

    public static void option_4() {
        String dataFile = "texto.txt";
        String inFile = "encriptado.cipher";
        String outFile = "desencriptado.clear";

        //var chars = new HashMap<Character, Character>();
        var statistic = new HashMap<Character, Integer>();
        Scanner console = new Scanner(System.in);
        System.out.println("\n\n\n4.- Descifrado por análisis estadístico.");
        System.out.print("Captura la ruta de archivo encriptado: ");
        Path pathIn = Path.of(console.nextLine(), inFile);

        System.out.print("Captura la ruta del archivo clear: ");
        Path pathOut = Path.of(console.nextLine(), outFile);

        Path pathInEst = Path.of(console.nextLine(), dataFile);
        genStatistic(pathInEst, statistic);

        try (FileReader in = new FileReader(pathIn.toFile());
             BufferedReader reader = new BufferedReader(in);
             FileWriter writer = new FileWriter(pathOut.toFile(), true);
             BufferedWriter bw = new BufferedWriter(writer)) {
            System.out.println("\nDesencriptando...");

            String line = null;
            while (reader.ready()) {
                line = reader.readLine();
            }
            for (int key = 0; key < alphabet.length() ; key++) {
                chars = scrollInverse(chars, key);
                String word = encrypt(chars, line);
                if (wordRules(word)) {
                    bw.write(key + "] " + word);
                    bw.newLine();
                }
            }
            System.out.println(" desencriptado realizado");
        } catch (FileNotFoundException e) {
            System.out.print("*** Ruta incorrecta!");
        } catch (Exception e) {
            System.out.println("Algo salió mal : " + e);
        }
        String enter = console.nextLine();
        System.out.println("\n\n\n");
    }


    public static String encrypt(HashMap<Character, Character> chars, String word) {
        String result = "";
        for (int i = 0; i < word.length() ; i++) {
            if (chars.containsKey(word.charAt(i))) {
                result = result + chars.get(word.charAt(i));
            }
        }
        return result;
    }
    public static HashMap scroll(HashMap chars, int key){
        for (int i = 0; i < alphabet.length(); i++) {
            if (i + key == alphabet.length()) {
                key = (i * -1);
            }
            chars.put(alphabet.charAt(i), alphabet.charAt(i + key));
        }
        return chars;
    }

    public static HashMap scrollInverse(HashMap chars, int key){
        for (int i = 0; i < alphabet.length(); i++) {
            if (i + key == alphabet.length()) {
                key = (i * -1);
            }
            chars.put(alphabet.charAt(i + key), alphabet.charAt(i));
        }
        return chars;
    }
    public static boolean wordRules(String word) {
        int countVocals = 0;
        int countConsonants = 0;
        int countSpace = 0;
        int countPointSpace = 0;
        boolean result = true;
        int i = 0;
        while (i < word.length() && result) {
            String c = "";
            c += word.charAt(i);
            if (consonant.contains(c)) {
                countConsonants++;
                countVocals = 0;
                countSpace = 0;
                countPointSpace = 0;
            } else if (vocal.contains(c)) {
                countConsonants=0;
                countVocals++;
                countSpace = 0;
                countPointSpace = 0;
            } else if (" ".contains(c)) {
                countConsonants=0;
                countVocals=0;
                countSpace++;
                if (countPointSpace > 0) {
                    countPointSpace++;
                }
            } else if (".".contains(c)) {
                countConsonants=0;
                countVocals=0;
                countSpace=0;
                countPointSpace++;
            }
            if (countConsonants > 3 || countVocals > 3 || countSpace > 1 || countPointSpace > 2) {
                result = false;
            }
            i++;
        }

        return result;
    }
    public static HashMap genStatistic(Path pathInEst, HashMap statistic) throws FileNotFoundException, IOException {

        FileReader in = new FileReader(pathInEst.toFile());
        BufferedReader reader = new BufferedReader(in);
        for (int i = 0; i < alphabet.length(); i++) {
            statistic.put(alphabet.charAt(i), 0);
        }
        while (reader.ready()) {
               String line = reader.readLine();
            int value = statistic.get(c);

            }
        return statistic;
    }
    public static int counter(String string, char c) {
        int i, count = 0;
        i = string.indexOf(c);
        while (i != -1) {
            count++;
            i = string.indexOf(c, i + 1);
        }
        return count;
    }
}
