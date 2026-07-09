package com.school.haja.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.school.haja.entities.Payment;
import com.school.haja.repository.PaymentRepository;
import com.school.haja.repository.model.JPayment;
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
class PaymentServiceTest {

  @Mock private PaymentRepository paymentRepository;

  @InjectMocks private PaymentService paymentService;

  private JPayment jPayment(UUID id, String type, Double price) {
    JPayment entity = new JPayment();
    entity.setId(id);
    entity.setType(type);
    entity.setPrice(price);
    return entity;
  }

  @Test
  void create_shouldSaveAndReturnPayment() {
    UUID id = UUID.randomUUID();
    when(paymentRepository.save(any(JPayment.class))).thenReturn(jPayment(id, "CB", 19.99));

    Payment result = paymentService.create(new Payment(id, "CB", 19.99));

    assertThat(result.getId()).isEqualTo(id);
    assertThat(result.getType()).isEqualTo("CB");
    assertThat(result.getPrice()).isEqualTo(19.99);
  }

  @Test
  void getById_whenExists_shouldReturnPayment() {
    UUID id = UUID.randomUUID();
    when(paymentRepository.findById(id)).thenReturn(Optional.of(jPayment(id, "Cash", 5.0)));

    Payment result = paymentService.getById(id);

    assertThat(result.getType()).isEqualTo("Cash");
  }

  @Test
  void getById_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(paymentRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> paymentService.getById(id))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void getAll_shouldReturnAllPayments() {
    when(paymentRepository.findAll())
        .thenReturn(
            List.of(jPayment(UUID.randomUUID(), "CB", 10.0), jPayment(UUID.randomUUID(), "Cash", 20.0)));

    List<Payment> result = paymentService.getAll();

    assertThat(result).hasSize(2);
  }

  @Test
  void update_whenExists_shouldUpdateAndReturnPayment() {
    UUID id = UUID.randomUUID();
    JPayment existing = jPayment(id, "OldType", 1.0);
    when(paymentRepository.findById(id)).thenReturn(Optional.of(existing));
    when(paymentRepository.save(existing)).thenReturn(existing);

    Payment result = paymentService.update(id, new Payment(id, "NewType", 99.0));

    assertThat(result.getType()).isEqualTo("NewType");
    assertThat(result.getPrice()).isEqualTo(99.0);
  }

  @Test
  void update_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(paymentRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> paymentService.update(id, new Payment(id, "X", 1.0)))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void delete_whenExists_shouldDelete() {
    UUID id = UUID.randomUUID();
    when(paymentRepository.existsById(id)).thenReturn(true);

    paymentService.delete(id);

    verify(paymentRepository).deleteById(id);
  }

  @Test
  void delete_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(paymentRepository.existsById(id)).thenReturn(false);

    assertThatThrownBy(() -> paymentService.delete(id))
        .isInstanceOf(EntityNotFoundException.class);

    verify(paymentRepository, never()).deleteById(any());
  }
}
