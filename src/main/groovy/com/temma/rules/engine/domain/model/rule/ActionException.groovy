package com.temma.rules.engine.domain.model.rule

import com.temma.rules.engine.domain.model.common.RuleEngineException

class ActionException extends RuleEngineException {

    def ActionException() {
        super()
    }

    def ActionException(String message) {
        super(message)
    }

    def ActionException(String message, Throwable cause) {
        super(message, cause)
    }

    def ActionException(Throwable cause) {
        super(cause)
    }
}
