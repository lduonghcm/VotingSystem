/**
 * Represents a voter's ballot in the election.
 * Ballot.java
 *
 * @author Thomas Haupert
 */
public class Ballot {
  private int currentDistribution;
  private int[] rankedVote;
  private boolean valid;


  /**
   * Constructor for ballot.
   *
   * @param rankedVote integer array representing the vote for each candidate
   */
  public Ballot(int[] rankedVote) {
    this.rankedVote = rankedVote;
    this.currentDistribution = 1;
    this.valid = true;
  }

  /**
   * Returns the number of candidates ranked by the ballot.
   *
   * @return the size of the rankedVote array.
   */
  public int getNumRankedCandidates() {
    int num = 0;
    for (int i : rankedVote) {
      if (i != 0) {
        num++;
      }
    }
    return num;
  }

  /**
   * Setter function to set the ballots status of its validity.
   *
   * @param validity A boolean representing the validity of the ballot.
   */
  public void setValid(boolean validity) {
    valid = validity;
  }

  /**
   * Return whether the ballot is valid.
   *
   * @return boolean representing the validity of the ballot
   */
  public boolean isValid() {
    return valid;
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
