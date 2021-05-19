import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the methods for the Candidate class.
 * CandidateTest.java
 *
 * @author Linh Duong
 */
public class CandidateTest {
  private Candidate cand1;
  private Candidate cand2;
  private Candidate cand3;
  private Candidate cand4;
  private Party party2;
  private Party party4;
  private ArrayList<Ballot> ballotList;

  /**
   * Initializes the candidates for testing.
   */
  @BeforeEach
  public void init() {
    // Initialize Candidate 1
    cand1 = new Candidate("Tester 1");

    //Initialize Candidate 2
    party2 = new Party("Republic");
    cand2 = new Candidate("Tester 2", party2);

    //Initialize Candidate 3
    cand3 = new Candidate("Tester 3", 10);

    //Initialize Candidate 4
    ballotList = new ArrayList<>();
    int[] ranked = new int[1];
    ranked[0] = 1;
    Ballot b1 = new Ballot(ranked);
    ballotList.add(b1);
    party4 = new Party("Liberal");
    cand4 = new Candidate("Tester 4", 5, ballotList, party4);
  }

  @Test
  public void testCandidateConstructor1() {
    assertEquals("Tester 1", cand1.getName());
  }

  @Test
  public void testCandidateConstructor2() {
    assertEquals("Tester 2", cand2.getName());
    assertEquals("Republic", cand2.getParty().getName());
  }

  @Test
  public void testCandidateConstructor3() {
    assertEquals("Tester 3", cand3.getName());
    assertEquals(10, cand3.getNumVotes());
  }

  @Test
  public void testCandidateConstructor4() {
    assertEquals("Tester 4", cand4.getName());
    assertEquals(5, cand4.getNumVotes());
    assertEquals(1, cand4.getBallots().get(0).getCurrentDistribution());
    assertEquals("Liberal", cand4.getParty().getName());
  }

  @Test
  public void testGetParty() {
    Party party = new Party("Independent");
    cand4.setParty(party);
    assertEquals("Independent", cand4.getParty().getName());
  }

  @Test
  public void testGetVotes() {
    cand1.setVotes(10);
    cand2.setVotes(1);
    cand3.setVotes(4);
    assertEquals(10, cand1.getNumVotes());
    assertEquals(1, cand2.getNumVotes());
    assertEquals(4, cand3.getNumVotes());
  }

  @Test
  public void testGetBallots() {
    assertEquals(1, cand4.getBallots().get(0).getCurrentDistribution());
  }
}
