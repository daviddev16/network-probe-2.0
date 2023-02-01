package com.networkprobe.core.model;

import com.networkprobe.core.enums.RuleType;
import com.networkprobe.core.util.Names;

import java.util.ArrayList;
import java.util.List;

import static com.networkprobe.core.util.Validator.validate;
import static com.networkprobe.core.util.Utilities.RANDOM;

public class Rule {

    public static final int DEFAULT_ORDER = 10;

    private int ruleOrder;
    private String ruleName;
    private String routeName;
    private List<String> addressBlockIds;
    private RuleType ruleType;

    public Rule(int ruleOrder, String ruleName, String routeName,
                List<String> addressBlockIds, RuleType ruleType) {
        this.ruleOrder = ruleOrder;
        this.ruleName = ruleName;
        this.routeName = routeName;
        this.addressBlockIds = addressBlockIds;
        this.ruleType = ruleType;
    }

    public Rule(int ruleOrder, String ruleName) {
        this(ruleOrder, ruleName, Route.UNDEFINED_ROUTE_VALUE,
                new ArrayList<>(), RuleType.IGNORE);
    }

    public Rule() {
        this(RANDOM.nextInt(1024), Names.createRuleName());
    }

    public int getRuleOrder() {
        return ruleOrder;
    }

    protected void setRuleOrder(int ruleOrder) {
        this.ruleOrder = ruleOrder;
    }

    public String getRuleName() {
        return ruleName;
    }

    protected void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRouteName() {
        return routeName;
    }

    protected void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public List<String> getAddressBlockIds() {
        return addressBlockIds;
    }

    protected void setAddressBlockIds(List<String> addressBlockIds) {
        this.addressBlockIds = addressBlockIds;
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    protected void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    public static final List<Rule> createRules(Rule rule0, Rule... rule1) {
        List<Rule> rules = new ArrayList<>();
        rules.add(validate(rule0, "Rule"));
        for (Rule rule : validate(rule1, "Rule[]")) {
            rules.add(validate(rule, "Rule em Rule[]"));
        }
        return rules;
    }
}
