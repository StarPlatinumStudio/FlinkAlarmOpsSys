package com.ktvmi.flinkops.apicenter.EntityClass;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name="rules")
@EntityListeners(AuditingEntityListener.class)
public class Rule {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="RuleID",nullable = true)
    private Long ruleid;

    @Column(name = "Content",nullable = true)
    private String content;

    public Rule(){}
    public Rule(Long ruleid,String content){
        this.ruleid=ruleid;
        this.content=content;
    }

    public String getContent() {
        return content;
    }

    public Long getRuleid() {
        return ruleid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRuleid(Long ruleid) {
        this.ruleid = ruleid;
    }
}
