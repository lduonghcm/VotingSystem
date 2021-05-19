import java.util.ArrayList;

/**
 * Represents a candidate in the election.
 * Candidate.java
 *
 * @author Linh Duong, Roshina Mohamed Rafee
 */
public class Candidate {
  private String name;
  private int numVotes;
  private ArrayList<Ballot> ballots;
  private Party party;


  /**
   * Constructor for Candidate.
   */
  public Candidate() {
    this.name = "";
    this.numVotes = 0;
    this.ballots = new ArrayList<>();
    this.party = new Party();
  }

  /**
   * Constructor for Candidate.
   *
   * @param name name of candidate
   */
  public Candidate(String name) {
    this.name = name;
    this.numVotes = 0;
    this.ballots = new ArrayList<>();
    this.party = new Party();
  }

  /**
   * Constructor for Candidate.
   *
   * @param name  name of candidate
   * @param party party of candidate
   */
  public Candidate(String name, Party party) {
    this.name = name;
    this.numVotes = 0;
    this.ballots = new ArrayList<>();
    this.party = party;
  }

  /**
   * Constructor for Candidate.
   *
   * @param name     name of candidate
   * @param numVotes number of votes for candidate
   */
  public Candidate(String name, int numVotes) {
    this.name = name;
    this.numVotes = numVotes;
  }

  /**
   * Constructor for Candidate.
   *
   * @param name     name of candidate
   * @param numVotes number of votes for candidate
   * @param ballots  list of ballots for candidate
   * @param party    party of candidate
   */
  public Candidate(String name, int numVotes, ArrayList<Ballot> ballots, Party party) {
    this.name = name;
    this.numVotes = numVotes;
    this.ballots = ballots;
    this.party = party;
  }

  /**
   * Set the party for the candidate.
   *
   * @param party the party which candidate belongs to
   */
  public void setParty(Party party) {
    this.party = party;
  }

  /**
   * Get the number of votes for the candidate.
   *
   * @return number of votes
   */
  public int getNumVotes() {
    return numVotes;
  }

  /**
   * Get the name of the candidate.
   *
   * @return name of candidate
   */
  public String getName() {
    return name;
  }

  /**
   * Set the number of votes for the candidate.
   *
   * @param v number of votes per candidate
   */
  public void setVotes(int v) {
    numVotes = v;
  }

  /**
   * Get the parties name the candidate belongs to.
   *
   * @return party of candidate
   */
  public Party getParty() {
    return party;
  }

  /**
   * Get list of ballots for the candidate.
   *
   * @return list of ballots
   */
  public ArrayList<Ballot> getBallots() {
    return ballots;
  }
}
