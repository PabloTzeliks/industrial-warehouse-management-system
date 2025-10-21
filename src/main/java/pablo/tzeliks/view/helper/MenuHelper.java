package pablo.tzeliks.view.helper;

public class MenuHelper {

    public static void espacador() {

        System.out.println("=".repeat(50));
    }

    public static void menuPrincipal() {

        espacador();

        MessageHelper.subtitulo("Menu Principal");

        System.out.println("\n1- Cadastrar Fornecedor");

        System.out.println("\n0- Sair do sistema");
    }
}
