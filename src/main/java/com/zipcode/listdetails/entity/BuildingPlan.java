package com.zipcode.listdetails.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "building_plan")
public class BuildingPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String style;
    private int squareFeet;
    private String notes;
    private String icon;

    @ManyToOne
    @JoinColumn(name = "builder_id")
    @JsonIgnoreProperties({"plans", "hibernateLazyInitializer"})
    private Builder builder;

    public BuildingPlan() {}

    public Long getId()                     { return id; }
    public void setId(Long id)              { this.id = id; }
    public String getTitle()                { return title; }
    public void setTitle(String title)      { this.title = title; }
    public String getStyle()                { return style; }
    public void setStyle(String style)      { this.style = style; }
    public int getSquareFeet()              { return squareFeet; }
    public void setSquareFeet(int sf)       { this.squareFeet = sf; }
    public String getNotes()                { return notes; }
    public void setNotes(String notes)      { this.notes = notes; }
    public String getIcon()                 { return icon; }
    public void setIcon(String icon)        { this.icon = icon; }
    public Builder getBuilder()             { return builder; }
    public void setBuilder(Builder builder) { this.builder = builder; }
}