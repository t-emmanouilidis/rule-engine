package com.temma.rules.engine.domain.model.rule

import com.temma.rules.engine.domain.model.common.RuleEngineException

class ConditionException extends RuleEngineException {

    def ConditionException() {
        super()
    }

    def ConditionException(String message) {
        super(message)
    }

    def ConditionException(String message, Throwable cause) {
        super(message, cause)
    }

    def ConditionException(Throwable cause) {
        super(cause)
    }
}
