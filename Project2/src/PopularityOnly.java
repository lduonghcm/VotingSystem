import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents the popularity only election type.
 * PopularityOnly.java
 *
 * @author Thomas Haupert
 */
public class PopularityOnly extends Election {
  private BufferedReader file;
  private ArrayList<Candidate> candidateList;

  /**
   * Constructor for popularity only election.
   *
   * @param file reader for the election file
   */
  public PopularityOnly(BufferedReader file) {
    this.file = file;
    this.candidateList = new ArrayList<>();
  }

  /**
   * Prepares the data from the file for processing.
   *
   * @throws IOException if file is not formatted properly
   */
  public void prepareData() throws IOException {
    String line;
    // read line 2: number of candidates
    file.readLine();

    // read line 3: candidates
    line = file.readLine();
    String[] candidates = line.split(",");
    for (int i = 0; i < candidates.length; i += 2) {
      candidates[i] = candidates[i].replace("[", "");
      candidates[i + 1] = candidates[i + 1].replace("]", "");
      candidateList.add(new Candidate(candidates[i], new Party(candidates[i + 1])));
    }

    // read line 4: number of ballots
    line = file.readLine();
    int numBallots = Integer.parseInt(line);
    setTotalVotes(numBallots);
  }

  /**
   * This function runs the algorithm which counts the votes and decides
   * the winner(s).
   *
   * @return a list of Candidate objects
   * @custom.pre The file has been processed.
   * @custom.post The list of candidates with voting information
   */
  public ArrayList<Candidate> runElection() {
    return new ArrayList<>();
  }

  /**
   * Gets the number of invalid ballots in an election.
   *
   * @return the integer 0, since Popularity Only does not have any invalid ballots
   */
  public int getNumInvalidBallots() {
    return 0;
  }

  /**
   * Gets the list of candidates in the election.
   *
   * @return list of candidates in the election
   */
  public ArrayList<Candidate> getCandidateList() {
    return candidateList;
  }
}
