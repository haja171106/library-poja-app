package com.school.haja.repository;

import com.school.haja.repository.model.JPayment;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPaymentRepository extends JpaRepository<JPayment, UUID> {

  @Override
  List<JPayment> findAll();

  List<JPayment> findByType(String type);
}
