package com.school.haja.repository;

import com.school.haja.repository.model.JPayment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<JPayment, UUID> {}
