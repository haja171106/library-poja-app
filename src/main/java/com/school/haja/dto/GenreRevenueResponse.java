package com.school.haja.dto;

import java.util.UUID;

public record GenreRevenueResponse(UUID genreId, String genreType, double revenue) {
}
