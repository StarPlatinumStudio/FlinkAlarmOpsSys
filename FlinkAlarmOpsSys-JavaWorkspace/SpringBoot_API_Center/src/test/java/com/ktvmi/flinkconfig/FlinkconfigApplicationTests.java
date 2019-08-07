package com.ktvmi.flinkconfig;

import com.ktvmi.flinkconfig.EntityClass.Config;
import com.ktvmi.flinkconfig.EntityClass.Job;
import com.ktvmi.flinkconfig.EntityClass.Rule;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

/**
 * 为API请求创建单元测试并运行单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FlinkconfigApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlinkconfigApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private  int port;

    private String getRootUrl() {
        return "http://localhost:" + port;
    }
    @Test
    public void contextLoads() {
    }

    @Test
    public void testGetAllJobs(){
        HttpHeaders headers=new HttpHeaders();
        HttpEntity<String> entity=new HttpEntity<String>(null,headers);

        ResponseEntity<String> responseEntity=restTemplate.exchange(getRootUrl()+"/jobs",
                HttpMethod.GET,entity,String.class);

        Assert.assertNotNull(responseEntity.getBody());
    }
    @Test
    public void testGetAllRules(){
        HttpHeaders headers=new HttpHeaders();
        HttpEntity<String> entity=new HttpEntity<String>(null,headers);

        ResponseEntity<String> responseEntity=restTemplate.exchange(getRootUrl()+"/rules",
                HttpMethod.GET,entity,String.class);

        Assert.assertNotNull(responseEntity.getBody());
    }
    @Test
    public void testJobUpdatePost() {
        int id = 1;
        Job job = restTemplate.getForObject(getRootUrl() + "/updatejob/" + id, Job.class);
        job.setRules("1,2,3,1");
        job.setJobName("testName");
        restTemplate.put(getRootUrl() + "/updatejob/" + id, job);
        Job updatedUser = restTemplate.getForObject(getRootUrl() + "/updatejob/" + id, Job.class);
        Assert.assertNotNull(updatedUser);
    }
    @Test
    public void testDeletePost() {
        int id = 2;
        Rule rule = restTemplate.getForObject(getRootUrl() + "/deleterule/" + id,Rule.class);
        Assert.assertNotNull(rule);
        restTemplate.delete(getRootUrl() + "/deleterule/" + id);
        try {
            rule= restTemplate.getForObject(getRootUrl() + "/deleterule/" + id, Rule.class);
        } catch (final HttpClientErrorException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }
    @Test
    public void testCreateRule() {
        Rule rule = new Rule();
        rule.setContent("admin");
        ResponseEntity<Rule> postResponse = restTemplate.postForEntity(getRootUrl() + "/newrule", rule, Rule.class);
        Assert.assertNotNull(postResponse);
        Assert.assertNotNull(postResponse.getBody());
    }

    @Test
    public void testGetConfigById() {
        Config config = restTemplate.getForObject(getRootUrl() + "/config/1001", Config.class);
        System.out.println(config.getHostname());
        Assert.assertNotNull(config);
    }
}
