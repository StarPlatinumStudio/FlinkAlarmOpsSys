package com.ktvmi.flinkconfig.Repository;

import com.ktvmi.flinkconfig.EntityClass.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {}
