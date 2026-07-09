package com.school.haja.dto;

import java.util.UUID;

public record BookStockResponse(
    UUID libraryId, UUID bookId, int totalArrivals, int totalSales, int stock) {}
