package com.school.haja.repository;

import com.school.haja.repository.model.JLibrary;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryRepository extends JpaRepository<JLibrary, UUID> {}
