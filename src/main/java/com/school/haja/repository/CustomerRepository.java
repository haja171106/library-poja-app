package com.school.haja.repository;

import com.school.haja.repository.model.JCustomer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<JCustomer, UUID> {

  @Override
  List<JCustomer> findAll();

  Optional<JCustomer> findByEmail(String email);

  boolean existsByEmail(String email);
}
