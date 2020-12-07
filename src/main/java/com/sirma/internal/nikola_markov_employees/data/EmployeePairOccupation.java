package com.sirma.internal.nikola_markov_employees.data;

import static com.sirma.internal.nikola_markov_employees.util.ValidationUtil.checkNullArg;
import static com.sirma.internal.nikola_markov_employees.util.ValidationUtil.checkPositive;

import java.util.Objects;

public final class EmployeePairOccupation {

  private final EmployeePair employeePair;
  private final int projectId;
  private final long daysCount;

  public EmployeePairOccupation(final EmployeePair employeePair, final int projectId, final long daysCount) {
    validateConstructorArgs(employeePair, projectId, daysCount);
    this.employeePair = employeePair;
    this.projectId = projectId;
    this.daysCount = daysCount;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EmployeePairOccupation)) {
      return false;
    }
    final EmployeePairOccupation that = (EmployeePairOccupation) o;
    return getProjectId() == that.getProjectId() &&
        getDaysCount() == that.getDaysCount() &&
        getEmployeePair().equals(that.getEmployeePair());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getEmployeePair(), getProjectId(), getDaysCount());
  }

  @Override
  public String toString() {
    return "EmployeePairOccupation{" +
        "employeePair=" + employeePair +
        ", projectId=" + projectId +
        ", daysCount=" + daysCount +
        '}';
  }

  public EmployeePair getEmployeePair() {
    return employeePair;
  }

  public int getProjectId() {
    return projectId;
  }

  public long getDaysCount() {
    return daysCount;
  }

  private void validateConstructorArgs(final EmployeePair employeePair, final int projectId, final long daysCount) {
    checkNullArg(employeePair, "employeePair");
    checkPositive(projectId, "projectId");
    checkPositive(daysCount, "daysCount");
  }
}
