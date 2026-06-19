package com.school.haja.repository;

import com.school.haja.repository.model.JArrival;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArrivalRepository extends JpaRepository<JArrival, UUID> {}
