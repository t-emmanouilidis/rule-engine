package rules

import com.temma.rules.engine.utils.BenefitAnalysis
import com.temma.rules.engine.utils.Department
import com.temma.rules.engine.utils.Employee

rules {
    [
            rule {
                name 'Day Nursery Allowance Benefit'
                when {
                    [
                            employee = where { it in Employee && it.isParent },
                            analysis = where { it in BenefitAnalysis }
                    ]
                }
                then {
                    analysis.dayNurseryAllowance = true
                }
            },

            rule {
                name 'People Management Course Discount Benefit'
                when {
                    [
                            employee = where { it in Employee && it.department == Department.QA },
                            analysis = where { it in BenefitAnalysis }
                    ]
                }
                then {
                    analysis.peopleMgmtCourseDiscount = true
                }
            }
    ]
}