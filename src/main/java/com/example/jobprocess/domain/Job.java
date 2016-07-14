package com.example.jobprocess.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Job {
	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Long ID;
	private String jobTitle;
	private String jobDescription;
	
	@OneToMany(mappedBy="job") 
	private Set<Applicant> applicants = new HashSet<>();
	
	
	
	

	public Job() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Job(String jobTitle, String jobDescription) {
		super();
		this.jobTitle = jobTitle;
		this.jobDescription = jobDescription;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public Set<Applicant> getApplicants() {
		return applicants;
	}

	public void setApplicants(Set<Applicant> applicants) {
		this.applicants = applicants;
	}
	
	
	
	
	
	
}
