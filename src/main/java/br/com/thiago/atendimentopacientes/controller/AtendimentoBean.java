package br.com.thiago.atendimentopacientes.controller;

import br.com.thiago.atendimentopacientes.exception.RegraNegocioException;
import br.com.thiago.atendimentopacientes.model.Atendimento;
import br.com.thiago.atendimentopacientes.model.Paciente;
import br.com.thiago.atendimentopacientes.service.AtendimentoService;
import br.com.thiago.atendimentopacientes.service.PacienteService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component("atendimentoBean")
@Scope("view")
public class AtendimentoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final AtendimentoService atendimentoService;
    private final PacienteService pacienteService;

    private Atendimento atendimento;
    private List<Atendimento> atendimentos;
    private List<Paciente> pacientes;
    private Long atendimentoId;
    private Long pacienteIdSelecionado;

    public AtendimentoBean(AtendimentoService atendimentoService, PacienteService pacienteService) {
        this.atendimentoService = atendimentoService;
        this.pacienteService = pacienteService;
    }

    @PostConstruct
    public void iniciar() {
        limparFormulario();
        carregarPacientes();
        carregarAtendimentos();
    }

    public void carregarAtendimentos() {
        atendimentos = atendimentoService.listarTodos();
    }

    public void carregarPacientes() {
        pacientes = pacienteService.listarTodos();
    }

    public void carregarFormulario() {
        carregarPacientes();

        if (atendimentoId == null) {
            limparFormulario();
            return;
        }

        carregarAtendimentoParaEdicao(atendimentoId);
    }

    public String novoAtendimento() {
        limparFormulario();
        return "/atendimentos/formulario?faces-redirect=true";
    }

    public String prepararEdicao(Long id) {
        try {
            carregarAtendimentoParaEdicao(id);
            return "/atendimentos/formulario?faces-redirect=true&id=" + id;
        } catch (RegraNegocioException exception) {
            adicionarMensagemErro(exception.getMessage());
            return null;
        } catch (RuntimeException exception) {
            adicionarMensagemErro("Erro ao carregar atendimento");
            return null;
        }
    }

    public String salvar() {
        try {
            vincularPacienteSelecionado();
            atendimentoService.salvar(atendimento);
            limparFormulario();
            carregarAtendimentos();
            adicionarMensagemSucesso("Atendimento salvo com sucesso");
            manterMensagensAposRedirect();
            return "/atendimentos/listar?faces-redirect=true";
        } catch (RegraNegocioException exception) {
            adicionarMensagemErro(exception.getMessage());
            return null;
        } catch (RuntimeException exception) {
            adicionarMensagemErro("Erro ao salvar atendimento");
            return null;
        }
    }

    public void excluir(Long id) {
        try {
            atendimentoService.excluir(id);
            carregarAtendimentos();
            adicionarMensagemSucesso("Atendimento excluido com sucesso");
        } catch (RegraNegocioException exception) {
            adicionarMensagemErro(exception.getMessage());
        } catch (RuntimeException exception) {
            adicionarMensagemErro("Erro ao excluir atendimento");
        }
    }

    public void limparFormulario() {
        atendimento = new Atendimento();
        pacienteIdSelecionado = null;
    }

    private void carregarAtendimentoParaEdicao(Long id) {
        atendimento = atendimentoService.buscarPorId(id)
                .orElseThrow(() -> new RegraNegocioException("Atendimento nao encontrado"));
        atendimentoId = atendimento.getId();

        if (atendimento.getPaciente() != null) {
            pacienteIdSelecionado = atendimento.getPaciente().getId();
        }
    }

    private void vincularPacienteSelecionado() {
        if (pacienteIdSelecionado == null) {
            atendimento.setPaciente(null);
            return;
        }

        Paciente paciente = new Paciente();
        paciente.setId(pacienteIdSelecionado);
        atendimento.setPaciente(paciente);
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

    public Atendimento getAtendimento() {
        return atendimento;
    }

    public void setAtendimento(Atendimento atendimento) {
        this.atendimento = atendimento;
    }

    public List<Atendimento> getAtendimentos() {
        if (atendimentos == null) {
            atendimentos = new ArrayList<>();
        }
        return atendimentos;
    }

    public void setAtendimentos(List<Atendimento> atendimentos) {
        this.atendimentos = atendimentos;
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

    public Long getAtendimentoId() {
        return atendimentoId;
    }

    public void setAtendimentoId(Long atendimentoId) {
        this.atendimentoId = atendimentoId;
    }

    public Long getPacienteIdSelecionado() {
        return pacienteIdSelecionado;
    }

    public void setPacienteIdSelecionado(Long pacienteIdSelecionado) {
        this.pacienteIdSelecionado = pacienteIdSelecionado;
    }

    public List<String> getStatusDisponiveis() {
        return Arrays.asList("Aberto", "Em andamento", "Finalizado", "Cancelado");
    }
}
