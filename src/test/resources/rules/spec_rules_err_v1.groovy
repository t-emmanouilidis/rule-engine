package rules

rules {
    [
            rule {
                name 'ErrorRule'
                when {
                    [
                            where { it in SimpleDeclaration && 'Tryfon' == it.consignee }
                    ]
                }
                then {
                    insert( new RiskScore(5))
                }
            }
    ]
}