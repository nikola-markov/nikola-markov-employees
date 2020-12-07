package com.sirma.internal.nikola_markov_employees.util;

import static com.sirma.internal.nikola_markov_employees.util.ValidationUtil.checkNullArg;
import static com.sirma.internal.nikola_markov_employees.util.DateFormatPattern.PATTERN_1;

import com.sirma.internal.nikola_markov_employees.data.EmployeeOccupation;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class EmployeeOccupationParserImpl implements EmployeeOccupationParser {

  private static final String DELIMITER = ",";
  private static final String NULL_DATE = "null";

  @Override
  public EmployeeOccupation parseEmployeeOccupation(final String line) {
    checkNullArg(line, "line");
    int employeeId;
    int projectId;
    LocalDate dateFrom;
    LocalDate dateTo;
    try {
      final String[] attributes = line.split(DELIMITER);
      employeeId = Integer.parseInt(attributes[0].strip());
      projectId = Integer.parseInt(attributes[1].strip());

      final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_1.getPattern());
      dateFrom = LocalDate.parse(attributes[2].strip(), formatter);

      final String date = attributes[3].strip();
      if (date.equalsIgnoreCase(NULL_DATE)) {
        dateTo = LocalDate.now();
        dateTo.format(formatter);
      } else {
        dateTo = LocalDate.parse(date, formatter);
      }
    } catch (RuntimeException e) {
      throw new IllegalStateException("Failed to parse employee occupation line:\n" + line, e);
    }
    return new EmployeeOccupation(employeeId, projectId, dateFrom, dateTo);
  }
}
