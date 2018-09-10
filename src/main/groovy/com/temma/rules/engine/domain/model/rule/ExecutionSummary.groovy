package com.temma.rules.engine.domain.model.rule

import java.time.Duration
import java.time.Instant

/**
 *
 **/
class ExecutionSummary {

    private int rulesFired

    private Instant startTime;

    private Instant endTime;

    private Duration totalElapsedTime

    private String threadName

    ExecutionSummary(int rulesFired, Instant startTime, Instant endTime, String threadName) {
        super()

        this.setRulesFired(rulesFired)
        this.setStartTime(startTime)
        this.setEndTime(endTime)
        this.setThreadName(threadName)

        this.initialize()
    }

    private void setRulesFired(int rulesFired) {
        if (rulesFired < 0) {
            throw new IllegalArgumentException("Number of rules fired cannot be negative")
        }

        this.rulesFired = rulesFired
    }

    private void setStartTime(Instant startTime) {
        if (startTime == null) {
            throw new IllegalArgumentException("startTime can't be null")
        }

        this.startTime = startTime
    }

    private void setEndTime(Instant endTime) {
        if (endTime == null) {
            throw new IllegalArgumentException("endTime can't be null")
        }

        this.endTime = endTime
    }

    private void setThreadName(String threadName) {
        Objects.requireNonNull(threadName, "threadName can't be null")

        this.threadName = threadName
    }

    private void initialize() {
        this.totalElapsedTime = Duration.between(this.startTime(), this.endTime())
    }

    Instant startTime() {
        return this.startTime
    }

    Instant endTime() {
        return this.endTime
    }

    Duration totalElapsedTime() {
        return this.totalElapsedTime
    }

    int rulesFired() {
        return this.rulesFired
    }

    String threadName() {
        return this.threadName
    }

    @Override
    String toString() {
        return new StringBuilder().append("ExecutionSummary: {rulesFired:'")
                                  .append(this.rulesFired())
                                  .append("', startTime: '")
                                  .append(this.startTime())
                                  .append("', endTime: '")
                                  .append(this.endTime())
                                  .append("', totalElapsedTime: '")
                                  .append(this.totalElapsedTime())
                                  .append("', threadName: '")
                                  .append(this.threadName())
                                  .append("'}")
    }

}
