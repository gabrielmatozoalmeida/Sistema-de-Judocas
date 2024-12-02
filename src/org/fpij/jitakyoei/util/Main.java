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
        // Configuração do LookAndFeel
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao configurar LookAndFeel: " + e.getMessage());
            e.printStackTrace();
        }

        // Inicialização da aplicação
        AppView view = new MainAppView();
        AppFacade facade = new AppFacadeImpl(view);
        view.registerFacade(facade);

        // Populando banco de dados com dados de exemplo
        dbPopulator();
    }

    public static void dbPopulator() {
        // Configuração de endereço com validação
        Endereco endereco = new Endereco();
        String cep = "64078-213";
        if (cep.matches("\\d{5}-\\d{3}")) { // Validação de formato do CEP (XXXXX-XXX)
            endereco.setCep(cep);
        } else {
            System.err.println("CEP inválido: " + cep);
            return;
        }

        endereco.setBairro("Dirceu");
        endereco.setCidade("Teresina");
        endereco.setEstado("PI");
        endereco.setRua("Rua Des. Berilo Mota");

        // Configuração de filiado (Professor) com validação de CPF
        Filiado filiadoProf = new Filiado();
        String cpf = "036.464.453-27";
        if (cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) { // Validação de formato do CPF
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
