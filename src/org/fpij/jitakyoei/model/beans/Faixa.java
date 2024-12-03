package org.fpij.jitakyoei.model.beans;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.fpij.jitakyoei.util.CorFaixa;
import org.hibernate.cfg.Configuration;

import java.util.Date;

public class Faixa {

    private CorFaixa cor;
    private Date dataEntrega;

    // Construtores
    public Faixa() {
        super();
    }

    public Faixa(CorFaixa cor, Date dataEntrega) {
        super();
        this.cor = cor;
        this.dataEntrega = dataEntrega;
    }

    // Getters e Setters
    public CorFaixa getCor() {
        return cor;
    }

    public void setCor(CorFaixa cor) {
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
        return this.cor + " - " + this.dataEntrega;
    }

    // Validação de Faixa
    public boolean isFaixaValida() {
        return cor != null && dataEntrega != null && dataEntrega.before(new Date());
    }

    // Método para atualizar a faixa no banco e recarregar interface
    public void atualizarFaixa(Faixa faixa) throws Exception {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Inicia uma transação
            transaction = session.beginTransaction();

            // Validação de dados
            if (!faixa.isFaixaValida()) {
                throw new IllegalArgumentException("Dados da faixa inválidos.");
            }

            // Atualiza faixa no banco
            session.update(faixa);

            // Confirma a transação
            transaction.commit();

            // Recarrega os dados da interface
            carregarDadosInterface(faixa);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Reverte alterações em caso de erro
            }
            throw new Exception("Erro ao atualizar faixa: " + e.getMessage(), e);
        }
    }

    // Método para recarregar a interface com os dados atualizados
    private void carregarDadosInterface(Faixa faixa) {
        System.out.println("Dados atualizados: " + faixa);
        // Aqui, você pode integrar com a lógica real da interface gráfica
    }

    // Hibernate Util para gerenciar sessões
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
}
