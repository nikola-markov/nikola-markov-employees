package com.sirma.internal.nikola_markov_employees.util;

import com.sirma.internal.nikola_markov_employees.data.EmployeeOccupation;

public interface WorkPeriodCalculator {

  long calculateJointWorkDays(
      EmployeeOccupation employeeOccupation, EmployeeOccupation otherEmployeeOccupation);
}
