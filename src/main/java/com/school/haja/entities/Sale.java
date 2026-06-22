package com.school.haja.entities;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
  private UUID id;
  private Double price;
  private int nbr_sale;
  private SaleStatus status;
  private BookFormat format;
  private UUID libraryId;
}
