package es.codegym.task.pro.task1730;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class Solution {
    public static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.,:-!? \"";
    public static final String alphabetUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ.,:-!? \"";
    public static final String consonant = "BCDFGHJKLMNPQRSTVWXZbcdfghjklmnpqrstvwxz";
    public static final String asVowel = "Yy";
    public static final String vowel = "AEIOUaeiou" + asVowel;

    public static void main(String[] args) {
        boolean outMenu = false;
        int option = 0;

        while (!outMenu) {
              try {
                 option = optionMenu();
              } catch (Exception ex) {
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
               case 4: option_4(); break;
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
                  if (key <= 0 || key > alphabet.length()){
                      throw new InputMismatchException();
                  }
              } catch (InputMismatchException e) {
                      System.out.println("*** Debe ser un número entero entre 0 y " + alphabet.length());
                      String absorbEnter = console.nextLine();
              }
        }
        String absorbEnter = console.nextLine();

        System.out.print("Captura la ruta de salida: ");
        Path pathOut = Path.of(console.nextLine(), outFile);

        try (FileReader in = new FileReader(pathIn.toFile());
             BufferedReader reader = new BufferedReader(in);
             FileWriter writer = new FileWriter(pathOut.toFile())) {

            System.out.print("\nCifrando...");
            chars = scroll(chars, key);
            while (reader.ready()) {
                  String line = reader.readLine();
                  String word = encrypt(chars, line);
                  writer.write(word);
            }
            System.out.print(" cifrado exitoso!");

        } catch (FileNotFoundException e) {
                System.out.print("*** Ruta incorrecta!");
        } catch (Exception e) {
                System.out.println("Algo salió mal : " + e);
        }
        String enter = console.nextLine();
        System.out.println("\n\n\n\n");
    }

    public static void option_2() {
        String inFile = "archivo.in";
        String outFile = "salida.clear";

        var chars = new HashMap<Character, Character>();
        Scanner console = new Scanner(System.in);

        System.out.println("\n\n\n2.- Descifrar archivo.");

        System.out.print("Captura la ruta de archivo cifrado: ");
        Path pathIn = Path.of(console.nextLine(), inFile);
        int key = 0;
        while (key <= 0 || key > alphabet.length()) {
              try {
                  System.out.print("Captura la llave: ");
                  key = console.nextInt();
                  if (key <= 0 || key > alphabet.length()){
                      throw new InputMismatchException();
                  }
              } catch (InputMismatchException e) {
                      System.out.println("*** Debe ser un número entero entre 0 y " + alphabet.length());
                      String absorbEnter = console.nextLine();
              }
        }
        String absorbEnter = console.nextLine();

        System.out.print("Captura la ruta del archivo clear: ");
        Path pathOut = Path.of(console.nextLine(), outFile);

        try (FileReader in = new FileReader(pathIn.toFile());
             BufferedReader reader = new BufferedReader(in);
             FileWriter writer = new FileWriter(pathOut.toFile())) {

            System.out.print("\nDescifrando...");
            chars = scrollInverse(chars, key);
            while (reader.ready()) {
                  String line = reader.readLine();
                  String word = encrypt(chars, line);
                  writer.write(word);
            }
            System.out.print(" descifrado realizado.");

        } catch (FileNotFoundException e) {
                System.out.print("*** Ruta incorrecta!");
        } catch (Exception e) {
                System.out.println("Algo salió mal : " + e);
        }
        String enter = console.nextLine();
        System.out.println("\n\n\n\n");
    }

    public static void option_3() {
        String inFile = "archivo.in";
        String outFile = "salida.clear";
        var chars = new HashMap<Character, Character>();
        Scanner console = new Scanner(System.in);

        System.out.println("\n\n\n3.- Descifrar por fuerza bruta.");

        System.out.print("Captura la ruta de archivo cifrado: ");
        Path pathIn = Path.of(console.nextLine(), inFile);

        System.out.print("Captura la ruta del archivo clear: ");
        Path pathOut = Path.of(console.nextLine(), outFile);

        try (FileReader in = new FileReader(pathIn.toFile());
            BufferedReader reader = new BufferedReader(in);
            FileWriter writer = new FileWriter(pathOut.toFile(), true);
            BufferedWriter bw = new BufferedWriter(writer)) {

            System.out.print("\nDescifrando...");
            int resultFound = 0;
            String line = null;
            while (reader.ready()) {
                  line = reader.readLine();
                  for (int key = 0; key < alphabet.length(); key++) {
                      chars = scrollInverse(chars, key);
                      String word = encrypt(chars, line);
                      if (wordRules(word)) {
                         bw.write("[Llave=" + key + "] " + word);
                         bw.newLine();
                         resultFound++;
                      }
                 }
            }
            if (resultFound > 0) {
               String plural = resultFound > 1? "s": "";
               System.out.print(" finalizado con " + resultFound + " resultado" + plural + ".");
            } else {
                   System.out.print(" finalizado sin resultados.");
            }

        } catch (FileNotFoundException e) {
                System.out.print("*** Ruta incorrecta!");
        } catch (Exception e) {
                System.out.println("Algo salió mal : " + e);
        }
        String Enter = console.nextLine();
        System.out.println("\n\n\n\n");
    }

    public static void option_4() {
        String dataFile = "stats.in";
        String inFile = "archivo.in";
        String outFile = "salida.clear";

        var percDataFile = new HashMap<Character, Double>();
        var percInFile = new HashMap<Character, Double>();
        var chars = new HashMap<Character, Character>();
        Scanner console = new Scanner(System.in);

        System.out.println("\n\n\n4.- Descifrado por análisis estadístico.");

        System.out.print("Captura la ruta de archivo cifrado: ");
        Path pathIn = Path.of(console.nextLine(), inFile);

        System.out.print("Captura la ruta del archivo clear: ");
        Path pathOut = Path.of(console.nextLine(), outFile);

        System.out.print("Captura la ruta del archivo fuente de estadística: ");
        Path pathInEst = Path.of(console.nextLine(), dataFile);

        genStatistic(pathInEst, percDataFile);
        genStatistic(pathIn, percInFile);
        genChars(chars, percDataFile, percInFile);

        try (FileReader in = new FileReader(pathIn.toFile());
             BufferedReader reader = new BufferedReader(in);
             FileWriter writer = new FileWriter(pathOut.toFile(), true);
             BufferedWriter bw = new BufferedWriter(writer)) {

             System.out.print("\nDescifrando...");
             int resultFound = 0;
             String line = null;
             while (reader.ready()) {
                   line = reader.readLine();
                   String word = encrypt(chars, line);
                   if (wordRules(word)) {
                       bw.write(word);
                       bw.newLine();
                       resultFound++;
                   }
             }
            if (resultFound > 0) {
                String plural = resultFound > 1? "s": "";
                System.out.print(" finalizado con " + resultFound + " resultado" + plural + ".");
            } else {
                   System.out.print(" finalizado sin resultados.");
            }

        } catch (FileNotFoundException e) {
                System.out.print("*** Ruta incorrecta!");
        } catch (Exception e) {
                System.out.println("Algo salió mal : " + e);
        }
        String Enter = console.nextLine();
        System.out.println("\n\n\n\n");
    }


    public static String encrypt(HashMap<Character, Character> chars, String word) {
        String result = "";
        for (int i = 0; i < word.length() ; i++) {
            if (chars.containsKey(word.charAt(i))) {
                result = result + chars.get(word.charAt(i));
            } else {
                   result = result + word.charAt(i);
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
        int countVowels = 0;
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
                countVowels = 0;
                countSpace = 0;
                countPointSpace = 0;
            } else if (vowel.contains(c)) {
                countConsonants=0;
                countVowels++;
                countSpace = 0;
                countPointSpace = 0;
            } else if (" ".contains(c)) {
                countConsonants=0;
                countVowels=0;
                countSpace++;
                if (countPointSpace > 0) {
                    countPointSpace++;
                }
            } else if (".".contains(c)) {
                countConsonants=0;
                countVowels=0;
                countSpace=0;
                countPointSpace++;
            }
            if (countConsonants > 3 || countVowels > 3 || countSpace > 1 || countPointSpace > 2) {
                result = false;
            }
            i++;
        }
        return result;
    }
    public static HashMap<Character, Double> genStatistic(Path path, HashMap<Character, Double> percentage)  {
        int sumChars = 0;
        HashMap statistic = new HashMap<Character, Integer>();

        try (FileReader in = new FileReader(path.toFile());
            BufferedReader reader = new BufferedReader(in);) {
            for (int i = 0; i < alphabet.length(); i++) {
                statistic.put(alphabet.charAt(i), 0);
            }
            while (reader.ready()) {
                  String line = reader.readLine();
                  for (int i = 0; i <  alphabet.length(); i++) {
                      char c =  alphabet.charAt(i);
                      int value = (int) statistic.get(c);
                      value = value + counter(line, c);
                      statistic.put(c, value);
                  }
            }
            for (int i = 0; i <  alphabet.length(); i++) {
                sumChars = sumChars + (int) statistic.get( alphabet.charAt(i));
            }

            double percChar;
            double sumPercChar = 0.0;
            for (int i = 0; i <  alphabet.length(); i++) {
                percChar = 0.0;
                char c =  alphabet.charAt(i);
                int value = (int) statistic.get(c);
                if (value > 0) {
                    int trunc = (value * 1000) / sumChars;
                    percChar = (double) trunc / 10;
                    percentage.put(c, percChar);
                } else {
                    percentage.put(c, 0.0);
                }
                sumPercChar = sumPercChar + percChar;
            }

        } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
        } catch (IOException e) {
                throw new RuntimeException(e);
        }
        return percentage;
    }
    public static int counter(String string, char c) {
        int count = 0;
        int i = string.indexOf(c);
        while (i != -1) {
              count++;
              i = string.indexOf(c, i + 1);
        }
        return count;
    }
    public static HashMap<Character, Character> genChars(HashMap<Character, Character> chars,
                                                         HashMap<Character, Double> percDataFile,
                                                         HashMap<Character, Double> percInFile) {

        Map.Entry<Character, Double> varData;
        Map.Entry<Character, Double> varIn;
        int sizePercChars = percDataFile.size();

        for (int i = 0; i < sizePercChars ; i++) {
            varData = maxValue(percDataFile);
            varIn = maxValue(percInFile);
            if (varData.getValue() > 0 && varIn.getValue() > 0) {
                chars.put(varData.getKey(), varIn.getKey());
            }
            percDataFile.remove(varData.getKey());
            percInFile.remove(varIn.getKey());
        }
        return chars;
    }
    public static Map.Entry<Character, Double> maxValue(Map<Character, Double> map) {
        Map.Entry<Character, Double> maxEntry = null;
        for (Map.Entry<Character, Double> entry : map.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }
        return maxEntry;
    }

}

