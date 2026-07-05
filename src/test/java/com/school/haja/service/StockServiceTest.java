package com.school.haja.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.school.haja.dto.StockResponse;
import com.school.haja.entities.BookFormat;
import com.school.haja.entities.SaleStatus;
import com.school.haja.repository.ArrivalRepository;
import com.school.haja.repository.SaleRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

  @Mock private ArrivalRepository arrivalRepository;
  @Mock private SaleRepository saleRepository;

  @InjectMocks private StockService stockService;

  private static final List<SaleStatus> COUNTED_STATUSES =
      List.of(SaleStatus.DONE, SaleStatus.BOOKED);

  @Test
  void getStock_withArrivalsAndSales_shouldReturnCorrectStock() {
    UUID libraryId = UUID.randomUUID();
    BookFormat format = BookFormat.PAPERBACK;

    when(arrivalRepository.sumByLibraryAndFormat(libraryId, format)).thenReturn(20L);
    when(saleRepository.sumByLibraryAndFormatAndStatuses(libraryId, format, COUNTED_STATUSES))
        .thenReturn(8L);

    StockResponse result = stockService.getStock(libraryId, format);

    assertThat(result.libraryId()).isEqualTo(libraryId);
    assertThat(result.format()).isEqualTo(format);
    assertThat(result.totalArrivals()).isEqualTo(20);
    assertThat(result.totalSales()).isEqualTo(8);
    assertThat(result.stock()).isEqualTo(12);
  }

  @Test
  void getStock_withNoArrivalsAndNoSales_shouldReturnZeroStock() {
    UUID libraryId = UUID.randomUUID();
    BookFormat format = BookFormat.HARDCOVER;

    when(arrivalRepository.sumByLibraryAndFormat(libraryId, format)).thenReturn(0L);
    when(saleRepository.sumByLibraryAndFormatAndStatuses(libraryId, format, COUNTED_STATUSES))
        .thenReturn(0L);

    StockResponse result = stockService.getStock(libraryId, format);

    assertThat(result.totalArrivals()).isZero();
    assertThat(result.totalSales()).isZero();
    assertThat(result.stock()).isZero();
  }

  @Test
  void getStock_withMoreSalesThanArrivals_shouldReturnNegativeStock() {
    UUID libraryId = UUID.randomUUID();
    BookFormat format = BookFormat.SOFTCOVER;

    when(arrivalRepository.sumByLibraryAndFormat(libraryId, format)).thenReturn(5L);
    when(saleRepository.sumByLibraryAndFormatAndStatuses(libraryId, format, COUNTED_STATUSES))
        .thenReturn(9L);

    StockResponse result = stockService.getStock(libraryId, format);

    assertThat(result.stock()).isEqualTo(-4);
  }

  @Test
  void getAllStocks_shouldReturnOneEntryPerBookFormat() {
    UUID libraryId = UUID.randomUUID();

    when(arrivalRepository.sumByLibraryAndFormat(eq(libraryId), any(BookFormat.class)))
        .thenReturn(10L);
    when(
            saleRepository.sumByLibraryAndFormatAndStatuses(
                eq(libraryId), any(BookFormat.class), eq(COUNTED_STATUSES)))
        .thenReturn(3L);

    List<StockResponse> results = stockService.getAllStocks(libraryId);

    assertThat(results).hasSize(BookFormat.values().length);
    assertThat(results).allMatch(r -> r.libraryId().equals(libraryId));
    assertThat(results).allMatch(r -> r.stock() == 7);
  }
}
