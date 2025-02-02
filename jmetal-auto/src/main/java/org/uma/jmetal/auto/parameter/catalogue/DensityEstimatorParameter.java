package org.uma.jmetal.auto.parameter.catalogue;

import java.util.List;
import org.uma.jmetal.auto.parameter.CategoricalParameter;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.densityestimator.DensityEstimator;
import org.uma.jmetal.util.densityestimator.impl.CrowdingDistanceDensityEstimator;
import org.uma.jmetal.util.densityestimator.impl.KnnDensityEstimator;
import org.uma.jmetal.util.errorchecking.JMetalException;

public class DensityEstimatorParameter<S extends Solution<?>> extends CategoricalParameter {

  public DensityEstimatorParameter(String name, String[] args,
      List<String> validDensityEstimators) {
    super(name, args, validDensityEstimators);
  }

  public DensityEstimator<S> getParameter() {
    DensityEstimator<S> result;
    switch (getValue()) {
      case "crowdingDistance":
        result = new CrowdingDistanceDensityEstimator<>();
        break;
      case "knn":
        result = new KnnDensityEstimator<>(1);
        break;
      default:
        throw new JMetalException("Density estimator does not exist: " + getName());
    }
    return result;
  }
}
