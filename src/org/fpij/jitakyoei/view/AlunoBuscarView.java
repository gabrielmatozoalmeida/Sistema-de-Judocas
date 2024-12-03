package org.fpij.jitakyoei.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import org.fpij.jitakyoei.facade.AppFacade;
import org.fpij.jitakyoei.model.beans.Aluno;
import org.fpij.jitakyoei.model.beans.Filiado;
import org.fpij.jitakyoei.view.forms.CamposBuscaForm;
import org.fpij.jitakyoei.view.gui.AlunoBuscarPanel;

public class AlunoBuscarView implements ViewComponent {

    private AlunoBuscarPanel gui;
    private AppFacade facade;
    private CamposBuscaForm campoBusca;
    private DefaultTableModel alunoTableModel;
    private List<Aluno> alunoList;
    private Aluno aluno;
    private Aluno selectedAluno;
    public int MODO;
    
    public static int ALTERACAO = 1;
    public static int BUSCA = 2;
    
    public AlunoBuscarView(int MODO) {
        this();
        this.MODO = MODO;
    }
    
    public AlunoBuscarView() {
        gui = new AlunoBuscarPanel();
        gui.registerView(this);
        gui.getBuscar().addActionListener(new BuscarActionHandler());
        campoBusca = new CamposBuscaForm(gui.getBuscaCamposPanel());
        alunoTableModel = (DefaultTableModel) gui.getAlunoTable().getModel();
    }
    
    @Override
    public JPanel getGui() {
        return gui;
    }
    
    @Override
    public void registerFacade(AppFacade fac) {
        this.facade = fac;
        buscar();
    }

    public void buscar() {
        Filiado filiado = new Filiado();
        
        if (campoBusca.getNome() != null && !campoBusca.getNome().trim().equals("")) {
            filiado.setNome(campoBusca.getNome());
        }
        
        if (campoBusca.getRegistroFpij() != null && !campoBusca.getRegistroFpij().trim().equals("")) {
            try {
                filiado.setId(Long.parseLong(campoBusca.getRegistroFpij()));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(gui, 
                    "Nº de Registro inválido! No registro só pode haver números.");
                return;
            }
        }
        
        Aluno aluno = new Aluno();
        aluno.setFiliado(filiado);
        alunoList = facade.searchAluno(aluno);
        setAlunoList(alunoList);
    }
    
    private void setAlunoList(List<Aluno> alunoList) {
        if (alunoList == null || alunoList.isEmpty()) {
            JOptionPane.showMessageDialog(gui, 
                "Nenhum aluno encontrado ou lista de alunos não inicializada.");
            return;
        }
        
        Object[][] data = new Object[alunoList.size()][4];
        for (int i = 0; i < alunoList.size(); i++) {
            try {
                Aluno aluno = alunoList.get(i);
                data[i][0] = aluno.getFiliado() != null ? aluno.getFiliado().getId() : "N/A";
                data[i][1] = aluno.getFiliado() != null ? aluno.getFiliado().getNome() : "N/A";
                data[i][2] = aluno.getProfessor() != null && aluno.getProfessor().getFiliado() != null 
                             ? aluno.getProfessor().getFiliado().getNome() : "N/A";
                data[i][3] = aluno.getEntidade() != null ? aluno.getEntidade().getNome() : "N/A";
            } catch (Exception e) {
                System.err.println("Erro ao processar aluno na posição " + i + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        if (alunoTableModel != null) {
            alunoTableModel.setDataVector(data, new String[] { 
                "Registro", "Nome", "Professor", "Entidade" });
        } else {
            System.err.println("alunoTableModel não foi inicializado.");
        }
    }

    public class BuscarActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            buscar();
        }
    }

    public List<Aluno> getAlunoList() {
        return alunoList;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Aluno getSelectedAluno() {
        return selectedAluno;
    }

    public void setSelectedAluno(Aluno selectedAluno) {
        this.selectedAluno = selectedAluno;
    }
}
