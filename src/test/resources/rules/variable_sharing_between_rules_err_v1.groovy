package rules

import com.temma.rules.engine.utils.Employee

import static com.temma.rules.engine.interfaces.dsl.Rules.rule
import static com.temma.rules.engine.interfaces.dsl.Rules.rules

rules {
    [
            rule {
                name 'Rule 1'
                when {
                    [
                            employee = where { it in Employee && it.name == 'John Doe' }
                    ]
                }
                then {
                    println 'Rule 1 fired!'
                }
            },
            rule {
                name 'Rule 2'
                when {
                    [
                            1 + 1 == 2
                    ]
                }
                then {
                    insert(employee)
                    println 'Rule 2 fired! Employee: ' + employee
                }
            }
    ]
}