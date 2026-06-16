package com.school.haja.repository;

import com.school.haja.repository.model.JBookEdition;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookEditionRepository extends JpaRepository<JBookEdition, UUID> {

  @Override
  List<JBookEdition> findAll();

  Optional<JBookEdition> findByType(String type);
}
