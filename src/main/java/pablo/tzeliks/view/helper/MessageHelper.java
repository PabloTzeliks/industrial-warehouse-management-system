package pablo.tzeliks.view.helper;

import java.util.Scanner;

public class MessageHelper {

    public static void subtitulo(String titulo) { System.out.println("=========" + titulo + "========="); }

    public static void sucesso(String mensagem) { System.out.println("[SUCESSO] " + mensagem); }

    public static void info(String mensagem) { System.out.println("[INFO] " + mensagem); }

    public static void erro(String mensagem) { System.out.println("[ERRO] " + mensagem); }
}
