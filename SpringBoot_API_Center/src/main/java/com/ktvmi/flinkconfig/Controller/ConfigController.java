package com.ktvmi.flinkconfig.Controller;

import com.ktvmi.flinkconfig.EntityClass.Config;
import com.ktvmi.flinkconfig.EntityClass.Job;
import com.ktvmi.flinkconfig.EntityClass.Rule;
import com.ktvmi.flinkconfig.Repository.JobRepository;
import com.ktvmi.flinkconfig.Repository.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/config")
public class ConfigController {
    @Autowired
    private RuleRepository ruleRepository;
    @Autowired
    private JobRepository jobRepository;

    /**
     * 通过job id获取统一的Config类.
     *
     * @param id
     * @return  config类

    @GetMapping("/{id}")
    public Config getConfigById(@PathVariable(value = "id")Long id) throws Exception{
        Job job=jobRepository.findById(id).orElseThrow(()->new Exception("Job not found on :: " + id));
        List<Rule> rules=ruleRepository.findAll();
        List<String> keys=new ArrayList<>();
        String[] rulesArray= job.getRules().split(",");//["1","2","3"]
        for(Rule rule:rules){
            for (int i=0;i<rulesArray.length;i++)
                if (Integer.parseInt(rulesArray[i])==(rule.getRuleid()))
            {
                keys.add(rule.getContent());
                break;
            }
        }
        Config config=new Config();
        config.setId(id);
        config.setKeys(keys);
        config.setHostname(job.getHostname());
        config.setIsZip(job.getIsZip());
        config.setName(job.getJobName());
        config.setPort(job.getPort());
        return config;
    }*/
}
