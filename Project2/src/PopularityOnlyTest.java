import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the methods for the PopularityOnly class.
 * PopularityOnly.java
 *
 * @author Linh Duong
 */
public class PopularityOnlyTest {
  private PopularityOnly po;

  /**
   * Set up the popularity only elections for testing.
   */
  @BeforeEach
  public void setUp() {
    ElectionManager.TESTINGMODE = true;
    BufferedReader fakeReader = new BufferedReader(new StringReader(
        "6" + System.lineSeparator()
            + "[Pike,D],[Foster,D],[Deutsch,R],[Borg,R],[Jones,R],[Smith,I]"
            + System.lineSeparator()
            + "9" + System.lineSeparator()
            + "1,,,,," + System.lineSeparator()
            + "1,,,,," + System.lineSeparator()
            + ",1,,,," + System.lineSeparator()
            + ",,,,1," + System.lineSeparator()
            + ",,,,,1" + System.lineSeparator()
            + ",,,1,," + System.lineSeparator()
            + ",,,1,," + System.lineSeparator()
            + "1,,,,," + System.lineSeparator()
            + ",1,,,,"

    ));
    po = new PopularityOnly(fakeReader);
    try {
      po.prepareData();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testPrepareData() {
    assertEquals(6, po.getCandidateList().size());
    assertEquals(9, po.getTotalVotes());
  }

  @Test
  public void testRunElection() {
    po.runElection();
    assertEquals("Pike", po.getCandidateList().get(0).getName());
    assertEquals("D", po.getCandidateList().get(0).getParty().getName());

    assertEquals("Foster", po.getCandidateList().get(1).getName());
    assertEquals("D", po.getCandidateList().get(1).getParty().getName());

    assertEquals("Deutsch", po.getCandidateList().get(2).getName());
    assertEquals("R", po.getCandidateList().get(2).getParty().getName());

    assertEquals("Borg", po.getCandidateList().get(3).getName());
    assertEquals("R", po.getCandidateList().get(3).getParty().getName());

    assertEquals("Jones", po.getCandidateList().get(4).getName());
    assertEquals("R", po.getCandidateList().get(4).getParty().getName());

    assertEquals("Smith", po.getCandidateList().get(5).getName());
    assertEquals("I", po.getCandidateList().get(5).getParty().getName());
  }

  @Test
  public void testGetNumInvalidBallots() {
    assertEquals(0, po.getNumInvalidBallots());
  }

  @Test
  public void testGetCandidateList() {
    ArrayList<Candidate> candidateList = po.getCandidateList();
    assertEquals("Pike", candidateList.get(0).getName());
    assertEquals("Foster", candidateList.get(1).getName());
    assertEquals("Deutsch", candidateList.get(2).getName());
    assertEquals("Borg", candidateList.get(3).getName());
    assertEquals("Jones", candidateList.get(4).getName());
    assertEquals("Smith", candidateList.get(5).getName());
  }
}
