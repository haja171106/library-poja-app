package com.school.haja.service;

import com.school.haja.entities.Admin;
import com.school.haja.repository.AdminRepository;
import com.school.haja.repository.model.JAdmin;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Couche service pour l'entité {@link Admin} (hérite de {@code User}). */
@Service
@AllArgsConstructor
@Transactional
public class AdminService {

    private final AdminRepository adminRepository;

    public Admin create(Admin admin) {
        JAdmin saved = adminRepository.save(toEntity(admin));
        return toDomain(saved);
    }

    public Admin getById(UUID id) {
        return adminRepository
                .findById(id)
                .map(this::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found: " + id));
    }

    public List<Admin> getAll() {
        return adminRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    public Admin update(UUID id, Admin admin) {
        JAdmin existing =
                adminRepository
                        .findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Admin not found: " + id));

        existing.setFirstname(admin.getFirstname());
        existing.setLastname(admin.getLastname());
        existing.setEmail(admin.getEmail());
        existing.setBirthday(admin.getBirthday());
        existing.setAdress(admin.getAdress());

        return toDomain(adminRepository.save(existing));
    }

    public void delete(UUID id) {
        if (!adminRepository.existsById(id)) {
            throw new EntityNotFoundException("Admin not found: " + id);
        }
        adminRepository.deleteById(id);
    }

    private JAdmin toEntity(Admin admin) {
        JAdmin entity = new JAdmin();
        entity.setId(admin.getId());
        entity.setFirstname(admin.getFirstname());
        entity.setLastname(admin.getLastname());
        entity.setEmail(admin.getEmail());
        entity.setBirthday(admin.getBirthday());
        entity.setAdress(admin.getAdress());
        return entity;
    }

    private Admin toDomain(JAdmin entity) {
        Admin admin = new Admin();
        admin.setId(entity.getId());
        admin.setFirstname(entity.getFirstname());
        admin.setLastname(entity.getLastname());
        admin.setEmail(entity.getEmail());
        admin.setBirthday(entity.getBirthday());
        admin.setAdress(entity.getAdress());
        return admin;
    }
}