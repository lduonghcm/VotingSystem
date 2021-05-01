import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests using the test files located in the testing directory.
 * The testing directory location must be specified in this file.
 * SystemTest.java
 *
 * @author Thomas Haupert, Linh Duong
 */
public class SystemTest {

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

  private String[] testElection(String testfilename, boolean testingmode) {
    // get the full file path
    File file = new File("testing" + File.separator + testfilename);
    String fullFilePath = file.getAbsolutePath();

    // set the input stream, so it automatically inputs the file name to the program
    System.setIn(new ByteArrayInputStream(fullFilePath.getBytes()));
    // set the output stream, so we can intercept the output before it gets printed to the screen
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
    // run the program
    String[] arguments = new String[1];
    if (testingmode) {
      arguments[0] = "test";
    }
    ElectionManager.main(arguments);
    // get the output string so we can check that it is correct
    String outputString = outputStream.toString();
    return outputString.split(System.lineSeparator());
  }

  private String[] testMultipleElection(String[] testFileNames) {
    // build input string for testing
    StringBuilder input = new StringBuilder();

    // get full path for each file
    for (int i = 0; i < testFileNames.length; i++) {
      File file = new File("testing" + File.separator + testFileNames[i]);
      String fullFilePath = file.getAbsolutePath();
      if (i != 0) {
        input.append("|");
      }
      input.append(fullFilePath);
    }

    // set the input stream, so it automatically inputs the file name to the program
    System.setIn(new ByteArrayInputStream(input.toString().getBytes()));

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
    String[] splitString = testElection("IR_given.csv", true);
    assertEquals("Voting Method: Instant Runoff", splitString[5]);
    assertEquals("Winner(s) of election: Rosen (D)", splitString[8]);
    assertEquals("Number of ballots cast: 6", splitString[10]);
  }

  @Test
  public void testIR_runofftie() {
    String[] splitString = testElection("IR_runofftie.csv", true);

    assertEquals("Voting Method: Instant Runoff", splitString[5]);
    assertEquals("Winner(s) of election: Candidate Blue (Blue Party)", splitString[8]);
    assertEquals("Number of ballots cast: 7", splitString[10]);
  }

  @Test
  public void testIR_tie() {
    String[] splitString = testElection("IR_tie.csv", true);

    assertEquals("Voting Method: Instant Runoff", splitString[5]);
    assertTrue(splitString[8].equals("Winner(s) of election: Candidate Red (Red Party)")
            || splitString[8].equals("Winner(s) of election: Candidate Blue (Blue Party)"));
    assertEquals("Number of ballots cast: 2", splitString[10]);
  }

  @Test
  public void testIR_tie3way() {
    String[] splitString = testElection("IR_tie(3way).csv", true);

    assertEquals("Voting Method: Instant Runoff", splitString[5]);
    assertTrue(splitString[8].equals("Winner(s) of election: Tie 1 (A)")
            || splitString[8].equals("Winner(s) of election: Tie 2 (B)")
            || splitString[8].equals("Winner(s) of election: Tie 3 (D)"));
    assertEquals("Number of ballots cast: 8", splitString[10]);
  }

  @Test
  public void testOPL_given() {
    String[] splitString = testElection("OPL_given.csv", true);

    assertEquals("Voting Method: Open Party List", splitString[5]);
    assertEquals("Winner(s) of election: Borg (R), Pike (D), Foster (D)", splitString[8]);
    assertEquals("Number of ballots cast: 9", splitString[10]);
  }

  @Test
  public void testOPL_moreSeatsThanCapacity() {
    String[] splitString = testElection("OPL_moreSeatsThanCapacity.csv", true);

    assertEquals("Voting Method: Open Party List", splitString[5]);
    assertEquals("Number of ballots cast: 14", splitString[10]);
  }

  @Test
  public void testOPL_onePartyWinAll() {
    String[] splitString = testElection("OPL_onePartyWinAll.csv", true);

    String[] winnerSplitString = splitString[8].split(" ");
    assertEquals("Voting Method: Open Party List", splitString[5]);
    assertEquals("(D),", winnerSplitString[5]);
    assertEquals("(D),", winnerSplitString[8]);
    assertEquals("(D)", winnerSplitString[11]);
    assertEquals("Number of ballots cast: 13", splitString[10]);
  }

  @Test
  public void testOPL_tie2TieChoose1() {
    String[] splitString = testElection("OPL_tie(2TieChoose1).csv", true);

    assertEquals("Voting Method: Open Party List", splitString[5]);
    assertTrue(splitString[8].equals("Winner(s) of election: Candidate 1 (D)")
            || splitString[8].equals("Winner(s) of election: Candidate 3 (R)"));
    assertEquals("Number of ballots cast: 7", splitString[10]);
  }

  @Test
  public void testOPL_tie3TieChoose1() {
    String[] splitString = testElection("OPL_tie(3TieChoose1).csv", true);

    assertEquals("Voting Method: Open Party List", splitString[5]);
    assertTrue(splitString[8].equals("Winner(s) of election: Candidate 1 (D)")
            || splitString[8].equals("Winner(s) of election: Candidate 3 (R)")
            || splitString[8].equals("Winner(s) of election: Candidate 5 (I)"));
    assertEquals("Number of ballots cast: 9", splitString[10]);
  }

  @Test
  public void testOPL_tie3TieChoose2() {
    String[] splitString = testElection("OPL_tie(3TieChoose2).csv", true);

    String[] winnerSplitString = splitString[8].split(" ");
    String winner1 = winnerSplitString[4];
    String winnerparty1 = winnerSplitString[5];
    String winner2 = winnerSplitString[7];
    String winnerparty2 = winnerSplitString[8];
    assertEquals("Voting Method: Open Party List", splitString[5]);
    assertTrue((winner1.equals("1") && winnerparty1.equals("(D),"))
            || (winner1.equals("3") && winnerparty1.equals("(R),"))
            || (winner1.equals("5") && winnerparty1.equals("(I),")));
    assertTrue((winner2.equals("1") && winnerparty2.equals("(D)"))
            || (winner2.equals("3") && winnerparty2.equals("(R)"))
            || (winner2.equals("5") && winnerparty2.equals("(I)")));
    assertEquals("Number of ballots cast: 9", splitString[10]);
  }

  @Test
  public void test2IRFiles() {
    String[] files = new String[]{
        "IR_given.csv",
        "IR_tie.csv"
    };
    String[] splitString = testMultipleElection(files);
    assertEquals("Voting Method: Instant Runoff", splitString[5]);
    assertEquals("Winner(s) of election: Rosen (D)", splitString[8]);
    assertEquals("Number of ballots cast: 6", splitString[10]);

    assertEquals("Voting Method: Instant Runoff", splitString[15]);
    assertTrue(splitString[18].equals("Winner(s) of election: Candidate Red (Red Party)")
        || splitString[18].equals("Winner(s) of election: Candidate Blue (Blue Party)"));
    assertEquals("Number of ballots cast: 2", splitString[20]);
  }

  @Test
  public void test2OPLFiles() {
    String[] files = new String[]{
        "OPL_given.csv",
        "OPL_tie(2TieChoose1).csv"
    };
    String[] splitString = testMultipleElection(files);

    assertEquals("Voting Method: Open Party List", splitString[5]);
    assertEquals("Winner(s) of election: Borg (R), Pike (D), Foster (D)", splitString[8]);
    assertEquals("Number of ballots cast: 9", splitString[10]);

    assertEquals("Voting Method: Open Party List", splitString[15]);
    assertTrue(splitString[18].equals("Winner(s) of election: Candidate 1 (D)")
        || splitString[18].equals("Winner(s) of election: Candidate 3 (R)"));
    assertEquals("Number of ballots cast: 7", splitString[20]);
  }

  @Test
  public void test2IR_OPLFiles() {
    String[] files = new String[]{
        "IR_given.csv",
        "OPL_given.csv",
    };
    String[] splitString = testMultipleElection(files);
    assertEquals("Voting Method: Instant Runoff", splitString[5]);
    assertEquals("Winner(s) of election: Rosen (D)", splitString[8]);
    assertEquals("Number of ballots cast: 6", splitString[10]);

    assertEquals("Voting Method: Open Party List", splitString[15]);
    assertEquals("Winner(s) of election: Borg (R), Pike (D), Foster (D)", splitString[18]);
    assertEquals("Number of ballots cast: 9", splitString[20]);
  }

  @Test
  public void test3Files() {
    String[] files = new String[]{
        "IR_given.csv",
        "OPL_given.csv",
        "IR_tie.csv"
    };
    String[] splitString = testMultipleElection(files);
    assertEquals("Voting Method: Instant Runoff", splitString[5]);
    assertEquals("Winner(s) of election: Rosen (D)", splitString[8]);
    assertEquals("Number of ballots cast: 6", splitString[10]);

    assertEquals("Voting Method: Open Party List", splitString[15]);
    assertEquals("Winner(s) of election: Borg (R), Pike (D), Foster (D)", splitString[18]);
    assertEquals("Number of ballots cast: 9", splitString[20]);

    assertEquals("Voting Method: Instant Runoff", splitString[25]);
    assertTrue(splitString[28].equals("Winner(s) of election: Candidate Red (Red Party)")
        || splitString[28].equals("Winner(s) of election: Candidate Blue (Blue Party)"));
    assertEquals("Number of ballots cast: 2", splitString[30]);
  }

  @Test
  public void test3FilesWithInvalidateBallots() {
    String[] files = new String[]{
        "IR_given.csv",
        "IR_invalidballots.csv",
        "IR_tie.csv"
    };
    String[] splitString = testMultipleElection(files);
    assertEquals("Voting Method: Instant Runoff", splitString[5]);
    assertEquals("Winner(s) of election: Rosen (D)", splitString[8]);
    assertEquals("Number of ballots cast: 6", splitString[10]);

    assertEquals("Voting Method: Instant Runoff", splitString[15]);
    assertEquals("Winner(s) of election: Candidate Red (Red Party)", splitString[18]);
    assertEquals("Number of ballots cast: 7", splitString[20]);

    assertEquals("Voting Method: Instant Runoff", splitString[25]);
    assertTrue(splitString[28].equals("Winner(s) of election: Candidate Red (Red Party)")
        || splitString[28].equals("Winner(s) of election: Candidate Blue (Blue Party)"));
    assertEquals("Number of ballots cast: 2", splitString[30]);
  }

  @Test
  public void test4FilesWithInvalidateBallots() {
    String[] files = new String[]{
            "IR_given.csv",
            "OPL_given.csv",
            "IR_invalidballots.csv",
            "OPL_tie(2TieChoose1).csv"
    };
    String[] splitString = testMultipleElection(files);
    assertEquals("Voting Method: Instant Runoff", splitString[5]);
    assertEquals("Winner(s) of election: Rosen (D)", splitString[8]);
    assertEquals("Number of ballots cast: 6", splitString[10]);

    assertEquals("Voting Method: Open Party List", splitString[15]);
    assertEquals("Winner(s) of election: Borg (R), Pike (D), Foster (D)", splitString[18]);
    assertEquals("Number of ballots cast: 9", splitString[20]);

    assertEquals("Voting Method: Instant Runoff", splitString[25]);
    assertEquals("Winner(s) of election: Candidate Red (Red Party)", splitString[28]);
    assertEquals("Number of ballots cast: 7", splitString[30]);

    assertEquals("Voting Method: Open Party List", splitString[35]);
    assertTrue(splitString[38].equals("Winner(s) of election: Candidate 1 (D)")
            || splitString[38].equals("Winner(s) of election: Candidate 3 (R)"));
    assertEquals("Number of ballots cast: 7", splitString[40]);
  }

  @Test
  public void testIR_invalidballots() {
    String[] splitString = testElection("IR_invalidballots.csv", false);
    assertEquals(splitString[8], "Winner(s) of election: Candidate Red (Red Party)");
    //assertTrue(splitString[9].equals());
  }
}
