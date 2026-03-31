package com.zipcode.listdetails.controller;

import com.zipcode.listdetails.entity.BuildingPlan;
import com.zipcode.listdetails.repository.BuildingPlanRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/plans")
public class BuildingPlanController {

    private final BuildingPlanRepository repo;

    public BuildingPlanController(BuildingPlanRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<BuildingPlan> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuildingPlan> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public BuildingPlan create(@RequestBody BuildingPlan plan) {
        return repo.save(plan);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuildingPlan> update(@PathVariable Long id,
                                               @RequestBody BuildingPlan updated) {
        return repo.findById(id).map(p -> {
            p.setTitle(updated.getTitle());
            p.setStyle(updated.getStyle());
            p.setSquareFeet(updated.getSquareFeet());
            p.setNotes(updated.getNotes());
            return ResponseEntity.ok(repo.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
