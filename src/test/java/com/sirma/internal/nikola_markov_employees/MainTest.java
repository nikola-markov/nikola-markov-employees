package com.sirma.internal.nikola_markov_employees;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import com.sirma.internal.nikola_markov_employees.data.EmployeePairOccupationResult;
import com.sirma.internal.nikola_markov_employees.query.EmployeesProcessor;
import com.sirma.internal.nikola_markov_employees.query.EmployeesProcessorImpl;
import com.sirma.internal.nikola_markov_employees.util.EmployeeOccupationParser;
import com.sirma.internal.nikola_markov_employees.util.EmployeeOccupationParserImpl;
import com.sirma.internal.nikola_markov_employees.util.WorkPeriodCalculator;
import com.sirma.internal.nikola_markov_employees.util.WorkPeriodCalculatorImpl;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class MainTest {

  private static final String CFG_EMPLOYEES = "employees.txt";
  private static final String MSG_MISSING_RESOURCE = "Missing resource %s";

  private List<EmployeePairOccupationResult> employeePairOccupationResults;

  @BeforeClass
  public void setUp() {
    final WorkPeriodCalculator workPeriodCalculator = new WorkPeriodCalculatorImpl();
    final EmployeeOccupationParser employeeOccupationParser = new EmployeeOccupationParserImpl();
    final EmployeesProcessor employeesProcessor =
        new EmployeesProcessorImpl(workPeriodCalculator, employeeOccupationParser);

    try (InputStream stream =
        Thread.currentThread().getContextClassLoader().getResourceAsStream(CFG_EMPLOYEES)) {
      Objects.requireNonNull(stream, String.format(MSG_MISSING_RESOURCE, CFG_EMPLOYEES));
      employeePairOccupationResults = employeesProcessor.getEmployeePairsResults(stream);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  @Test
  public void getEmployeePairOccupationResultTest() {

    assertEquals(employeePairOccupationResults.size(), 3);

    assertTrue(
        employeePairOccupationResults.contains(new EmployeePairOccupationResult(40, 10, 667, 2)));

    assertTrue(
        employeePairOccupationResults.contains(new EmployeePairOccupationResult(40, 10, 444, 6)));

    assertTrue(
        employeePairOccupationResults.contains(new EmployeePairOccupationResult(20, 70, 333, 8)));
  }
}
