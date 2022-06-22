package org.uma.jmetal.experimental.auto.parameter.catalogue;

import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.experimental.auto.parameter.CategoricalParameter;
import org.uma.jmetal.experimental.componentbasedalgorithm.catalogue.pso.globalbestselection.GlobalBestSelection;
import org.uma.jmetal.experimental.componentbasedalgorithm.catalogue.pso.globalbestselection.impl.BinaryTournamentGlobalBestSelection;
import org.uma.jmetal.experimental.componentbasedalgorithm.catalogue.pso.globalbestselection.impl.RandomGlobalBestSelection;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;

public class GlobalBestSelectionParameter extends CategoricalParameter {
  public GlobalBestSelectionParameter(String args[], List<String> selectionStrategies) {
    super("globalBestSelection", args, selectionStrategies) ;
  }

  public GlobalBestSelection getParameter(Comparator<DoubleSolution> comparator) {
    GlobalBestSelection result ;
    switch(getValue()) {
      case "binaryTournament":
        result = new BinaryTournamentGlobalBestSelection(comparator) ;
        break ;
      case "random":
        result = new RandomGlobalBestSelection();
        break ;
      default:
        throw new RuntimeException("Global Best Selection component unknown: " + getValue()) ;
    }

    return result ;
  }
}