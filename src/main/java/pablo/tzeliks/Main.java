package pablo.tzeliks;

import pablo.tzeliks.dao.FornecedorDAO;
import pablo.tzeliks.dao.MaterialDAO;
import pablo.tzeliks.dao.NotaEntradaDAO;
import pablo.tzeliks.dao.RequisicaoDAO;
import pablo.tzeliks.model.Fornecedor;
import pablo.tzeliks.model.Material;
import pablo.tzeliks.model.NotaEntrada;
import pablo.tzeliks.model.Requisicao;
import pablo.tzeliks.model.enums.StatusRequisicao;
import pablo.tzeliks.view.helper.InputHelper;
import pablo.tzeliks.view.helper.MenuHelper;
import pablo.tzeliks.view.helper.MessageHelper;
import pablo.tzeliks.view.helper.PrintHelper;

import java.util.*;

public class Main {

    //arruma esse código
    //ass: igor

    public static void main(String[] args) {

        FornecedorDAO fornecedorDAO = new FornecedorDAO();
        MaterialDAO materialDAO = new MaterialDAO();
        NotaEntradaDAO notaEntradaDAO = new NotaEntradaDAO();
        RequisicaoDAO requisicaoDAO = new RequisicaoDAO();

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
                case "4":
                    registrarRequisicao(sc, requisicaoDAO, materialDAO);
                    break;
                case "5":
                    atenderRequisicao(sc, requisicaoDAO);
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

            double quantidadeMaterial = InputHelper.lerDouble(sc, "Digite a quantidade do material");

            Material materialEscolhido;

            if (materialOptional.isPresent()) {

                materialEscolhido = materialOptional.get();

                materiais.remove(materialEscolhido); // Retira o Material da lista escolhida

                estoqueMaterial.put(materialEscolhido.getId(), quantidadeMaterial); // Insere o Material e o estoque do mesmo

                MessageHelper.sucesso("Material adicionado com sucesso!");
            } else {

                MessageHelper.erro("Material com ID: " + materialId + ", não foi encontrado. Tente novamente.");
                continue;
            }

            String opcao = InputHelper.lerString(sc, "\nDigite 0 para sair." +
                    "\nDigite qualquer outro caráctere para continuar.\n" +
                    "\nEscolha");
            if (opcao.equalsIgnoreCase("0")) {
                break;
            }
        }

        // Criação do Objeto
        notaEntradaDAO.salvar(new NotaEntrada(0, fornecedorEscolhido), estoqueMaterial);
    }

    public static void registrarRequisicao(Scanner sc, RequisicaoDAO requisicaoDAO, MaterialDAO materialDAO) {

        MenuHelper.menuRegistrarRequisicao();

        MessageHelper.subtitulo("Escolha do Setor");
        String setorRequisicao = InputHelper.lerString(sc, "Digite o setor do requisição");

        List<Material> materiais = materialDAO.listarMateriais();

        HashMap<Integer, Double> estoqueMaterial = new HashMap<>();

        while (true) {
            // Listagem de Materiáis
            MessageHelper.subtitulo("Escolha de Materiáis");

            if (materiais.isEmpty()) {

                MessageHelper.erro("Não há materiais para selecionar.");
                break;
            }

            PrintHelper.listarMateriais(materiais);

            // Escolha do Material
            int materialId = InputHelper.lerInt(sc, "Digite o id do material");
            Material materialEscolhido = null;

            for (Material material : materiais) {

                if (material.getId() ==  materialId) {
                    materialEscolhido = material;
                    break;
                }
            }

            double quantidadeMaterial = InputHelper.lerDouble(sc, "Digite a quantidade do material");

            if (materialEscolhido != null) {

                materiais.remove(materialEscolhido); // Retira o Material da lista escolhida

                // Valida se quantidade inserida pelo usuário é válida
                if (quantidadeMaterial <= 0 || quantidadeMaterial > materialEscolhido.getEstoque()) {

                    MessageHelper.erro("Valor inválido de Material inserido.");
                    continue;
                }

                estoqueMaterial.put(materialEscolhido.getId(), quantidadeMaterial); // Insere o Material e o estoque do mesmo

                MessageHelper.sucesso("Material adicionado com sucesso!");
            } else {

                MessageHelper.erro("Material com ID: " + materialId + ", não foi encontrado. Tente novamente.");
                continue;
            }

            String opcao = InputHelper.lerString(sc, "\nDigite 0 para sair." +
                    "\nDigite qualquer outro caráctere para continuar.\n" +
                    "\nEscolha");
            if (opcao.equalsIgnoreCase("0")) {
                break;
            }
        }

        Requisicao requisicao = new Requisicao(0, setorRequisicao, StatusRequisicao.PENDENTE);

        // Executa a inserção
        requisicaoDAO.salvar(requisicao, estoqueMaterial);
    }

    public static void atenderRequisicao(Scanner sc, RequisicaoDAO requisicaoDAO) {

        MenuHelper.menuAtendeRequisicao();

        // Listagem de Requisições Pendentes
        List<Requisicao> requisicoes = requisicaoDAO.listarRequisicao(StatusRequisicao.PENDENTE);

        if (requisicoes.isEmpty()) {

            MessageHelper.erro("Nenhuma requisicao foi encontrada.");
            return;
        }

        PrintHelper.listarRequisicao(requisicoes);

        // Seleção de Requisição
        int requisicaoId = InputHelper.lerInt(sc, "Digite o id da requisição");
        Optional<Requisicao> requisicao = requisicaoDAO.buscarRequisicaoPorId(requisicaoId);

        Requisicao requisicaoEscolhida;

        if (requisicao.isPresent()) {

            requisicaoEscolhida = requisicao.get();
        } else {

            MessageHelper.erro("Requisição com ID: " + requisicaoId + ", não encontrado. Tente novamente.");
            return;
        }

        // Atender Requisição
        requisicaoDAO.atenderRequisicao(requisicaoEscolhida);
    }
}