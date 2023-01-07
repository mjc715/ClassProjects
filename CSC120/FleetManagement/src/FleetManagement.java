import java.io.*;
import java.util.*;
//======================================================================================================================
public class FleetManagement {
    private static final Scanner keyboard = new Scanner(System.in);

    //---- Final variable for where to load and save data from
    private static final String FILE_SAVE_LOCATION = "FleetData.accdb";
//----------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {

        //---- Declaring the fleet variable to hold boat objects
        BoatHolder daFleet;

        boolean proceed = false;
        String inputLine, name;
        double expense;
        int boatPosition = -1;

        //---- This if statement loads data from the csv file if an argument is inputted, otherwise it loads from database
        if (args.length > 0) {
            daFleet = new BoatHolder(inputCSVData(args[0]));
        } else {
            daFleet = inputData();
        }

        //---- Options menu display and selection process
        System.out.println("Welcome to Castellucci Fleet Management");
        System.out.println("---------------------------------------");
        System.out.println();
        daFleet.displayFleet();

        try {
            while (!proceed) {
                System.out.println("(P)rint, (A)dd, (R)emove, (E)xpense, or E(x)it");
                switch (keyboard.next().toUpperCase().charAt(0)) {
                    case 'P':
                        daFleet.displayFleet();
                        System.out.println();
                        break;
                    case 'A':
                        System.out.printf("%-50s:", "Please enter the new boat CSV data");
                        keyboard.nextLine();
                        inputLine = keyboard.nextLine();
                        daFleet.addBoat(inputLine);
                        break;
                    case 'R':
                        System.out.println("Which boat to remove?");
                        keyboard.nextLine();
                        name = keyboard.nextLine();
                        boatPosition = daFleet.findBoat(name.toUpperCase());
                        if (boatPosition == -1) {
                            System.out.println(name + " not found.");
                            break;
                        } else {
                            daFleet.removeBoat(boatPosition);
                            System.out.println("Boat removed successfully");
                            break;
                        }

                    case 'E':
                        System.out.println("Which boat do you want to spend on?");
                        keyboard.nextLine();
                        name = keyboard.nextLine();
                        boatPosition = daFleet.findBoat(name);
                        if (boatPosition == -1) {
                            System.out.println(name + " not found.");
                            break;
                        } else {
                            System.out.println("How much do you want to spend?");
                            expense = keyboard.nextDouble();
                            daFleet.addExpense(expense, boatPosition);
                        }
                        break;
                    case 'X':
                        System.out.println("Exiting Castellucci Fleet Management");
                        saveFleet(daFleet);
                        proceed = true;
                        break;
                    default:
                        System.out.println("Invalid menu option, try again.");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

//----------------------------------------------------------------------------------------------------------------------
    private static ArrayList<Boat> inputCSVData(String fileName) {

        /* Gets strings from the CSV (1 line = 1 string) then splits them by commas and uses that to make boats */

        Scanner fileScanner = null;
        String line;
        ArrayList<Boat> boatList = new ArrayList<>();

         try {
            fileScanner = new Scanner(new FileInputStream(fileName));
            while (fileScanner.hasNextLine()) {
                line = fileScanner.nextLine();

                String[] attributes = line.split(",");
                Boat boat = new Boat(attributes);
                boatList.add(boat);

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (fileScanner != null) {
                fileScanner.close();
            }
        }
        return(boatList);
    }
//----------------------------------------------------------------------------------------------------------------------
    private static void saveFleet(BoatHolder daFleet) {

        ObjectOutputStream toStream = null;

        //---- Saves fleet object to the file save location (database)
        try {
            toStream = new ObjectOutputStream(new FileOutputStream(FILE_SAVE_LOCATION));
            toStream.writeObject(daFleet);

        } catch (IOException e) {
            System.out.println("Error saving 1: " + e.getLocalizedMessage());
        } finally {
            if (toStream != null) {
                try {
                    toStream.close();
                } catch (IOException e) {
                    System.out.println("Error saving 2: " + e.getMessage());
                }
            }
        }

    }
//----------------------------------------------------------------------------------------------------------------------
    private static BoatHolder inputData() {

        ObjectInputStream fromStream = null;
        BoatHolder daFleet = null;

        //---- Inputs data from the database file instead of the CSV argument
            try {
                fromStream = new ObjectInputStream(new FileInputStream(FleetManagement.FILE_SAVE_LOCATION));
                daFleet = (BoatHolder)fromStream.readObject();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                if (fromStream != null) {
                    try {
                        fromStream.close();
                        System.out.println("Saved successfully.");
                    } catch (IOException e) {
                        System.out.println("Could not close " + e.getMessage());
                        return (null);
                    }
                }
            }

        return (daFleet);
    }
}
//======================================================================================================================