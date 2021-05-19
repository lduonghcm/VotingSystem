import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the methods for the Party class.
 * PartyTest.java
 *
 * @author Linh Duong
 */
public class PartyTest {
  private Party party1;
  private Party party2;
  private Party party3;
  private Candidate cand1;
  private Candidate cand2;
  private Candidate cand3;
  private ArrayList<Candidate> candidateList;

  /**
   * Initializes the parties for testing.
   */
  @BeforeEach
  public void init() {
    party1 = new Party();
    party2 = new Party("Republic");

    candidateList = new ArrayList<Candidate>();
    cand1 = new Candidate("Tester1", 100);
    cand2 = new Candidate("Tester2", 200);
    cand3 = new Candidate("Tester3", 120);
    candidateList.add(cand1);
    candidateList.add(cand2);
    candidateList.add(cand3);
    party3 = new Party("Liberal", candidateList);
  }

  // Test the constructors
  @Test
  public void testPartyConstructor1() {
    assertEquals("", party1.getName());
    assertNull(party1.getCandidateList());
    assertEquals(0, party1.getPartyVotes());
    assertEquals(0, party1.getPartySeats());
  }

  @Test
  public void testPartyConstructor2() {
    assertEquals("Republic", party2.getName());
    assertNull(party2.getCandidateList());
    assertEquals(0, party2.getPartyVotes());
    assertEquals(0, party2.getPartySeats());
  }

  @Test
  public void testPartyConstructor3() {
    assertEquals("Liberal", party3.getName());
    assertEquals("Tester1", party3.getCandidateList().get(0).getName());
    assertEquals("Tester2", party3.getCandidateList().get(1).getName());
    assertEquals(420, party3.getPartyVotes());
    assertEquals(0, party3.getPartySeats());
  }

  @Test
  public void testPartySetters() {
    party1.addCandidateList(candidateList);
    party1.setPartySeats(2);
    party1.setPartyVotes(100);
    assertEquals("Tester1", party1.getCandidateList().get(0).getName());
    assertEquals("Tester2", party1.getCandidateList().get(1).getName());
    assertEquals(2, party1.getPartySeats());
    assertEquals(100, party1.getPartyVotes());
  }

  @Test
  public void testGetTopXCandidate() {
    ArrayList<Candidate> top = party3.getTopXCandidate(2);
    assertEquals(2, top.size());
    assertTrue(top.contains(cand2));
    assertTrue(top.contains(cand3));
  }
}
