package pablo.tzeliks;

import pablo.tzeliks.dao.FornecedorDAO;
import pablo.tzeliks.dao.MaterialDAO;
import pablo.tzeliks.model.Fornecedor;
import pablo.tzeliks.model.Material;
import pablo.tzeliks.view.helper.InputHelper;
import pablo.tzeliks.view.helper.MenuHelper;
import pablo.tzeliks.view.helper.MessageHelper;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        FornecedorDAO fornecedorDAO = new FornecedorDAO();
        MaterialDAO materialDAO = new MaterialDAO();

        Scanner sc = new Scanner(System.in);

        while (true) {

            MenuHelper.menuPrincipal();

            String opcao = InputHelper.lerString(sc, "Digite a opção desejada");

            switch (opcao) {

                case "0":
                    MessageHelper.info("Saindo ...");
                    return;
                case "1":
                    cadastrarFornecedor(sc, fornecedorDAO);
                    break;
                case "2":
                    cadastrarMaterial(sc, materialDAO);
                    break;
                default:
                    MessageHelper.erro("Valor inválido. Tente novamente.");
                    break;
            }
        }
    }

    public static void cadastrarFornecedor(Scanner sc, FornecedorDAO fornecedorDAO) {

        MenuHelper.menuCadastroFornecedor();

        String nomeFornecedor = InputHelper.lerString(sc, "Digite o nome do fornecedor");
        String cnpjFornecedor = InputHelper.lerCnpj(sc, "Digite o CNPJ do fornecedor");

        Fornecedor fornecedor = new Fornecedor(0, nomeFornecedor, cnpjFornecedor);

        fornecedorDAO.salvar(fornecedor);
    }

    public static void cadastrarMaterial(Scanner sc, MaterialDAO materialDAO) {

        MenuHelper.menuCadastroMaterial();

        String nomeMaterial = InputHelper.lerString(sc, "Digite o nome do material");
        String unidadeMedida = InputHelper.lerString(sc, "Digite o unidade do material");
        double quantidadeInicial = InputHelper.lerDouble(sc, "Digite a quantidade inicial de estoque");

        if (quantidadeInicial <= 0) {

            MessageHelper.erro("Quantidade inserida não pode ser negativa. Tente novamente.");
            return;
        }

        Material material = new Material(0, nomeMaterial, unidadeMedida, quantidadeInicial);

        materialDAO.salvar(material);
    }
}