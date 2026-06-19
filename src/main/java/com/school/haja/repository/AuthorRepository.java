package com.school.haja.repository;

import com.school.haja.repository.model.JAuthor;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<JAuthor, UUID> {

  @Override
  List<JAuthor> findAll();

  List<JAuthor> findByLastname(String lastname);

  List<JAuthor> findByFirstnameAndLastname(String firstname, String lastname);
}
