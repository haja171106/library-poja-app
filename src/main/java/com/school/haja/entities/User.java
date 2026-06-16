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
public class User {
  private UUID id;

  private String firstname;
  private String lastname;
  private String email;
  private Instant birthday;
  private String adress;
}
