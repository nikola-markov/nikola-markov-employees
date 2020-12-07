package com.sirma.internal.nikola_markov_employees.util;

public enum DateFormatPattern {

  PATTERN_1("yyyy-MM-dd"),
  PATTERN_2("MM-dd-yyyy");

  private final String pattern;

  DateFormatPattern(final String pattern) {
    this.pattern = pattern;
  }

  public String getPattern() {
    return pattern;
  }
}
