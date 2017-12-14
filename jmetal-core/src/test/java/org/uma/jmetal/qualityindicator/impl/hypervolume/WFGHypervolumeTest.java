package org.uma.jmetal.qualityindicator.impl.hypervolume;

import org.junit.Before;
import org.junit.Test;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.qualityindicator.impl.Hypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.DefaultDoubleSolution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by ajnebro on 17/12/15.
 */
public class WFGHypervolumeTest {
  private Hypervolume<DoubleSolution> hypervolume ;

  @Before
  public void setup() {
    hypervolume = new WFGHypervolume<>() ;
  }

  @Test
  public void simpleTest() {
    DoubleProblem problem = new MockDoubleProblem(2) ;

    DoubleSolution solution = problem.createSolution() ;
    solution.setObjective(0, 0.0);
    solution.setObjective(1, 1.0);

    DoubleSolution solution2 = problem.createSolution() ;
    solution2.setObjective(0, -1.0);
    solution2.setObjective(1, 2.0);

    DoubleSolution solution3 = problem.createSolution() ;
    solution3.setObjective(0, -2.0);
    solution3.setObjective(1, 1.5);

    List<DoubleSolution> list = Arrays.asList(solution, solution2, solution3) ;

    double hv = hypervolume.evaluate(list) ;

    assertNotEquals(0, hv) ;
  }

  /**
   * CASE 1: solution set -> front obtained from the ZDT1.rf file. Reference front: [0,1], [1,0]
   * @throws FileNotFoundException
   */
  @Test
  public void shouldEvaluateWorkProperlyCase1() throws FileNotFoundException {
    Front referenceFront = new ArrayFront(2, 2) ;
    referenceFront.setPoint(0, new ArrayPoint(new double[]{1.0, 0.0}));
    referenceFront.setPoint(0, new ArrayPoint(new double[]{0.0, 1.0}));

    Front storeFront = new ArrayFront("/pareto_fronts/ZDT1.pf") ;

    DoubleProblem problem = new MockDoubleProblem(2) ;

    List<DoubleSolution> frontToEvaluate = new ArrayList<>() ;
    for (int i = 0 ; i < storeFront.getNumberOfPoints(); i++) {
      DoubleSolution solution = problem.createSolution() ;
      solution.setObjective(0, storeFront.getPoint(i).getDimensionValue(0));
      solution.setObjective(1, storeFront.getPoint(i).getDimensionValue(1));
      frontToEvaluate.add(solution) ;
    }

    WFGHypervolume<DoubleSolution> hypervolume = new WFGHypervolume<>() ;
    double result = hypervolume.computeHypervolume(frontToEvaluate, new ArrayPoint(new double[]{0.0, 1.0})) ;

    System.out.println("F: " + result) ;
    assertEquals(0.6661, result, 0.0001) ;
  }

  /**
   * Mock class representing a binary problem
   */
  @SuppressWarnings("serial")
  private class MockDoubleProblem extends AbstractDoubleProblem {
    /** Constructor */
    public MockDoubleProblem(Integer numberOfVariables) {
      setNumberOfVariables(numberOfVariables);
      setNumberOfObjectives(2);

      List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
      List<Double> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

      for (int i = 0; i < getNumberOfVariables(); i++) {
        lowerLimit.add(-4.0);
        upperLimit.add(4.0);
      }

      setLowerLimit(lowerLimit);
      setUpperLimit(upperLimit);
    }

    @Override
    public DoubleSolution createSolution() {
      return new DefaultDoubleSolution(this) ;
    }

    /** Evaluate() method */
    @Override
    public void evaluate(DoubleSolution solution) {
      solution.setObjective(0, 0.0);
      solution.setObjective(1, 1.0);
    }
  }
}