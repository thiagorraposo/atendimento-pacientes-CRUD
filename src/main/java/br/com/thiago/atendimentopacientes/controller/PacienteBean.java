package br.com.thiago.atendimentopacientes.controller;

import br.com.thiago.atendimentopacientes.exception.RegraNegocioException;
import br.com.thiago.atendimentopacientes.model.Paciente;
import br.com.thiago.atendimentopacientes.service.PacienteService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component("pacienteBean")
@Scope("view")
public class PacienteBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final PacienteService pacienteService;

    private Paciente paciente;
    private List<Paciente> pacientes;
    private Long pacienteId;
    private String nomeBusca;

    public PacienteBean(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostConstruct
    public void iniciar() {
        limparFormulario();
        carregarPacientes();
    }

    public void carregarPacientes() {
        pacientes = pacienteService.listarTodos();
    }

    public void buscarPorNome() {
        if (estaEmBranco(nomeBusca)) {
            carregarPacientes();
            return;
        }

        pacientes = pacienteService.buscarPorNome(nomeBusca);
    }

    public void limparBusca() {
        nomeBusca = null;
        carregarPacientes();
    }

    public void carregarFormulario() {
        if (pacienteId == null) {
            limparFormulario();
            return;
        }

        prepararEdicao(pacienteId);
    }

    public String salvar() {
        try {
            pacienteService.salvar(paciente);
            limparFormulario();
            carregarPacientes();
            adicionarMensagemSucesso("Paciente salvo com sucesso");
            manterMensagensAposRedirect();
            return "/pacientes/listar?faces-redirect=true";
        } catch (RegraNegocioException exception) {
            adicionarMensagemErro(exception.getMessage());
            return null;
        } catch (RuntimeException exception) {
            adicionarMensagemErro("Erro ao salvar paciente");
            return null;
        }
    }

    public String novoPaciente() {
        limparFormulario();
        return "/pacientes/formulario?faces-redirect=true";
    }

    public String prepararEdicao(Long id) {
        try {
            paciente = pacienteService.buscarPorId(id)
                    .orElseThrow(() -> new RegraNegocioException("Paciente nao encontrado"));
            pacienteId = paciente.getId();
            return "/pacientes/formulario?faces-redirect=true&id=" + pacienteId;
        } catch (RegraNegocioException exception) {
            adicionarMensagemErro(exception.getMessage());
            return null;
        }
    }

    public void excluir(Long id) {
        try {
            pacienteService.excluir(id);
            atualizarListagem();
            adicionarMensagemSucesso("Paciente excluido com sucesso");
        } catch (RegraNegocioException exception) {
            adicionarMensagemErro(exception.getMessage());
        } catch (RuntimeException exception) {
            adicionarMensagemErro("Erro ao excluir paciente");
        }
    }

    public void limparFormulario() {
        paciente = new Paciente();
    }

    private void atualizarListagem() {
        if (estaEmBranco(nomeBusca)) {
            carregarPacientes();
            return;
        }

        buscarPorNome();
    }

    private boolean estaEmBranco(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private void adicionarMensagemSucesso(String mensagem) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, null));
    }

    private void adicionarMensagemErro(String mensagem) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null));
    }

    private void manterMensagensAposRedirect() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public List<Paciente> getPacientes() {
        if (pacientes == null) {
            pacientes = new ArrayList<>();
        }
        return pacientes;
    }

    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getNomeBusca() {
        return nomeBusca;
    }

    public void setNomeBusca(String nomeBusca) {
        this.nomeBusca = nomeBusca;
    }
}
