package br.com.fiap.esg_residuos.repository;

import br.com.fiap.esg_residuos.model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
}
