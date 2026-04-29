package br.com.fiap.esg_residuos.service;

import br.com.fiap.esg_residuos.dto.request.CollectionPointRequest;
import br.com.fiap.esg_residuos.dto.response.CollectionPointResponse;
import br.com.fiap.esg_residuos.exception.ResourceNotFoundException;
import br.com.fiap.esg_residuos.model.CollectionPoint;
import br.com.fiap.esg_residuos.repository.CollectionPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionPointService {

    private final CollectionPointRepository repository;

    public List<CollectionPointResponse> findAll() {
        return repository.findAll().stream()
                .map(CollectionPointResponse::from)
                .toList();
    }

    public CollectionPointResponse findById(Long id) {
        return repository.findById(id)
                .map(CollectionPointResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Colletion point not found: " + id));
    }

    public CollectionPointResponse create(CollectionPointRequest dto) {
        var point = CollectionPoint.builder()
                .name(dto.name())
                .capacityKg(dto.capacityKg())
                .alertVolumeKg(dto.alertVolumeKg())
                .occupiedVolumeKg(BigDecimal.ZERO)
                .status("DISPONIVEL")
                .updatedAt(LocalDate.now())
                .build();
        return CollectionPointResponse.from(repository.save(point));
    }

    public CollectionPointResponse update(Long id, CollectionPointRequest dto) {
        var point = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collection point not found: " + id));
        point.setName(dto.name());
        point.setCapacityKg(dto.capacityKg());
        point.setAlertVolumeKg(dto.alertVolumeKg());
        point.setUpdatedAt(LocalDate.now());
        return CollectionPointResponse.from(repository.save(point));
    }

    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Collection point not found: " + id);
        repository.deleteById(id);
    }
}
