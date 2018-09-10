package rules

import static com.temma.rules.engine.interfaces.dsl.Rules.*

rules {
    [
            rule {
                name "ThrowingRule"
                when {
                    [
                            true
                    ]
                }
                then {
                    throw new Exception("RuleException")
                }
            }
    ]
}
