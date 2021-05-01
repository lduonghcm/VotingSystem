import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Represents the open party list election type.
 * OpenPartyList.java
 *
 * @author Linh Duong, Henry Huynh
 */
public class OpenPartyList extends Election {
  private final BufferedReader reader;
  private ArrayList<Party> partyList;
  private ArrayList<Candidate> electedList;
  private ArrayList<Party> initialVotes;
  private ArrayList<String> candidateList;
  private ArrayList<String> partyIndex;
  private int totalSeats;
  private int numSeats;
  private int quota;

  /**
   * OpenPartyList constructor.
   *
   * @param file Takes in a BufferedReader to prepare data from.
   */
  public OpenPartyList(BufferedReader file) {
    this.reader = file;
  }


  /**
   * Prepares data from the BufferedReader.
   * On BufferedReader line 3: generates parties and candidates from [Candidate,Party],.... format
   * On BufferedReader line 4: sets the available seats to win.
   * On BufferedReader line 6+: counts the ballot and updates the data stored in partyList.
   */
  public void prepareData() {
    int lineNum = 1;
    try {
      String line = reader.readLine();
      while (line != null) {
        //Line 2: Instantiate ArrayList for candidate and party index.
        if (lineNum == 1) {
          candidateList = new ArrayList<String>();
          partyIndex = new ArrayList<String>();
        }
        //Line 3: Uses helper function to generate parties and candidates.
        if (lineNum == 2) {
          generateParticipants(line);
        }
        //Line 4: Sets number of seats available to win.
        if (lineNum == 3) {
          numSeats = Integer.parseInt(line);
        }
        //Line 5: Set total votes.
        if (lineNum == 4) {
          setTotalVotes(Integer.parseInt(line));
        }
        //Line 6+: Uses helper function to generate a ballot for each line.
        if (lineNum >= 5) {
          countBallot(line);
        }
        lineNum += 1;
        line = reader.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public int getNumInvalidBallots() {
    return 0;
  }

  /**
   * Helper function for BufferedReader line 3, generates the parties and candidates.
   *
   * @param line String of pattern [Candidate,Party],...
   */
  public void generateParticipants(String line) {
    line = line.replace("[", "");
    line = line.replace("]", "");
    // Splits line into array of ["Candidate" , "Party"]
    String[] toSort = line.split(",");
    //Map with party name as key, each containing an arraylist of candidate names.
    Map<String, ArrayList<Candidate>> sorted = new HashMap<>();

    // Candidates are located at even indices, parties at odd.
    for (int i = 0; i < toSort.length; i++) {
      // Checks if the current index contains a party.
      if (i % 2 != 0) {
        //Adds candidate object to a key (party object) and creates the key if it doesn't exist.
        sorted.computeIfAbsent(toSort[i], k -> new ArrayList<>()).add(new Candidate(toSort[i - 1]));
        partyIndex.add(toSort[i]);                     //To store party order.
      } else {
        candidateList.add(toSort[i]);                  //To store candidate order.
      }
    }

    // Iterate through the hashmap and add each candidates to its key (party object).
    // Then adds to the global party list.
    for (Map.Entry<String, ArrayList<Candidate>> stringArrayListEntry : sorted.entrySet()) {
      Party partyName = new Party(stringArrayListEntry.getKey());
      ArrayList<Candidate> candidates = stringArrayListEntry.getValue();
      partyName.addCandidateList(candidates);
      if (partyList == null) {
        partyList = new ArrayList<Party>();
      }
      partyList.add(partyName);
    }

    for (Party party : partyList) {
      for (Candidate cand : party.getCandidateList()) {
        cand.setParty(party);
      }
    }
  }

  /**
   * Main function to control and run the OpenPartyList type election.
   *
   * @return the winner of the election
   */
  public ArrayList<Candidate> runElection() {
    appendFinalResult("Type of election: Open Party List" + System.lineSeparator());
    appendAuditFile("Beginning Open Party List election." + System.lineSeparator());
    prepareData();

    // Displaying the initial votes
    appendFinalResult("Initial votes:");
    appendAuditFile("Initial status:");
    totalSeats = numSeats;
    initialVotes = partyList;
    for (Party party : partyList) {
      appendAuditFile(String.format("Party \"%s\" has total of %d votes.",
              party.getName(), party.getPartyVotes()));
      appendFinalResult(String.format("Party \"%s\": %d", party.getName(), party.getPartyVotes()));
    }

    // First Allocation of Seats
    appendAuditFile(System.lineSeparator() + "First allocation of seats:");
    quota = getTotalVotes() / numSeats;
    allocateSeats(partyList);
    checkRemainingSeats();

    // Second Allocation of Seats
    appendAuditFile(System.lineSeparator() + "Second allocation of seats:");
    appendAuditFile(String.format("%d remaining seats needed to be allocated.", numSeats));
    allocateRemainingSeats(partyList, numSeats);

    // Final Seat Total
    appendFinalResult(System.lineSeparator() + "Winner(s) of election:");
    appendAuditFile(System.lineSeparator() + "Final result total:");
    getResult();

    // Statistic
    appendAuditFile(System.lineSeparator() + "Calculating the % of Votes / % of Seats:");
    appendFinalResult(System.lineSeparator() + "Seats allocation:");
    getStatistic();
    appendAuditFile(System.lineSeparator() + System.lineSeparator());
    return electedList;
  }

  /**
   * Organize the winner list for displaying or saving as audit file.
   */
  private void getResult() {
    electedList = new ArrayList<Candidate>();
    for (Party party : partyList) {
      int winningSeats = party.getPartySeats();
      appendAuditFile(String.format("Party \"%s\" got %d seats. This party has %d candidates. ",
          party.getName(), party.getPartySeats(), party.getCandidateList().size()));

      ArrayList<Candidate> electedCandidates = party.getTopXCandidate(winningSeats);
      for (Candidate cand : electedCandidates) {
        appendAuditFile(String.format("  Candidate \"%s\" won a seat.", cand.getName()));
        appendFinalResult(String.format("Candidate \"%s\" from party \"%s\".",
            cand.getName(), party.getName()));
      }
      electedList.addAll(electedCandidates);
    }
  }

  /**
   * Calculate the statistics for displaying or saving as audit file.
   */
  private void getStatistic() {
    for (Party party : partyList) {
      double percentVotes = ((double) (initialVotes.get(initialVotes.indexOf(party)).getPartyVotes())
          / (double) (getTotalVotes())) * 100.00;
      double percentSeats = (double) party.getPartySeats() / totalSeats * 100;

      appendFinalResult(String.format("Party \"%s\" has %d seats.",
          party.getName(), party.getPartySeats()));
      appendAuditFile(String.format("Party \"%s\": %d / %d",
          party.getName(), (int) percentVotes, (int) percentSeats));
    }
  }

  /**
   * Helper function used on BufferedReader line 6+ to count the vote.
   * Each ballot given in format where a vote is '1' and non-vote is ','.
   *
   * @param line String of ballot format.
   */
  private void countBallot(String line) {
    int candidateIndex = -1;
    char[] vote = line.toCharArray();
    for (int i = 0; i < line.length(); i++) {
      if (vote[i] == '1') {
        candidateIndex = i;
      }
    }

    String candidateName = candidateList.get(candidateIndex);
    String partyName = partyIndex.get(candidateIndex);

    for (Party party : partyList) {

      if (party.getName().equals(partyName)) {

        //Update the vote count for the party total.
        party.setPartyVotes(party.getPartyVotes() + 1);

        //Update the vote count for the candidate who received the vote.
        ArrayList<Candidate> updatedList = party.getCandidateList();
        for (Candidate currCandidate : updatedList) {
          if (currCandidate.getName().equals(candidateName)) {
            currCandidate.setVotes(currCandidate.getNumVotes() + 1);
          }
        }
        party.addCandidateList(updatedList);
      }
    }
  }

  /**
   * Function to decrement seats if another party has more seats.
   */
  public void checkRemainingSeats() {
    appendAuditFile(System.lineSeparator() + "Checking seats capacity for parties:");
    boolean fixCapacity = false;
    for (Party party : partyList) {
      int oldNumSeat = party.getPartySeats();
      while (party.getPartySeats() > party.getCandidateList().size()) {
        party.setPartySeats(party.getPartySeats() - 1);
        numSeats++;
      }
      if (oldNumSeat != party.getPartySeats()) {
        fixCapacity = true;
        appendAuditFile(
            String.format("Party \"%s\" has capacity of %d but it received %d seats; Deduct %d seat from the party.",
            party.getName(), party.getCandidateList().size(), oldNumSeat, (oldNumSeat - party.getCandidateList().size())));
        appendAuditFile(
            String.format("Party \"%s\" now has a total of %d seats. This party won't be participate in next allocation of seats.",
            party.getName(), party.getPartySeats()));
      }
    }
    if (!fixCapacity) {
      appendAuditFile("No party has more seats than its capacity.");
    } else {
      appendAuditFile("No more parties has more seats than its capacity.");
    }
  }

  /**
   * Function to allocate the seats.
   * Checks the party's votes against the quota and assigns seats accordingly.
   *
   * @param partyLst ArrayList of Party containing all the parties and their candidates.
   */
  public void allocateSeats(ArrayList<Party> partyLst) {
    appendAuditFile(String.format("Quota required for a seat is %d.", quota));
    for (Party party : partyLst) {
      party.setPartySeats(party.getPartyVotes() / quota);
      party.setPartyVotes(party.getPartyVotes() % quota);
      numSeats -= party.getPartySeats();
      appendAuditFile(
          String.format("Party \"%s\" can have %d seats; after first allocation, its remaining votes is %d.",
          party.getName(), party.getPartySeats(), party.getPartyVotes()));
    }
  }


  /**
   * Function to find the party with the greatest remaining votes after seat allocation.
   *
   * @param partylst ArrayList of Party containing all the parties and their candidates.
   * @return Party with the largest remaining votes.
   */
  public Party getLargestRemainderVotes(ArrayList<Party> partylst) {
    Party largestParty = partylst.get(0);
    int maxVotes = largestParty.getPartyVotes();
    // First loop: check for the party with largest votes
    for (Party party : partylst) {
      if ((party.getPartyVotes() > maxVotes)
          && (party.getPartySeats() < party.getCandidateList().size())) {
        maxVotes = party.getPartyVotes();
        largestParty = party;
      }
    }

    ArrayList<Party> tieParties = new ArrayList<>();
    // Second loop: check if other parties have the same largest votes
    for (Party party : partylst) {
      if (party.getPartyVotes() == maxVotes) {
        tieParties.add(party);
      }
    }

    if (tieParties.size() > 1) {
      appendAuditFile(String.format("There are %d parties with the same largest votes:", tieParties.size()));
      for (Party party : tieParties) {
        appendAuditFile(String.format("    - Party \"%s\"", party.getName()));
      }
      largestParty = coinTossParty(tieParties);
    }

    return largestParty;
  }

  /**
   * Function to fairly toss a coin and decide a winner given that party(s) have tied in remaining seats.
   *
   * @param tieParties ArrayList of Party containing all the parties tied in remaining votes.
   * @return Winning party.
   */
  public Party coinTossParty(ArrayList<Party> tieParties) {
    appendAuditFile(
        String.format("Tossing a coin to choose a winning party from %d tie parties.", tieParties.size()));
    Random rand = new Random();
    int upperbound = 1000;
    int intRandom = rand.nextInt(upperbound);
    int winnerIndex = intRandom % tieParties.size();

    Party winner = tieParties.get(winnerIndex);
    appendAuditFile(String.format("After tossing coin, party \"%s\" is the winner party for coin toss.", winner.getName()));
    tieParties.set(winnerIndex, null);
    return winner;
  }

  /**
   * Function to allocate the remaining seats after previous seat allocation.
   *
   * @param partyLst   ArrayList of Party containing all the parties and their candidates.
   * @param availSeats int representing the remaining available seats.
   */
  public void allocateRemainingSeats(ArrayList<Party> partyLst, int availSeats) {
    if (availSeats == 0) {
      return;
    }

    Party winner = getLargestRemainderVotes(partyLst);
    winner.setPartySeats(winner.getPartySeats() + 1);
    winner.setPartyVotes(winner.getPartyVotes() - quota);
    if (winner.getPartyVotes() < 0) {
      winner.setPartyVotes(0);
    }

    numSeats--;
    appendAuditFile(String.format("Party \"%s\" has won a seat. It currently have total of %d seats.",
        winner.getName(), winner.getPartySeats()));
    appendAuditFile(String.format("%d remaining seats needed to be allocated.", numSeats));
    allocateRemainingSeats(partyLst, numSeats);
  }

  /**
   * Gets the list of parties in the election.
   *
   * @return the list of parties participating in the election
   */
  public ArrayList<Party> getPartyList() {
    return partyList;
  }

  /**
   * Set the quota required for each party.
   *
   * @param quota the number of votes required to meet
   */
  public void setQuota(int quota) {
    this.quota = quota;
  }

  /**
   * Get the quota required for each party.
   *
   * @return the quota required for the party
   */
  public int getQuota() {
    return quota;
  }
}
