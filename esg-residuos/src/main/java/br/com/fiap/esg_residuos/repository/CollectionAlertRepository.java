package br.com.fiap.esg_residuos.repository;

import br.com.fiap.esg_residuos.model.CollectionAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionAlertRepository extends JpaRepository<CollectionAlert, Long> {

    List<CollectionAlert> findByCollectionPointId(Long collectionPointId);
}
