import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the methods for the Election class.
 * ElectionTest.java
 *
 * @author Thomas Haupert
 */
public class ElectionTest {
  private Election election;
  private ArrayList<Candidate> candlist;

  @BeforeEach
  public void init() {
    election = new InstantRunoff(new BufferedReader(new StringReader("")));
  }

  @Test
  public void testTotalVotes() {
    election.setTotalVotes(100);
    assertEquals(100, election.getTotalVotes());
  }

  @Test
  public void testCoinTossOne() {
    candlist = new ArrayList<>();
    candlist.add(new Candidate("cand1"));
    assertEquals("cand1", election.coinToss(candlist).getName());
  }

  @Test
  public void testCoinTossTwo() {
    candlist = new ArrayList<>();
    Party party1 = new Party("party1");
    candlist.add(new Candidate("cand1", party1));
    candlist.add(new Candidate("cand2", party1));
    assertEquals("party1", election.coinToss(candlist).getParty().getName());
  }

  @Test
  public void testCoinTossThree() {
    candlist = new ArrayList<>();
    Party party1 = new Party("party1");
    candlist.add(new Candidate("cand1", 100, new ArrayList<>(), party1));
    candlist.add(new Candidate("cand2", 150, new ArrayList<>(), party1));
    candlist.add(new Candidate("cand3", 200, new ArrayList<>(), party1));
    int numOfVotes = election.coinToss(candlist).getNumVotes();
    assertTrue(numOfVotes == 100 || numOfVotes == 150 || numOfVotes == 200);
  }

  @Test
  public void testSetTotalVotes() {
    election.setTotalVotes(100);
    assertEquals(100, election.getTotalVotes());
  }

  @Test
  public void testAppendMediaFile() {
    election.appendFinalResult("test");
  }

  @Test
  public void testAppendAuditFile() {
    election.appendAuditFile("test");
  }
}
