package br.com.fiap.esg_residuos.repository;

import br.com.fiap.esg_residuos.model.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
}
