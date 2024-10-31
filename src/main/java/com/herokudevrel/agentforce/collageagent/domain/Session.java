package com.herokudevrel.agentforce.collageagent.domain;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "session__c", schema = "salesforce")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 80)
    private String name;

    @Column(name = "isdeleted")
    private Boolean isDeleted;

    @Column(name = "systemmodstamp")
    private Timestamp systemModStamp;

    @Column(name = "is_canceled__c")
    private Boolean isCanceled;

    @Column(name = "date__c")
    private Date date;

    @Column(name = "createddate")
    private Timestamp createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experience__c", referencedColumnName = "sfid")
    private Experience experience;

    @Column(name = "start_time__c")
    private Time startTime;

    @Column(name = "sfid", length = 18)
    private String sfid;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    public Timestamp getSystemModStamp() { return systemModStamp; }
    public void setSystemModStamp(Timestamp systemModStamp) { this.systemModStamp = systemModStamp; }

    public Boolean getIsCanceled() { return isCanceled; }
    public void setIsCanceled(Boolean isCanceled) { this.isCanceled = isCanceled; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public Timestamp getCreatedDate() { return createdDate; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }

    public Experience getExperience() { return experience; }
    public void setExperience(Experience experience) { this.experience = experience; }

    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime) { this.startTime = startTime; }

    public String getSfid() { return sfid; }
    public void setSfid(String sfid) { this.sfid = sfid; }
}
