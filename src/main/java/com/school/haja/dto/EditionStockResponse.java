package com.school.haja.dto;

import java.util.UUID;

public record EditionStockResponse(
        UUID libraryId, UUID bookEditionId, int totalArrivals, int totalSales, int stock) {}