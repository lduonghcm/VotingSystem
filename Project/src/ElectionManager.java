import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class for the voting system.
 * ElectionManager.java
 *
 * @author Roshina Mohamed Rafee, Thomas Haupert
 */
public class ElectionManager {
  public static BufferedWriter auditFileWriter;
  public static BufferedWriter mediaFileWriter;
  public static boolean TESTINGMODE = false; // setting true disables invalidation of ballots; used for testing

  /**
   * Main method that will be initially run.
   *
   * @param args arguments passed in will be ignored
   */
  public static void main(String[] args) {
    if (args.length > 0 && args[0] != null && args[0].equals("test")) {
      TESTINGMODE = true;
    } else {
      TESTINGMODE = false;
    }
    System.out.println("*********** Voting System 1.0 Team #18 ***********");
    Scanner sc = new Scanner(System.in);
    System.out.println("To input multiple files, use format file1|file2|...|fileN");
    System.out.println("Please enter election file name(s):");
    String electionFileInput = sc.nextLine();
    System.out.println();

    // Split file names by | and put into list
    String[] electionFileSplit = electionFileInput.split("\\|");
    File[] electionFileList = new File[electionFileSplit.length];

    String auditFileName = null;
    try {
      // Create media and audit files
      File mediaFile = new File("report.txt");
      if (!mediaFile.exists()) {
        mediaFile.createNewFile();
      }
      mediaFileWriter = new BufferedWriter(new FileWriter(mediaFile));

      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("'audit_'MM-dd-yyyy'_'HH.mm.ss'.txt'");
      LocalDateTime localDateTime = LocalDateTime.now();
      auditFileName = dtf.format(localDateTime);
      File auditFile = new File(auditFileName);
      if (!auditFile.exists()) {
        auditFile.createNewFile();
      }
      auditFileWriter = new BufferedWriter(new FileWriter(auditFile));
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Add files to file list
    for (int i = 0; i < electionFileList.length; i++) {
      electionFileList[i] = new File(electionFileSplit[i]);
    }

    // Run election for each election file passed in
    for (int i = 0; i < electionFileList.length; i++) {
      // Check to see if the election file was found. If not, skip it
      if (!electionFileList[i].exists()) {
        System.out.println("Election file " + (i + 1) + " not found.");
      } else {
        if (i > 0) {
          System.out.println();
        }
        System.out.println("Running election file: " + (i + 1) + ".");
        runElectionFile(electionFileList[i]);
      }
    }

    // Finalize media and audit files
    try {
      auditFileWriter.close();
      System.out.printf("An audit file has been created: %s%n", auditFileName);
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      mediaFileWriter.close();
      System.out.printf("A media report has been created: report.txt%n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void runElectionFile(File electionFile) {
    BufferedReader electionReader = null;
    String votingMethod = "";

    try {
      // Open election file for reading
      electionReader = new BufferedReader(new FileReader(electionFile));
      // Read first line of file
      votingMethod = electionReader.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Check first line of file for voting method
    Election votingCounter = null;
    if (votingMethod.equals("IR")) {
      System.out.println("Voting Method: Instant Runoff" + System.lineSeparator()
          + "Running Instant Runoff..." + System.lineSeparator());
      votingCounter = new InstantRunoff(electionReader);
    } else if (votingMethod.equals("OPL")) {
      System.out.println("Voting Method: Open Party List" + System.lineSeparator()
          + "Running Open Party List..." + System.lineSeparator());
      votingCounter = new OpenPartyList(electionReader);
    } else if (votingMethod.equals("PO")) {
      System.out.println("Voting Method: Popularity Only" + System.lineSeparator());
      votingCounter = new PopularityOnly(electionReader);
    } else {
      System.out.println("None.");
      return;
    }

    // Run prepare data method for election
    try {
      votingCounter.prepareData();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Run election and get the winners
    ArrayList<Candidate> winners = votingCounter.runElection();

    // Print information about election to the screen
    System.out.print("Winner(s) of election: ");
    for (int i = 0; i < winners.size(); i++) {
      System.out.print(winners.get(i).getName() + " (" + winners.get(i).getParty().getName() + ")");
      if (i != winners.size() - 1) {
        System.out.print(", ");
      }
    }
    System.out.println();
    System.out.printf("Type of election: %s%n", votingMethod);
    System.out.printf("Number of ballots cast: %d%n", votingCounter.getTotalVotes());
    System.out.printf("Number of valid ballots: %d%n",
        (votingCounter.getTotalVotes() - votingCounter.getNumInvalidBallots()));
    System.out.println();
  }
}

