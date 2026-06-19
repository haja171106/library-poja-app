package com.school.haja.repository;

import com.school.haja.repository.model.JBook;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<JBook, UUID> {}
