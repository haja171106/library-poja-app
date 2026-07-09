package com.school.haja.service;

import com.school.haja.dto.GenreRevenueResponse;
import com.school.haja.entities.Sale;
import com.school.haja.entities.SaleStatus;
import com.school.haja.repository.BookEditionRepository;
import com.school.haja.repository.LibraryRepository;
import com.school.haja.repository.SaleRepository;
import com.school.haja.repository.model.JBookEdition;
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
  private final BookEditionRepository bookEditionRepository;

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
    existing.setFormat(sale.getFormat());
    existing.setBookEdition(resolveBookEdition(sale.getBookEditionId()));

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
    entity.setBookEdition(resolveBookEdition(sale.getBookEditionId()));
    return entity;
  }

  public List<GenreRevenueResponse> getRevenueByGenre(UUID libraryId) {
    return saleRepository.sumRevenueByGenre(libraryId, SaleStatus.DONE).stream()
            .map(r -> new GenreRevenueResponse(r.getGenreId(), r.getGenreType(), r.getRevenue()))
            .collect(Collectors.toList());
  }

  private JBookEdition resolveBookEdition(UUID bookEditionId) {
    if (bookEditionId == null) {
      return null;
    }
    return bookEditionRepository
            .findById(bookEditionId)
            .orElseThrow(() -> new EntityNotFoundException("BookEdition not found: " + bookEditionId));
  }

  private Sale toDomain(JSale entity) {
    return new Sale(
            entity.getId(),
            entity.getPrice(),
            entity.getNbr_sale(),
            entity.getStatus(),
            entity.getFormat(),
            entity.getLibrary().getId(),
            entity.getBookEditionId());
  }
}