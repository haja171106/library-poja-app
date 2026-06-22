package com.school.haja.service;

import com.school.haja.entities.Arrival;
import com.school.haja.repository.ArrivalRepository;
import com.school.haja.repository.LibraryRepository;
import com.school.haja.repository.model.JArrival;
import com.school.haja.repository.model.JLibrary;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class ArrivalService {

  private final ArrivalRepository arrivalRepository;
  private final LibraryRepository libraryRepository;

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
    existing.setFormat(arrival.getFormat()); // ← ajout

    return toDomain(arrivalRepository.save(existing));
  }

  public void delete(UUID id) {
    if (!arrivalRepository.existsById(id)) {
      throw new EntityNotFoundException("Arrival not found: " + id);
    }
    arrivalRepository.deleteById(id);
  }

  private JArrival toEntity(Arrival arrival) {
    JLibrary library =
        libraryRepository
            .findById(arrival.getLibraryId())
            .orElseThrow(
                () -> new EntityNotFoundException("Library not found: " + arrival.getLibraryId()));

    JArrival entity = new JArrival();
    entity.setId(arrival.getId());
    entity.setNbr_book(arrival.getNbr_book());
    entity.setFormat(arrival.getFormat()); // ← ajout
    entity.setLibrary(library); // ← ajout
    return entity;
  }

  private Arrival toDomain(JArrival entity) {
    return new Arrival(
        entity.getId(),
        entity.getNbr_book(),
        entity.getFormat(), // ← ajout
        entity.getLibrary().getId() // ← ajout
        );
  }
}
