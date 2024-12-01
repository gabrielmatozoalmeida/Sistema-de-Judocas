package org.fpij.jitakyoei.util;

import java.util.Date;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import org.fpij.jitakyoei.facade.AppFacade;
import org.fpij.jitakyoei.facade.AppFacadeImpl;
import org.fpij.jitakyoei.model.beans.Endereco;
import org.fpij.jitakyoei.model.beans.Entidade;
import org.fpij.jitakyoei.model.beans.Filiado;
import org.fpij.jitakyoei.model.beans.Professor;
import org.fpij.jitakyoei.model.dao.DAO;
import org.fpij.jitakyoei.model.dao.DAOImpl;
import org.fpij.jitakyoei.view.AppView;
import org.fpij.jitakyoei.view.MainAppView;

public class Main {

    public static void main(String[] args) {
        configureLookAndFeel();

        // Inicializa a aplicação
        AppView view = new MainAppView();
        AppFacade facade = new AppFacadeImpl(view);
        view.registerFacade(facade);
    }

    /**
     * Configura o LookAndFeel da aplicação, com fallback para o padrão do sistema
     * caso o Nimbus não esteja disponível.
     */
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
                System.exit(1); // Saída segura caso não seja possível aplicar nenhum LookAndFeel
            }
        }
    }

    /**
     * Popula o banco de dados com dados de exemplo.
     */
    public static void dbPopulator() {
        // Configuração de endereço
        Endereco endereco = new Endereco();
        endereco.setBairro("Dirceu");
        endereco.setCep("64078-213");
        endereco.setCidade("Teresina");
        endereco.setEstado("PI");
        endereco.setRua("Rua Des. Berilo Mota");

        // Configuração de filiado (Professor)
        Filiado filiadoProf = new Filiado();
        filiadoProf.setNome("Neto");
        filiadoProf.setCpf("036.464.453-27");
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

        // Persistência dos dados no banco
        DAO<Professor> professorDao = new DAOImpl<>(Professor.class);
        professorDao.save(professor);

        DAO<Entidade> entidadeDao = new DAOImpl<>(Entidade.class);
        entidadeDao.save(entidade);

    }
}
