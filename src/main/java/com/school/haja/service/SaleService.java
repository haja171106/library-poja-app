package com.school.haja.service;

import com.school.haja.entities.Sale;
import com.school.haja.repository.SaleRepository;
import com.school.haja.repository.model.JSale;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Couche service pour l'entité {@link Sale}. */
@Service
@AllArgsConstructor
@Transactional
public class SaleService {

  private final SaleRepository saleRepository;

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

    return toDomain(saleRepository.save(existing));
  }

  public void delete(UUID id) {
    if (!saleRepository.existsById(id)) {
      throw new EntityNotFoundException("Sale not found: " + id);
    }
    saleRepository.deleteById(id);
  }

  private JSale toEntity(Sale sale) {
    return new JSale(sale.getId(), sale.getPrice(), sale.getNbr_sale(), sale.getStatus());
  }

  private Sale toDomain(JSale entity) {
    return new Sale(entity.getId(), entity.getPrice(), entity.getNbr_sale(), entity.getStatus());
  }
}
