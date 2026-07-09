package com.school.haja.repository.model;

import com.school.haja.entities.BookFormat;
import jakarta.persistence.*;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book_editions")
@Getter @Setter @NoArgsConstructor
public class JBookEdition {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "type")
  private String type;

  @Enumerated(EnumType.STRING)
  @Column(name = "format", nullable = false)
  private BookFormat format;

  @ManyToOne(optional = false)
  @JoinColumn(name = "book_id", nullable = false)
  private JBook book;
}
