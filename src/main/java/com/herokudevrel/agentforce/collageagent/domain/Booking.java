package com.herokudevrel.agentforce.collageagent.domain;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "booking__c", schema = "salesforce")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "createddate")
    private Timestamp createdDate;

    @Column(name = "isdeleted")
    private Boolean isDeleted;

    @Column(name = "name", length = 80)
    private String name;

    @Column(name = "systemmodstamp")
    private Timestamp systemModStamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact__c", referencedColumnName = "sfid")
    private Contact contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session__c", referencedColumnName = "sfid")
    private Session session;

    @Column(name = "number_of_guests__c")
    private Double numberOfGuests;

    @Column(name = "sfid", length = 18)
    private String sfid;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Timestamp getCreatedDate() { return createdDate; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }

    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Timestamp getSystemModStamp() { return systemModStamp; }
    public void setSystemModStamp(Timestamp systemModStamp) { this.systemModStamp = systemModStamp; }

    public Contact getContact() { return contact; }
    public void setContact(Contact contact) { this.contact = contact; }

    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }

    public Double getNumberOfGuests() { return numberOfGuests; }
    public void setNumberOfGuests(Double numberOfGuests) { this.numberOfGuests = numberOfGuests; }

    public String getSfid() { return sfid; }
    public void setSfid(String sfid) { this.sfid = sfid; }
}
