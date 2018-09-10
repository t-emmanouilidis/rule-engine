package rules

import com.temma.rules.engine.utils.BenefitAnalysis
import com.temma.rules.engine.utils.Employee

rules {
    [
            rule {
                name 'Free Bike Allowance Benefit v1'
                when {
                    [
                            employee = where { it in Employee && it.driveLicenceOwner },
                            analysis = where { it in BenefitAnalysis }
                    ]
                }
                then {
                    employee.isHeavyCommuter = false
                }
            },
            rule {
                name 'Free Bike Allowance Benefit v2'
                when {
                    [
                            employee = where { it in Employee && !it.isHeavyCommuter },
                            analysis = where { it in BenefitAnalysis }
                    ]
                }
                then {
                    analysis.bikeAllowance = false
                }
            }
    ]
}