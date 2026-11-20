package com.campuspulse.entity;
import jakarta.persistence.*;import java.time.Instant;import com.campuspulse.enums.Status;
@Entity @Table(name="complaint_history",indexes=@Index(name="idx_history_complaint",columnList="complaint_id")) public class ComplaintHistory{
@Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
@ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="complaint_id") public Complaint complaint;
@ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="actor_id") public User actor;
@Enumerated(EnumType.STRING) @Column(nullable=false) public Status status;
@Column(length=2000) public String note;
@Column(nullable=false,updatable=false) public Instant createdAt=Instant.now();
}
