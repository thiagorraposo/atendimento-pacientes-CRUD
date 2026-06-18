package br.com.thiago.atendimentopacientes.service;

import br.com.thiago.atendimentopacientes.exception.RegraNegocioException;
import br.com.thiago.atendimentopacientes.model.Atendimento;
import br.com.thiago.atendimentopacientes.model.Paciente;
import br.com.thiago.atendimentopacientes.repository.AtendimentoRepository;
import br.com.thiago.atendimentopacientes.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final PacienteRepository pacienteRepository;

    public AtendimentoService(AtendimentoRepository atendimentoRepository, PacienteRepository pacienteRepository) {
        this.atendimentoRepository = atendimentoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional
    public Atendimento salvar(Atendimento atendimento) {
        validarAtendimento(atendimento);
        Paciente paciente = buscarPacienteDoAtendimento(atendimento);
        atendimento.setPaciente(paciente);
        return atendimentoRepository.save(atendimento);
    }

    @Transactional(readOnly = true)
    public List<Atendimento> listarTodos() {
        return atendimentoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Atendimento> buscarPorId(Long id) {
        if (id == null) {
            throw new RegraNegocioException("Id do atendimento e obrigatorio");
        }

        return atendimentoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Atendimento> listarPorPaciente(Long pacienteId) {
        if (pacienteId == null) {
            throw new RegraNegocioException("Id do paciente e obrigatorio");
        }

        if (!pacienteRepository.existsById(pacienteId)) {
            throw new RegraNegocioException("Paciente nao encontrado");
        }

        return atendimentoRepository.findByPacienteId(pacienteId);
    }

    @Transactional(readOnly = true)
    public List<Atendimento> listarPorStatus(String status) {
        if (estaEmBranco(status)) {
            throw new RegraNegocioException("Status do atendimento e obrigatorio");
        }

        return atendimentoRepository.findByStatusIgnoreCase(status);
    }

    @Transactional
    public void excluir(Long id) {
        if (id == null) {
            throw new RegraNegocioException("Id do atendimento e obrigatorio");
        }

        if (!atendimentoRepository.existsById(id)) {
            throw new RegraNegocioException("Atendimento nao encontrado para exclusao");
        }

        atendimentoRepository.deleteById(id);
    }

    private void validarAtendimento(Atendimento atendimento) {
        if (atendimento == null) {
            throw new RegraNegocioException("Atendimento nao pode ser nulo");
        }

        if (atendimento.getPaciente() == null) {
            throw new RegraNegocioException("Paciente do atendimento e obrigatorio");
        }

        if (atendimento.getPaciente().getId() == null) {
            throw new RegraNegocioException("Id do paciente e obrigatorio");
        }

        if (atendimento.getDataAtendimento() == null) {
            throw new RegraNegocioException("Data do atendimento e obrigatoria");
        }

        if (estaEmBranco(atendimento.getDescricao())) {
            throw new RegraNegocioException("Descricao do atendimento e obrigatoria");
        }

        if (estaEmBranco(atendimento.getStatus())) {
            throw new RegraNegocioException("Status do atendimento e obrigatorio");
        }
    }

    private Paciente buscarPacienteDoAtendimento(Atendimento atendimento) {
        return pacienteRepository.findById(atendimento.getPaciente().getId())
                .orElseThrow(() -> new RegraNegocioException("Paciente nao encontrado"));
    }

    private boolean estaEmBranco(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
