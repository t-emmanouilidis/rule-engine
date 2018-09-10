package com.temma.rules.engine.utils

import java.time.LocalDate

/**
 *
 */
class Employee {

    String name
    Department department
    LocalDate hiredOn
    boolean driveLicenceOwner
    boolean isHeavyCommuter
    boolean isParent

    private Employee(String name, Department department, LocalDate hiredOn, boolean driveLicenceOwner, boolean isHeavyCommuter, boolean isParent) {
        this.name = name
        this.department = department
        this.hiredOn = hiredOn
        this.driveLicenceOwner = driveLicenceOwner
        this.isHeavyCommuter = isHeavyCommuter
        this.isParent = isParent
    }

    static def builder() {
        return new Builder()
    }

    private static class Builder {
        String name
        Department department
        LocalDate hiredOn = LocalDate.now()
        boolean driveLicenceOwner = false
        boolean isHeavyCommuter = false
        boolean isParent = false

        private Builder() {

        }

        def withName(String name) {
            assert name

            this.name = name

            return this
        }

        def withDepartment(Department department) {
            assert department

            this.department = department

            return this
        }

        def isParent() {
            this.isParent = true

            return this
        }

        def hasDriverLicence() {
            this.driveLicenceOwner = true

            return this
        }

        def build() {
            assert this.name
            assert this.department

            return new Employee(this.name, this.department, this.hiredOn, this.driveLicenceOwner, this.isHeavyCommuter, this.isParent)
        }

    }
}
