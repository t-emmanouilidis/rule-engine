package com.temma.rules.engine.interfaces.dsl

import com.temma.rules.engine.domain.model.rule.ExecutionSummary
import com.temma.rules.engine.domain.model.rule.KnowledgeBase
import com.temma.rules.engine.utils.BenefitAnalysis
import com.temma.rules.engine.utils.Department
import com.temma.rules.engine.utils.Employee
import org.codehaus.groovy.control.CompilationFailedException
import org.junit.Assert
import org.junit.Test


/**
 *
 **/
class BasicTest {

    @Test
    void basicRuleSpecLoadTest() {
        File file = fileFrom('/rules/spec_rules_v1.groovy')

        KnowledgeBase knowledgeBase = new KnowledgeBaseBuilder()
                .withBaseName('default')
                .withRuleFile(file)
                .build()

        Assert.assertEquals(2, knowledgeBase.rules.size())
    }

    @Test(expected = CompilationFailedException.class)
    void testKnowledgeBaseLoadingWithErrors() {
        File file = fileFrom('/rules/spec_rules_err_v1.groovy')

        new KnowledgeBaseBuilder()
                .withBaseName('default')
                .withRuleFile(file)
                .build()
    }

    @Test
    void testLoadUsingGroovyClassLoader() {

        File ruleFile = fileFrom('/rules/spec_rules_v1.groovy')

        KnowledgeBase knowledgeBase = new KnowledgeBaseBuilder()
                .withBaseName('default')
                .withRuleFile(ruleFile)
                .build()

        Assert.assertEquals(2, knowledgeBase.rules().size())
    }

    @Test
    void basicRuleLoadingAndEvaluatingTest() {

        File file = fileFrom('/rules/spec_rules_v1.groovy')

        KnowledgeBase knowledgeBase = new KnowledgeBaseBuilder()
                .withBaseName('default')
                .withRuleFile(file)
                .build()

        def ruleSession = knowledgeBase.newSession()

        def employee = Employee.builder()
                               .withName('John Doe')
                               .withDepartment(Department.QA)
                               .isParent()
                               .build()

        ruleSession.newFact(employee)

        ruleSession.newFact(new BenefitAnalysis())

        ExecutionSummary summary = ruleSession.fireAllRules()

        Assert.assertEquals(2, summary.rulesFired())

        def results = ruleSession.getObjects(BenefitAnalysis.class)

        Assert.assertEquals(1, results.size())

        def analysis = results.iterator().next()

        Assert.assertTrue(analysis.peopleMgmtCourseDiscount)
        Assert.assertTrue(analysis.dayNurseryAllowance)
    }

    @Test
    void basicDSLRuleTest() {
        // Set
        File file = fileFrom('/rules/basic_rules_v1.groovy')

        KnowledgeBase knowledgeBase = new KnowledgeBaseBuilder()
                .withBaseName('default')
                .withRuleFile(file)
                .build()

        // Act
        ExecutionSummary summary = knowledgeBase.newSession().fireAllRules()

        // Assert
        Assert.assertEquals(2, summary.rulesFired())
    }

    @Test
    void basicRuleSetSpecTest() {
        // Set
        File file = fileFrom('/rules/basic_rules_v1.groovy')

        KnowledgeBase knowledgeBase = new KnowledgeBaseBuilder()
                .withBaseName('default')
                .withRuleFile(file)
                .build()

        // Act
        ExecutionSummary summary = knowledgeBase.newSession().fireAllRules()

        // Assert
        Assert.assertEquals(2, summary.rulesFired())
    }

    private File fileFrom(String filePath) {
        URL fileURL = getClass().getResource(filePath)

        return new File(fileURL.toURI())
    }

}
