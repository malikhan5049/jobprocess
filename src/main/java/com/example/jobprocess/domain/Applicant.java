package com.example.jobprocess.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Applicant {
	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Long ID;
	private String name;
	private String email;
	
	@ManyToOne
	private Job job;

	
	
	
	public Applicant() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Applicant(String name, String email) {
		super();
		this.name = name;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}
	
	
	
}
