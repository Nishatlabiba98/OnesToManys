package com.zipcode.listdetails.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zipcode.listdetails.entity.Builder;
import com.zipcode.listdetails.entity.BuildingPlan;
import com.zipcode.listdetails.repository.BuilderRepository;
import com.zipcode.listdetails.repository.BuildingPlanRepository;

@RestController
@RequestMapping("/api/plans")
public class BuildingPlanController {

    private final BuildingPlanRepository repo;
    private final BuilderRepository builderRepo;

    public BuildingPlanController(BuildingPlanRepository repo, BuilderRepository builderRepo) {
        this.repo = repo;
        this.builderRepo = builderRepo;
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
    public ResponseEntity<BuildingPlan> create(@RequestBody BuildingPlan plan) {
        if (plan.getBuilder() != null && plan.getBuilder().getId() != null) {
            Builder builder = builderRepo.findById(plan.getBuilder().getId()).orElse(null);
            if (builder == null) return ResponseEntity.badRequest().build();
            plan.setBuilder(builder);
        }
        return ResponseEntity.ok(repo.save(plan));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuildingPlan> update(@PathVariable Long id,
                                               @RequestBody BuildingPlan updated) {
        return repo.findById(id).map(p -> {
            p.setTitle(updated.getTitle());
            p.setStyle(updated.getStyle());
            p.setSquareFeet(updated.getSquareFeet());
            p.setNotes(updated.getNotes());
            p.setIcon(updated.getIcon());
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