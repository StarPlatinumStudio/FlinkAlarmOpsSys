package com.ktvmi.flinkconfig.Controller;

import com.ktvmi.flinkconfig.EntityClass.ResponseMsg;
import com.ktvmi.flinkconfig.EntityClass.Rule;
import com.ktvmi.flinkconfig.Repository.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rules")
public class RulesController {

    @Autowired
    private RuleRepository ruleRepository;
    /**
     * Get all Rule list
     *
     * @return the list
     */
    @GetMapping("/allrules")
    public List<Rule> getAllRules(){
        return ruleRepository.findAll();
    }
    /**
     * Delete rule map by id .
     *
     * @param RuleID
     * @return the map
     * @throws Exception the exception
     */
    @DeleteMapping("/deleterule/{RuleID}")
    public Map<String, Boolean> deleteRule(@PathVariable(value = "RuleID") Long RuleID) throws Exception {
         Rule rule=
                ruleRepository
                        .findById(RuleID)
                        .orElseThrow(() -> new Exception("User not found on :: " + RuleID));
        //ruleRepository.delete(rule);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    @PutMapping("/updaterule/{ruleid}")
    public ResponseMsg updateRule(@PathVariable(value = "ruleid") Long ruleid, @Valid @RequestBody Rule ruleDetails)throws Exception {

        Rule rule =
                ruleRepository
                        .findById(ruleid)
                        .orElseThrow(() -> new Exception(" not found on ::" + ruleid));
        rule.setRuleid(ruleDetails.getRuleid());
        rule.setContent(ruleDetails.getContent());
        final Rule updatedRule = ruleRepository.save(rule);
        ResponseEntity.ok(updatedRule);
        ResponseMsg goodmsg = new ResponseMsg("Success", 0);
        return goodmsg;
    }
    /**
     * 新增Rule
     * @param rule
     * @return
     */
    @PostMapping("/newrule")
    public Rule newRule(@Valid @RequestBody Rule rule){
        return ruleRepository.save(rule);
    }
}
