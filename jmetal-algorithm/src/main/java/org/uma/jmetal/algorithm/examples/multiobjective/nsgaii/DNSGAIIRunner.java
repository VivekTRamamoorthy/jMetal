package org.uma.jmetal.algorithm.examples.multiobjective.nsgaii;

import java.io.IOException;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.examples.AlgorithmRunner;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.DNSGAII;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.crossover.impl.SBXCrossover;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.operator.mutation.impl.PolynomialMutation;
import org.uma.jmetal.operator.selection.SelectionOperator;
import org.uma.jmetal.operator.selection.impl.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.ProblemFactory;
import org.uma.jmetal.qualityindicator.QualityIndicatorUtils;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.VectorUtils;
import org.uma.jmetal.util.comparator.RankingAndDirScoreDistanceComparator;
import org.uma.jmetal.util.fileinput.VectorFileUtils;

/**
 * created at 2:50 pm, 2019/1/29
 * runner of DIR-Enhanced NSGA-II
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
public class DNSGAIIRunner extends AbstractAlgorithmRunner {

  public static void main(String[] args) throws IOException {

    String referenceParetoFront = "resources/referenceFrontsCSV/DTLZ1.3D.csv";

    String problemName = "org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1";
    Problem<DoubleSolution> problem = ProblemFactory.loadProblem(problemName);

    double crossoverProbability = 0.9;
    double crossoverDistributionIndex = 30;
    CrossoverOperator<DoubleSolution> crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

    double mutationProbability = 1.0 / problem.numberOfVariables();
    double mutationDistributionIndex = 20.0;
    MutationOperator<DoubleSolution> mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection = new BinaryTournamentSelection<>(
            new RankingAndDirScoreDistanceComparator<>());

    int populationSize = 300;
    Algorithm<List<DoubleSolution>> algorithm = new NSGAIIBuilder<>(problem, crossover, mutation, populationSize)
            .setMaxEvaluations(300000)
            .setVariant(NSGAIIBuilder.NSGAIIVariant.DNSGAII)
            .setSelectionOperator(selection).build();

    double[][] referenceVectors = VectorFileUtils.readVectors("resources/weightVectorFiles/moead/W" + problem.numberOfObjectives() + "D_" + populationSize + ".dat");
    ((DNSGAII<DoubleSolution>) algorithm).setReferenceVectors(referenceVectors);

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
    List<DoubleSolution> population = algorithm.getResult();
    long computingTime = algorithmRunner.getComputingTime();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);

    QualityIndicatorUtils.printQualityIndicators(
        SolutionListUtils.getMatrixWithObjectiveValues(population),
        VectorUtils.readVectors(referenceParetoFront, ","));
  }
}
