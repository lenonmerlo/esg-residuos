package br.com.fiap.esg_residuos.service;

import br.com.fiap.esg_residuos.dto.request.WasteRequest;
import br.com.fiap.esg_residuos.dto.response.WasteResponse;
import br.com.fiap.esg_residuos.exception.ResourceNotFoundException;
import br.com.fiap.esg_residuos.model.Waste;
import br.com.fiap.esg_residuos.repository.WasteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WasteService {

    private final WasteRepository repository;

    public List<WasteResponse> findAll() {
        return repository.findAll().stream()
                .map(WasteResponse::from)
                .toList();
    }

    public WasteResponse findById(Long id) {
        return repository.findById(id)
                .map(WasteResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Waste not found: " + id));
    }

    public WasteResponse create(WasteRequest dto) {
        var waste = Waste.builder()
                .wasteType(dto.wasteType())
                .description(dto.description())
                .build();
        return WasteResponse.from(repository.save(waste));
    }

    public WasteResponse update(Long id, WasteRequest dto) {
        var waste = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Waste not found: " + id));
        waste.setWasteType(dto.wasteType());
        waste.setDescription(dto.description());
        return WasteResponse.from(repository.save(waste));
    }

    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Waste not found: " + id);
        repository.deleteById(id);
    }


}
