package rules

import static com.temma.rules.engine.interfaces.dsl.Rules.rule

rules {
    [
            rule {
                name 'Rule 1'
                when {
                    [
                            1 + 1 == 2
                    ]
                }
                then {
                    println 'Rule Test 1'
                }
            },
            rule {
                name 'Rule 2'
                when {
                    [
                            2 + 2 == 4
                    ]
                }
                then {
                    println 'Rule Test 2'
                }
            }
    ]
}