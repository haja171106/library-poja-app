package com.school.haja.endpoint.controller;

import com.school.haja.entities.Arrival;
import com.school.haja.service.ArrivalService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/arrivals")
@AllArgsConstructor
public class ArrivalController {

  private final ArrivalService arrivalService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Arrival create(@RequestBody Arrival arrival) {
    return arrivalService.create(arrival);
  }

  @GetMapping
  public List<Arrival> getAll() {
    return arrivalService.getAll();
  }

  @GetMapping("/{id}")
  public Arrival getById(@PathVariable UUID id) {
    return arrivalService.getById(id);
  }

  @PutMapping("/{id}")
  public Arrival update(@PathVariable UUID id, @RequestBody Arrival arrival) {
    return arrivalService.update(id, arrival);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID id) {
    arrivalService.delete(id);
  }
}
