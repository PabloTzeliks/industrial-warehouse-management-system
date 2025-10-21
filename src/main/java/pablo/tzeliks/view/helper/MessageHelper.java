package pablo.tzeliks.view.helper;

import java.util.Scanner;

public class MessageHelper {

    public static void subtitulo(String titulo) { System.out.println("\n========= " + titulo + " ========="); }

    public static void sucesso(String mensagem) { System.out.println("\n[SUCESSO] " + mensagem); }

    public static void info(String mensagem) { System.out.println("\n[INFO] " + mensagem); }

    public static void erro(String mensagem) { System.out.println("\n[ERRO] " + mensagem); }
}
