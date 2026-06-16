package com.school.haja.repository;

import com.school.haja.repository.model.JUser;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JUserRepository extends JpaRepository<JUser, UUID> {

  @Override
  List<JUser> findAll();

  Optional<JUser> findByEmail(String email);

  boolean existsByEmail(String email);
}
