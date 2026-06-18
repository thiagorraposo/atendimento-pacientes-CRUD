package br.com.thiago.atendimentopacientes.repository;

import br.com.thiago.atendimentopacientes.model.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

    List<Atendimento> findByPacienteId(Long pacienteId);

    List<Atendimento> findByStatusIgnoreCase(String status);

    List<Atendimento> findByDataAtendimento(LocalDate dataAtendimento);
}
