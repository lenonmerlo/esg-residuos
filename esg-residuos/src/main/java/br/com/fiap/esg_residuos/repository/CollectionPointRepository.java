package br.com.fiap.esg_residuos.repository;

import br.com.fiap.esg_residuos.model.CollectionPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionPointRepository extends JpaRepository<CollectionPoint, Long> {
}
