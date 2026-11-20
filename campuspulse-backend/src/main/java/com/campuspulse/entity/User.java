package com.campuspulse.entity;
import jakarta.persistence.*;import java.time.Instant;import com.campuspulse.enums.Role;
@Entity @Table(name="users",indexes={@Index(name="idx_users_role",columnList="role")}) public class User {
@Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
@Column(nullable=false,length=100) public String name;
@Column(nullable=false,unique=true,length=190) public String email;
@Column(nullable=false) public String password;
@Enumerated(EnumType.STRING) @Column(nullable=false,length=20) public Role role;
@Column(nullable=false,updatable=false) public Instant createdAt=Instant.now();
}
