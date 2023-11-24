package edu.project3.log;

import edu.project3.ConsoleHandler;
import edu.project3.export.AsciiDocExporter;
import edu.project3.export.Exporter;
import edu.project3.export.MarkdownExporter;

public class LogAnalysisExecutor {

    private LogAnalysisExecutor() {
    }

    public static void run(ConsoleHandler.ConsoleCommand command) {
        LogAnalyzer logAnalyzer = new LogAnalyzer(command);
        LogStatistics logStatistics = new LogStatistics(logAnalyzer);

        export(logStatistics, command.format(),
            "src/main/java/edu/project3/resources/file.adoc"
        );
    }

    private static void export(
        LogStatistics logStatistics,
        ConsoleHandler.ConsoleCommand.ResultFileFormat resultFileFormat, String path
    ) {
        Exporter exporter;
        if (resultFileFormat == ConsoleHandler.ConsoleCommand.ResultFileFormat.ADoc) {
            exporter = new AsciiDocExporter();
        } else {
            exporter = new MarkdownExporter();
        }
        String generalRes = exporter.convertGeneralInfo(logStatistics.getGeneralMetrics());
        String resourcesRequestedRes =
            exporter.convertResourcesRequested(logStatistics.getResourcesRequested());
        String responseCodeMetricsRes =
            exporter.convertResponseCodeMetrics(logStatistics.getResponseCodeMetrics());
        exporter.writeToFile(generalRes + resourcesRequestedRes + responseCodeMetricsRes, path);
    }
}
