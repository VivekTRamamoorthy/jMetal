package org.uma.jmetal.algorithm.examples.multiobjective.wasfga;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.uma.jmetal.algorithm.examples.AlgorithmRunner;
import org.uma.jmetal.algorithm.multiobjective.wasfga.WASFGAMeasures;
import org.uma.jmetal.operator.crossover.impl.SBXCrossover;
import org.uma.jmetal.operator.mutation.impl.PolynomialMutation;
import org.uma.jmetal.operator.selection.impl.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.ProblemFactory;
import org.uma.jmetal.qualityindicator.QualityIndicatorUtils;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.VectorUtils;
import org.uma.jmetal.util.chartcontainer.ChartContainer;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.measure.MeasureListener;
import org.uma.jmetal.util.measure.MeasureManager;
import org.uma.jmetal.util.measure.impl.BasicMeasure;
import org.uma.jmetal.util.measure.impl.CountingMeasure;

public class WASFGAMeasuresRunner3D extends AbstractAlgorithmRunner {

  /**
   * @param args Command line arguments.
   */
  public static void main(String[] args) throws IOException {

    String problemName = "org.uma.jmetal.problem.multiobjective.dtlz.DTLZ2";
    String referenceParetoFront = "resources/referenceFrontsCSV/DTLZ2.3D.csv";

    Problem<DoubleSolution> problem = ProblemFactory.<DoubleSolution>loadProblem(problemName);

    List<Double> referencePoint = new ArrayList<>();
    referencePoint.add(0.0);
    referencePoint.add(0.0);
    referencePoint.add(0.0);

    double crossoverProbability = 0.9;
    double crossoverDistributionIndex = 20.0;
    var crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

    double mutationProbability = 1.0 / problem.numberOfVariables();
    double mutationDistributionIndex = 20.0;
    var mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

    var selection = new BinaryTournamentSelection<>(
        new RankingAndCrowdingDistanceComparator<DoubleSolution>());

    double epsilon = 0.01;
    var algorithm = new WASFGAMeasures<>(
        problem,
        100,
        250,
        crossover,
        mutation,
        selection,
        new SequentialSolutionListEvaluator<DoubleSolution>(),
        epsilon,
        referencePoint,
        "resources/weightVectorFiles/moead/W3D_100.dat");


    /* Measure management */
    MeasureManager measureManager = algorithm.getMeasureManager();

    BasicMeasure<List<DoubleSolution>> solutionListMeasure = (BasicMeasure<List<DoubleSolution>>) measureManager
        .<List<DoubleSolution>>getPushMeasure("currentPopulation");
    CountingMeasure iterationMeasure = (CountingMeasure) measureManager.<Long>getPushMeasure(
        "currentEvaluation");

    ChartContainer chart = new ChartContainer(algorithm.getName(), 200);
    chart.setFrontChart(0, 1, referenceParetoFront);
    chart.setReferencePoint(referencePoint);
    chart.setVarChart(0, 1);
    chart.initChart();

    solutionListMeasure.register(new ChartListener(chart));
    iterationMeasure.register(new IterationListener(chart));

    /* End of measure management */

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
        .execute();

    chart.saveChart("WASFGA", BitmapFormat.PNG);
    List<DoubleSolution> population = algorithm.getResult();
    long computingTime = algorithmRunner.getComputingTime();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);
    QualityIndicatorUtils.printQualityIndicators(
        SolutionListUtils.getMatrixWithObjectiveValues(population),
        VectorUtils.readVectors(referenceParetoFront, ","));
  }

  private static class ChartListener implements MeasureListener<List<DoubleSolution>> {

    private ChartContainer chart;
    private int iteration = 0;

    public ChartListener(ChartContainer chart) {
      this.chart = chart;
      this.chart.getFrontChart().setTitle("Iteration: " + this.iteration);
    }

    private void refreshChart(List<DoubleSolution> solutionList) {
      if (this.chart != null) {
        iteration++;
        this.chart.getFrontChart().setTitle("Iteration: " + this.iteration);
        this.chart.updateFrontCharts(solutionList);
        this.chart.refreshCharts();
      }
    }

    @Override
    synchronized public void measureGenerated(List<DoubleSolution> solutions) {
      refreshChart(solutions);
    }
  }

  private static class IterationListener implements MeasureListener<Long> {

    ChartContainer chart;

    public IterationListener(ChartContainer chart) {
      this.chart = chart;
      this.chart.getFrontChart().setTitle("Iteration: " + 0);
    }

    @Override
    synchronized public void measureGenerated(Long iteration) {
      if (this.chart != null) {
        this.chart.getFrontChart().setTitle("Iteration: " + iteration);
      }
    }
  }
}
