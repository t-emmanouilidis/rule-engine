package com.temma.rules.engine.domain.model.rule

import groovy.transform.PackageScope

import java.time.Duration
import java.time.Instant

/**
 * Describes an execution attempt for a specific {@link Rule},
 * a specific {@link Session} and a specific {@link RuleExecutionContext}.*/
class RuleExecution {

    private Session session

    private Rule rule

    private RuleExecutionContext context = new RuleExecutionContext()

    private Duration totalRuleExecutionDuration = Duration.ZERO

    @PackageScope
    RuleExecution(Rule rule, Session session) {
        assert rule: 'rule can\'t be null'
        assert session: 'session can\'t be null'

        this.rule = rule
        this.session = session
    }

    private void addTime(Duration additionalDuration) {
        Objects.requireNonNull(additionalDuration, "additionalDuration can't be null")

        this.totalRuleExecutionDuration = this.totalRuleExecutionDuration.plus(additionalDuration)
    }

    private Conditions conditions() {
        return this.session.conditions()
    }

    private Actions actions() {
        return this.session.actions()
    }

    /**
     * Evaluates the 'when' part of the {@link Rule} against
     * the current {@link RuleExecutionContext} and {@link WorkingMemory}.
     *
     * @return true if all the predicates match the current
     * @throws ConditionException if anything goes wrong during evaluation
     * of the condition part of the Rule.
     */
    @PackageScope
    boolean matches() throws ConditionException {

        try {
            Instant startTime = Instant.now()

            boolean result = this.rule.evalConditionAgainst(this.conditions(), this.context)

            Instant endTime = Instant.now()

            this.addTime(Duration.between(startTime, endTime))

            return result
        } catch (Exception ex) {
            throw new ConditionException("Condition clause for rule '${rule.name()}' failed", ex)
        }
    }

    /**
     * Executes the action part of a rule against the current
     * {@link RuleExecutionContext} and {@link WorkingMemory}.
     *
     * @throws ActionException if anything goes wrong during execution of the
     * action part
     */
    @PackageScope
    void execute() throws ActionException {
        try {
            Instant startTime = Instant.now()

            this.rule.executeActionAgainst(this.actions(), this.context)

            Instant endTime = Instant.now()

            this.addTime(Duration.between(startTime, endTime))
        } catch (Exception ex) {
            throw new ActionException("Action clause for rule '${rule.name()}' failed", ex)
        }
    }

    Duration executionDuration() {
        return this.totalRuleExecutionDuration
    }

    String logMessage() {
        return "Rule: " + this.ruleName() + ". ExecutionDuration: " + this.executionDuration()
    }

    @PackageScope
    String ruleName() {
        return this.rule.name()
    }

    @Override
    String toString() {
        return logMessage()
    }

}
