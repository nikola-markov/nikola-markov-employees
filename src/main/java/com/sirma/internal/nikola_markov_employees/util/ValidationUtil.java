package com.sirma.internal.nikola_markov_employees.util;

import java.util.Objects;

public final class ValidationUtil {

  private static final String NULL_ARG = "%s argument is null";
  private static final String NEGATIVE_ARG = "%s argument is negative";
  private static final String ZERO_ARG = "%s argument is zero";

  private ValidationUtil() {}

  public static void checkNullArg(final Object obj, String argName) {
    if (Objects.isNull(obj)) {
      String errorMsg = String.format(NULL_ARG, argName);
      throw new IllegalArgumentException(errorMsg);
    }
  }

  public static void checkPositive(final long arg, final String name) {
    if (arg == 0) {
      throw new IllegalArgumentException(String.format(ZERO_ARG, name));
    } else if (arg < 0) {
      throw new IllegalArgumentException(String.format(NEGATIVE_ARG, name));
    }
  }
}
