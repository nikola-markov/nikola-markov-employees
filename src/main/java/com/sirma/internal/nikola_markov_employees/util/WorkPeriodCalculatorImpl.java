package com.sirma.internal.nikola_markov_employees.util;

import static com.sirma.internal.nikola_markov_employees.util.ValidationUtil.checkNullArg;

import com.sirma.internal.nikola_markov_employees.data.EmployeeOccupation;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class WorkPeriodCalculatorImpl implements WorkPeriodCalculator {

  @Override
  public long calculateJointWorkDays(
      final EmployeeOccupation employeeOccupation,
      final EmployeeOccupation employeeOccupationOther) {

    validateConstructorArgs(employeeOccupation, employeeOccupationOther);
    if (employeeOccupation.getProjectId() != employeeOccupationOther.getProjectId()) {
      throw new IllegalStateException("Project IDs do not match");
    }

    long jointWorkDays = 0;
    if (isJointWorkPeriodExist(employeeOccupation, employeeOccupationOther)) {
      final LocalDate jointDateFrom =
          employeeOccupation.getDateFrom().isAfter(employeeOccupationOther.getDateFrom())
              ? employeeOccupation.getDateFrom()
              : employeeOccupationOther.getDateFrom();

      final LocalDate jointDateTo =
          employeeOccupation.getDateTo().isBefore(employeeOccupationOther.getDateTo())
              ? employeeOccupation.getDateTo()
              : employeeOccupationOther.getDateTo();

      jointWorkDays = ChronoUnit.DAYS.between(jointDateFrom, jointDateTo) + 1;
    }
    return jointWorkDays;
  }

  boolean isJointWorkPeriodExist(
      final EmployeeOccupation employeeOccupation,
      final EmployeeOccupation otherEmployeeOccupation) {
    return !(employeeOccupation.getDateTo().isBefore(otherEmployeeOccupation.getDateFrom())
        || employeeOccupation.getDateFrom().isAfter(otherEmployeeOccupation.getDateTo()));
  }

  private void validateConstructorArgs(final EmployeeOccupation employeeOccupation,
      final EmployeeOccupation otherEmployeeOccupation) {
    checkNullArg(employeeOccupation, "employeeOccupation");
    checkNullArg(otherEmployeeOccupation, "otherEmployeeOccupation");
  }
}
