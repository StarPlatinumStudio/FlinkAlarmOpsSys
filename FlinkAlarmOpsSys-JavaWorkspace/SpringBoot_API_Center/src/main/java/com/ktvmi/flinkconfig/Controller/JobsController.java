package com.ktvmi.flinkconfig.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.ktvmi.flinkconfig.EntityClass.Config;
import com.ktvmi.flinkconfig.EntityClass.Job;
import com.ktvmi.flinkconfig.EntityClass.ResponseMsg;
import com.ktvmi.flinkconfig.EntityClass.Rule;
import com.ktvmi.flinkconfig.Repository.JobRepository;
import com.ktvmi.flinkconfig.Repository.RuleRepository;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")

public class JobsController {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private RuleRepository ruleRepository;

    //注册默认服务
    @NacosInjected
    private ConfigService configService;
    /**
     * Get all Job list
     *
     * @return the list
     */
    @GetMapping("/alljobs")
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    /**
     * Update job  entity.
     *
     * @param jobid
     * @param jobDetails the job details
     * @return the response entity
     * @throws Exception
     */
    @PutMapping("/updatejob/{jobid}")
    public ResponseMsg updateJob(@PathVariable(value = "jobid") Long jobid, @Valid @RequestBody Job jobDetails)throws Exception {
    try {
        Job job =
                jobRepository
                        .findById(jobid)
                        .orElseThrow(() -> new Exception(" not found on ::" + jobid));
        job.setJobId(jobDetails.getJobId());
        job.setJobName(jobDetails.getJobName());
        job.setRules(jobDetails.getRules());
        final Job updatedJob = jobRepository.save(job);


        //生成并且更新配置到Nacos
        List<Rule> rules = ruleRepository.findAll();
        List<String> keys = new ArrayList<>();
        String[] rulesArray = job.getRules().split(",");//["1","2","3"]
        for (Rule rule : rules) {
            for (int i = 0; i < rulesArray.length; i++)
                if (Integer.parseInt(rulesArray[i]) == (rule.getRuleid())) {
                    keys.add(rule.getContent());
                    break;
                }
        }
        Config config = new Config();
        config.setId(updatedJob.getJobId());
        config.setKeys(keys);
        config.setHostname(updatedJob.getHostname());
        config.setIsZip(updatedJob.getIsZip());
        config.setName(updatedJob.getJobName());
        config.setPort(updatedJob.getPort());
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(config);
        configService.publishConfig("FlinkJob." + updatedJob.getJobId() + ".properties", "BaseService", content);
        ResponseMsg goodmsg = new ResponseMsg("Success", 0);
        return goodmsg;
    }
    catch (Exception ex){
        ResponseMsg badmsg = new ResponseMsg("Catch Exception"+ex.getMessage(), 233);
        return badmsg;
    }
    }
}

