import java.io.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests using the test files located in the testing directory.
 * The testing directory location must be specified in this file.
 * SystemTest.java
 *
 * @author Thomas Haupert
 */
public class SystemTest {
  //////////////////////////////////////////////////////////////////////////////////////////////
  // IMPORTANT: Change this directory below to where the "testing" folder is on your computer
  // Remember: All backslashes (\) in the file path need to have a second backslash added (\\)
  //////////////////////////////////////////////////////////////////////////////////////////////
  private String testingDirectory = "C:\\Users\\Thomas\\IdeaProjects\\repo-Team18\\Project1\\testing";

  private InputStream origInStream;
  private OutputStream origOutStream;

  /**
   * Redirects the input and output streams so that tests can modify the input and read the output.
   */
  @BeforeEach
  public void setUp() {
    origInStream = System.in;
    origOutStream = System.out;
  }

  /**
   * Undoes any changes made to input and output streams.
   */
  @AfterEach
  public void cleanUp() {
    System.setIn(origInStream);
    System.setOut((PrintStream) origOutStream);
  }

  private String[] testElection(String testfilename) {
    // get the full file path
    String fullFilePath = testingDirectory + "\\" + testfilename;
    // set the input stream, so it automatically inputs the file name to the program
    System.setIn(new ByteArrayInputStream(fullFilePath.getBytes()));
    // set the output stream, so we can intercept the output before it gets printed to the screen
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
    // run the program
    ElectionManager.main(new String[0]);
    // get the output string so we can check that it is correct
    String outputString = outputStream.toString();
    return outputString.split(System.lineSeparator());
  }

  @Test
  public void testIR_given() {
    String[] splitString = testElection("IR_given.csv");
    assertEquals("Voting Method: Instant Runoff", splitString[2]);
    assertEquals("Winner(s) of election: Rosen (D)", splitString[5]);
    assertEquals("Number of ballots cast: 6", splitString[7]);
  }

  @Test
  public void testIR_runofftie() {
    String[] splitString = testElection("IR_runofftie.csv");
    assertEquals("Voting Method: Instant Runoff", splitString[2]);
    assertEquals("Winner(s) of election: Candidate Blue (Blue Party)", splitString[5]);
    assertEquals("Number of ballots cast: 7", splitString[7]);
  }

  @Test
  public void testIR_tie() {
    String[] splitString = testElection("IR_tie.csv");
    assertEquals("Voting Method: Instant Runoff", splitString[2]);
    assertTrue(splitString[5].equals("Winner(s) of election: Candidate Red (Red Party)")
        || splitString[5].equals("Winner(s) of election: Candidate Blue (Blue Party)"));
    assertEquals("Number of ballots cast: 2", splitString[7]);
  }

  @Test
  public void testIR_tie3way() {
    String[] splitString = testElection("IR_tie(3way).csv");
    assertEquals("Voting Method: Instant Runoff", splitString[2]);
    assertTrue(splitString[5].equals("Winner(s) of election: Tie 1 (A)")
        || splitString[5].equals("Winner(s) of election: Tie 2 (B)")
        || splitString[5].equals("Winner(s) of election: Tie 3 (D)"));
    assertEquals("Number of ballots cast: 8", splitString[7]);
  }

  @Test
  public void testOPL_given() {
    String[] splitString = testElection("OPL_given.csv");
    assertEquals("Voting Method: Open Party List", splitString[2]);
    assertEquals("Winner(s) of election: Borg (R), Pike (D), Foster (D)", splitString[5]);
    assertEquals("Number of ballots cast: 9", splitString[7]);
  }

  @Test
  public void testOPL_moreSeatsThanCapacity() {
    String[] splitString = testElection("OPL_moreSeatsThanCapacity.csv");
    assertEquals("Voting Method: Open Party List", splitString[2]);
    assertEquals("Number of ballots cast: 14", splitString[7]);
  }

  @Test
  public void testOPL_onePartyWinAll() {
    String[] splitString = testElection("OPL_onePartyWinAll.csv");
    String[] winnerSplitString = splitString[5].split(" ");
    assertEquals("Voting Method: Open Party List", splitString[2]);
    assertEquals("(D),", winnerSplitString[5]);
    assertEquals("(D),", winnerSplitString[8]);
    assertEquals("(D)", winnerSplitString[11]);
    assertEquals("Number of ballots cast: 13", splitString[7]);
  }

  @Test
  public void testOPL_tie2TieChoose1() {
    String[] splitString = testElection("OPL_tie(2TieChoose1).csv");
    assertEquals("Voting Method: Open Party List", splitString[2]);
    assertTrue(splitString[5].equals("Winner(s) of election: Candidate 1 (D)")
        || splitString[5].equals("Winner(s) of election: Candidate 3 (R)"));
    assertEquals("Number of ballots cast: 7", splitString[7]);
  }

  @Test
  public void testOPL_tie3TieChoose1() {
    String[] splitString = testElection("OPL_tie(3TieChoose1).csv");
    assertEquals("Voting Method: Open Party List", splitString[2]);
    assertTrue(splitString[5].equals("Winner(s) of election: Candidate 1 (D)")
        || splitString[5].equals("Winner(s) of election: Candidate 3 (R)")
        || splitString[5].equals("Winner(s) of election: Candidate 5 (I)"));
    assertEquals("Number of ballots cast: 9", splitString[7]);
  }

  @Test
  public void testOPL_tie3TieChoose2() {
    String[] splitString = testElection("OPL_tie(3TieChoose2).csv");
    String[] winnerSplitString = splitString[5].split(" ");
    String winner1 = winnerSplitString[4];
    String winnerparty1 = winnerSplitString[5];
    String winner2 = winnerSplitString[7];
    String winnerparty2 = winnerSplitString[8];
    assertEquals("Voting Method: Open Party List", splitString[2]);
    assertTrue((winner1.equals("1") && winnerparty1.equals("(D),"))
        || (winner1.equals("3") && winnerparty1.equals("(R),"))
        || (winner1.equals("5") && winnerparty1.equals("(I),")));
    assertTrue((winner2.equals("1") && winnerparty2.equals("(D)"))
        || (winner2.equals("3") && winnerparty2.equals("(R)"))
        || (winner2.equals("5") && winnerparty2.equals("(I)")));
    assertEquals("Number of ballots cast: 9", splitString[7]);
  }
}
