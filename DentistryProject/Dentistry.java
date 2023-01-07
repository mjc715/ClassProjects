import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
//======================================================================================================================
public class Dentistry {

    //----Declaring final variables
    private static final Scanner keyboard = new Scanner(System.in);
    private static final int ROWS_OF_TEETH = 2;
    private static final int MAX_TEETH = 10;
//----------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {

       //---- Declaring variables for main method
        String uppers;
        String lowers;
        int familyMembers, i, j;
        char[][][] teethArray;
        char optionChar;
        String optionString;
        String[] namesArray;
        boolean proceed = false;

        //---- Printing welcome statement and getting number of family members
        System.out.println("Welcome to Castellucci Tooth Records");
        System.out.println("====================================================");
        System.out.printf("%-50s :", "Please enter the number of people in the family");
        familyMembers = keyboard.nextInt();

        //---- Ensuring # family members is in range
        while (familyMembers < 0 || familyMembers > 6) {
            System.out.printf("%-50s :", "Invalid number, please try again");
            familyMembers = keyboard.nextInt();
        }

        //---- Creating new strings for names and teeth based on number of family members
        namesArray = new String[familyMembers];
        teethArray = new char[familyMembers][ROWS_OF_TEETH][MAX_TEETH];

        //---- Gets teeth inputs using methods getUppers and getLowers
        for (i = 0; i < familyMembers; i++) {
            System.out.printf("%-50s :", "Please enter family member " + (i + 1) + " name");
            namesArray[i] = keyboard.next();
            uppers = getUppers(i, namesArray);
            lowers = getLowers(i, namesArray);

            for (j = 0; j < uppers.length(); j++) {
                teethArray[i][0][j] = uppers.charAt(j);
            }

            for (j = 0; j < lowers.length(); j++) {
                teethArray[i][1][j] = lowers.charAt(j);
            }

        }

        //---- Creates menu selection screen and gets user's option
        while (!proceed) {
            System.out.printf("%-50s :", "(P)rint, (E)xtract, (R)oot, or e(X)it");
            optionString = keyboard.next();
            optionString = optionString.toUpperCase();
            optionChar = optionString.charAt(0);


            switch (optionChar) {
                case 'P':
                    printTeeth(teethArray, namesArray);
                    break;
                case 'E':
                    teethArray = extractTeeth(teethArray, namesArray);
                    break;
                case 'R':
                    rootTeeth(teethArray, familyMembers);
                    break;
                case 'X':
                    proceed = true;
                    System.out.println();
                    System.out.println("Thank you for choosing Castellucci Dentistry!");
                    break;
                default:
                    System.out.printf("%-50s\n", "Invalid character, try again.");
                    break;
            }
        }


    }
//----------------------------------------------------------------------------------------------------------------------
    private static String getUppers(int i, String[] namesArray) {

        String uppers;
        int teethCount;
        int j;

        //---- Gets upper teeth row and converts to chars. Includes error messages
        System.out.printf("%-50s :", "Please enter uppers for " + namesArray[i]);
        uppers = keyboard.next();
        uppers = uppers.toUpperCase();
        teethCount = uppers.length();

        for (j = 0; j < uppers.length(); j++) {
            while (teethCount > 10) {
                System.out.printf("%-50s :", "Too many teeth, try again ");
                uppers = keyboard.next();
                uppers = uppers.toUpperCase();
                teethCount = uppers.length();

                switch (uppers.charAt(j)) {
                    case 'M':
                    case 'B':
                    case 'C':
                        break;
                    default:
                        System.out.printf("%-50s :", "Invalid characters, try again ");
                        uppers = keyboard.next();
                        uppers = uppers.toUpperCase();
                        teethCount = uppers.length();
                        j = 0;
                        break;
                }
            }
            switch (uppers.charAt(j)) {
                case 'M':
                case 'B':
                case 'C':
                    break;
                default:
                    System.out.printf("%-50s :", "Invalid characters, try again ");
                    uppers = keyboard.next();
                    uppers = uppers.toUpperCase();
                    teethCount = uppers.length();
                    j = 0;
                    break;
            }
        }
        //---- Returns upper set of teeth
        return (uppers);
    }
//----------------------------------------------------------------------------------------------------------------------
    private static String getLowers(int i, String[] namesArray) {
        String lowers;
        int teethCount;
        int j;

        //---- Gets upper teeth row and converts to chars. Includes error messages
        System.out.printf("%-50s :", "Please enter lowers for " + namesArray[i]);
        lowers = keyboard.next();
        lowers = lowers.toUpperCase();
        teethCount = lowers.length();

        for (j = 0; j < lowers.length(); j++) {
            while (teethCount > 10) {
                System.out.printf("%-50s :", "Too many teeth, try again ");
                lowers = keyboard.next();
                lowers = lowers.toUpperCase();
                teethCount = lowers.length();

                switch (lowers.charAt(j)) {
                    case 'M':
                    case 'B':
                    case 'C':
                        break;
                    default:
                        System.out.printf("%-50s :", "Invalid characters, try again ");
                        lowers = keyboard.next();
                        lowers = lowers.toUpperCase();
                        teethCount = lowers.length();
                        j = 0;
                        break;
                }
            }
            switch (lowers.charAt(j)) {
                case 'M':
                case 'B':
                case 'C':
                    break;
                default:
                    System.out.printf("%-50s :", "Invalid characters, try again ");
                    lowers = keyboard.next();
                    lowers = lowers.toUpperCase();
                    teethCount = lowers.length();
                    j = 0;
                    break;
            }
        }
        //---- Returns upper set of teeth
        return (lowers);
    }
//----------------------------------------------------------------------------------------------------------------------
    private static void printTeeth(char[][][] teethArray, String[] namesArray) {

        int i, j;

        //---- Prints the teeth for each family member in a formatted way, only includes existing teeth
        for (i = 0; i < namesArray.length; i++) {
            System.out.println(namesArray[i]);
            System.out.printf("%10s :", "Uppers");

            for (j = 0; j < teethArray[i][0].length; j++) {
                if (teethArray[i][0][j] != '\0') {
                    System.out.print("  " + (j + 1) + ":" + teethArray[i][0][j]);
                }
            }

            System.out.println();
            System.out.printf("%10s :", "Lowers");

            for (j = 0; j < teethArray[i][1][j]; j++) {
                System.out.print("  " + (j + 1) + ":" + teethArray[i][1][j]);
            }
            System.out.println();
        }

    }
//----------------------------------------------------------------------------------------------------------------------
    private static char[][][] extractTeeth(char[][][] teethArray, String[] namesArray) {

        String member, rowInput;
        int toothNumber;
        char rowChar;
        int i;
        boolean proceed = false;

        //---- Copying namesArray for use in this method so the original names don't get changed
        String[] namesArrayCopy = namesArray.clone();

        //---- namesArrayCopy and user inputted names both converted to all caps to be able to compare easier
        for (i = 0; i < namesArrayCopy.length; i++) {
            namesArrayCopy[i] = namesArrayCopy[i].toUpperCase();
        }

        System.out.printf("%-50s :", "Which family member?");
        member = keyboard.next();
        member = member.toUpperCase();

        //---- Searches all array entries for member, and outputs error if not found
        while (!Arrays.asList(namesArrayCopy).contains(member)) {

            System.out.printf("%-50s :", "Invalid family member, try again.");
            member = keyboard.next();
            member = member.toUpperCase();

        }

        //---- Gets exact tooth and converts it to missing if possible
        for (i = 0; i < namesArray.length; i++) {
            if (Objects.equals(namesArrayCopy[i], member)) {

                System.out.printf("%-50s :", "(U)pper or (L)ower row?");
                rowInput = keyboard.next();
                rowInput = rowInput.toUpperCase();
                rowChar = rowInput.charAt(0);

                while (!proceed) {

                    switch (rowChar) {
                        case 'U':
                            System.out.printf("%-50s :", "Which tooth number?");
                            toothNumber = keyboard.nextInt();

                            while (toothNumber > 10 || teethArray[i][0][(toothNumber - 1)] == '\0'
                                    || teethArray[i][0][(toothNumber - 1)] == 'M') {

                                System.out.printf("%-50s :", "Invalid tooth, try again");
                                toothNumber = keyboard.nextInt();

                            }
                            teethArray[i][0][(toothNumber - 1)] = 'M';
                            proceed = true;
                            break;
                        case 'L':

                            System.out.printf("%-50s :", "Which tooth number?");
                            toothNumber = keyboard.nextInt();

                            while (toothNumber > 10 || teethArray[i][1][(toothNumber - 1)] == '\0'
                                    || teethArray[i][1][(toothNumber - 1)] == 'M') {

                                System.out.printf("%-50s :", "Invalid tooth, try again");
                                toothNumber = keyboard.nextInt();

                            }
                            teethArray[i][1][(toothNumber - 1)] = 'M';
                            proceed = true;
                            break;
                        default:
                            System.out.printf("%-50s :", "Invalid character, try again");
                            rowInput = keyboard.next();
                            rowInput = rowInput.toUpperCase();
                            rowChar = rowInput.charAt(0);
                            break;
                    }
                }
            }
        }
        //---- Returns edited teethArray
        return (teethArray);
    }
//----------------------------------------------------------------------------------------------------------------------
    private static void rootTeeth(char [][][] teethArray, int familyMembers) {

        int numBiting, numMissing, numChewing, i, j;
        double root1, root2, discriminant;

        numMissing = 0;
        numBiting = 0;
        numChewing = 0;

        //---- For loop checks every tooth for M B or C and increments the corresponding variable
        for (i = 0; i < familyMembers; i++ ) {
            for (j = 0; j < teethArray[i][0].length; j++) {
                switch (teethArray[i][0][j]) {
                    case 'M':
                        ++numMissing;
                        break;
                    case 'B':
                        ++numBiting;
                        break;
                    case 'C':
                        ++numChewing;
                        break;
                    case '\0':
                        break;
                    default:
                        System.out.println("Error in rootTeeth, 1st switch");
                        break;
                }

            }
            for (j = 0; j < teethArray[i][1].length; j++) {
                switch (teethArray[i][1][j]) {
                    case 'M':
                        ++numMissing;
                        break;
                    case 'B':
                        ++numBiting;
                        break;
                    case 'C':
                        ++numChewing;
                        break;
                    case '\0':
                        break;
                    default:
                        System.out.println("Error in rootTeeth, 2nd switch");
                        break;
                }
            }
        }

        //---- a = biting, b = chewing, c = missing
        //---- Quadratic formula calculation with above variables
        discriminant = (Math.pow(numChewing, 2) - (4 * numBiting * -numMissing));


        root1 = (-numChewing + Math.sqrt(discriminant)) / (2 * numBiting);
        root2 = (-numChewing - Math.sqrt(discriminant)) / (2 * numBiting);

        //---- Prints roots
        System.out.printf("%-50s :%.2f\n", "One root canal at", root1);
        System.out.printf("%-50s :%.2f\n", "Another root canal at", root2);
    }
//----------------------------------------------------------------------------------------------------------------------
}
//======================================================================================================================
