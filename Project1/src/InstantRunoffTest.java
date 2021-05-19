import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the methods for the InstantRunoff class.
 * InstantRunoffTest.java
 *
 * @author Thomas Haupert
 */
public class InstantRunoffTest {
  private InstantRunoff ir;
  private InstantRunoff ir2;
  private InstantRunoff ir3;

  /**
   * Set up the instant runoff elections for testing.
   */
  @BeforeEach
  public void setUp() {
    BufferedReader fakeReader = new BufferedReader(new StringReader(
        "4" + System.lineSeparator()
            + "Rosen (D),Kleinberg (R),Chou (I),Royce (L)" + System.lineSeparator()
            + "6" + System.lineSeparator()
            + "1,3,4,2" + System.lineSeparator()
            + "1,,2," + System.lineSeparator()
            + "1,2,3," + System.lineSeparator()
            + "3,2,1,4" + System.lineSeparator()
            + ",,1,2" + System.lineSeparator()
            + ",,,1"
    ));
    ir = new InstantRunoff(fakeReader);
    try {
      ir.prepareData();
    } catch (IOException e) {
      e.printStackTrace();
    }

    BufferedReader fakeReader2 = new BufferedReader(new StringReader(
        "0" + System.lineSeparator()
            + "" + System.lineSeparator()
            + "0" + System.lineSeparator()
    ));
    ir2 = new InstantRunoff(fakeReader2);
    try {
      ir2.prepareData();
    } catch (IOException e) {
      e.printStackTrace();
    }

    BufferedReader fakeReader3 = new BufferedReader(new StringReader(
        "3" + System.lineSeparator() +
            "Candidate Blue (Blue Party),Candidate Red (Red Party),Candidate Green (Green Party)" + System.lineSeparator()
            + "4" + System.lineSeparator()
            + "1,,2" + System.lineSeparator()
            + ",1,2" + System.lineSeparator()
            + ",,1" +  System.lineSeparator()
            + ",,1" +  System.lineSeparator()
    ));
    ir3 = new InstantRunoff(fakeReader3);
    try {
      ir3.prepareData();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testPrepareData() {
    assertEquals(4, ir.getCandidateList().size());
    assertEquals(6, ir.getTotalVotes());
  }

  @Test
  public void testPrepareDataEmpty() {
    assertEquals(0, ir2.getCandidateList().size());
    assertEquals(0, ir2.getTotalVotes());
  }

  @Test
  public void testEliminateMin() {
    ArrayList<Candidate> candidateList = ir.getCandidateList();
    Candidate loser = ir.eliminateMin(candidateList);
    assertEquals("Kleinberg", loser.getName());
  }

  @Test
  public void testEliminateMinTied() {
    ArrayList<Candidate> candidateList = ir3.getCandidateList();
    Candidate loser = ir3.eliminateMin(candidateList);
    assertTrue(loser.getName().equals("Candidate Blue")
        || loser.getName().equals("Candidate Red"));
  }

  @Test
  public void testRedistributeVotes() {
    ArrayList<Candidate> candidateList = ir.getCandidateList();
    // Chou has two votes initially, Kleinberg has 0, and Royce has 1
    assertEquals(2, candidateList.get(2).getNumVotes());
    assertEquals(0, candidateList.get(1).getNumVotes());
    assertEquals(1, candidateList.get(3).getNumVotes());
    // If we redistribute theirs, Kleinburg has 1 and Royce has 2
    Candidate loser = candidateList.get(2);
    ir.redistributeVotes(candidateList, loser);
    assertEquals(1, candidateList.get(1).getNumVotes());
    assertEquals(2, candidateList.get(3).getNumVotes());
  }

  @Test
  public void testRedistributeVotesTwice() {
    ArrayList<Candidate> candidateList = ir3.getCandidateList();
    assertEquals(1, candidateList.get(0).getNumVotes());
    assertEquals(1, candidateList.get(1).getNumVotes());
    assertEquals(2, candidateList.get(2).getNumVotes());
    Candidate loser = candidateList.get(0);
    Candidate loser2 = candidateList.get(1);
    ir3.redistributeVotes(candidateList, loser);
    ir3.redistributeVotes(candidateList, loser2);
    assertEquals(4, candidateList.get(2).getNumVotes());
  }

  @Test
  public void testGetSetRunoffVotes() {
    ir.setRunoffVotes(0);
    assertEquals(0, ir.getRunoffVotes());
    ir.setRunoffVotes(10);
    assertEquals(10, ir.getRunoffVotes());
  }
}
