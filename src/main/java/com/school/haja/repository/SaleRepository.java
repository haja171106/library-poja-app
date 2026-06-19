package com.school.haja.repository;

import com.school.haja.repository.model.JSale;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<JSale, UUID> {}
