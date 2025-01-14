package org.uma.jmetal.component.catalogue.pso.globalbestselection.impl;

import java.util.Comparator;
import java.util.List;
import org.uma.jmetal.component.catalogue.pso.globalbestselection.GlobalBestSelection;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class TournamentGlobalBestSelection implements GlobalBestSelection {

  private int tournamentSize;
  private Comparator<DoubleSolution> comparator;

  public TournamentGlobalBestSelection(int tournamentSize, Comparator<DoubleSolution> comparator) {
    this.tournamentSize = tournamentSize;
    this.comparator = comparator ;
  }

  @Override
  public DoubleSolution select(List<DoubleSolution> globalBestList) {
    /*
    NaryTournamentSelection<DoubleSolution> tournament = new NaryTournamentSelection<>(
        tournamentSize, comparator);

    DoubleSolution result ;
    if (globalBestList.size() < tournamentSize) {
      result = globalBestList.get(0) ;
    } else {
      result = tournament.execute(globalBestList);
    }


    return result ;
*/

    DoubleSolution one, two;
    DoubleSolution bestGlobal;
    int pos1 = JMetalRandom.getInstance().nextInt(0, globalBestList.size() - 1);
    int pos2 = JMetalRandom.getInstance().nextInt(0, globalBestList.size() - 1);
    one = globalBestList.get(pos1);
    two = globalBestList.get(pos2);

    if (comparator.compare(one, two) < 1) {
      bestGlobal = (DoubleSolution) one.copy();
    } else {
      bestGlobal = (DoubleSolution) two.copy();
    }

    return bestGlobal;

  }
}
