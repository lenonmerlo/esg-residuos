package br.com.fiap.esg_residuos.service;

import br.com.fiap.esg_residuos.dto.request.CollectionRequest;
import br.com.fiap.esg_residuos.dto.response.CollectionResponse;
import br.com.fiap.esg_residuos.exception.BusinessException;
import br.com.fiap.esg_residuos.exception.ResourceNotFoundException;
import br.com.fiap.esg_residuos.model.Collection;
import br.com.fiap.esg_residuos.repository.CollectionPointRepository;
import br.com.fiap.esg_residuos.repository.CollectionRepository;
import br.com.fiap.esg_residuos.repository.WasteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository repository;
    private final CollectionPointRepository collectionPointRepository;
    private final WasteRepository wasteRepository;

    public List<CollectionResponse> findAll() {
        return repository.findAll().stream()
                .map(CollectionResponse::from)
                .toList();
    }

    public CollectionResponse findById(Long id) {
        return repository.findById(id)
                .map(CollectionResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found: " + id));
    }

    public CollectionResponse create(CollectionRequest dto) {
        var point = collectionPointRepository.findById(dto.collectionPointId())
                .orElseThrow(() -> new ResourceNotFoundException("Collection point not found: " + dto.collectionPointId()));

        var waste = wasteRepository.findById(dto.wasteId())
                .orElseThrow(() -> new ResourceNotFoundException("Waste not found: " + dto.wasteId()));

        var projected = point.getOccupiedVolumeKg().add(dto.volumeKg());

        if (projected.compareTo(point.getCapacityKg()) > 0)
            throw new BusinessException("Collection point capacity exceeded");

        var collection = Collection.builder()
                .collectionPoint(point)
                .waste(waste)
                .collectionDate(LocalDate.now())
                .volumeKg(dto.volumeKg())
                .status("ABERTA")
                .build();

        return CollectionResponse.from(repository.save(collection));
    }

    public CollectionResponse update(Long id, CollectionRequest dto) {
        var collection = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found: " + id));

        if (!collection.getStatus().equals("ABERTA"))
            throw new BusinessException("Only open collections can be updated");

        var point = collectionPointRepository.findById(dto.collectionPointId())
                .orElseThrow(() -> new ResourceNotFoundException("Collection point not found: " + dto.collectionPointId()));

        var waste = wasteRepository.findById(dto.wasteId())
                .orElseThrow(() -> new ResourceNotFoundException("Waste not found: " + dto.wasteId()));

        collection.setCollectionPoint(point);
        collection.setWaste(waste);
        collection.setVolumeKg(dto.volumeKg());

        return CollectionResponse.from(repository.save(collection));
    }

    public void delete(Long id) {
        var collection = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found: " + id));

        if (!collection.getStatus().equals("ABERTA"))
            throw new BusinessException("Only open collections can be cancelled");

        collection.setStatus("CANCELADA");
        repository.save(collection);
    }
}