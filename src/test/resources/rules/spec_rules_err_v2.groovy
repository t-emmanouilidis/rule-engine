package rules

rules {
    [
            rule {
                name 'ErrorRuleV2'
                when {
                    [
                            insert ("NewFact")
                    ]
                }
                then {
                    println "Hello World"
                }
            }
    ]
}