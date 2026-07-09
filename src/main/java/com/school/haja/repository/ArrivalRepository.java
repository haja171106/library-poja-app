package com.school.haja.repository;

import com.school.haja.entities.BookFormat;
import com.school.haja.repository.model.JArrival;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArrivalRepository extends JpaRepository<JArrival, UUID> {

  @Override
  List<JArrival> findAll();

  @Query(
      "SELECT COALESCE(SUM(a.nbr_book), 0) FROM JArrival a "
          + "WHERE a.library.id = :libraryId AND a.format = :format")
  Long sumByLibraryAndFormat(
      @Param("libraryId") UUID libraryId, @Param("format") BookFormat format);
}
