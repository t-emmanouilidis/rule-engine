package com.temma.rules.engine.domain.model.common

class RuleEngineException extends Exception {

    RuleEngineException() {
        super()
    }

    RuleEngineException(String message) {
        super(message)
    }

    RuleEngineException(String message, Throwable cause) {
        super(message, cause)
    }

    RuleEngineException(Throwable cause) {
        super(cause)
    }
}
