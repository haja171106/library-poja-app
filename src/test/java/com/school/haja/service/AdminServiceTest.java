package com.school.haja.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.school.haja.entities.Admin;
import com.school.haja.repository.AdminRepository;
import com.school.haja.repository.model.JAdmin;
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
class AdminServiceTest {

  @Mock private AdminRepository adminRepository;

  @InjectMocks private AdminService adminService;

  private Admin admin(
      UUID id, String first, String last, String email, Instant bday, String adress) {
    Admin admin = new Admin();
    admin.setId(id);
    admin.setFirstname(first);
    admin.setLastname(last);
    admin.setEmail(email);
    admin.setBirthday(bday);
    admin.setAdress(adress);
    return admin;
  }

  private JAdmin jAdmin(
      UUID id, String first, String last, String email, Instant bday, String adress) {
    JAdmin admin = new JAdmin();
    admin.setId(id);
    admin.setFirstname(first);
    admin.setLastname(last);
    admin.setEmail(email);
    admin.setBirthday(bday);
    admin.setAdress(adress);
    return admin;
  }

  @Test
  void create_shouldSaveAndReturnAdmin() {
    UUID id = UUID.randomUUID();
    Instant bday = Instant.parse("1985-03-03T00:00:00Z");
    when(adminRepository.save(any(JAdmin.class)))
        .thenReturn(jAdmin(id, "Jean", "Dupont", "jean@d.com", bday, "10 rue A"));

    Admin result = adminService.create(admin(id, "Jean", "Dupont", "jean@d.com", bday, "10 rue A"));

    assertThat(result.getId()).isEqualTo(id);
    assertThat(result.getFirstname()).isEqualTo("Jean");
    assertThat(result.getLastname()).isEqualTo("Dupont");
    assertThat(result.getEmail()).isEqualTo("jean@d.com");
    assertThat(result.getBirthday()).isEqualTo(bday);
    assertThat(result.getAdress()).isEqualTo("10 rue A");
  }

  @Test
  void getById_whenExists_shouldReturnAdmin() {
    UUID id = UUID.randomUUID();
    when(adminRepository.findById(id))
        .thenReturn(Optional.of(jAdmin(id, "A", "B", "a@b.com", Instant.now(), "addr")));

    Admin result = adminService.getById(id);

    assertThat(result.getEmail()).isEqualTo("a@b.com");
  }

  @Test
  void getById_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(adminRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> adminService.getById(id)).isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void getAll_shouldReturnAllAdmins() {
    when(adminRepository.findAll())
        .thenReturn(
            List.of(
                jAdmin(UUID.randomUUID(), "A", "B", "a@b.com", Instant.now(), "addr1"),
                jAdmin(UUID.randomUUID(), "C", "D", "c@d.com", Instant.now(), "addr2")));

    List<Admin> result = adminService.getAll();

    assertThat(result).hasSize(2);
  }

  @Test
  void update_whenExists_shouldUpdateAndReturnAdmin() {
    UUID id = UUID.randomUUID();
    Instant newBday = Instant.parse("1995-01-01T00:00:00Z");
    JAdmin existing = jAdmin(id, "Old", "Name", "old@x.com", Instant.now(), "OldAddr");
    Admin update = admin(id, "New", "Name2", "new@x.com", newBday, "NewAddr");

    when(adminRepository.findById(id)).thenReturn(Optional.of(existing));
    when(adminRepository.save(existing)).thenReturn(existing);

    Admin result = adminService.update(id, update);

    assertThat(result.getFirstname()).isEqualTo("New");
    assertThat(result.getLastname()).isEqualTo("Name2");
    assertThat(result.getEmail()).isEqualTo("new@x.com");
    assertThat(result.getBirthday()).isEqualTo(newBday);
    assertThat(result.getAdress()).isEqualTo("NewAddr");
  }

  @Test
  void update_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(adminRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(
            () -> adminService.update(id, admin(id, "X", "Y", "x@y.com", Instant.now(), "addr")))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void delete_whenExists_shouldDelete() {
    UUID id = UUID.randomUUID();
    when(adminRepository.existsById(id)).thenReturn(true);

    adminService.delete(id);

    verify(adminRepository).deleteById(id);
  }

  @Test
  void delete_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(adminRepository.existsById(id)).thenReturn(false);

    assertThatThrownBy(() -> adminService.delete(id)).isInstanceOf(EntityNotFoundException.class);

    verify(adminRepository, never()).deleteById(any());
  }
}
