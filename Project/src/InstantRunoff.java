import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents the instant runoff election type.
 * InstantRunoff.java
 *
 * @author Thomas Haupert
 */
public class InstantRunoff extends Election {
  private BufferedReader file;
  private ArrayList<Candidate> candidateList;
  private Candidate elected;
  private int majority;
  private int runoffVotes;
  private int numInvalidBallots;


  public InstantRunoff(BufferedReader file) {
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
    if (!candidates[0].equals("")) {
      for (String s : candidates) {   // read each candidate
        String[] fullcandidate = s.split(" \\(");  // split into candidate and party
        fullcandidate[1] =
            fullcandidate[1].substring(0, fullcandidate[1].length() - 1); // remove trailing )
        candidateList.add(new Candidate(fullcandidate[0], new Party(fullcandidate[1])));
      }
    }

    // read line 4: number of ballots
    line = file.readLine();
    int numBallots = Integer.parseInt(line);
    setTotalVotes(numBallots);
    setRunoffVotes(numBallots);

    // read remaining lines: ballots
    for (int i = 0; i < numBallots; i++) {
      countBallot();
    }

    // invalidate ballots of candidates that are not at least half filled out
    updateBallotValidity();

    appendAuditFile(numBallots + " ballot(s) were cast, but "
        + getNumInvalidBallots() + " invalid ballot(s) were removed leaving "
        + (numBallots-getNumInvalidBallots()) + " valid ballot(s).");
    appendFinalResult("Ballots cast: " + numBallots
        + " (valid: " + (numBallots-getNumInvalidBallots())
        + ", invalid: " + getNumInvalidBallots() + ")");

    // Set the total number of votes for each candidate
    for (Candidate candidate : getCandidateList()) {
      for (Ballot ballot : candidate.getBallots()) {
        if (ballot.isValid()) {
          candidate.setVotes(candidate.getNumVotes() + 1);
        }
      }
    }
  }

  /**
   * Runs the instant runoff election.
   *
   * @return the winner of the election
   */
  public ArrayList<Candidate> runElection() {
    // Append information about election to media and audit files
    fileInitialInfo();

    // Run the election
    Candidate winner;
    while ((winner = checkMajority(candidateList)) == null) {
      appendAuditFile("No candidate has majority.");
      Candidate loser = eliminateMin(candidateList);
      appendAuditFile(
          String.format("Candidate \"%s\" has been eliminated. "
              + "Their votes will now be redistributed and their vote count will be set to -1.",
              loser.getName()));
      redistributeVotes(candidateList, loser);
      loser.setVotes(-1);
    }

    // Append more information about election to media and audit files
    fileFinalInfo(winner);

    // Return the winner of the election
    ArrayList<Candidate> winnerArray = new ArrayList<>();
    winnerArray.add(winner);
    return winnerArray;
  }

  /**
   * Adds initial info about the election to the media and audit files.
   */
  private void fileInitialInfo() {
    appendAuditFile("Beginning Instant Runoff election.");
    appendFinalResult("Type of election: Instant Runoff" + System.lineSeparator());
    appendFinalResult("Initial votes in runoff:");
    for (Candidate candidate : candidateList) {
      appendFinalResult(String.format("%s: %d", candidate.getName(), candidate.getNumVotes()));
    }
    appendFinalResult("");
  }

  /**
   * Adds final info about the the election (including the winner) to the media and audit files.
   *
   * @param winner winner of the election
   */
  private void fileFinalInfo(Candidate winner) {
    appendAuditFile(
        String.format("Candidate \"%s\" is the winner of the election with %d votes.",
            winner.getName(), winner.getNumVotes())
            + System.lineSeparator() + System.lineSeparator() + System.lineSeparator());
    appendFinalResult(
        String.format("Winner of election: %s (%s)",
            winner.getName(), winner.getParty().getName()));
    appendFinalResult(
        String.format("Final runoff votes for winner: %d",
            winner.getNumVotes()));
  }

  /**
   * Counts a single ballot.
   */
  private void countBallot() {
    String line = null;
    try {
      line = file.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Convert ballots in file to Ballot class
    String[] ballotVotes = line.split(",");
    int[] rankedBallotVotes = new int[candidateList.size()];
    int candidateToGiveBallot = -1;
    for (int i = 0; i < ballotVotes.length; i++) {
      if (ballotVotes[i].equals("")) {
        continue;
      }
      rankedBallotVotes[i] = Integer.parseInt(ballotVotes[i]);
      if (rankedBallotVotes[i] == 1) {
        candidateToGiveBallot = i;
      }
    }
    candidateList.get(candidateToGiveBallot).getBallots().add(new Ballot(rankedBallotVotes));
  }

  /**
   * Checks to see if there is a candidate with the majority of the votes.
   *
   * @param clist list of candidates
   * @return candidate with the majority of the votes, null otherwise
   */
  public Candidate checkMajority(ArrayList<Candidate> clist) {
    // if there is only one candidate, they are the winner
    if (candidateList.size() == 1) {
      appendAuditFile("Only one candidate in election, they are automatic winner.");
      return candidateList.get(0);
    }
    // iterate through candidates and return the one that has majority
    majority = (getRunoffVotes() / 2) + 1;
    appendAuditFile(System.lineSeparator() + "Now checking if any candidate has majority.");
    appendAuditFile(String.format("Votes required for majority: %d", majority));
    for (Candidate candidate : clist) {
      if (candidate.getNumVotes() >= majority) {
        appendAuditFile(
            String.format("Candidate \"%s\" has majority (has %d of required %d votes).",
                candidate.getName(), candidate.getNumVotes(), majority));
        return candidate;
      }
      appendAuditFile(
          String.format("Candidate \"%s\" does not have majority (has %d of required %d votes).",
              candidate.getName(), candidate.getNumVotes(), majority));
    }
    return null;
  }

  /**
   * Returns the candidate with the least amount of votes.
   *
   * @param clist list of candidates
   * @return candidate with the least number of votes
   */
  public Candidate eliminateMin(ArrayList<Candidate> clist) {
    appendAuditFile(
        System.lineSeparator() + "Now checking which candidate has the least number of votes.");
    int minVotes = -2;
    ArrayList<Candidate> tiedlist = new ArrayList<>();

    // initial check for the least number of votes (ignoring eliminated candidates)
    for (int i = 0; i < clist.size(); i++) {
      if ((minVotes == -2)
          || clist.get(i).getNumVotes() < minVotes && clist.get(i).getNumVotes() != -1) {
        minVotes = clist.get(i).getNumVotes();
      }
    }

    appendAuditFile(
        String.format("The following candidate(s) have the least number of votes with %d:",
            minVotes));

    // check which candidates are tied for least number of votes
    for (Candidate candidate : clist) {
      if (candidate.getNumVotes() == minVotes) {
        appendAuditFile(String.format("  %s", candidate.getName()));
        tiedlist.add(candidate);
      }
    }

    if (tiedlist.size() == 1) {                 // if no tie, then return candidate with least votes
      return tiedlist.get(0);
    } else {                                    // else randomly select who to eliminate
      return coinToss(tiedlist);
    }
  }

  /**
   * Redistributes the votes for the ballots that selected the losing candidate.
   *
   * @param clist list of candidates
   * @param loser candidate whose votes should be redistributed
   */
  public void redistributeVotes(ArrayList<Candidate> clist, Candidate loser) {
    ArrayList<Ballot> loserBallots = loser.getBallots();

    // select the next choice for ballots for losing candidate
    appendAuditFile(
        String.format("Incrementing the choice number of \"%s\"'s ballots:",
            loser.getName()));
    for (Ballot loserBallot : loserBallots) {
      loserBallot.incrementCurrentDistribution();
    }

    // give each ballot to the next choice candidate
    for (int i = 0; i < loserBallots.size(); i++) {
      Ballot loserBallot = loserBallots.get(i);
      int choiceNumber = loserBallot.findDistributionVote();
      if (choiceNumber == -1) {     // if voter did not select a next choice, then ignore the ballot
        appendAuditFile(
            String.format("  Ballot %d did not have next choice listed, so it is deleted.", i));
        setRunoffVotes(getRunoffVotes() - 1);
        continue;
      }
      Candidate nextChoice = clist.get(choiceNumber);
      appendAuditFile(
          String.format("  Ballot %d selected candidate \"%s\" as their next choice, "
                  + "so it will be given to them.",
              i, nextChoice.getName()));
      nextChoice.getBallots().add(loserBallot);
      nextChoice.setVotes(nextChoice.getNumVotes() + 1);
    }
  }

  /**
   * Updates the status of the validity of all ballots contained in each candidate of candidateList.
   */
  public void updateBallotValidity() {
    if (ElectionManager.TESTINGMODE) {
      return;
    }
    int validThreshold = (getCandidateList().size() / 2);
    if (getCandidateList().size() % 2 != 0) {
      // if threshold is odd, add 1 to round the integer up instead of down
      validThreshold++;
    }
    for (Candidate candidate : getCandidateList()) {
      ArrayList<Ballot> ballots = candidate.getBallots();
      for (Ballot ballot : ballots) {
        if (ballot.getNumRankedCandidates() < validThreshold) {
          numInvalidBallots++;
          ballot.setValid(false);
        }
      }
      candidate.setBallots(ballots);
    }
  }

  /**
   * Sets the amount of runoff votes.
   *
   * @param runoffVotes amount of votes that remain after candidates have been eliminated
   */
  public void setRunoffVotes(int runoffVotes) {
    this.runoffVotes = runoffVotes;
  }

  /**
   * Gets the number of runoff votes.
   *
   * @return amount of votes that remain after candidates have been eliminated
   */
  public int getRunoffVotes() {
    return runoffVotes;
  }

  /**
   * Gets the list of candidates in the election.
   *
   * @return list of candidates in the election
   */
  public ArrayList<Candidate> getCandidateList() {
    return candidateList;
  }

  /**
   * Gets the number of invalid ballots in the election.
   *
   * @return integer representing the number of invalid ballots
   */
  public int getNumInvalidBallots() {
    return numInvalidBallots;
  }
}
