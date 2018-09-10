package com.temma.rules.engine.domain.model.rule

import com.temma.rules.engine.domain.model.common.RuleEngineException
import groovy.transform.PackageScope

@PackageScope
class RuleExecutionContext {

    private final Map<String, Object> factReferences = new HashMap<>()

    @PackageScope
    RuleExecutionContext() {}

    /**
     *
     *
     * @param name
     * @param value
     * @return
     */
    @PackageScope
    def set(String name, Object value) {
        if (name == null || value == null) {
            return false
        }

        this.factReferences.put(name, value)

        return true
    }

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
