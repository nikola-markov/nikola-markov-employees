package com.sirma.internal.nikola_markov_employees.data;

import static com.sirma.internal.nikola_markov_employees.util.ValidationUtil.checkNullArg;
import static com.sirma.internal.nikola_markov_employees.util.ValidationUtil.checkPositive;

import java.time.LocalDate;
import java.util.Objects;

public final class EmployeeOccupation {

  private final int employeeId;
  private final int projectId;
  private final LocalDate dateFrom;
  private final LocalDate dateTo;

  public EmployeeOccupation(final int employeeId, final int projectId, final LocalDate dateFrom, final LocalDate dateTo) {
    validateConstructorArgs(employeeId, projectId, dateFrom, dateTo);
    this.employeeId = employeeId;
    this.projectId = projectId;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EmployeeOccupation)) {
      return false;
    }
    final EmployeeOccupation that = (EmployeeOccupation) o;
    return getEmployeeId() == that.getEmployeeId() &&
        getProjectId() == that.getProjectId() &&
        getDateFrom().equals(that.getDateFrom()) &&
        getDateTo().equals(that.getDateTo());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getEmployeeId(), getProjectId(), getDateFrom(), getDateTo());
  }

  @Override
  public String toString() {
    return "EmployeeOccupation{" +
        "employeeId=" + employeeId +
        ", projectId=" + projectId +
        ", dateFrom=" + dateFrom +
        ", dateTo=" + dateTo +
        '}';
  }

  public int getEmployeeId() {
    return employeeId;
  }

  public int getProjectId() {
    return projectId;
  }

  public LocalDate getDateFrom() {
    return dateFrom;
  }

  public LocalDate getDateTo() {
    return dateTo;
  }

  private void validateConstructorArgs(final int employeeId, final int projectId, final LocalDate dateFrom, final LocalDate dateTo) {
    checkPositive(employeeId, "employeeId");
    checkPositive(projectId, "projectId");
    checkNullArg(dateFrom, "dateFrom");
    checkNullArg(dateTo, "dateTo");
  }
}
