package com.zipcode.listdetails.controller;

import com.zipcode.listdetails.entity.Builder;
import com.zipcode.listdetails.entity.BuildingPlan;
import com.zipcode.listdetails.repository.BuilderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/builders")
public class BuilderController {

    private final BuilderRepository repo;

    public BuilderController(BuilderRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Builder> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Builder> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/plans")
    public ResponseEntity<List<BuildingPlan>> getPlansByBuilder(@PathVariable Long id) {
        return repo.findById(id)
                .map(b -> ResponseEntity.ok(b.getPlans()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/export")
    public List<Builder> exportAll() {
        return repo.findAll();
    }

    @PostMapping
    public Builder create(@RequestBody Builder builder) {
        return repo.save(builder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Builder> update(@PathVariable Long id,
                                          @RequestBody Builder updated) {
        return repo.findById(id).map(b -> {
            b.setName(updated.getName());
            b.setVision(updated.getVision());
            b.setLocation(updated.getLocation());
            b.setIcon(updated.getIcon());
            return ResponseEntity.ok(repo.save(b));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}