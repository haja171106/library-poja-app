package com.school.haja.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JBook {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id")
  private UUID id;

  @Column(name = "title")
  private String title;

  @Column(name = "isbn")
  private String isbn;

  @Column(name = "nbr_page")
  private int nbr_page;

  @Column(name = "price")
  private double price;

  @Column(name = "release_date")
  private Instant release_date;
}
