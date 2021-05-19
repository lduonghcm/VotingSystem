import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * An abstract class for the different election types.
 * Election.java
 *
 * @author Thomas Haupert
 */
public abstract class Election {
  private int totalVotes;

  /**
   * This function randomly decides the winner out of the candidates who has
   * same amounts of votes.
   *
   * @param candidates A list of candidates whose have same votes
   * @return A candidate chosen randomly from the list of candidates
   * @custom.pre Candidates in the given list has the same votes
   * @custom.post The chosen candidate is removed from the list of candidates with same votes.
   */
  public Candidate coinToss(ArrayList<Candidate> candidates) {
    appendAuditFile("Starting coin toss between the following candidates:");
    for (Candidate candidate : candidates) {
      appendAuditFile("  " + candidate.getName());
    }
    int numCandidates = candidates.size();

    Random rand = new Random();
    int upperbound = 1000;
    int intRandom = rand.nextInt(upperbound);
    int winnerIndex = intRandom % numCandidates;

    Candidate winner = candidates.get(winnerIndex);
    candidates.set(winnerIndex, null);
    appendAuditFile(
        String.format("Randomly selected candidate \"%s\" in coin toss.", winner.getName()));

    return winner;
  }

  /**
   * This function retrieves the total number of votes in the election.
   *
   * @return The number of total votes of the current election.
   * @custom.pre The file has been processed
   * @custom.post None
   */
  public int getTotalVotes() {
    return totalVotes;
  }

  public void setTotalVotes(int totalVotes) {
    this.totalVotes = totalVotes;
  }

  /**
   * This function runs the algorithm which counts the votes and decides
   * the winner(s).
   *
   * @return a list of Candidate objects
   * @custom.pre The file has been processed.
   * @custom.post The list of candidates with voting information
   */
  public abstract ArrayList<Candidate> runElection();

  /**
   * Appends the string to the media report file.
   *
   * @param append string to append to the media file
   * @custom.pre The algorithm has finished calculating the votes and decided the winner(s)
   * @custom.post A string used to print out information onto the screen has been appended
   *     with extra information about winner(s).
   */
  public void appendFinalResult(String append) {
    try {
      ElectionManager.mediaFileWriter.write(append);
      ElectionManager.mediaFileWriter.newLine();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NullPointerException npe) {
      // Catches null pointer exceptions that may be thrown in test cases
    }
  }

  /**
   * Appends the string to the audit file.
   *
   * @param append string to append to the audit file
   * @custom.pre The algorithm has finished calculating the votes and decided the winner(s)
   * @custom.post A string used for storing in the audit file has been appended with extra
   *     information that replicate the election.
   */
  public void appendAuditFile(String append) {
    try {
      ElectionManager.auditFileWriter.write(append);
      ElectionManager.auditFileWriter.newLine();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NullPointerException npe) {
      // Catches null pointer exceptions that may be thrown in test cases
    }
  }

  public abstract void prepareData() throws IOException;
}
