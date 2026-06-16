package com.school.haja.repository;

import com.school.haja.repository.model.JArrival;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JArrivalRepository extends JpaRepository<JArrival, UUID> {

  @Override
  List<JArrival> findAll();
}
