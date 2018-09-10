package rules

import static com.temma.rules.engine.interfaces.dsl.Rules.rule
import static com.temma.rules.engine.interfaces.dsl.Rules.rules

rules {
    [
            rule {
                name 'TestRule'
                when {
                    [
                            true
                    ]
                }
                then {
                    println 'TestRule1'
                }
            },
            rule {
                name 'TestRule'
                when {
                    [
                            true
                    ]
                }
                then {
                    println 'TestRule2'
                }
            }
    ]
}
