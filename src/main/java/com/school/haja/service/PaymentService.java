package com.school.haja.service;

import com.school.haja.entities.Payment;
import com.school.haja.repository.PaymentRepository;
import com.school.haja.repository.model.JPayment;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Couche service pour l'entité {@link Payment}. */
@Service
@AllArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment create(Payment payment) {
        JPayment saved = paymentRepository.save(toEntity(payment));
        return toDomain(saved);
    }

    public Payment getById(UUID id) {
        return paymentRepository
                .findById(id)
                .map(this::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found: " + id));
    }

    public List<Payment> getAll() {
        return paymentRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    public Payment update(UUID id, Payment payment) {
        JPayment existing =
                paymentRepository
                        .findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Payment not found: " + id));

        existing.setType(payment.getType());
        existing.setPrice(payment.getPrice());

        return toDomain(paymentRepository.save(existing));
    }

    public void delete(UUID id) {
        if (!paymentRepository.existsById(id)) {
            throw new EntityNotFoundException("Payment not found: " + id);
        }
        paymentRepository.deleteById(id);
    }

    private JPayment toEntity(Payment payment) {
        return new JPayment(payment.getId(), payment.getType(), payment.getPrice());
    }

    private Payment toDomain(JPayment entity) {
        return new Payment(entity.getId(), entity.getType(), entity.getPrice());
    }
}