package com.school.haja.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.school.haja.dto.GenreRevenue;
import com.school.haja.dto.GenreRevenueResponse;
import com.school.haja.entities.SaleStatus;
import com.school.haja.repository.BookEditionRepository;
import com.school.haja.repository.LibraryRepository;
import com.school.haja.repository.SaleRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

  @Mock private SaleRepository saleRepository;
  @Mock private LibraryRepository libraryRepository;
  @Mock private BookEditionRepository bookEditionRepository;

  @InjectMocks private SaleService saleService;

  private record GenreRevenueFake(UUID genreId, String genreType, Double revenue)
      implements GenreRevenue {
    @Override
    public UUID getGenreId() {
      return genreId;
    }

    @Override
    public String getGenreType() {
      return genreType;
    }

    @Override
    public Double getRevenue() {
      return revenue;
    }
  }

  @Test
  void getRevenueByGenre_withSales_shouldReturnRevenuePerGenre() {
    UUID libraryId = UUID.randomUUID();
    UUID romanceId = UUID.randomUUID();
    UUID sciFiId = UUID.randomUUID();

    when(saleRepository.sumRevenueByGenre(libraryId, SaleStatus.DONE))
        .thenReturn(
            List.of(
                new GenreRevenueFake(romanceId, "Romance", 150.0),
                new GenreRevenueFake(sciFiId, "Science-Fiction", 90.0)));

    List<GenreRevenueResponse> result = saleService.getRevenueByGenre(libraryId);

    assertThat(result)
        .containsExactly(
            new GenreRevenueResponse(romanceId, "Romance", 150.0),
            new GenreRevenueResponse(sciFiId, "Science-Fiction", 90.0));
  }

  @Test
  void getRevenueByGenre_withNoSales_shouldReturnEmptyList() {
    UUID libraryId = UUID.randomUUID();

    when(saleRepository.sumRevenueByGenre(libraryId, SaleStatus.DONE)).thenReturn(List.of());

    List<GenreRevenueResponse> result = saleService.getRevenueByGenre(libraryId);

    assertThat(result).isEmpty();
  }
}
