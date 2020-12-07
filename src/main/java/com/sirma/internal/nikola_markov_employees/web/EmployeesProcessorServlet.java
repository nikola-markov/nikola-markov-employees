package com.sirma.internal.nikola_markov_employees.web;

import com.sirma.internal.nikola_markov_employees.data.EmployeePairOccupationResult;
import com.sirma.internal.nikola_markov_employees.query.EmployeesProcessor;
import com.sirma.internal.nikola_markov_employees.query.EmployeesProcessorImpl;
import com.sirma.internal.nikola_markov_employees.util.EmployeeOccupationParser;
import com.sirma.internal.nikola_markov_employees.util.EmployeeOccupationParserImpl;
import com.sirma.internal.nikola_markov_employees.util.WorkPeriodCalculator;
import com.sirma.internal.nikola_markov_employees.util.WorkPeriodCalculatorImpl;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class EmployeesProcessorServlet extends HttpServlet {

  private static final String RESULT_COLUMNS =
      "<tr> <th>Employee ID #1</th> <th>Employee ID #2</th> <th>Project ID</th> <th>Days worked</th> </tr>";
  private static final String RESULT_LINE =
      "<tr> <td> %s </td> <td> %s </td> <td> %s </td> <td> %s </td> </tr>";

  private EmployeesProcessor employeesProcessor;

  @Override
  protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
      throws IOException {

    resp.setContentType("text/html;charset=UTF-8");
    try (final PrintWriter out = resp.getWriter()) {
      out.println(
          "<form id='uploadbanner' enctype='multipart/form-data' method='post' action='/nikola-markov-employees'>");
      out.println("<input id='fileupload' name='myfile' type='file' />");
      out.println("<input type='submit' value='submit' id='submit' />");
      out.println("</form>");
    }
  }

  @Override
  protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
      throws IOException, ServletException {

    final List<EmployeePairOccupationResult> employeePairOccupationResults;
    final Part filePart = req.getPart("myfile");
    try (final InputStream inputStream = filePart.getInputStream()) {
      employeePairOccupationResults = employeesProcessor.getEmployeePairsResults(inputStream);
    }

    resp.setContentType("text/html;charset=UTF-8");
    try (final PrintWriter out = resp.getWriter()) {
      out.println("<table cellspacing='3'>");
      out.println(RESULT_COLUMNS);

      for (EmployeePairOccupationResult result : employeePairOccupationResults) {
        out.println(
            String.format(
                RESULT_LINE,
                result.getEmployeeId(),
                result.getEmployeeIdOther(),
                result.getProjectId(),
                result.getDaysWorked()));
      }
      out.println("</table>");
    }
  }

  @Override
  public void init() {
    final WorkPeriodCalculator workPeriodCalculator = new WorkPeriodCalculatorImpl();
    final EmployeeOccupationParser employeeOccupationParser = new EmployeeOccupationParserImpl();
    employeesProcessor = new EmployeesProcessorImpl(workPeriodCalculator, employeeOccupationParser);
  }
}
