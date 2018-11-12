package com.temma.rules.engine.interfaces.dsl

import com.temma.rules.engine.domain.model.rule.KnowledgeBase
import com.temma.rules.engine.domain.model.rule.Rule
import com.temma.rules.engine.infrastructure.rule.basic.RuleFileLoader

/**
 * A builder class for a {@link KnowledgeBase}s.
 *
 * @author temma
 */
class KnowledgeBaseBuilder {

    private String baseName

    private ClassLoader classLoader = getClass().getClassLoader()

    private final List<File> ruleFiles = new ArrayList<>()

    private final List<String> ruleFileContentList = new ArrayList<>()

    private final RuleFileLoader ruleRepository = new RuleFileLoader()

    /**
     * Sets the name of the new {@link KnowledgeBase}
     *
     * @param baseName the name of the new {@code KnowledgeBase}
     * @return this builder instance
     */
    KnowledgeBaseBuilder withBaseName(String baseName) {
        assert baseName: 'baseName can\'t be null'

        this.baseName = baseName

        return this
    }

    /**
     * Sets an optional {@link ClassLoader} for the new {@link KnowledgeBase}.
     *
     * @param classLoader the optional {@code ClassLoader}
     * @return this builder instance
     */
    KnowledgeBaseBuilder withClassLoader(ClassLoader classLoader) {
        assert classLoader: 'classLoader can\'t be null'

        this.classLoader = classLoader

        return this
    }

    /**
     * Adds a new rule {@link File} to the builder.
     * <p>
     * The rule file should be a Groovy script that evaluates
     * to a {@link Collection} of {@link Rules}.
     *
     * @param ruleFile the rule {@code File}
     * @return this builder instance
     */
    KnowledgeBaseBuilder withRuleFile(File ruleFile) {
        assert ruleFile: 'ruleFile can\'t be null'

        this.ruleFiles.add(ruleFile)

        return this
    }

    /**
     * Adds a {@link List} of rule file contents to the builder.
     * <p>
     * The {@link String} instances should contain Groovy scripts that
     * evaluate to {@link Collection} of {@link Rule}s.
     *
     * @param ruleFileContentList the list with the rule content as a {@code String}
     * @return this builder instance
     */
    KnowledgeBaseBuilder withRuleFileContentList(List<String> ruleFileContentList) {
        assert ruleFileContentList: 'ruleFileContentList can\'t be null or empty'

        this.ruleFileContentList.addAll(ruleFileContentList)

        return this
    }

    /**
     * Builds the knowledge base by reading and evaluating the Groovy script contents
     * in the provided {@link File} and {@link String} instances.
     *
     * @return a new {@link KnowledgeBase} with the given name and {@link Rule}s
     */
    KnowledgeBase build() {
        assert this.baseName: 'baseName can\'t be null'
        assert this.classLoader: 'classLoader can\'t be null'
        assert this.ruleFiles || this.ruleFileContentList: 'Both ruleFiles and ruleFileContentList can\'t be null or empty'

        final Collection<Rule> rules = new ArrayList<>()

        def self = this

        this.ruleFiles.forEach({ fileURL -> rules.addAll(self.ruleRepository.allRules(fileURL, self.classLoader)) })

        this.ruleFileContentList.forEach({ fileContent -> rules.addAll(self.ruleRepository.evaluate(fileContent, self.classLoader)) })

        return new KnowledgeBase(this.baseName, rules)
    }

}
