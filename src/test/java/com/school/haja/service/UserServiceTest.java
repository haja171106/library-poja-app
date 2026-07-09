package com.school.haja.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.school.haja.entities.User;
import com.school.haja.repository.UserRepository;
import com.school.haja.repository.model.JUser;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private UserService userService;

  private JUser jUser(UUID id, String first, String last, String email, Instant bday, String adress) {
    return new JUser(id, first, last, email, bday, adress);
  }

  @Test
  void create_shouldSaveAndReturnUser() {
    UUID id = UUID.randomUUID();
    Instant bday = Instant.parse("1990-01-01T00:00:00Z");
    User user = new User(id, "John", "Doe", "john@doe.com", bday, "1 rue de Paris");

    when(userRepository.save(any(JUser.class)))
        .thenReturn(jUser(id, "John", "Doe", "john@doe.com", bday, "1 rue de Paris"));

    User result = userService.create(user);

    assertThat(result.getId()).isEqualTo(id);
    assertThat(result.getFirstname()).isEqualTo("John");
    assertThat(result.getLastname()).isEqualTo("Doe");
    assertThat(result.getEmail()).isEqualTo("john@doe.com");
    assertThat(result.getBirthday()).isEqualTo(bday);
    assertThat(result.getAdress()).isEqualTo("1 rue de Paris");
  }

  @Test
  void getById_whenExists_shouldReturnUser() {
    UUID id = UUID.randomUUID();
    when(userRepository.findById(id))
        .thenReturn(Optional.of(jUser(id, "A", "B", "a@b.com", Instant.now(), "addr")));

    User result = userService.getById(id);

    assertThat(result.getEmail()).isEqualTo("a@b.com");
  }

  @Test
  void getById_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(userRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.getById(id))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void getAll_shouldReturnAllUsers() {
    when(userRepository.findAll())
        .thenReturn(
            List.of(
                jUser(UUID.randomUUID(), "A", "B", "a@b.com", Instant.now(), "addr1"),
                jUser(UUID.randomUUID(), "C", "D", "c@d.com", Instant.now(), "addr2")));

    List<User> result = userService.getAll();

    assertThat(result).hasSize(2);
  }

  @Test
  void update_whenExists_shouldUpdateAndReturnUser() {
    UUID id = UUID.randomUUID();
    Instant newBday = Instant.parse("2000-01-01T00:00:00Z");
    JUser existing = jUser(id, "Old", "Name", "old@x.com", Instant.now(), "OldAddr");
    User update = new User(id, "New", "Name2", "new@x.com", newBday, "NewAddr");

    when(userRepository.findById(id)).thenReturn(Optional.of(existing));
    when(userRepository.save(existing)).thenReturn(existing);

    User result = userService.update(id, update);

    assertThat(result.getFirstname()).isEqualTo("New");
    assertThat(result.getLastname()).isEqualTo("Name2");
    assertThat(result.getEmail()).isEqualTo("new@x.com");
    assertThat(result.getBirthday()).isEqualTo(newBday);
    assertThat(result.getAdress()).isEqualTo("NewAddr");
  }

  @Test
  void update_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(userRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(
            () -> userService.update(id, new User(id, "X", "Y", "x@y.com", Instant.now(), "addr")))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void delete_whenExists_shouldDelete() {
    UUID id = UUID.randomUUID();
    when(userRepository.existsById(id)).thenReturn(true);

    userService.delete(id);

    verify(userRepository).deleteById(id);
  }

  @Test
  void delete_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(userRepository.existsById(id)).thenReturn(false);

    assertThatThrownBy(() -> userService.delete(id))
        .isInstanceOf(EntityNotFoundException.class);

    verify(userRepository, never()).deleteById(any());
  }
}
