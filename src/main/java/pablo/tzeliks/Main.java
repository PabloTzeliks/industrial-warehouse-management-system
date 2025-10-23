package pablo.tzeliks;

import pablo.tzeliks.dao.FornecedorDAO;
import pablo.tzeliks.dao.MaterialDAO;
import pablo.tzeliks.dao.NotaEntradaDAO;
import pablo.tzeliks.model.Fornecedor;
import pablo.tzeliks.model.Material;
import pablo.tzeliks.model.NotaEntrada;
import pablo.tzeliks.view.helper.InputHelper;
import pablo.tzeliks.view.helper.MenuHelper;
import pablo.tzeliks.view.helper.MessageHelper;
import pablo.tzeliks.view.helper.PrintHelper;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        FornecedorDAO fornecedorDAO = new FornecedorDAO();
        MaterialDAO materialDAO = new MaterialDAO();
        NotaEntradaDAO notaEntradaDAO = new NotaEntradaDAO();

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
                case "3":
                    registrarNotaEntrada(sc, notaEntradaDAO, fornecedorDAO, materialDAO);
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

    public static void registrarNotaEntrada(Scanner sc, NotaEntradaDAO notaEntradaDAO, FornecedorDAO fornecedorDAO, MaterialDAO materialDAO) {

        MenuHelper.menuRegistroNotaEntrada();

        // Listagem de Fornecedores
        MessageHelper.subtitulo("Escolha de Fornecedor");
        System.out.println();

        List<Fornecedor> fornecedores = fornecedorDAO.listarFornecedores();
        PrintHelper.listarFornecedores(fornecedores);

        // Escolha do Fornecedor
        int fornecedorId = InputHelper.lerInt(sc, "Digite o id do fornecedor");
        Optional<Fornecedor> fornecedorOptional = fornecedorDAO.buscarFornecedorPorId(fornecedorId);

        Fornecedor fornecedorEscolhido;

        if (fornecedorOptional.isPresent()) {

            fornecedorEscolhido = fornecedorOptional.get();

            MessageHelper.sucesso("Fornecedor adicionado com sucesso!");
        } else {

            MessageHelper.erro("Fornecedor com ID: " + fornecedorId + ", não foi encontrado. Tente novamente.");
            return;
        }

        // Escolha dos Materiáis

        HashMap<Integer, Double> estoqueMaterial = new HashMap<>();

        while (true) {
            // Listagem de Materiáis
            MessageHelper.subtitulo("Escolha de Materiáis");

            List<Material> materiais = materialDAO.listarMateriais();
            PrintHelper.listarMateriais(materiais);

            // Escolha do Material
            int materialId = InputHelper.lerInt(sc, "Digite o id do material");
            Optional<Material> materialOptional = materialDAO.buscarMaterialPorId(materialId);

            Material materialEscolhido;

            if (materialOptional.isPresent()) {

                materialEscolhido = materialOptional.get();

                materiais.remove(materialEscolhido); // Retira o Material da lista escolhida

                estoqueMaterial.put(materialEscolhido.getId(), materialEscolhido.getEstoque()); // Insere o Material e o estoque do mesmo

                MessageHelper.sucesso("Material adicionado com sucesso!");
            } else {

                MessageHelper.erro("Material com ID: " + materialId + ", não foi encontrado. Tente novamente.");
                continue;
            }

            String opcao = InputHelper.lerString(sc, "Deseja parar de Adicionar Materiáis? Se sim Digite 0. Caso queira continuar, digite qualquer outro carácter.");
            if (opcao.equalsIgnoreCase("0")) {
                break;
            }
        }

        // Criação do Objeto
        notaEntradaDAO.salvar(new NotaEntrada(0, fornecedorEscolhido), estoqueMaterial);
    }
}