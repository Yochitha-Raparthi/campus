package com.campuspulse.entity;
import jakarta.persistence.*;import java.time.Instant;import com.campuspulse.enums.*;
@Entity @Table(name="complaints",indexes={@Index(name="idx_complaint_student",columnList="student_id"),@Index(name="idx_complaint_status",columnList="status"),@Index(name="idx_complaint_created",columnList="createdAt")}) public class Complaint {
@Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
@Column(nullable=false,length=150) public String title;
@Column(nullable=false,length=5000) public String description;
@Column(nullable=false,length=150) public String location;
@Enumerated(EnumType.STRING) @Column(nullable=false,length=30) public Category category;
@Enumerated(EnumType.STRING) @Column(nullable=false,length=20) public Priority priority;
@Enumerated(EnumType.STRING) @Column(nullable=false,length=30) public Status status=Status.SUBMITTED;
@Column(length=500) public String aiExplanation;
@ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="student_id",nullable=false) public User student;
@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="department_id") public Department department;
@Column(nullable=false,updatable=false) public Instant createdAt=Instant.now();
@Column(nullable=false) public Instant updatedAt=Instant.now();
@PreUpdate public void update(){updatedAt=Instant.now();}
}
