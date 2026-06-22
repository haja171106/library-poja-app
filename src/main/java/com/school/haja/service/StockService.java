package com.school.haja.service;

import com.school.haja.dto.StockResponse;
import com.school.haja.entities.BookFormat;
import com.school.haja.entities.SaleStatus;
import com.school.haja.repository.ArrivalRepository;
import com.school.haja.repository.SaleRepository;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {

  private static final List<SaleStatus> COUNTED_STATUSES =
      List.of(SaleStatus.DONE, SaleStatus.BOOKED);

  private final ArrivalRepository arrivalRepository;
  private final SaleRepository saleRepository;

  public StockResponse getStock(UUID libraryId, BookFormat format) {
    int totalArrivals = arrivalRepository.sumByLibraryAndFormat(libraryId, format).intValue();
    int totalSales =
        saleRepository
            .sumByLibraryAndFormatAndStatuses(libraryId, format, COUNTED_STATUSES)
            .intValue();
    int stock = totalArrivals - totalSales;
    return new StockResponse(libraryId, format, totalArrivals, totalSales, stock);
  }

  public List<StockResponse> getAllStocks(UUID libraryId) {
    return Arrays.stream(BookFormat.values()).map(format -> getStock(libraryId, format)).toList();
  }
}
