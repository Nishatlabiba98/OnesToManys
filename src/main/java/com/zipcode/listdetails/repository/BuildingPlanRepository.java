package com.zipcode.listdetails.repository;

import com.zipcode.listdetails.entity.BuildingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BuildingPlanRepository extends JpaRepository<BuildingPlan, Long> {
    List<BuildingPlan> findByBuilderId(Long builderId);
}