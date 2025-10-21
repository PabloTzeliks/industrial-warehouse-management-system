package pablo.tzeliks.view.helper;

import pablo.tzeliks.model.Fornecedor;

import java.util.List;

public class PrintHelper {

    public static void printFornecedor(Fornecedor fornecedor) {

        MenuHelper.espacador();

        System.out.println(fornecedor.toString());
    }

    public static void listarFornecedores(List<Fornecedor> fornecedores) {

        for (Fornecedor fornecedor : fornecedores) {

            System.out.println(fornecedor.toString());

            MenuHelper.espacador();
        }
    }
}