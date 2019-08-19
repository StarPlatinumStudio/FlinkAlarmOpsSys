package com.ktvmi.flinkops.apicenter.Repository;

import com.ktvmi.flinkops.apicenter.EntityClass.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface RuleRepository extends JpaRepository<Rule, Long> {}
