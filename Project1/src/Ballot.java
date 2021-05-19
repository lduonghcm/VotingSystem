/**
 * Represents a voter's ballot in the election.
 * Ballot.java
 *
 * @author Thomas Haupert
 */
public class Ballot {
  private int currentDistribution;
  private int[] rankedVote;

  //CONSTRUCTORS
  public Ballot(int[] rankedVote) {
    this.rankedVote = rankedVote;
    this.currentDistribution = 1;
  }

  /**
   * Returns the current distribution of the ballot.
   *
   * @return the current distribution
   */
  public int getCurrentDistribution() {
    return currentDistribution;
  }

  /**
   * Increments the current vote distribution.
   */
  public void incrementCurrentDistribution() {
    currentDistribution += 1;
  }

  /**
   * Returns an integer representing the candidate that will receive the ballot.
   *
   * @return the index of the candidate to whom the ballot will be given
   */
  public int findDistributionVote() {
    for (int i = 0; i < rankedVote.length; i++) {
      if (rankedVote[i] == getCurrentDistribution()) {
        return i;
      }
    }
    return -1;
  }
}
