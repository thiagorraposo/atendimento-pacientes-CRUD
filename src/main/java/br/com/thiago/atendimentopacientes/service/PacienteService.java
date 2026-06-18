package br.com.thiago.atendimentopacientes.service;

import br.com.thiago.atendimentopacientes.exception.RegraNegocioException;
import br.com.thiago.atendimentopacientes.model.Paciente;
import br.com.thiago.atendimentopacientes.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional
    public Paciente salvar(Paciente paciente) {
        validarPaciente(paciente);
        validarCpfDuplicadoAoCadastrar(paciente);
        return pacienteRepository.save(paciente);
    }

    @Transactional(readOnly = true)
    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Paciente> buscarPorId(Long id) {
        return pacienteRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Paciente> buscarPorCpf(String cpf) {
        return pacienteRepository.findByCpf(cpf);
    }

    @Transactional(readOnly = true)
    public List<Paciente> buscarPorNome(String nome) {
        return pacienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional
    public void excluir(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new RegraNegocioException("Paciente nao encontrado para exclusao");
        }

        pacienteRepository.deleteById(id);
    }

    private void validarPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new RegraNegocioException("Paciente nao pode ser nulo");
        }

        if (estaEmBranco(paciente.getNome())) {
            throw new RegraNegocioException("Nome do paciente e obrigatorio");
        }

        if (estaEmBranco(paciente.getCpf())) {
            throw new RegraNegocioException("CPF do paciente e obrigatorio");
        }
    }

    private void validarCpfDuplicadoAoCadastrar(Paciente paciente) {
        if (paciente.getId() != null) {
            return;
        }

        if (pacienteRepository.findByCpf(paciente.getCpf()).isPresent()) {
            throw new RegraNegocioException("Ja existe paciente cadastrado com este CPF");
        }
    }

    private boolean estaEmBranco(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
