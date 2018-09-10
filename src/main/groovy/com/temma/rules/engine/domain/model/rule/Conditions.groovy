package com.temma.rules.engine.domain.model.rule

import groovy.transform.PackageScope

/**
 * Describes an object that is responsible for supporting (delegate)
 * the condition clause of a rule and also makes available
 * the various functions that can be used in a rule condition clause.
 */
class Conditions {

    private WorkingMemory workingMemory;

    @PackageScope
    Conditions(WorkingMemory workingMemory) {
        super()

        this.setWorkingMemory(workingMemory)
    }

    private void setWorkingMemory(WorkingMemory workingMemory) {
        assert workingMemory: 'workingMemory can\'t be null'

        this.workingMemory = workingMemory
    }

    def where(@DelegatesTo(Conditions) Closure<Boolean> conditionClosure) {
        return this.workingMemory.factThatMatches(conditionClosure)
    }

    def methodMissing(String name, args) throws ConditionException {
        throw new ConditionException('Method \'' + name + '\' cannot be called in the rule condition clause')
    }

}
