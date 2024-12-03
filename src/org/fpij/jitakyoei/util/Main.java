public class Main {

    public static void main(String[] args) {
        configureLookAndFeel();

        // Inicialização da aplicação
        AppView view = new MainAppView();
        AppFacade facade = new AppFacadeImpl(view);
        view.registerFacade(facade);

        // Populando banco de dados com dados de exemplo
        dbPopulator();
    }

    private static void configureLookAndFeel() {
        try {
            boolean nimbusSet = false;
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    nimbusSet = true;
                    System.out.println("Nimbus LookAndFeel aplicado com sucesso.");
                    break;
                }
            }
            if (!nimbusSet) {
                System.out.println("Nimbus LookAndFeel não disponível. Aplicando padrão do sistema.");
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (Exception e) {
            System.err.println("Erro ao configurar o LookAndFeel. Aplicando o padrão do sistema.");
            e.printStackTrace();
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                System.err.println("Erro ao aplicar o LookAndFeel padrão do sistema. Encerrando aplicação.");
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }

    public static void dbPopulator() {
        // Configuração de endereço
        Endereco endereco = new Endereco();
        String cep = "64078-213";
        if (cep.matches("\\d{5}-\\d{3}")) {
            endereco.setCep(cep);
        } else {
            System.err.println("CEP inválido: " + cep);
            return;
        }
        endereco.setBairro("Dirceu");
        endereco.setCidade("Teresina");
        endereco.setEstado("PI");
        endereco.setRua("Rua Des. Berilo Mota");

        // Configuração de filiado (Professor)
        Filiado filiadoProf = new Filiado();
        String cpf = "036.464.453-27";
        if (cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            filiadoProf.setCpf(cpf);
        } else {
            System.err.println("CPF inválido: " + cpf);
            return;
        }

        filiadoProf.setNome("Neto");
        filiadoProf.setDataNascimento(new Date());
        filiadoProf.setDataCadastro(new Date());
        filiadoProf.setId(3332L);
        filiadoProf.setEndereco(endereco);

        Professor professor = new Professor();
        professor.setFiliado(filiadoProf);

        // Configuração de entidade
        Entidade entidade = new Entidade();
        entidade.setEndereco(endereco);
        entidade.setNome("Ricardo Paraguasu");
        entidade.setTelefone1("(086)1234-5432");

        // Persistência dos dados no banco com tratamento de exceções
        persistData(professor, entidade);
    }

    private static void persistData(Professor professor, Entidade entidade) {
        try {
            DAO<Professor> professorDao = new DAOImpl<>(Professor.class);
            professorDao.save(professor);
            System.out.println("Professor salvo com sucesso no banco de dados.");
        } catch (Exception e) {
            System.err.println("Erro ao salvar professor no banco de dados: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            DAO<Entidade> entidadeDao = new DAOImpl<>(Entidade.class);
            entidadeDao.save(entidade);
            System.out.println("Entidade salva com sucesso no banco de dados.");
        } catch (Exception e) {
            System.err.println("Erro ao salvar entidade no banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
