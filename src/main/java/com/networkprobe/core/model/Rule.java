package com.networkprobe.core.model;

import com.networkprobe.core.util.Names;

import java.util.ArrayList;
import java.util.List;

import static com.networkprobe.core.util.Validator.validate;
import static com.networkprobe.core.util.Utilities.RANDOM;

public class Rule {

    public static final int DEFAULT_ORDER = 10;
    public static final String ANY_COMMAND = "any";

    private int ruleOrder;
    private String ruleName;
    private String routeName;
    private String command;
    private List<String> addressBlocks;

    public Rule(int ruleOrder, String ruleName, String routeName, String command,
                List<String> addressBlocks) {
        this.ruleOrder = ruleOrder;
        this.ruleName = ruleName;
        this.routeName = routeName;
        this.command = command;
        this.addressBlocks = null;
    }

    public Rule(int ruleOrder, String ruleName) {
        this(ruleOrder, ruleName, Route.UNDEFINED_ROUTE_VALUE, ANY_COMMAND,
                new ArrayList<>());
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

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
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

    public List<String> getAddressBlocks() {
        return addressBlocks;
    }

    protected void setAddressBlocks(List<String> addressBlocks) {
        this.addressBlocks = addressBlocks;
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
