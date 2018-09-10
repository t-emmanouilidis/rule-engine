package com.temma.rules.engine.domain.model.rule

import com.temma.rules.engine.domain.model.common.RuleEngineException
import groovy.transform.PackageScope

/**
 * Defines a rule in the rules engine. A rule holds a name that will be used
 * to enforce its uniqueness. A rule's conditions and subsequent actions are
 * defined as a boolean {@link Closure} (returns a boolean value) and
 * a {@link Void} {@link Closure} (does not return a value) respectively.*/
class Rule {

    private final String name

    private final Closure<List<Boolean>> condition

    private final Closure action

    Rule(String name, Closure<List<Boolean>> condition, Closure<Void> action) {
        assert name: 'name can\'t be null'
        assert condition: 'condition can\'t be null'
        assert action: 'action can\'t be null'

        this.name = name
        this.condition = condition
        this.action = action
    }

    def name() {
        return this.name
    }

    /**
     * Tests the rule's condition {@link Closure} by executing the list of boolean
     * expressions in the provided context - delegate. This is done to give access
     * to a rule to the functionality offered by a session like inserting facts or
     * reusing facts added by previous rules in the execution chain.
     *
     * A simple, stateless rule that does not need access to a {@link Session}
     * can also be executed in the context of an object that is not a {@link Session}.
     *
     * @param availableConditions the object against which to evaluate the condition {@code Closure} of a {@code Rule}
     * @param ruleContext the context used for storing references to facts to carry from condition to action clause
     * @return true if all the conditions of a rule matches the current context or false otherwise
     * @throws ConditionException if anything goes wrong during execution of the condition part of the rule
     */
    @PackageScope
    def evalConditionAgainst(availableConditions, RuleExecutionContext ruleContext) throws RuleEngineException {
        assert availableConditions: 'availableConditions can\'t be null'

        assert ruleContext: 'ruleContext can\'t be null'

        def conditionCopy = condition.rehydrate(ruleContext, availableConditions, condition.thisObject)

        conditionCopy.resolveStrategy = Closure.DELEGATE_FIRST

        // TODO: if at least one condition does not apply to the current state, then there is no need to execute the rest of the rules.
        def conditionResults = conditionCopy()

        def result = true

        conditionResults.each { result = result && it }

        return result
    }

    /**
     * Executes the action {@link Closure} of the rule in the provided context - delegate. Again
     * this is done to give access to the action part to specific functionality offered by a
     * {@link Session} like inserting facts in the working memory or altering already existing facts.
     *
     * @param availableActions the object against which to evaluate the action {@code Closure} of a {@code Rule}
     * @param executionContext the context used to retrieving references to facts stored during the condition clause
     * @throws ActionException if anything goes wrong during execution of the action part of the rule
     */
    @PackageScope
    def executeActionAgainst(availableActions, RuleExecutionContext executionContext) throws RuleEngineException {
        assert availableActions: 'availableConditions can\'t be null'

        assert executionContext: 'executionContext can\'t be null'

        def actionCopy = action.rehydrate(executionContext, availableActions, action.thisObject)

        actionCopy.resolveStrategy = Closure.DELEGATE_FIRST

        actionCopy()
    }

    @Override
    boolean equals(other) {
        if (this.is(other)) {
            return true
        }

        if (getClass() != other.class) {
            return false
        }

        Rule rule = (Rule) other

        return this.name == rule.name
    }

    @Override
    int hashCode() {
        return this.name.hashCode()
    }
}
