package rules

import com.temma.rules.engine.utils.BenefitAnalysis
import com.temma.rules.engine.utils.Department
import com.temma.rules.engine.utils.Employee

rules {
    [
            rule {
                name 'Development Conference Price Discount'
                when {
                    [
                            result = where { it in Employee && it.department == Department.Engineering },
                            analysis = where { it in BenefitAnalysis }
                    ]
                }
                then {
                    analysis.devConferenceDiscount = true
                }
            }
    ]
}