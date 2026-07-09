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
public class Arrival {
  private UUID id;
  private int nbr_book;
  private BookFormat format;
  private UUID libraryId;
}
