package com.example.jobprocess.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jobprocess.domain.Applicant;

public interface ApplicantRepository extends JpaRepository<Applicant, Long>{

}
