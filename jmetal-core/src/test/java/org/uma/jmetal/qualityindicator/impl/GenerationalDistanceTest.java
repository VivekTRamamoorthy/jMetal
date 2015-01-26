//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.uma.jmetal.qualityindicator.impl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;

/**
 * @author Antonio J. Nebro
 * @version 1.0
 */
public class GenerationalDistanceTest {
  private static final double EPSILON = 0.0000000000001 ;

  @Rule
  public ExpectedException exception = ExpectedException.none();

  private GenerationalDistance generationalDistance ;

  @Before public void setup() {
    generationalDistance = new GenerationalDistance() ;
  }

  @Test
  public void shouldExecuteRaiseAnExceptionIfTheFrontApproximationIsNull() {
    exception.expect(JMetalException.class);
    exception.expectMessage(containsString("The pareto front approximation object is null"));

    Front front = new ArrayFront(0, 0) ;
    generationalDistance.execute(null, front) ;
  }

  @Test
  public void shouldExecuteRaiseAnExceptionIfTheParetoFrontIsNull() {
    exception.expect(JMetalException.class);
    exception.expectMessage(containsString("The pareto front object is null"));

    Front front = new ArrayFront(0, 0) ;

    generationalDistance.execute(front, null) ;
  }

  @Test
  public void shouldExecuteRaiseAnExceptionIfTheFrontApproximationListIsNull() {
    exception.expect(JMetalException.class);
    exception.expectMessage(containsString("The pareto front approximation list is null"));

    List<DoubleSolution> list = new ArrayList<>();
    generationalDistance.execute(null, list) ;
  }

  @Test
  public void shouldExecuteRaiseAnExceptionIfTheParetoFrontListIsNull() {
    exception.expect(JMetalException.class);
    exception.expectMessage(containsString("The pareto front list is null"));

    List<DoubleSolution> list = new ArrayList<>();
    generationalDistance.execute(list, null) ;
  }

  @Test
  public void shouldExecuteRaiseAndExceptionIfTheFrontsContainOnePointWhichIsTheSame() {
    exception.expect(JMetalException.class);
    exception.expectMessage(containsString("Maximum and minimum values of index 0 are the same: 10"));

    int numberOfPoints = 1 ;
    int numberOfDimensions = 2 ;
    Front frontApproximation = new ArrayFront(numberOfPoints, numberOfDimensions);
    Front paretoFront = new ArrayFront(numberOfPoints, numberOfDimensions);

    Point point1 = new ArrayPoint(numberOfDimensions) ;
    point1.setDimensionValue(0, 10.0);
    point1.setDimensionValue(1, 12.0);

    frontApproximation.setPoint(0, point1);
    paretoFront.setPoint(0, point1);

    assertEquals(0.0, generationalDistance.execute(frontApproximation, paretoFront), EPSILON);
  }

  @Test
  public void shouldGetNameReturnTheCorrectValue() {
    assertEquals("GD", generationalDistance.getName());
  }

}