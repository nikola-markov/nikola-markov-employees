package com.sirma.internal.nikola_markov_employees.query;

import com.sirma.internal.nikola_markov_employees.data.EmployeeOccupation;
import com.sirma.internal.nikola_markov_employees.data.EmployeePairOccupationResult;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface EmployeesProcessor {

  List<EmployeePairOccupationResult> getEmployeePairsResults(
      final Stream<EmployeeOccupation> employeeOccupations);

  List<EmployeePairOccupationResult> getEmployeePairsResults(final Path path) throws IOException;

  List<EmployeePairOccupationResult> getEmployeePairsResults(final InputStream inputStream)
      throws IOException;
}
