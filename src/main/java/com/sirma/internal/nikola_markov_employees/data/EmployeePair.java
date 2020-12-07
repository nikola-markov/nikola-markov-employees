package com.sirma.internal.nikola_markov_employees.data;

import static com.sirma.internal.nikola_markov_employees.util.ValidationUtil.checkPositive;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class EmployeePair {

  private final Set<Integer> employeePair;

  public EmployeePair(final int employeeId, final int employeePairId) {
    validateConstructorArgs(employeeId, employeePairId);
    employeePair = new HashSet<>();
    employeePair.add(employeeId);
    employeePair.add(employeePairId);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EmployeePair)) {
      return false;
    }
    final EmployeePair that = (EmployeePair) o;
    return employeePair.equals(that.employeePair);
  }

  @Override
  public int hashCode() {
    return Objects.hash(employeePair);
  }

  @Override
  public String toString() {
    return "EmployeePair{" +
        "employeePair=" + employeePair +
        '}';
  }

  public Set<Integer> getEmployeePair() {
    return Collections.unmodifiableSet(employeePair);
  }

  private void validateConstructorArgs(final int employeeId, final int employeePairId) {
    checkPositive(employeeId, "employeeId");
    checkPositive(employeePairId, "employeePairId");
  }
}
