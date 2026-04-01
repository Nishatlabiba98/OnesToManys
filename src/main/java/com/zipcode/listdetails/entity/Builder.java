package com.zipcode.listdetails.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "builder")
public class Builder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String vision;
    private String location;
    private String icon;

    @OneToMany(mappedBy = "builder", cascade = CascadeType.ALL)
    private List<BuildingPlan> plans;

    public Builder() {}

    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }
    public String getName()                    { return name; }
    public void setName(String name)           { this.name = name; }
    public String getVision()                  { return vision; }
    public void setVision(String vision)       { this.vision = vision; }
    public String getLocation()                { return location; }
    public void setLocation(String location)   { this.location = location; }
    public String getIcon()                    { return icon; }
    public void setIcon(String icon)           { this.icon = icon; }
    public List<BuildingPlan> getPlans()       { return plans; }
    public void setPlans(List<BuildingPlan> p) { this.plans = p; }
}