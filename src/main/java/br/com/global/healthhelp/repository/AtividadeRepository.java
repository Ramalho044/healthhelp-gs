package br.com.global.healthhelp.repository;

import br.com.global.healthhelp.model.Atividade;
import br.com.global.healthhelp.model.RegistroDiario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtividadeRepository extends JpaRepository<Atividade, Long> {

    List<Atividade> findByRegistro(RegistroDiario registro);
}
