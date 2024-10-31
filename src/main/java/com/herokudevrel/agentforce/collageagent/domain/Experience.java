package com.herokudevrel.agentforce.collageagent.domain;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "experience__c", schema = "salesforce")
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description__c")
    private String description;

    @Column(name = "capacity__c")
    private Double capacity;

    @Column(name = "type__c", length = 255)
    private String type;

    @Column(name = "name", length = 80)
    private String name;

    @Column(name = "location__c", length = 255)
    private String location;

    @Column(name = "activity_level__c", length = 255)
    private String activityLevel;

    @Column(name = "isdeleted")
    private Boolean isDeleted;

    @Column(name = "systemmodstamp")
    private Timestamp systemModStamp;

    @Column(name = "picture_url__c", length = 255)
    private String pictureUrl;

    @Column(name = "createddate")
    private Timestamp createdDate;

    @Column(name = "price__c")
    private Double price;

    @Column(name = "sfid", length = 18)
    private String sfid;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getCapacity() { return capacity; }
    public void setCapacity(Double capacity) { this.capacity = capacity; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getActivityLevel() { return activityLevel; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }

    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    public Timestamp getSystemModStamp() { return systemModStamp; }
    public void setSystemModStamp(Timestamp systemModStamp) { this.systemModStamp = systemModStamp; }

    public String getPictureUrl() { return pictureUrl; }
    public void setPictureUrl(String pictureUrl) { this.pictureUrl = pictureUrl; }

    public Timestamp getCreatedDate() { return createdDate; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getSfid() { return sfid; }
    public void setSfid(String sfid) { this.sfid = sfid; }
}
