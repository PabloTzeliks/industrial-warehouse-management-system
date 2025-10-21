package pablo.tzeliks.view.helper;

import pablo.tzeliks.model.Fornecedor;
import pablo.tzeliks.model.Material;

import java.util.List;

public class PrintHelper {

    // Print único

    public static void printFornecedor(Fornecedor fornecedor) {

        MenuHelper.espacador();

        System.out.println(fornecedor.toString());
    }

    public static void printMaterial(Material material) {

        MenuHelper.espacador();

        System.out.println(material.toString());
    }

    // Listagem

    public static void listarFornecedores(List<Fornecedor> fornecedores) {

        for (Fornecedor fornecedor : fornecedores) {

            System.out.println(fornecedor.toString());

            MenuHelper.espacador();
        }
    }

    public static void listarMateriais(List<Material> materiais) {

        for (Material material : materiais) {

            System.out.println(material.toString());

            MenuHelper.espacador();
        }
    }
}