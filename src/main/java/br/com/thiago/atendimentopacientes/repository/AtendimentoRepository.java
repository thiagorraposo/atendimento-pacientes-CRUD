package br.com.thiago.atendimentopacientes.repository;

import br.com.thiago.atendimentopacientes.model.Atendimento;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

    @Override
    @EntityGraph(attributePaths = "paciente")
    List<Atendimento> findAll();

    @Override
    @EntityGraph(attributePaths = "paciente")
    Optional<Atendimento> findById(Long id);

    @EntityGraph(attributePaths = "paciente")
    List<Atendimento> findByPacienteId(Long pacienteId);

    @EntityGraph(attributePaths = "paciente")
    List<Atendimento> findByStatusIgnoreCase(String status);

    @EntityGraph(attributePaths = "paciente")
    List<Atendimento> findByDataAtendimento(LocalDate dataAtendimento);
}
