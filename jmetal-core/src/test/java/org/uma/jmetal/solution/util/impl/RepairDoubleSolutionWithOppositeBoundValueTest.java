package org.uma.jmetal.solution.util.impl;

import org.junit.Before;
import org.junit.Test;
import org.uma.jmetal.solution.util.RepairDoubleSolution;
import org.uma.jmetal.util.JMetalException;

import static org.junit.Assert.assertEquals;

public class RepairDoubleSolutionWithOppositeBoundValueTest {
  private static final double EPSILON = 0.0000000000001 ;
  private RepairDoubleSolution repair ;

  @Before
  public void setup() {
    repair = new RepairDoubleSolutionWithOppositeBoundValue() ;
  }

  @Test(expected = JMetalException.class)
  public void shouldRepairRaiseAnExceptionIfTheBoundsAreIncorrect() {
    repair.repairSolutionVariableValue(0.0, 1.0, -1.0) ;
  }

  @Test
  public void shouldRRepairAssignTheLowerBoundIfValueIsHigherThanTheUpperBound() {
    assertEquals(-1.0, repair.repairSolutionVariableValue(3.0, -1.0, 1.0), EPSILON) ;
  }

  @Test
  public void shouldRRepairAssignTheUpperBoundIfValueIsLowerThanTheLowerBound() {
    assertEquals(1.0, repair.repairSolutionVariableValue(-4, -1.0, 1.0), EPSILON) ;
  }
}