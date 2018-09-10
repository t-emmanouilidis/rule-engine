package com.temma.rules.engine.interfaces.dsl

import com.temma.rules.engine.domain.model.rule.Actions
import com.temma.rules.engine.domain.model.rule.Conditions
import com.temma.rules.engine.domain.model.rule.Rule

class RuleBuilder {

    private String name

    private Closure<List<Boolean>> condition

    private Closure action

    /**
     * Sets the new {@link Rule}'s name
     *
     * @param name the name of the rule
     */
    def name(String name) {
        assert name: 'name can\'t be null'

        this.name = name
    }

    /**
     * Evaluates and sets the 'when' part of a rule
     *
     * @param condition the condition/predicate closure
     */
    def when(@DelegatesTo(Conditions) Closure<List<Boolean>> condition) {
        assert this.name: 'name can\'t be null'
        assert condition: 'condition can\'t be null'

        this.condition = condition
    }

    /**
     * Evaluates and sets the 'then' part of a rule
     *
     * @param action the action closure
     */
    def then(@DelegatesTo(Actions) Closure action) {
        // Make the order of the arguments explicit,
        // so that the user is not able to define something wrong
        assert this.name: 'name can\'t be null'
        assert this.condition: 'condition can\'t be null'
        assert action: 'action can\'t be null'

        this.action = action
    }

    /**
     * Given the name, 'when' and 'then' parts builds
     * and returns a new {@link Rule} instance.
     *
     * @return a new {@code Rule} instance
     */
    def build() {
        assert this.name: 'name can\'t be null'
        assert this.condition: 'condition can\'t be null'
        assert this.action: 'action can\'t be null'

        return new Rule(this.name, this.condition, this.action)
    }

}
