package org.uma.jmetal.auto.parameter.catalogue;

import java.util.List;
import org.uma.jmetal.auto.parameter.CategoricalParameter;
import org.uma.jmetal.util.aggregationfunction.AggregationFunction;
import org.uma.jmetal.util.aggregationfunction.impl.PenaltyBoundaryIntersection;
import org.uma.jmetal.util.aggregationfunction.impl.Tschebyscheff;
import org.uma.jmetal.util.aggregationfunction.impl.WeightedSum;
import org.uma.jmetal.util.errorchecking.JMetalException;

public class AggregationFunctionParameter extends CategoricalParameter {
  public AggregationFunctionParameter(List<String> aggregativeFunctions,  String[] args) {
    super("aggregationFunction", args, aggregativeFunctions);
  }

  public AggregationFunction getParameter() {
    AggregationFunction result;

    switch (getValue()) {
      case "tschebyscheff":
        result =  new Tschebyscheff() ;
        break;
      case "weightedSum":
        result = new WeightedSum() ;
        break;
      case "penaltyBoundaryIntersection":
        double theta = (double) findSpecificParameter("pbiTheta").getValue();
        result = new PenaltyBoundaryIntersection(theta) ;
        break;
      default:
        throw new JMetalException("Aggregation function does not exist: " + getName());
    }
    return result;
  }
}
