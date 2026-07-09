package com.school.haja.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.school.haja.entities.Customer;
import com.school.haja.repository.CustomerRepository;
import com.school.haja.repository.model.JCustomer;
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
class CustomerServiceTest {

  @Mock private CustomerRepository customerRepository;

  @InjectMocks private CustomerService customerService;

  private Customer customer(
      UUID id, String first, String last, String email, Instant bday, String adress) {
    Customer customer = new Customer();
    customer.setId(id);
    customer.setFirstname(first);
    customer.setLastname(last);
    customer.setEmail(email);
    customer.setBirthday(bday);
    customer.setAdress(adress);
    return customer;
  }

  private JCustomer jCustomer(
      UUID id, String first, String last, String email, Instant bday, String adress) {
    JCustomer customer = new JCustomer();
    customer.setId(id);
    customer.setFirstname(first);
    customer.setLastname(last);
    customer.setEmail(email);
    customer.setBirthday(bday);
    customer.setAdress(adress);
    return customer;
  }

  @Test
  void create_shouldSaveAndReturnCustomer() {
    UUID id = UUID.randomUUID();
    Instant bday = Instant.parse("1992-02-02T00:00:00Z");
    when(customerRepository.save(any(JCustomer.class)))
        .thenReturn(jCustomer(id, "Marie", "Curie", "marie@c.com", bday, "5 rue B"));

    Customer result =
        customerService.create(customer(id, "Marie", "Curie", "marie@c.com", bday, "5 rue B"));

    assertThat(result.getId()).isEqualTo(id);
    assertThat(result.getFirstname()).isEqualTo("Marie");
    assertThat(result.getLastname()).isEqualTo("Curie");
    assertThat(result.getEmail()).isEqualTo("marie@c.com");
    assertThat(result.getBirthday()).isEqualTo(bday);
    assertThat(result.getAdress()).isEqualTo("5 rue B");
  }

  @Test
  void getById_whenExists_shouldReturnCustomer() {
    UUID id = UUID.randomUUID();
    when(customerRepository.findById(id))
        .thenReturn(Optional.of(jCustomer(id, "A", "B", "a@b.com", Instant.now(), "addr")));

    Customer result = customerService.getById(id);

    assertThat(result.getEmail()).isEqualTo("a@b.com");
  }

  @Test
  void getById_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(customerRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> customerService.getById(id))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void getAll_shouldReturnAllCustomers() {
    when(customerRepository.findAll())
        .thenReturn(
            List.of(
                jCustomer(UUID.randomUUID(), "A", "B", "a@b.com", Instant.now(), "addr1"),
                jCustomer(UUID.randomUUID(), "C", "D", "c@d.com", Instant.now(), "addr2")));

    List<Customer> result = customerService.getAll();

    assertThat(result).hasSize(2);
  }

  @Test
  void update_whenExists_shouldUpdateAndReturnCustomer() {
    UUID id = UUID.randomUUID();
    Instant newBday = Instant.parse("2001-01-01T00:00:00Z");
    JCustomer existing = jCustomer(id, "Old", "Name", "old@x.com", Instant.now(), "OldAddr");
    Customer update = customer(id, "New", "Name2", "new@x.com", newBday, "NewAddr");

    when(customerRepository.findById(id)).thenReturn(Optional.of(existing));
    when(customerRepository.save(existing)).thenReturn(existing);

    Customer result = customerService.update(id, update);

    assertThat(result.getFirstname()).isEqualTo("New");
    assertThat(result.getLastname()).isEqualTo("Name2");
    assertThat(result.getEmail()).isEqualTo("new@x.com");
    assertThat(result.getBirthday()).isEqualTo(newBday);
    assertThat(result.getAdress()).isEqualTo("NewAddr");
  }

  @Test
  void update_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(customerRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(
            () ->
                customerService.update(
                    id, customer(id, "X", "Y", "x@y.com", Instant.now(), "addr")))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void delete_whenExists_shouldDelete() {
    UUID id = UUID.randomUUID();
    when(customerRepository.existsById(id)).thenReturn(true);

    customerService.delete(id);

    verify(customerRepository).deleteById(id);
  }

  @Test
  void delete_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(customerRepository.existsById(id)).thenReturn(false);

    assertThatThrownBy(() -> customerService.delete(id))
        .isInstanceOf(EntityNotFoundException.class);

    verify(customerRepository, never()).deleteById(any());
  }
}
