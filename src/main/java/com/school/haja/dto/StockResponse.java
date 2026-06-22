package com.school.haja.dto;

import com.school.haja.entities.BookFormat;
import java.util.UUID;

public record StockResponse(
    UUID libraryId, BookFormat format, int totalArrivals, int totalSales, int stock) {}
