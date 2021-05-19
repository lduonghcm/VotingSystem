import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the methods for the Ballot class.
 * BallotTest.java
 *
 * @author Roshina Mohamed Rafee
 */
public class BallotTest {
  @Test
  public void testCurrentDistribution() {
    Ballot currentBallot = new Ballot(new int[1]);
    assertEquals(1, currentBallot.getCurrentDistribution());
  }

  @Test
  public void testGetNumRankedCandidates() {
    int[] ints = new int[4];
    ints[0] = 1;
    ints[1] = 2;
    ints[2] = 3;
    ints[3] = 0;

    Ballot newBallot = new Ballot(ints);
    assertEquals(3, newBallot.getNumRankedCandidates());

    ints[1] = 0;
    assertEquals(2, newBallot.getNumRankedCandidates());
  }

  @Test
  public void testGetterSetterValid() {
    Ballot newBallot = new Ballot(new int[4]);
    newBallot.setValid(false);
    assertFalse(newBallot.isValid());
    newBallot.setValid(true);
    assertTrue(newBallot.isValid());
  }

  @Test
  public void testIncrementCurrentDistribution() {
    Ballot newBallot = new Ballot(new int[1]);
    newBallot.incrementCurrentDistribution();
    assertEquals(2, newBallot.getCurrentDistribution());
  }

  @Test
  public void testIncrementCurrentDistributionTwice() {
    Ballot newBallot = new Ballot(new int[1]);
    newBallot.incrementCurrentDistribution();
    newBallot.incrementCurrentDistribution();
    assertEquals(3, newBallot.getCurrentDistribution());
  }

  @Test
  public void testFindDistributionVote() {
    int[] ints = new int[2];
    ints[0] = 1;
    ints[1] = 2;
    Ballot newBallot = new Ballot(ints);
    assertEquals(0, newBallot.findDistributionVote());
    newBallot.incrementCurrentDistribution();
    assertEquals(1, newBallot.findDistributionVote());
  }

  @Test
  public void testFindDistributionVoteEnd() {
    int[] ints = new int[1];
    ints[0] = 1;
    Ballot newBallot = new Ballot(ints);
    assertEquals(0, newBallot.findDistributionVote());
    newBallot.incrementCurrentDistribution();
    assertEquals(-1, newBallot.findDistributionVote());
  }
}
