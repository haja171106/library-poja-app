package com.school.haja.repository.model;

import com.school.haja.entities.BookFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "arrivals")
@Getter
@Setter
@NoArgsConstructor
public class JArrival {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id")
  private UUID id;

  @Column(name = "nbr_book")
  private int nbr_book;

  @Enumerated(EnumType.STRING)
  @Column(name = "format", nullable = false)
  private BookFormat format;

  @ManyToOne(optional = false)
  @JoinColumn(name = "library_id", nullable = false)
  private JLibrary library;

  @ManyToOne
  @JoinColumn(name = "book_edition_id")
  private JBookEdition bookEdition;
}
