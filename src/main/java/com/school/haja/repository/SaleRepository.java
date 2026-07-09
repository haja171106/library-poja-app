package com.school.haja.repository;

import com.school.haja.entities.BookFormat;
import com.school.haja.entities.BookEdition;
import com.school.haja.entities.SaleStatus;
import com.school.haja.repository.model.JSale;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<JSale, UUID> {

  @Override
  List<JSale> findAll();

  List<JSale> findByStatus(SaleStatus status);

  @Query(
      "SELECT COALESCE(SUM(s.nbr_sale), 0) FROM JSale s "
          + "WHERE s.library.id = :libraryId AND s.format = :format "
          + "AND s.status IN :statuses")
  Long sumByLibraryAndFormatAndStatuses(
      @Param("libraryId") UUID libraryId,
      @Param("format") BookFormat format,
      @Param("statuses") List<SaleStatus> statuses);

  @Query(
          "SELECT COALESCE(SUM(s.nbr_sale), 0) FROM JSale s "
                  + "WHERE s.library.id = :libraryId AND s.bookEdition.id = :bookEditionId "
                  + "AND s.status IN :statuses")
  Long sumByLibraryAndBookEditionAndStatuses(
          @Param("libraryId") UUID libraryId,
          @Param("bookEditionId") UUID bookEditionId,
          @Param("statuses") List<SaleStatus> statuses);

  @Query(
          "SELECT COALESCE(SUM(s.nbr_sale), 0) FROM JSale s "
                  + "JOIN s.bookEdition be "
                  + "JOIN be.book b "
                  + "WHERE s.library.id = :libraryId AND b.id = :bookId "
                  + "AND s.status IN :statuses")
  Long sumByLibraryAndBookAndStatuses(
          @Param("libraryId") UUID libraryId,
          @Param("bookId") UUID bookId,
          @Param("statuses") List<SaleStatus> statuses);
}
