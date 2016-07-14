package com.example.jobprocess.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jobprocess.domain.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

}
