package com.school.haja.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.school.haja.entities.Arrival;
import com.school.haja.entities.BookFormat;
import com.school.haja.repository.ArrivalRepository;
import com.school.haja.repository.LibraryRepository;
import com.school.haja.repository.model.JArrival;
import com.school.haja.repository.model.JLibrary;
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
class ArrivalServiceTest {

  @Mock private ArrivalRepository arrivalRepository;

  @Mock private LibraryRepository libraryRepository;

  @InjectMocks private ArrivalService arrivalService;

  private JArrival jArrival(UUID id, int nbrBook, BookFormat format, UUID libraryId) {
    JLibrary library = new JLibrary();
    library.setId(libraryId);

    JArrival arrival = new JArrival();
    arrival.setId(id);
    arrival.setNbr_book(nbrBook);
    arrival.setFormat(format);
    arrival.setLibrary(library);
    return arrival;
  }

  @Test
  void create_shouldSaveAndReturnArrival() {
    UUID id = UUID.randomUUID();
    UUID libraryId = UUID.randomUUID();
    Arrival arrival = new Arrival(id, 10, BookFormat.PAPERBACK, libraryId);

    when(arrivalRepository.save(any(JArrival.class)))
        .thenReturn(jArrival(id, 10, BookFormat.PAPERBACK, libraryId));

    Arrival result = arrivalService.create(arrival);

    assertThat(result.getId()).isEqualTo(id);
    assertThat(result.getNbr_book()).isEqualTo(10);
    assertThat(result.getFormat()).isEqualTo(BookFormat.PAPERBACK);
    assertThat(result.getLibraryId()).isEqualTo(libraryId);
  }

  @Test
  void getById_whenExists_shouldReturnArrival() {
    UUID id = UUID.randomUUID();
    UUID libraryId = UUID.randomUUID();
    when(arrivalRepository.findById(id))
        .thenReturn(Optional.of(jArrival(id, 5, BookFormat.HARDCOVER, libraryId)));

    Arrival result = arrivalService.getById(id);

    assertThat(result.getNbr_book()).isEqualTo(5);
    assertThat(result.getFormat()).isEqualTo(BookFormat.HARDCOVER);
  }

  @Test
  void getById_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(arrivalRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> arrivalService.getById(id))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void getAll_shouldReturnAllArrivals() {
    when(arrivalRepository.findAll())
        .thenReturn(
            List.of(
                jArrival(UUID.randomUUID(), 1, BookFormat.PAPERBACK, UUID.randomUUID()),
                jArrival(UUID.randomUUID(), 2, BookFormat.SOFTCOVER, UUID.randomUUID())));

    List<Arrival> result = arrivalService.getAll();

    assertThat(result).hasSize(2);
  }

  @Test
  void update_whenExists_shouldUpdateAndReturnArrival() {
    UUID id = UUID.randomUUID();
    UUID libraryId = UUID.randomUUID();
    JArrival existing = jArrival(id, 1, BookFormat.PAPERBACK, libraryId);
    Arrival update = new Arrival(id, 42, BookFormat.HARDCOVER, libraryId);

    when(arrivalRepository.findById(id)).thenReturn(Optional.of(existing));
    when(arrivalRepository.save(existing)).thenReturn(existing);

    Arrival result = arrivalService.update(id, update);

    assertThat(result.getNbr_book()).isEqualTo(42);
    assertThat(result.getFormat()).isEqualTo(BookFormat.HARDCOVER);
  }

  @Test
  void update_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(arrivalRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(
            () ->
                arrivalService.update(
                    id, new Arrival(id, 1, BookFormat.PAPERBACK, UUID.randomUUID())))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void delete_whenExists_shouldDelete() {
    UUID id = UUID.randomUUID();
    when(arrivalRepository.existsById(id)).thenReturn(true);

    arrivalService.delete(id);

    verify(arrivalRepository).deleteById(id);
  }

  @Test
  void delete_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(arrivalRepository.existsById(id)).thenReturn(false);

    assertThatThrownBy(() -> arrivalService.delete(id))
        .isInstanceOf(EntityNotFoundException.class);

    verify(arrivalRepository, never()).deleteById(any());
  }
}
