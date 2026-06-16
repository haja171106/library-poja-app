package com.school.haja.service;

import com.school.haja.entities.Arrival;
import com.school.haja.repository.ArrivalRepository;
import com.school.haja.repository.model.JArrival;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Couche service pour l'entité {@link Arrival}. */
@Service
@AllArgsConstructor
@Transactional
public class ArrivalService {

    private final ArrivalRepository arrivalRepository;

    public Arrival create(Arrival arrival) {
        JArrival saved = arrivalRepository.save(toEntity(arrival));
        return toDomain(saved);
    }

    public Arrival getById(UUID id) {
        return arrivalRepository
                .findById(id)
                .map(this::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Arrival not found: " + id));
    }

    public List<Arrival> getAll() {
        return arrivalRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    public Arrival update(UUID id, Arrival arrival) {
        JArrival existing =
                arrivalRepository
                        .findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Arrival not found: " + id));

        existing.setNbr_book(arrival.getNbr_book());

        return toDomain(arrivalRepository.save(existing));
    }

    public void delete(UUID id) {
        if (!arrivalRepository.existsById(id)) {
            throw new EntityNotFoundException("Arrival not found: " + id);
        }
        arrivalRepository.deleteById(id);
    }

    private JArrival toEntity(Arrival arrival) {
        return new JArrival(arrival.getId(), arrival.getNbr_book());
    }

    private Arrival toDomain(JArrival entity) {
        return new Arrival(entity.getId(), entity.getNbr_book());
    }
}