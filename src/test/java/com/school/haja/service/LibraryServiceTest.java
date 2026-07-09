package com.school.haja.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.school.haja.entities.Library;
import com.school.haja.repository.LibraryRepository;
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
class LibraryServiceTest {

  @Mock private LibraryRepository libraryRepository;

  @InjectMocks private LibraryService libraryService;

  private JLibrary jLibrary(UUID id, String name) {
    JLibrary lib = new JLibrary();
    lib.setId(id);
    lib.setName(name);
    return lib;
  }

  @Test
  void create_shouldSaveAndReturnLibrary() {
    UUID id = UUID.randomUUID();
    Library library = new Library(id, "Main Library");
    when(libraryRepository.save(any(JLibrary.class))).thenReturn(jLibrary(id, "Main Library"));

    Library result = libraryService.create(library);

    assertThat(result.getId()).isEqualTo(id);
    assertThat(result.getName()).isEqualTo("Main Library");
  }

  @Test
  void getById_whenExists_shouldReturnLibrary() {
    UUID id = UUID.randomUUID();
    when(libraryRepository.findById(id)).thenReturn(Optional.of(jLibrary(id, "Branch")));

    Library result = libraryService.getById(id);

    assertThat(result.getName()).isEqualTo("Branch");
  }

  @Test
  void getById_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(libraryRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> libraryService.getById(id))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void getAll_shouldReturnAllLibraries() {
    when(libraryRepository.findAll())
        .thenReturn(List.of(jLibrary(UUID.randomUUID(), "A"), jLibrary(UUID.randomUUID(), "B")));

    List<Library> result = libraryService.getAll();

    assertThat(result).hasSize(2);
  }

  @Test
  void update_whenExists_shouldUpdateAndReturnLibrary() {
    UUID id = UUID.randomUUID();
    JLibrary existing = jLibrary(id, "OldName");
    when(libraryRepository.findById(id)).thenReturn(Optional.of(existing));
    when(libraryRepository.save(existing)).thenReturn(existing);

    Library result = libraryService.update(id, new Library(id, "NewName"));

    assertThat(result.getName()).isEqualTo("NewName");
  }

  @Test
  void update_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(libraryRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> libraryService.update(id, new Library(id, "X")))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void delete_whenExists_shouldDelete() {
    UUID id = UUID.randomUUID();
    when(libraryRepository.existsById(id)).thenReturn(true);

    libraryService.delete(id);

    verify(libraryRepository).deleteById(id);
  }

  @Test
  void delete_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(libraryRepository.existsById(id)).thenReturn(false);

    assertThatThrownBy(() -> libraryService.delete(id))
        .isInstanceOf(EntityNotFoundException.class);

    verify(libraryRepository, never()).deleteById(any());
  }
}
