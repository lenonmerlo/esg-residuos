package br.com.fiap.esg_residuos.service;

import br.com.fiap.esg_residuos.dto.response.CollectionAlertResponse;
import br.com.fiap.esg_residuos.exception.ResourceNotFoundException;
import br.com.fiap.esg_residuos.repository.CollectionAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionAlertService {

    private final CollectionAlertRepository repository;

    public List<CollectionAlertResponse> findAll() {
        return repository.findAll().stream()
                .map(CollectionAlertResponse::from)
                .toList();
    }

    public CollectionAlertResponse findById(Long id) {
        return repository.findById(id)
                .map(CollectionAlertResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found: " + id));
    }

    public List<CollectionAlertResponse> findByCollectionPointId(Long collectionPointId) {
        return repository.findByCollectionPointId(collectionPointId).stream()
                .map(CollectionAlertResponse::from)
                .toList();
    }
}
