package com.temma.rules.engine.domain.model.rule

import com.temma.rules.engine.domain.model.common.RuleEngineException

/**
 * A {@code KnowledgeBase} instance is a named collection of rules and
 * knows how to create a session which is an object that a client can use to evaluate against the rules.
 */
class KnowledgeBase {

    private String baseName

    private Collection<Rule> rules

    KnowledgeBase(String baseName, Collection<Rule> rules) {
        super()

        this.setBaseName(baseName)
        this.setRules(rules)
    }

    private void setBaseName(String baseName) {
        assert baseName: 'baseName can\'t be null'

        this.baseName = baseName
    }

    private void setRules(Collection<Rule> rules) {
        assert rules: 'rules can\'t be null or empty'

        Set<Rule> validationSet = new HashSet<>()

        for (rule in rules) {

            if (!validationSet.add(rule)) {
                throw new RuleEngineException('A rule with name \'' + rule.name() + '\' already exists in the knowledge base')
            }

        }

        this.rules = rules
    }

    Collection<Rule> rules() {
        return this.rules
    }

    Session newSession() {
        return new Session(Collections.unmodifiableList(new ArrayList<Rule>(this.rules)))
    }

}
