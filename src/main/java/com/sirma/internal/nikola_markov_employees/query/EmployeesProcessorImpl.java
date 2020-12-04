package com.sirma.internal.nikola_markov_employees.query;

import static com.sirma.internal.nikola_markov_employees.util.ValidationUtil.checkNullArg;

import com.sirma.internal.nikola_markov_employees.data.EmployeeOccupation;
import com.sirma.internal.nikola_markov_employees.data.EmployeePair;
import com.sirma.internal.nikola_markov_employees.data.EmployeePairOccupation;
import com.sirma.internal.nikola_markov_employees.data.EmployeePairOccupationResult;
import com.sirma.internal.nikola_markov_employees.util.EmployeeOccupationParser;
import com.sirma.internal.nikola_markov_employees.util.WorkPeriodCalculator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class EmployeesProcessorImpl implements EmployeesProcessor {

  private final WorkPeriodCalculator workPeriodCalculator;
  private final EmployeeOccupationParser employeeOccupationParser;

  public EmployeesProcessorImpl(
      final WorkPeriodCalculator workPeriodCalculator,
      final EmployeeOccupationParser employeeOccupationParser) {
    validateConstructorArgs(workPeriodCalculator, employeeOccupationParser);
    this.workPeriodCalculator = workPeriodCalculator;
    this.employeeOccupationParser = employeeOccupationParser;
  }

  Map<Integer, List<EmployeeOccupation>> getEmployeeOccupationsByProject(
      final Stream<EmployeeOccupation> employeeOccupations) {
    return employeeOccupations.collect(Collectors.groupingBy(EmployeeOccupation::getProjectId));
  }

  Set<EmployeePairOccupation> getEmployeePairOccupationsForProject(
      final List<EmployeeOccupation> employeeOccupations) {

    final Set<EmployeePairOccupation> employeePairOccupations = new HashSet<>();
    for (int i = 0; i < employeeOccupations.size(); i++) {
      for (int j = 0; j < employeeOccupations.size(); j++) {
        if (i == j) {
          continue;
        }
        final EmployeeOccupation employeeOccupation = employeeOccupations.get(i);
        final EmployeeOccupation employeeOccupationOther = employeeOccupations.get(j);
        if (employeeOccupation.getEmployeeId() == employeeOccupationOther.getEmployeeId()
            || employeeOccupation.getProjectId() != employeeOccupationOther.getProjectId()) {
          continue;
        }
        final long jointWorkDays =
            workPeriodCalculator.calculateJointWorkDays(
                employeeOccupation, employeeOccupationOther);
        if (jointWorkDays != 0) {
          final EmployeePair employeePair =
              new EmployeePair(
                  employeeOccupation.getEmployeeId(), employeeOccupationOther.getEmployeeId());

          final EmployeePairOccupation employeePairOccupation =
              new EmployeePairOccupation(
                  employeePair, employeeOccupation.getProjectId(), jointWorkDays);

          employeePairOccupations.add(employeePairOccupation);
        }
      }
    }
    return employeePairOccupations;
  }

  Map<EmployeePair, List<EmployeePairOccupation>> getEmployeePairOccupationsByEmployeePair(
      final List<EmployeePairOccupation> employeePairOccupations) {
    return employeePairOccupations.stream()
        .collect(Collectors.groupingBy(EmployeePairOccupation::getEmployeePair));
  }

  Map<EmployeePair, Long> sumJointWorkDaysByEmployeePair(
      Map<EmployeePair, List<EmployeePairOccupation>> employeePairOccupations) {
    return employeePairOccupations.entrySet().stream()
        .map(
            mapEntry ->
                Map.entry(
                    mapEntry.getKey(),
                    mapEntry.getValue().stream()
                        .map(EmployeePairOccupation::getDaysCount)
                        .mapToLong(Long::longValue)
                        .sum()))
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }

  Optional<Long> getMaxJointWorkDaysSum(Map<EmployeePair, Long> jointWorkDaysSums) {
    return jointWorkDaysSums.values().stream().max(Long::compareTo);
  }

  List<EmployeePair> getEmployeePairsWithMaxJointWorkingDays(
      Map<EmployeePair, Long> jointWorkDaysSums) {
    List<EmployeePair> employeePairs;
    final Optional<Long> maxJointWorkDaysSum = getMaxJointWorkDaysSum(jointWorkDaysSums);
    employeePairs =
        maxJointWorkDaysSum
            .map(
                longNumber ->
                    jointWorkDaysSums.entrySet().stream()
                        .filter(mapEntry -> mapEntry.getValue().equals(longNumber))
                        .map(Entry::getKey)
                        .collect(Collectors.toList()))
            .orElseGet(ArrayList::new);
    return employeePairs;
  }

  public List<EmployeePairOccupation> getEmployeePairOccupations(
      final Stream<EmployeeOccupation> employeeOccupations) {

    final Map<Integer, List<EmployeeOccupation>> employeeOccupationsMap =
        getEmployeeOccupationsByProject(employeeOccupations);

    return employeeOccupationsMap.values().stream()
        .map(this::getEmployeePairOccupationsForProject)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  @Override
  public List<EmployeePairOccupationResult> getEmployeePairsResults(
      final Stream<EmployeeOccupation> employeeOccupations) {
    checkNullArg(employeeOccupations, Stream.class.getSimpleName());

    final List<EmployeePairOccupation> employeePairOccupations =
        getEmployeePairOccupations(employeeOccupations);

    final Map<EmployeePair, List<EmployeePairOccupation>> employeePairOccupationsMap =
        getEmployeePairOccupationsByEmployeePair(employeePairOccupations);

    final Map<EmployeePair, Long> jointWorkDaysSumsMap =
        sumJointWorkDaysByEmployeePair(employeePairOccupationsMap);

    final List<EmployeePair> employeePairs =
        getEmployeePairsWithMaxJointWorkingDays(jointWorkDaysSumsMap);

    final List<EmployeePairOccupationResult> employeePairsResults = new ArrayList<>();
    for (EmployeePair employeePair : employeePairs) {
      final Integer[] ids = employeePair.getEmployeePair().toArray(new Integer[2]);
      final List<EmployeePairOccupation> occupations = employeePairOccupationsMap.get(employeePair);
      for (EmployeePairOccupation occupation : occupations) {
        employeePairsResults.add(
            new EmployeePairOccupationResult(
                ids[0], ids[1], occupation.getProjectId(), occupation.getDaysCount()));
      }
    }
    return employeePairsResults;
  }

  @Override
  public List<EmployeePairOccupationResult> getEmployeePairsResults(final Path path)
      throws IOException {
    checkNullArg(path, Path.class.getSimpleName());

    final List<EmployeePairOccupationResult> employeePairOccupationResults;
    try (Stream<String> lines = Files.lines(path)) {
      employeePairOccupationResults =
          getEmployeePairsResults(
              lines
                  .filter(line -> !line.isBlank())
                  .map(employeeOccupationParser::parseEmployeeOccupation));
    }
    return employeePairOccupationResults;
  }

  @Override
  public List<EmployeePairOccupationResult> getEmployeePairsResults(final InputStream inputStream)
      throws IOException {
    checkNullArg(inputStream, InputStream.class.getSimpleName());

    final List<String> lines = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      while (reader.ready()) {
        lines.add(reader.readLine());
      }
    }
    return getEmployeePairsResults(
        lines.stream()
            .filter(line -> !line.isBlank())
            .map(employeeOccupationParser::parseEmployeeOccupation));
  }

  private void validateConstructorArgs(
      final WorkPeriodCalculator workPeriodCalculator,
      final EmployeeOccupationParser employeeOccupationParser) {
    checkNullArg(workPeriodCalculator, WorkPeriodCalculator.class.getSimpleName());
    checkNullArg(employeeOccupationParser, EmployeeOccupationParser.class.getSimpleName());
  }
}
