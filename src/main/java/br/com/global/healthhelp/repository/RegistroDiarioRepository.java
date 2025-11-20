package br.com.global.healthhelp.repository;

import br.com.global.healthhelp.model.RegistroDiario;
import br.com.global.healthhelp.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RegistroDiarioRepository extends JpaRepository<RegistroDiario, Long> {

    Page<RegistroDiario> findByUsuario(Usuario usuario, Pageable pageable);

}