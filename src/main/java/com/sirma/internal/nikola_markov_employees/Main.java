package com.sirma.internal.nikola_markov_employees;

import com.sirma.internal.nikola_markov_employees.data.EmployeePairOccupationResult;
import com.sirma.internal.nikola_markov_employees.query.EmployeesProcessor;
import com.sirma.internal.nikola_markov_employees.query.EmployeesProcessorImpl;
import com.sirma.internal.nikola_markov_employees.util.EmployeeOccupationParser;
import com.sirma.internal.nikola_markov_employees.util.EmployeeOccupationParserImpl;
import com.sirma.internal.nikola_markov_employees.util.WorkPeriodCalculator;
import com.sirma.internal.nikola_markov_employees.util.WorkPeriodCalculatorImpl;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public final class Main {

  private static final String RESULT_COLUMNS =
      "Employee ID #1, Employee ID #2, Project ID, Days worked";
  private static final String RESULT_LINE = "%s, %s, %s, %s";

  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      throw new IllegalArgumentException("Please provide a valid file path argument");
    }
    final Path path = Path.of(args[0]);

    final WorkPeriodCalculator workPeriodCalculator = new WorkPeriodCalculatorImpl();
    final EmployeeOccupationParser employeeOccupationParser = new EmployeeOccupationParserImpl();
    final EmployeesProcessor employeesProcessor =
        new EmployeesProcessorImpl(workPeriodCalculator, employeeOccupationParser);

    final List<EmployeePairOccupationResult> employeePairOccupationResults =
        employeesProcessor.getEmployeePairsResults(path);
    printResult(employeePairOccupationResults);
  }

  private static void printResult(
      final List<EmployeePairOccupationResult> employeePairOccupationResults) {
    System.out.println(RESULT_COLUMNS);
    for (EmployeePairOccupationResult result : employeePairOccupationResults) {
      System.out.println(
          String.format(
              RESULT_LINE,
              result.getEmployeeId(),
              result.getEmployeeIdOther(),
              result.getProjectId(),
              result.getDaysWorked()));
    }
  }
}
