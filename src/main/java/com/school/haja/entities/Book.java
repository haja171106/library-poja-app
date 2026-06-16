package com.school.haja.entities;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
  private UUID id;

  private String title;
  private String isbn;
  private int nbr_page;
  private double price;
  private Instant release_date;
}
