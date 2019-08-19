package com.ktvmi.flinkops.apicenter.Repository;

import com.ktvmi.flinkops.apicenter.EntityClass.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {}
