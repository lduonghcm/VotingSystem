import java.util.ArrayList;

/**
 * Represents a party of candidates in an election.
 * Party.java
 *
 * @author Thomas Haupert
 */
public class Party {
  private String name;
  private ArrayList<Candidate> candidateList;
  private int numVotes;
  private int numSeats;

  // CONSTRUCTORS

  /**
   * Constructor for Party.
   */
  public Party() {
    this.name = "";
    this.candidateList = null;
    this.numVotes = 0;
    this.numSeats = 0;
  }

  /**
   * Constructor for Party.
   *
   * @param name name of party
   */
  public Party(String name) {
    this.name = name;
    this.candidateList = null;
    this.numVotes = 0;
    this.numSeats = 0;
  }

  /**
   * Constructor for Party.
   *
   * @param name          name of party
   * @param candidateList list of candidates in party
   */
  public Party(String name, ArrayList<Candidate> candidateList) {
    this.name = name;
    this.candidateList = candidateList;
    this.numVotes = 0;
    for (Candidate candidate : candidateList) {
      this.numVotes += candidate.getNumVotes();
    }
    this.numSeats = 0;
  }

  /**
   * Set the list of candidates for the party.
   *
   * @param candidateList list of candidates
   */
  public void addCandidateList(ArrayList<Candidate> candidateList) {
    this.candidateList = candidateList;
  }

  /**
   * Get the list of candidates for the party.
   *
   * @return list of candidates
   */
  public ArrayList<Candidate> getCandidateList() {
    return candidateList;
  }

  /**
   * Get name of party.
   *
   * @return name of party
   */
  public String getName() {
    return name;
  }

  /**
   * This function get the top number of candidates with the highest votes.
   *
   * @param x number of candidates need to be chosen from the list.
   * @return list of the candidates with the most votes
   */
  public ArrayList<Candidate> getTopXCandidate(int x) {
    if (x > candidateList.size()) {
      return candidateList;
    }
    ArrayList<Candidate> topCandidates = new ArrayList<Candidate>();
    ArrayList<Candidate> winningList = candidateList;
    while (x > 0) {
      int maxVotes = -1;
      Candidate maxCandidate = new Candidate();
      for (Candidate candidate : winningList) {
        if (candidate.getNumVotes() > maxVotes) {
          maxVotes = candidate.getNumVotes();
          maxCandidate = candidate;
        }
      }
      topCandidates.add(maxCandidate);
      winningList.remove(maxCandidate);
      x--;
    }
    return topCandidates;
  }

  /**
   * Get votes for all candidates of the party.
   *
   * @return number of votes for all candidates in the party
   */
  public int getPartyVotes() {
    return numVotes;
  }

  /**
   * Set votes for all candidates of the party.
   *
   * @param votes number of votes for all candidates in the party
   */
  public void setPartyVotes(int votes) {
    numVotes = votes;
  }

  /**
   * Get number of seats for the party.
   *
   * @return number of seats in the party
   */
  public int getPartySeats() {
    return numSeats;
  }

  /**
   * Set number of seats for the party.
   *
   * @param seats number of seats in the party
   */
  public void setPartySeats(int seats) {
    numSeats = seats;
  }
}
