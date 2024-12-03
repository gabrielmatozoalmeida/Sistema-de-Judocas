package org.fpij.jitakyoei.model.beans;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.Date;

public class Faixa {

    private String cor;
    private Date dataEntrega;

    // Construtores
    public Faixa() {
        super();
    }

    public Faixa(String cor, Date dataEntrega) {
        super();
        this.cor = cor;
        this.dataEntrega = dataEntrega;
    }

    // Getters e Setters
    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Date getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    // Método toString
    @Override
    public String toString() {
        return cor + " - " + dataEntrega;
    }

    // Validação de Faixa
    public boolean validarFaixa() {
        return cor != null && !cor.isEmpty() && dataEntrega != null && dataEntrega.before(new Date());
    }

    // Atualizar a faixa no banco de dados
    public boolean atualizarFaixa(Session session) {
        Transaction transaction = null;
        try {
            // Inicia uma transação
            transaction = session.beginTransaction();

            // Valida a faixa antes da atualização
            if (!this.validarFaixa()) {
                throw new IllegalArgumentException("Dados da faixa inválidos.");
            }

            // Atualiza a faixa no banco de dados
            session.update(this);

            // Confirma a transação
            transaction.commit();

            // Recarrega a interface com os dados atualizados
            recarregarInterface();

            return true;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Reverte alterações em caso de erro
            }
            System.err.println("Erro ao atualizar faixa: " + e.getMessage());
            return false;
        }
    }

    // Método para recarregar a interface
    private void recarregarInterface() {
        // Simula a recarga da interface
        System.out.println("Interface recarregada com dados atualizados: " + this);
    }

    // Hibernate Utility para gerenciar sessões
    static class HibernateUtil {
        private static final org.hibernate.SessionFactory sessionFactory = buildSessionFactory();

        private static org.hibernate.SessionFactory buildSessionFactory() {
            try {
                return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
            } catch (Throwable ex) {
                throw new ExceptionInInitializerError("Falha ao criar o SessionFactory: " + ex);
            }
        }

        public static org.hibernate.SessionFactory getSessionFactory() {
            return sessionFactory;
        }

        public static void shutdown() {
            getSessionFactory().close();
        }
    }

    // Exemplo de uso
    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Faixa faixa = new Faixa("Preta", new Date(System.currentTimeMillis() - 86400000L)); // Data de ontem
            if (faixa.atualizarFaixa(session)) {
                System.out.println("Faixa atualizada com sucesso.");
            } else {
                System.out.println("Falha ao atualizar a faixa.");
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
