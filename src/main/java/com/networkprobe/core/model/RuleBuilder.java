package com.networkprobe.core.model;

import com.networkprobe.core.enums.RuleType;

import static com.networkprobe.core.util.Validator.validate;

public class RuleBuilder {

    private final Rule rule;

    private RuleBuilder(int ruleOrder, String ruleName) {
        rule = new Rule( ruleOrder, validate(ruleName, "ruleName") );
    }

    private  RuleBuilder() {
        rule = new Rule();
    }

    public RuleBuilder order(int order) {
        rule.setRuleOrder(order);
        return this;
    }

    public RuleBuilder name(String name) {
        rule.setRuleName( validate(name, "name") );
        return this;
    }

    public RuleBuilder route(String routeName) {
        rule.setRouteName( validate(routeName, "routeName") );
        return this;
    }

    public RuleBuilder addressBlock(String addressBlockId) {
        rule.getAddressBlockIds().add( validate(addressBlockId, "addressBlockId") );
        return this;
    }

    public RuleBuilder range(String startAddress, String endAddress) {
        return addressBlock(startAddress + "-" + endAddress);
    }

    public RuleBuilder type(RuleType ruleType) {
        rule.setRuleType( validate(ruleType, "ruleType") );
        return this;
    }

    public Rule get() {
        return rule;
    }

    public static RuleBuilder create(int ruleOrder, String ruleName) {
        return new RuleBuilder(ruleOrder, ruleName);
    }

    public static RuleBuilder create() {
        return new RuleBuilder();
    }

}
