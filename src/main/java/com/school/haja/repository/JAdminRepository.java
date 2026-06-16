package com.school.haja.repository;

import com.school.haja.repository.model.JAdmin;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JAdminRepository extends JpaRepository<JAdmin, UUID> {

  @Override
  List<JAdmin> findAll();

  Optional<JAdmin> findByEmail(String email);
}
