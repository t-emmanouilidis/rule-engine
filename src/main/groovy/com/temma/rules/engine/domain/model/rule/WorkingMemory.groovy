package com.temma.rules.engine.domain.model.rule

import groovy.transform.PackageScope

import java.util.stream.Collectors

@PackageScope
class WorkingMemory {

    private final Collection<Object> facts = new ArrayList<>()

    @PackageScope
    WorkingMemory() {
        super()
    }

    /**
     * Adds a fact to the working memory
     *
     * @param fact
     * @return
     */
    @PackageScope
    def addFact(fact) {
        assert fact: 'fact can\'t be null'

        this.facts.add(fact)
    }

    /**
     * Retrieves the first fact in the working memory
     * that matches the given predicate in the form of a {@link Closure}
     *
     * @param conditionClosure the predicate closure
     * @return the first fact that matches or null if no matching fact
     * can be found
     */
    @PackageScope
    def factThatMatches(@DelegatesTo(Session) Closure<Boolean> conditionClosure) {
        assert conditionClosure: 'conditionClosure can\'t be null'

        return this.facts.stream().filter(conditionClosure).findFirst().orElse(null)
    }

    /**
     * Retrieves all facts in the working memory that are of
     * the given type.
     *
     * @param requiredType the type whose instances to retrieve
     * from the working memory
     * @return the facts that are of the type
     */
    @PackageScope
    <T> Collection<T> getObjects(Class<T> requiredType) {
        assert requiredType: 'requiredType can\'t be null'

        return this.facts.stream()
                   .filter({ requiredType.isAssignableFrom(it.class) })
                   .collect(Collectors.toList())
    }

}
