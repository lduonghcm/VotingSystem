import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the methods for the OpenPartyList class.
 * OpenPartyListTest.java
 *
 * @author Linh Duong
 */
public class OpenPartyListTest {
  private OpenPartyList opl;

  /**
   * Set up the open party list elections for testing.
   */
  @BeforeEach
  public void setUp() {
    BufferedReader fakeReader = new BufferedReader(new StringReader(
        "6" + System.lineSeparator()
            + "[Pike,D],[Foster,D],[Deutsch,R],[Borg,R],[Jones,R],[Smith,I]"
            + System.lineSeparator()
            + "3" + System.lineSeparator()
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

    opl = new OpenPartyList(fakeReader);
    opl.prepareData();
  }

  @Test
  public void testPrepareData() {
    assertEquals(3, opl.getPartyList().size());
    assertEquals(9, opl.getTotalVotes());
  }

  @Test
  public void testGenerateParticipants() {
    assertEquals("Deutsch", opl.getPartyList().get(0).getCandidateList().get(0).getName());
    assertEquals("Borg",    opl.getPartyList().get(0).getCandidateList().get(1).getName());
    assertEquals("Jones",   opl.getPartyList().get(0).getCandidateList().get(2).getName());

    assertEquals("Pike",    opl.getPartyList().get(1).getCandidateList().get(0).getName());
    assertEquals("Foster",  opl.getPartyList().get(1).getCandidateList().get(1).getName());

    assertEquals("Smith",   opl.getPartyList().get(2).getCandidateList().get(0).getName());
  }

  @Test
  public void testAllocateSeats() {
    ArrayList<Party> partyList = opl.getPartyList();
    opl.setQuota(3);
    opl.allocateSeats(partyList);

    // Party "R" has 1 seat. Party "D" has 1 seat. Party "I" has 0 seat.
    assertEquals(1, opl.getPartyList().get(0).getPartySeats());
    assertEquals(1, opl.getPartyList().get(1).getPartySeats());
    assertEquals(0, opl.getPartyList().get(2).getPartySeats());
  }

  @Test
  public void testCheckRemainingSeats() {
    ArrayList<Party> partyList = opl.getPartyList();

    // All party has 0 seats.
    partyList.get(0).setPartySeats(1);      // Set Party "R" to have 1 seats
    partyList.get(1).setPartySeats(3);      // Set Party "D" to have 3 seats
    partyList.get(2).setPartySeats(0);      // Set Party "I" to have 0 seats


    // Max seat capacity of Party "R" is 3 seats, Party "D" is 2, Party "I" is 1.
    opl.checkRemainingSeats();

    // After check, party "D" reduce to 2 seats. Other parties remains same.
    assertEquals(1, partyList.get(0).getPartySeats());
    assertEquals(2, partyList.get(1).getPartySeats());
    assertEquals(0, partyList.get(2).getPartySeats());

  }

  @Test
  public void testGetLargestRemainderVotes() {
    ArrayList<Party> partyList = opl.getPartyList();
    opl.setQuota(3);

    // Initially, Party "R" has 3 votes. Party "D" has 5. Party "I" has 1.
    opl.allocateSeats(partyList);
    opl.checkRemainingSeats();

    // After allocation. Party "R" has 0 votes. Party "D" has 2. Party "I" has 1.
    Party winner = opl.getLargestRemainderVotes(partyList);
    assertEquals("D", winner.getName());
  }

  @Test
  public void testCoinTossPartyOne() {
    ArrayList<Party> parties = new ArrayList<>();
    parties.add(new Party("party1"));
    String partyName = opl.coinTossParty(parties).getName();
    assertEquals("party1", partyName);
  }

  @Test
  public void testCoinTossPartyTwo() {
    ArrayList<Party> parties = new ArrayList<>();
    parties.add(new Party("party1"));
    parties.add(new Party("party2"));
    String partyName = opl.coinTossParty(parties).getName();
    assertTrue(partyName.equals("party1")
        || partyName.equals("party2"));
  }

  @Test
  public void testAllocateRemainingSeats() {
    ArrayList<Party> partyList = opl.getPartyList();

    opl.setQuota(3);
    opl.allocateSeats(partyList);
    opl.checkRemainingSeats();

    // Party "R" has 1 seat. Party "D" has 1 seat. Party "I" has 0 seat.
    // Party "R" has remaining 0 votes. Party "D" has 2. Party "I" has 1.
    // 1 available seat to be allocated
    opl.allocateRemainingSeats(partyList, 1);

    // Party "D" receives a seat.
    assertEquals(1, partyList.get(0).getPartySeats());
    assertEquals(2, partyList.get(1).getPartySeats());
    assertEquals(0, partyList.get(2).getPartySeats());
  }

  @Test
  public void testGetPartyList() {
    assertEquals(3, opl.getPartyList().size());
  }

  @Test
  public void testSetQuota() {
    opl.setQuota(10);
    assertEquals(10, opl.getQuota());
  }
}
