package org.uma.jmetal.experimental.componentbasedalgorithm.catalogue.pso.initializelocalbest.impl ;

import org.uma.jmetal.experimental.componentbasedalgorithm.catalogue.pso.initializelocalbest.LocalBestInitialization;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.errorchecking.Check;

import java.util.List;

public class DefaultLocalBestInitialization implements LocalBestInitialization {
  public DoubleSolution[] initialize(List<DoubleSolution> swarm) {
    Check.notNull(swarm);

    DoubleSolution[] localBest = new DoubleSolution[swarm.size()] ;
    for (int i = 0; i < swarm.size(); i++) {
      localBest[i] = (DoubleSolution) swarm.get(i).copy() ;
    }

    return localBest ;
  }
}
