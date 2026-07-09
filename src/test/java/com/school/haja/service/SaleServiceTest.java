package com.school.haja.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.school.haja.dto.GenreRevenue;
import com.school.haja.dto.GenreRevenueResponse;
import com.school.haja.entities.BookFormat;
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
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

  @Mock private SaleRepository saleRepository;
  @Mock private LibraryRepository libraryRepository;
  @Mock private BookEditionRepository bookEditionRepository;

  @InjectMocks private SaleService saleService;

  private record GenreRevenueFake(UUID genreId, String genreType, Double revenue)
      implements GenreRevenue {
    @Override
    public UUID getGenreId() {
      return genreId;
    }

    @Override
    public String getGenreType() {
      return genreType;
    }

    @Override
    public Double getRevenue() {
      return revenue;
    }
  }

  @Test
  void getRevenueByGenre_withSales_shouldReturnRevenuePerGenre() {
    UUID libraryId = UUID.randomUUID();
    UUID romanceId = UUID.randomUUID();
    UUID sciFiId = UUID.randomUUID();

    when(saleRepository.sumRevenueByGenre(libraryId, SaleStatus.DONE))
        .thenReturn(
            List.of(
                new GenreRevenueFake(romanceId, "Romance", 150.0),
                new GenreRevenueFake(sciFiId, "Science-Fiction", 90.0)));

    List<GenreRevenueResponse> result = saleService.getRevenueByGenre(libraryId, null);

    assertThat(result)
        .containsExactly(
            new GenreRevenueResponse(romanceId, "Romance", 150.0),
            new GenreRevenueResponse(sciFiId, "Science-Fiction", 90.0));
  }

  @Test
  void getRevenueByGenre_withNoSales_shouldReturnEmptyList() {
    UUID libraryId = UUID.randomUUID();

    when(saleRepository.sumRevenueByGenre(libraryId, SaleStatus.DONE)).thenReturn(List.of());

    List<GenreRevenueResponse> result = saleService.getRevenueByGenre(libraryId, null);

    assertThat(result).isEmpty();
  }

  private JLibrary jLibrary(UUID id) {
    JLibrary library = new JLibrary();
    library.setId(id);
    return library;
  }

  private JBookEdition jBookEdition(UUID id) {
    JBookEdition edition = new JBookEdition();
    edition.setId(id);
    return edition;
  }

  private JSale jSale(
      UUID id, double price, int nbrSale, SaleStatus status, BookFormat format, UUID libraryId) {
    JSale sale = new JSale();
    sale.setId(id);
    sale.setPrice(price);
    sale.setNbr_sale(nbrSale);
    sale.setStatus(status);
    sale.setFormat(format);
    sale.setLibrary(jLibrary(libraryId));
    return sale;
  }

  @Test
  void create_withoutBookEdition_shouldSaveAndReturnSale() {
    UUID id = UUID.randomUUID();
    UUID libraryId = UUID.randomUUID();
    Sale sale = new Sale(id, 15.0, 2, SaleStatus.DONE, BookFormat.PAPERBACK, libraryId, null);

    when(libraryRepository.findById(libraryId)).thenReturn(Optional.of(jLibrary(libraryId)));
    when(saleRepository.save(any(JSale.class)))
        .thenReturn(jSale(id, 15.0, 2, SaleStatus.DONE, BookFormat.PAPERBACK, libraryId));

    Sale result = saleService.create(sale);

    assertThat(result.getId()).isEqualTo(id);
    assertThat(result.getPrice()).isEqualTo(15.0);
    assertThat(result.getNbr_sale()).isEqualTo(2);
    assertThat(result.getStatus()).isEqualTo(SaleStatus.DONE);
    assertThat(result.getFormat()).isEqualTo(BookFormat.PAPERBACK);
    assertThat(result.getLibraryId()).isEqualTo(libraryId);
  }

  @Test
  void create_withBookEdition_shouldResolveBookEditionAndSave() {
    UUID id = UUID.randomUUID();
    UUID libraryId = UUID.randomUUID();
    UUID bookEditionId = UUID.randomUUID();
    Sale sale =
        new Sale(id, 20.0, 1, SaleStatus.BOOKED, BookFormat.HARDCOVER, libraryId, bookEditionId);

    when(libraryRepository.findById(libraryId)).thenReturn(Optional.of(jLibrary(libraryId)));
    when(bookEditionRepository.findById(bookEditionId))
        .thenReturn(Optional.of(jBookEdition(bookEditionId)));
    when(saleRepository.save(any(JSale.class)))
        .thenReturn(jSale(id, 20.0, 1, SaleStatus.BOOKED, BookFormat.HARDCOVER, libraryId));

    Sale result = saleService.create(sale);

    assertThat(result.getId()).isEqualTo(id);
    verify(bookEditionRepository).findById(bookEditionId);
  }

  @Test
  void create_whenLibraryMissing_shouldThrow() {
    UUID libraryId = UUID.randomUUID();
    Sale sale =
        new Sale(UUID.randomUUID(), 1.0, 1, SaleStatus.DONE, BookFormat.PAPERBACK, libraryId, null);

    when(libraryRepository.findById(libraryId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> saleService.create(sale))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("Library not found");
  }

  @Test
  void create_whenBookEditionMissing_shouldThrow() {
    UUID libraryId = UUID.randomUUID();
    UUID bookEditionId = UUID.randomUUID();
    Sale sale =
        new Sale(
            UUID.randomUUID(),
            1.0,
            1,
            SaleStatus.DONE,
            BookFormat.PAPERBACK,
            libraryId,
            bookEditionId);

    when(libraryRepository.findById(libraryId)).thenReturn(Optional.of(jLibrary(libraryId)));
    when(bookEditionRepository.findById(bookEditionId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> saleService.create(sale))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("BookEdition not found");
  }

  @Test
  void getById_whenExists_shouldReturnSale() {
    UUID id = UUID.randomUUID();
    UUID libraryId = UUID.randomUUID();
    when(saleRepository.findById(id))
        .thenReturn(
            Optional.of(jSale(id, 9.0, 1, SaleStatus.DONE, BookFormat.SOFTCOVER, libraryId)));

    Sale result = saleService.getById(id);

    assertThat(result.getId()).isEqualTo(id);
    assertThat(result.getFormat()).isEqualTo(BookFormat.SOFTCOVER);
  }

  @Test
  void getById_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(saleRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> saleService.getById(id)).isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void getAll_shouldReturnAllSales() {
    UUID libraryId = UUID.randomUUID();
    when(saleRepository.findAll())
        .thenReturn(
            List.of(
                jSale(UUID.randomUUID(), 1.0, 1, SaleStatus.DONE, BookFormat.PAPERBACK, libraryId),
                jSale(
                    UUID.randomUUID(),
                    2.0,
                    2,
                    SaleStatus.BOOKED,
                    BookFormat.HARDCOVER,
                    libraryId)));

    List<Sale> result = saleService.getAll();

    assertThat(result).hasSize(2);
  }

  @Test
  void update_whenExists_shouldUpdateAndReturnSale() {
    UUID id = UUID.randomUUID();
    UUID libraryId = UUID.randomUUID();
    JSale existing = jSale(id, 1.0, 1, SaleStatus.BOOKED, BookFormat.PAPERBACK, libraryId);
    Sale update = new Sale(id, 50.0, 5, SaleStatus.DONE, BookFormat.HARDCOVER, libraryId, null);

    when(saleRepository.findById(id)).thenReturn(Optional.of(existing));
    when(saleRepository.save(existing)).thenReturn(existing);

    Sale result = saleService.update(id, update);

    assertThat(result.getPrice()).isEqualTo(50.0);
    assertThat(result.getNbr_sale()).isEqualTo(5);
    assertThat(result.getStatus()).isEqualTo(SaleStatus.DONE);
    assertThat(result.getFormat()).isEqualTo(BookFormat.HARDCOVER);
  }

  @Test
  void update_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(saleRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(
            () ->
                saleService.update(
                    id,
                    new Sale(
                        id,
                        1.0,
                        1,
                        SaleStatus.DONE,
                        BookFormat.PAPERBACK,
                        UUID.randomUUID(),
                        null)))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void delete_whenExists_shouldDelete() {
    UUID id = UUID.randomUUID();
    when(saleRepository.existsById(id)).thenReturn(true);

    saleService.delete(id);

    verify(saleRepository).deleteById(id);
  }

  @Test
  void delete_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(saleRepository.existsById(id)).thenReturn(false);

    assertThatThrownBy(() -> saleService.delete(id)).isInstanceOf(EntityNotFoundException.class);

    verify(saleRepository, never()).deleteById(any());
  }
}
