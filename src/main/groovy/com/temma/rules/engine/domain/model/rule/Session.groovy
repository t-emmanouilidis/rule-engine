package com.temma.rules.engine.domain.model.rule

import com.temma.rules.engine.domain.model.common.RuleEngineException
import groovy.transform.PackageScope
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.time.Instant

/**
 * Describes an object that is the entrypoint to the rule execution.
 * This session object manages the working memory of the rule execution.*/
class Session {

    private static Logger logger = LoggerFactory.getLogger(Session.class)

    private final List<Rule> rules

    // Working memory for storing facts and retrieving them
    private final WorkingMemory workingMemory = new WorkingMemory()

    private final Conditions conditions = new Conditions(this.workingMemory)

    private final Actions actions = new Actions(this.workingMemory)

    @PackageScope
    Session(List<Rule> rules) {
        assert rules: 'rules can\'t be null or empty'

        this.rules = rules
    }

    /**
     * This method returns the session objects that are currently
     * stored in the session and are instances the given type.
     * The objects that are not instances of the given type will not be returned.
     *
     * @param requiredType the type of the objects to return.
     * @return the objects currently in the session that are instances of the given type
     */
    public <T> Collection<T> getObjects(Class<T> requiredType) {
        return this.workingMemory.getObjects(requiredType)
    }

    /**
     * This method fires all rules that are available to the session
     * and returns the number of the rules that were successfully executed.
     *
     * @return a {@link ExecutionSummary} instance
     */
    ExecutionSummary fireAllRules() throws RuleEngineException {

        Set<String> alreadyExecutedRules = new HashSet<>()

        int ruleCount = 0

        Instant startTime = Instant.now()

        // find rules that are valid for execution ie. their condition holds for current context
        // assign an execution context to each rule before execution

        // agenda in a sense is a set of rule-executions i.e.
        // the set of rules and the contexts that have matching condition clauses.
        Collection<RuleExecution> agenda = ruleExecutionAgenda(alreadyExecutedRules)

        while (!agenda.isEmpty()) {

            // execute actions
            agenda.each { ruleExecution ->

                ruleExecution.execute()

                alreadyExecutedRules.add(ruleExecution.ruleName())

                ruleCount++

            }

            // log times
            if (logger.isDebugEnabled()) {
                agenda.each { ruleExecution -> logger.debug(ruleExecution.logMessage()) }
            }

            // clear the agenda and re-compute the valid rules for next execution iteration
            // assign execution context
            agenda = ruleExecutionAgenda(alreadyExecutedRules)

        }

        Instant endTime = Instant.now()

        ExecutionSummary summary = new ExecutionSummary(ruleCount, startTime, endTime, Thread.currentThread().getName())

        if (logger.isDebugEnabled()) {
            logger.debug(summary.toString())
        }

        return summary

    }

    /*
     * This functions returns the rules that can be executed - are valid for the current state
     * of things - given the rules that have already been executed and given all the rules
     * available in the session/knowledge base.
     *
     * TODO: Currently a no-loop rule execution strategy is supported (alreadyExecutedRules is needed for that).
     */
    private Collection<RuleExecution> ruleExecutionAgenda(Set<String> alreadyExecutedRules) throws ConditionException {

        Collection<RuleExecution> agenda = new ArrayList<>()

        def currentSession = this

        this.rules.each { rule ->

            if (!alreadyExecutedRules.contains(rule.name())) {

                RuleExecution ruleExecution = new RuleExecution(rule, currentSession)

                boolean matches = ruleExecution.matches()

                if (matches) {
                    agenda.add(ruleExecution)
                }

            }
        }

        return agenda
    }

    @PackageScope
    def conditions() {
        return this.conditions
    }

    @PackageScope
    def actions() {
        return this.actions
    }

    /**
     * This method allows a client to insert facts into the session
     * to setup the initial state of the execution.
     *
     * @param fact The fact to insert
     */
    void newFact(fact) {
        this.workingMemory.addFact(fact)
    }

}
