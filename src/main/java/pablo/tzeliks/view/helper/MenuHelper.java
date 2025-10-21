package pablo.tzeliks.view.helper;

public class MenuHelper {

    public static void espacador() {

        System.out.println();
        System.out.println("=".repeat(50));
        System.out.println();
    }

    public static void menuPrincipal() {

        espacador();

        MessageHelper.subtitulo("Menu Principal");

        System.out.println("\n1- Cadastrar Fornecedor");

        System.out.println("\n0- Sair do sistema");
    }

    public static void menuCadastroFornecedor() {

        espacador();

        MessageHelper.subtitulo("Menu Cadastro Fornecedor");
    }
}
