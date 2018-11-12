package com.temma.rules.engine.interfaces.dsl

import com.temma.rules.engine.domain.model.rule.Rule

/**
 * Simple DSL entry point for defining one or more {@link Rule}s
 *
 * @author temma
 */
final class Rules {

    /**
     * Simple method for defining a new {@link Rule}.
     * It delegates to {@link RuleBuilder}
     *
     * @param closure the script code for the rule
     * @return a new {@code Rule}
     */
    static def rule(@DelegatesTo(RuleBuilder) Closure closure) {
        assert closure

        RuleBuilder ruleSpec = new RuleBuilder()

        closure.delegate = ruleSpec

        closure()

        return ruleSpec.build()
    }

    /**
     * Simple method for defining an aggregation of {@link Rule}s.
     * It delegates to {@link RuleBuilder}.
     *
     * @param closure the code for the rules to define
     * @return a collection of {@link Rule}s
     */
    static def rules(Closure closure) {
        assert closure

        return closure()
    }

}
