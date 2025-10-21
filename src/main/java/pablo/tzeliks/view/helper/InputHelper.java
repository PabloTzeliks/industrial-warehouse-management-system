package pablo.tzeliks.view.helper;

import java.util.Scanner;

public class InputHelper {

    public static String lerString(Scanner sc, String prompt) {

        System.out.println(prompt + ": ");

        while (true) {
            String input = sc.nextLine();

            if (validaInputNulo(input)) {
                MessageHelper.erro("Input inválido, tente novamente.");
                continue;
            }

            return input;
        }
    }

    public static int lerInt(Scanner sc, String prompt) {

        System.out.println(prompt + ": ");

        while (true) {
            String input = sc.next();

            if (validaInputNulo(input)) {
                MessageHelper.erro("Input inválido, tente novamente.");
                continue;
            }

            return Integer.parseInt(input);
        }
    }

    public static String lerCnpj(Scanner sc, String prompt) {

        System.out.println(prompt + ": ");

        while (true) {
            String input = sc.nextLine();

            if (validaCnpj(input)) {
                MessageHelper.erro("CNPJ inválido, tente novamente.");

                System.out.println("\nCertifique-se que possua exatamente 14 carácteres numéricos");
                continue;
            }

            return input;
        }
    }

    // Validadores

    private static boolean validaInputNulo(String mensagem) {

        return mensagem.isBlank();
    }

    private static boolean validaCnpj(String cnpj) {

        if (!validaInputNulo(cnpj)) {
            return false;
        }

        if (cnpj.length() != 14) {
            return false;
        }

        return true;
    }
}
