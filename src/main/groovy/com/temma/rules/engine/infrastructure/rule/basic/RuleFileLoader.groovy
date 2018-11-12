package com.temma.rules.engine.infrastructure.rule.basic

import com.temma.rules.engine.domain.model.rule.Rule
import com.temma.rules.engine.interfaces.dsl.Rules
import org.codehaus.groovy.control.CompilerConfiguration

import java.nio.charset.StandardCharsets

/**
 * Basic load utility class for loading rules from files
 *
 * @author temma
 */
class RuleFileLoader {

    RuleFileLoader() {
        super();
    }

    /**
     * Reads the given rule file and evaluates it as
     * a Groovy script.
     * <p>
     * If the passed Groovy script evaluates to a collection
     * of rules
     *
     * @param ruleFile the file containing the {@link Rule}(s)
     * @param classLoader the class loader to use to load the rules (in case they need access to classes outside the current classloader)
     * @return a {@link Collection} of {@link Rule}s
     */
    Collection<Rule> allRules(Object ruleFile, ClassLoader classLoader) {

        assert ruleFile: 'ruleFileURL can\'t be null'

        assert ruleFile in File: 'ruleFileURL is not a File'

        String scriptText = ruleFile.getText(StandardCharsets.UTF_8.name())

        return evaluate(scriptText, classLoader)

    }

    /**
     * Read and parses one or more rules from the given script text.
     *
     * @param scriptText the text containing the rule scripts
     * @param classLoader the class loader to use to load the rules (in case they need access to classes outside the current classloader)
     * @return a {@link Collection} of {@link Rule}s
     */
    Collection<Rule> evaluate(String scriptText, ClassLoader classLoader) {

        assert scriptText: 'scriptText can\'t be null'

        def evaluatingClassLoader = classLoader ?: this.getClass().getClassLoader()

        def compilerConfiguration = new CompilerConfiguration()

        compilerConfiguration.setScriptBaseClass(DelegatingScript.class.getName())

        def groovyShell = new GroovyShell(evaluatingClassLoader, compilerConfiguration)

        def scriptObject = groovyShell.parse(scriptText)

        scriptObject.setDelegate(new Rules())

        Collection<Rule> loadedRules = scriptObject.run()

        return loadedRules
    }

}
