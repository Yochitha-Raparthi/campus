package com.campuspulse.entity;
import jakarta.persistence.*;import java.time.Instant;
@Entity @Table(name="complaint_assignments",indexes={@Index(name="idx_assignment_staff",columnList="staff_id"),@Index(name="idx_assignment_complaint",columnList="complaint_id")}) public class ComplaintAssignment{
@Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
@ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="complaint_id") public Complaint complaint;
@ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="staff_id") public User staff;
@ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="assigned_by_id") public User assignedBy;
@Column(nullable=false,updatable=false) public Instant assignedAt=Instant.now();
}
