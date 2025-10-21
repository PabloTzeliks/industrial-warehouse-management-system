package pablo.tzeliks.view.helper;

import java.util.Scanner;

public class InputHelper {

    public static String lerString(Scanner sc, String prompt) {

        System.out.println(prompt + ": ");

        while (true) {
            String input = sc.nextLine();

            if (validaInput(input)) {
                System.out.println("Input inválido, tente novamente.");
                continue;
            }

            return input;
        }
    }

    // Validadores

    private static boolean validaInput(String mensagem) {

        return !mensagem.isBlank();
    }
}
