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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "libraries")
@Getter
@Setter
@NoArgsConstructor
public class JLibrary {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id")
  private UUID id;

  @Column(name = "name")
  private String name;

  @OneToMany(mappedBy = "library")
  private Set<JSale> sales = new HashSet<>();

  @OneToMany(mappedBy = "library")
  private Set<JArrival> arrivals = new HashSet<>();

  @ManyToMany
  @JoinTable(
      name = "library_books",
      joinColumns = @JoinColumn(name = "library_id"),
      inverseJoinColumns = @JoinColumn(name = "book_id"))
  private Set<JBook> books = new HashSet<>();
}
