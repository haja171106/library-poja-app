package com.school.haja.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
public class JPayment {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id")
  private UUID id;

  @Column(name = "type")
  private String type;

  @Column(name = "price")
  private Double price;

  @OneToOne(optional = false)
  @JoinColumn(name = "sale_id", nullable = false, unique = true)
  private JSale sale;
}
