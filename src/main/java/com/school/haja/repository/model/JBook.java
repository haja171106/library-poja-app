package com.school.haja.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
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
  private int nbrPage;

  @Column(name = "price")
  private double price;

  @Column(name = "release_date")
  private Instant releaseDate;

  @OneToMany(mappedBy = "book")
  private Set<JBookEdition> editions = new HashSet<>();

  @ManyToMany
  @JoinTable(
          name = "book_genres",
          joinColumns = @JoinColumn(name = "book_id"),
          inverseJoinColumns = @JoinColumn(name = "genre_id"))
  private Set<JGenre> genres = new HashSet<>();

  @ManyToMany
  @JoinTable(
          name = "book_authors",
          joinColumns = @JoinColumn(name = "book_id"),
          inverseJoinColumns = @JoinColumn(name = "author_id"))
  private Set<JAuthor> authors = new HashSet<>();
}