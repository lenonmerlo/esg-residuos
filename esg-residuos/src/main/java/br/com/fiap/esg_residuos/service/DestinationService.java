package br.com.fiap.esg_residuos.service;

import br.com.fiap.esg_residuos.dto.request.DestinationRequest;
import br.com.fiap.esg_residuos.dto.response.DestinationResponse;
import br.com.fiap.esg_residuos.exception.BusinessException;
import br.com.fiap.esg_residuos.exception.ResourceNotFoundException;
import br.com.fiap.esg_residuos.model.Destination;
import br.com.fiap.esg_residuos.repository.CollectionRepository;
import br.com.fiap.esg_residuos.repository.DestinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final DestinationRepository repository;
    private final CollectionRepository collectionRepository;

    public List<DestinationResponse> findAll() {
        return repository.findAll().stream()
                .map(DestinationResponse::from)
                .toList();
    }

    public DestinationResponse findById(Long id) {
        return repository.findById(id)
                .map(DestinationResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found: " + id));

    }

    public DestinationResponse create(DestinationRequest dto) {
        var collection = collectionRepository.findById(dto.collectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found: " + dto.collectionId()));

        if (!collection.getStatus().equals("ABERTA"))
            throw new BusinessException("Only open collections can be destined");

        if (dto.destinedVolumeKg().compareTo(collection.getVolumeKg()) > 0)
            throw new BusinessException("Destined volume cannot exceed collected volume");

        var destination = Destination.builder()
                .collection(collection)
                .destinationDate(LocalDate.now())
                .destinationName(dto.destinationName())
                .processingType(dto.processingType())
                .destinedVolumeKg(dto.destinedVolumeKg())
                .build();

        collection.setStatus("DESTINADA");
        collection.setDestinationDate(LocalDate.now());
        collectionRepository.save(collection);

        return DestinationResponse.from(repository.save(destination));
    }

    public DestinationResponse update(Long id, DestinationRequest dto) {
        var destination = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found: " + id));

        destination.setDestinationName(dto.destinationName());
        destination.setProcessingType(dto.processingType());
        destination.setDestinedVolumeKg(dto.destinedVolumeKg());

        return DestinationResponse.from(repository.save(destination));
    }
}
