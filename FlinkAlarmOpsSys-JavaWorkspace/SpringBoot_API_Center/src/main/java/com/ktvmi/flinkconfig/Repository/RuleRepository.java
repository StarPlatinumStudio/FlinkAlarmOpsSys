package com.ktvmi.flinkconfig.Repository;

import com.ktvmi.flinkconfig.EntityClass.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface RuleRepository extends JpaRepository<Rule, Long> {}
