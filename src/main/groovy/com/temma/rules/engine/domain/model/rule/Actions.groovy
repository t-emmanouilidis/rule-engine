package com.temma.rules.engine.domain.model.rule

import groovy.transform.PackageScope

/**
 * Describes an object that is responsible for supporting (delegate)
 * the action clause of a rule and also makes available
 * the various functions that can be used in a rule action clause.
 */
class Actions {

    private WorkingMemory workingMemory

    @PackageScope
    Actions(WorkingMemory workingMemory) {
        super()

        this.setWorkingMemory(workingMemory)
    }

    /**
     * Sets the {@link WorkingMemory} for this {@link Actions}
     * instance
     *
     * @param workingMemory the {@code WorkingMemory}
     */
    private void setWorkingMemory(WorkingMemory workingMemory) {
        assert workingMemory: 'workingMemory can\'t be null'

        this.workingMemory = workingMemory
    }

    /**
     * Inserts a new fact object to the {@link WorkingMemory}.
     *
     * @param fact the fact to insert
     */
    def insert(fact) {
        assert fact: 'fact can\'t be null'

        this.workingMemory.addFact(fact)
    }

    /**
     * Throw an exception if the any command in the action part
     * of a {@link Rule} is not recognized.
     *
     * @param name the name of the command that was not recognized
     * @param args the args, if any, of the command that was not recognized
     * @throws ActionException if any command in the action part is not recognized
     */
    def methodMissing(String name, args) throws ActionException {
        throw new ActionException('Method \'' + name + '\' cannot be called in the rule action clause')
    }

}
