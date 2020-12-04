package com.sirma.internal.nikola_markov_employees.data;

import static com.sirma.internal.nikola_markov_employees.util.ValidationUtil.checkPositive;

import java.util.Objects;

public final class EmployeePairOccupationResult {

  private final int employeeId;
  private final int employeeIdOther;
  private final int projectId;
  private final long daysWorked;

  public EmployeePairOccupationResult(
      final int employeeId, final int employeeIdOther, final int projectId, final long daysWorked) {
    validateConstructorArgs(employeeId, employeeIdOther, projectId, daysWorked);
    this.employeeId = employeeId;
    this.employeeIdOther = employeeIdOther;
    this.projectId = projectId;
    this.daysWorked = daysWorked;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EmployeePairOccupationResult)) {
      return false;
    }
    final EmployeePairOccupationResult that = (EmployeePairOccupationResult) o;
    return getEmployeeId() == that.getEmployeeId() &&
        getEmployeeIdOther() == that.getEmployeeIdOther() &&
        getProjectId() == that.getProjectId() &&
        getDaysWorked() == that.getDaysWorked();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getEmployeeId(), getEmployeeIdOther(), getProjectId(), getDaysWorked());
  }

  @Override
  public String toString() {
    return "EmployeePairOccupationResult{" +
        "employeeId=" + employeeId +
        ", employeeIdOther=" + employeeIdOther +
        ", projectId=" + projectId +
        ", daysWorked=" + daysWorked +
        '}';
  }

  public int getEmployeeId() {
    return employeeId;
  }

  public int getEmployeeIdOther() {
    return employeeIdOther;
  }

  public int getProjectId() {
    return projectId;
  }

  public long getDaysWorked() {
    return daysWorked;
  }

  private void validateConstructorArgs(int employeeId, final int employeeIdOther, final int projectId, final long daysWorked) {
    checkPositive(employeeId, "employeeId");
    checkPositive(employeeIdOther, "employeeIdOther");
    checkPositive(projectId, "projectId");
    checkPositive(daysWorked, "daysWorked");
  }
}
