package br.com.fiap.esg_residuos.repository;

import br.com.fiap.esg_residuos.model.Waste;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WasteRepository extends JpaRepository<Waste, Long> {
}
