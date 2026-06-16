package com.school.haja.service;

import com.school.haja.entities.User;
import com.school.haja.repository.UserRepository;
import com.school.haja.repository.model.JUser;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Couche service pour l'entité {@link User}. Sert pour les opérations communes à tous les
 * utilisateurs, indépendamment de leur sous-type ({@link com.school.haja.entities.Customer} ou
 * {@link com.school.haja.entities.Admin}).
 */
@Service
@AllArgsConstructor
@Transactional
public class UserService {

  private final UserRepository userRepository;

  public User create(User user) {
    JUser saved = userRepository.save(toEntity(user));
    return toDomain(saved);
  }

  public User getById(UUID id) {
    return userRepository
        .findById(id)
        .map(this::toDomain)
        .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
  }

  public List<User> getAll() {
    return userRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
  }

  public User update(UUID id, User user) {
    JUser existing =
        userRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));

    existing.setFirstname(user.getFirstname());
    existing.setLastname(user.getLastname());
    existing.setEmail(user.getEmail());
    existing.setBirthday(user.getBirthday());
    existing.setAdress(user.getAdress());

    return toDomain(userRepository.save(existing));
  }

  public void delete(UUID id) {
    if (!userRepository.existsById(id)) {
      throw new EntityNotFoundException("User not found: " + id);
    }
    userRepository.deleteById(id);
  }

  private JUser toEntity(User user) {
    return new JUser(
        user.getId(),
        user.getFirstname(),
        user.getLastname(),
        user.getEmail(),
        user.getBirthday(),
        user.getAdress());
  }

  private User toDomain(JUser entity) {
    return new User(
        entity.getId(),
        entity.getFirstname(),
        entity.getLastname(),
        entity.getEmail(),
        entity.getBirthday(),
        entity.getAdress());
  }
}
