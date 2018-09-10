package com.temma.rules.engine.interfaces.dsl

import com.temma.rules.engine.domain.model.common.RuleEngineException
import com.temma.rules.engine.domain.model.rule.ActionException
import com.temma.rules.engine.domain.model.rule.ConditionException
import com.temma.rules.engine.domain.model.rule.ExecutionSummary
import com.temma.rules.engine.domain.model.rule.KnowledgeBase
import com.temma.rules.engine.utils.BenefitAnalysis
import com.temma.rules.engine.utils.Department
import com.temma.rules.engine.utils.Employee
import org.codehaus.groovy.control.CompilationFailedException
import org.junit.Assert
import org.junit.Test

class RuleEvaluationTest {

    @Test
    void basicVariablePassingBetweenConditionAndActionTest() {
        // Set
        File file = fileFrom('/rules/setting_spec_rules_v1.groovy')

        KnowledgeBase knowledgeBase = new KnowledgeBaseBuilder()
                .withBaseName('default')
                .withRuleFile(file)
                .build()

        def ruleSession = knowledgeBase.newSession()

        def employee = Employee.builder()
                               .withName('Tryfon Emman')
                               .withDepartment(Department.Engineering)
                               .hasDriverLicence()
                               .build()

        ruleSession.newFact(employee)
        ruleSession.newFact(new BenefitAnalysis())

        // Act
        ExecutionSummary summary = ruleSession.fireAllRules()

        // Assert
        Assert.assertEquals(1, summary.rulesFired())

        Collection<BenefitAnalysis> results = ruleSession.getObjects(BenefitAnalysis.class)
        Assert.assertEquals(1, results.size())

        BenefitAnalysis analysis = results.iterator().next()

        Assert.assertTrue(analysis.devConferenceDiscount)
        Assert.assertFalse(analysis.bikeAllowance)
        Assert.assertFalse(analysis.dayNurseryAllowance)
        Assert.assertFalse(analysis.peopleMgmtCourseDiscount)
    }

    @Test(expected = ConditionException.class)
    void testIllegalMethodCalledOnConditionPart() {
        File file = fileFrom('/rules/spec_rules_err_v2.groovy')

        KnowledgeBase knowledgeBase = new KnowledgeBaseBuilder()
                .withBaseName('default')
                .withRuleFile(file)
                .build()

        def ruleSession = knowledgeBase.newSession()

        ruleSession.fireAllRules()
    }

    @Test(expected = CompilationFailedException.class)
    void testScrambledRuleFile() {
        File file = fileFrom('/rules/scrambled_rule_file.groovy')

        new KnowledgeBaseBuilder()
                .withBaseName('default')
                .withRuleFile(file)
                .build()
    }

    @Test(expected = RuleEngineException.class)
    void variablesShouldNotBeSharedBetweenDifferentRules() {
        File file = fileFrom('/rules/variable_sharing_between_rules_err_v1.groovy')

        KnowledgeBase knowledgeBase = new KnowledgeBaseBuilder()
                .withBaseName('default')
                .withRuleFile(file)
                .build()

        def session = knowledgeBase.newSession()

        session.newFact(Employee.builder().withName('John Doe').withDepartment(Department.QA).build())

        session.fireAllRules()
    }

    @Test(expected = RuleEngineException.class)
    void testThatEquallyNamedRulesAreNotLoaded() {
        // set
        File file = fileFrom('/rules/duplicate_rules_err_v1.groovy')

        new KnowledgeBaseBuilder()
                .withBaseName('default')
                .withRuleFile(file)
                .build()
    }

    @Test
    void basicRuleChainingTest() {

        File file = fileFrom('/rules/chaining_spec_rules_v1.groovy')

        def knowledgeBase = new KnowledgeBaseBuilder()
                .withBaseName('default')
                .withRuleFile(file)
                .build()

        def ruleSession = knowledgeBase.newSession()

        def employee = Employee.builder()
                               .withName('John Doe')
                               .withDepartment(Department.Engineering)
                               .hasDriverLicence()
                               .build()

        ruleSession.newFact(employee)

        def analysis = new BenefitAnalysis()
        analysis.bikeAllowance = true

        ruleSession.newFact(analysis)

        ExecutionSummary summary = ruleSession.fireAllRules()

        Assert.assertEquals(2, summary.rulesFired())

        Assert.assertFalse(analysis.bikeAllowance)
    }

    @Test
    void basicVariableSettingTest() {

        File file = fileFrom('/rules/setting_spec_rules_v1.groovy')

        def knowledgeBase = new KnowledgeBaseBuilder()
                .withBaseName('default')
                .withRuleFile(file)
                .build()

        def ruleSession = knowledgeBase.newSession()

        def employee = Employee.builder()
                               .withName('John Doe')
                               .withDepartment(Department.Engineering)
                               .build()

        ruleSession.newFact(employee)

        ruleSession.newFact(new BenefitAnalysis())

        ExecutionSummary summary = ruleSession.fireAllRules()

        Assert.assertEquals(1, summary.rulesFired())

        def results = ruleSession.getObjects(BenefitAnalysis.class)

        Assert.assertEquals(1, results.size())

        def analysis = results.iterator().next()

        Assert.assertTrue(analysis.devConferenceDiscount)
    }

    @Test(expected = ActionException.class)
    void ruleThrowsExceptionTest() {
        File ruleFile = fileFrom('/rules/throwing_rule_v1.groovy')

        def knowledgeBase = new KnowledgeBaseBuilder().withBaseName('default')
                                                      .withRuleFile(ruleFile)
                                                      .build()

        def session = knowledgeBase.newSession()

        session.fireAllRules()
    }

    private File fileFrom(String filePath) {
        URL fileURL = getClass().getResource(filePath)

        return new File(fileURL.toURI())
    }

}
