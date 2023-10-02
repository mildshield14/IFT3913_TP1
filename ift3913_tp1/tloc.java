package ift3913_tp1;

import java.io.*;

public class Tloc {

    private static boolean calledByTls = false;
    private static  int countTlocExp;

    public static void setCalledByTls(boolean calledByTLS) {
        calledByTls = calledByTLS;
    }
    public static boolean getCalledbyTls() {
        return calledByTls;
    }

    public static void setCountTlocExp(int i) {
        Tloc.countTlocExp = i;
    }
    public static int getCountTlocExp() {
        return countTlocExp;
    }

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Please enter a correct file name");
            return;
        }
        String nameSourceFile = args[0]; //argument de la fonction "countTloc"
        int tlocNum = countTloc(nameSourceFile);
        setCountTlocExp(tlocNum);

        if (!getCalledbyTls()){
            System.out.println("TLOC: " + tlocNum);
            setCalledByTls(false);
        }

    }

    /* Le paramètre de cette fonction est le nom du fichier de test, obtenu depuis le terminal. Cette fonction
    * peut obtenir le nombre réel de lignes de code (hors commentaires et espaces)
    * */
    public static int countTloc(String nameFile) {
        int tlocNum = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(nameFile))) {
            String lineInfo; // Le contenu de chaque ligne du fichier
            while ((lineInfo = reader.readLine()) != null) {
                lineInfo = lineInfo.trim(); //Supprimez les espaces avant et après une ligne de contenu
                if (!lineInfo.isEmpty() && !lineInfo.startsWith("*")&&!lineInfo.startsWith("/")&& !lineInfo.startsWith("@")) {
                    tlocNum+=1;
                }
            }
        } catch (IOException e) {
            System.err.println("Error while reading the file" );
        }

        return tlocNum;
    }


}