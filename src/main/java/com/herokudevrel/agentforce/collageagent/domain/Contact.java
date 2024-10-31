package com.herokudevrel.agentforce.collageagent.domain;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "contact", schema = "salesforce")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id__c", length = 18, unique = true)
    private String externalId;

    @Column(name = "lastname", length = 80)
    private String lastname;

    @Column(name = "name", length = 121)
    private String name;

    @Column(name = "isdeleted")
    private Boolean isDeleted;

    @Column(name = "systemmodstamp")
    private Timestamp systemModStamp;

    @Column(name = "createddate")
    private Timestamp createdDate;

    @Column(name = "salutation", length = 255)
    private String salutation;

    @Column(name = "title", length = 128)
    private String title;

    @Column(name = "firstname", length = 40)
    private String firstname;

    @Column(name = "photourl", length = 255)
    private String photoUrl;

    @Column(name = "sfid", length = 18)
    private String sfid;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    public Timestamp getSystemModStamp() { return systemModStamp; }
    public void setSystemModStamp(Timestamp systemModStamp) { this.systemModStamp = systemModStamp; }

    public Timestamp getCreatedDate() { return createdDate; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }

    public String getSalutation() { return salutation; }
    public void setSalutation(String salutation) { this.salutation = salutation; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getSfid() { return sfid; }
    public void setSfid(String sfid) { this.sfid = sfid; }
}
