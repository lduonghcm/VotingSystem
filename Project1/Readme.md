# Voting System
## Authors
- Linh Duong (duong172)
- Thomas Haupert (haupe033)
- Henry Huynh (huynh407)
- Roshina Mohamed Rafee (moha0797)

## Description
This is a voting system that supports the open party list and instant runoff election types.

## Compilation and Execution
Navigate to the the `src` folder.  
Run the following commands to compile and execute the program:
```
javac *.java
java ElectionManager
```
If the compilation fails, it may be because JUnit is not set up properly.
In that case, you may skip the compilation of the test classes by only compiling the non-test classes instead:
```
javac Ballot.java; javac Candidate.java; javac Election.java; javac ElectionManager.java; javac InstantRunoff.java; javac OpenPartyList.java; javac Party.java
java ElectionManager
```
Once the program starts, follow the on-screen instructions.

## Testing
### Test Files
After compiling and executing the program as explained above, the display will show:
```
*********** Voting System 1.0 Team #18 ***********
Please enter election file name:
```
To test a file, type in the path of the file to test:
```
../testing/<TESTFILE>.csv
```
where `<TESTFILE>` is the name of the file you want to test.

### Unit Testing
Unit tests are located in `src`. Each unit class is suffixed with "Test", aside from "SystemTest" which is a system test.
Unit testing is done through JUnit.

### System Testing
The system tests located at `SystemTest.java` simply runs each file in the `testing` folder automatically and ensures the output is correct.  
**IMPORTANT**: In order for these tests to execute properly, 
you must open the `SystemTest.java` file and edit the `testingDirectory` string to point to the `testing` folder on your machine.
Remember to recompile again by following the instructions above.

## Documentation
An HTML file that contains the Javadoc is available at `documentation/package-summary.html`.
This file can be opened using a web browser.
