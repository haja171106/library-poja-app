package com.school.haja.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "arrivals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JArrival {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id")
  private UUID id;

  @Column(name = "nbr_book")
  private int nbr_book;
}
