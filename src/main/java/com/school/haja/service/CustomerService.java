package com.school.haja.service;

import com.school.haja.entities.Customer;
import com.school.haja.repository.CustomerRepository;
import com.school.haja.repository.model.JCustomer;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Couche service pour l'entité {@link Customer} (hérite de {@code User}). */
@Service
@AllArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer create(Customer customer) {
        JCustomer saved = customerRepository.save(toEntity(customer));
        return toDomain(saved);
    }

    public Customer getById(UUID id) {
        return customerRepository
                .findById(id)
                .map(this::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + id));
    }

    public List<Customer> getAll() {
        return customerRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public Customer update(UUID id, Customer customer) {
        JCustomer existing =
                customerRepository
                        .findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + id));

        existing.setFirstname(customer.getFirstname());
        existing.setLastname(customer.getLastname());
        existing.setEmail(customer.getEmail());
        existing.setBirthday(customer.getBirthday());
        existing.setAdress(customer.getAdress());

        return toDomain(customerRepository.save(existing));
    }

    public void delete(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new EntityNotFoundException("Customer not found: " + id);
        }
        customerRepository.deleteById(id);
    }

    private JCustomer toEntity(Customer customer) {
        JCustomer entity = new JCustomer();
        entity.setId(customer.getId());
        entity.setFirstname(customer.getFirstname());
        entity.setLastname(customer.getLastname());
        entity.setEmail(customer.getEmail());
        entity.setBirthday(customer.getBirthday());
        entity.setAdress(customer.getAdress());
        return entity;
    }

    private Customer toDomain(JCustomer entity) {
        Customer customer = new Customer();
        customer.setId(entity.getId());
        customer.setFirstname(entity.getFirstname());
        customer.setLastname(entity.getLastname());
        customer.setEmail(entity.getEmail());
        customer.setBirthday(entity.getBirthday());
        customer.setAdress(entity.getAdress());
        return customer;
    }
}