package com.temma.rules.engine.domain.model.rule

import com.temma.rules.engine.domain.model.common.RuleEngineException
import groovy.transform.PackageScope

/**
 * Simple container for references to facts created when executing one or more rules.
 * This allows the client to define a reference in the where clause and reuse it
 * in the actions clause.
 *
 * @author temma
 */
@PackageScope
class RuleExecutionContext {

    private final Map<String, Object> factReferences = new HashMap<>()

    @PackageScope
    RuleExecutionContext() {}

    /**
     * Sets a new reference to the given variable
     *
     * @param name the name of the variable
     * @param value the variable
     * @return true to denote that this condition passes
     */
    @PackageScope
    def set(String name, Object value) {
        if (name == null || value == null) {
            return false
        }

        this.factReferences.put(name, value)

        return true
    }

    /**
     * Retrieves the variable that the given name references if it exists.
     *
     * @param name the name of the variable
     * @return the variable if exists in the context
     * @throws RuleEngineException if a variable with this name cannot be found in the context
     */
    @PackageScope
    def get(String name) {

        assert name: 'name can\'t be null'

        def fact = this.factReferences.get(name)

        if (fact == null) {
            throw new RuleEngineException('No variable named \'' + name + '\' has been set in the conditions\' clause')
        }

        return fact
    }

}
