package com.school.haja.service;

import com.school.haja.entities.Sale;
import com.school.haja.repository.LibraryRepository;
import com.school.haja.repository.SaleRepository;
import com.school.haja.repository.model.JLibrary;
import com.school.haja.repository.model.JSale;
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
public class SaleService {

  private final SaleRepository saleRepository;
  private final LibraryRepository libraryRepository;

  public Sale create(Sale sale) {
    JSale saved = saleRepository.save(toEntity(sale));
    return toDomain(saved);
  }

  public Sale getById(UUID id) {
    return saleRepository
        .findById(id)
        .map(this::toDomain)
        .orElseThrow(() -> new EntityNotFoundException("Sale not found: " + id));
  }

  public List<Sale> getAll() {
    return saleRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
  }

  public Sale update(UUID id, Sale sale) {
    JSale existing =
        saleRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Sale not found: " + id));

    existing.setPrice(sale.getPrice());
    existing.setNbr_sale(sale.getNbr_sale());
    existing.setStatus(sale.getStatus());
    existing.setFormat(sale.getFormat()); // ← nouveau

    return toDomain(saleRepository.save(existing));
  }

  public void delete(UUID id) {
    if (!saleRepository.existsById(id)) {
      throw new EntityNotFoundException("Sale not found: " + id);
    }
    saleRepository.deleteById(id);
  }

  private JSale toEntity(Sale sale) {
    JLibrary library =
        libraryRepository
            .findById(sale.getLibraryId())
            .orElseThrow(
                () -> new EntityNotFoundException("Library not found: " + sale.getLibraryId()));

    JSale entity = new JSale();
    entity.setId(sale.getId());
    entity.setPrice(sale.getPrice());
    entity.setNbr_sale(sale.getNbr_sale());
    entity.setStatus(sale.getStatus());
    entity.setFormat(sale.getFormat());
    entity.setLibrary(library);
    return entity;
  }

  private Sale toDomain(JSale entity) {
    return new Sale(
        entity.getId(),
        entity.getPrice(),
        entity.getNbr_sale(),
        entity.getStatus(),
        entity.getFormat(),
        entity.getLibrary().getId());
  }
}
