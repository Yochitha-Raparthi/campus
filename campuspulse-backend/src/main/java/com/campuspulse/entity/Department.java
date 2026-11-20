package com.campuspulse.entity;
import jakarta.persistence.*;
@Entity @Table(name="departments") public class Department{@Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;@Column(nullable=false,unique=true,length=100) public String name;}
