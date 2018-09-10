package com.temma.rules.engine.interfaces.dsl
/**
 *
 */
final class Rules {

    static def rule(@DelegatesTo(RuleBuilder) Closure closure) {
        assert closure

        RuleBuilder ruleSpec = new RuleBuilder()

        closure.delegate = ruleSpec

        closure()

        return ruleSpec.build()
    }

    static def rules(Closure closure) {
        assert closure

        return closure()
    }

}
