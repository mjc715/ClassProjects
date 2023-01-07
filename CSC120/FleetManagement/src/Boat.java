import java.io.Serializable;

//======================================================================================================================
 public class Boat implements Serializable {

     //---- Boat object just holds all these variables and manipulates them
    private String name, model, boatTypeString;
    private double value, expenses;
    private int year, length;
    private static enum boatType {POWER, SAILING};
//----------------------------------------------------------------------------------------------------------------------
    public Boat() {
        name = "";
        value = 0;
        expenses = 0;
    }
//----------------------------------------------------------------------------------------------------------------------
    public Boat(String [] boatData) {

        //---- Constructor uses an array of Strings to create a boat
        this();
        boatTypeString = boatData[0];
        name = boatData[1];
        year = Integer.parseInt(boatData[2]);
        model = boatData[3];
        length = Integer.parseInt(boatData[4]);
        value = Double.parseDouble(boatData[5]);

    }
 //---------------------------------------------------------------------------------------------------------------------
    public void printAll() {

        //---- Prints all the boat data in a formatted fashion
        System.out.printf("    %8s %-20s %-5d%-10s%2d' Paid: $%10.2f Expenses: $%10.2f\n", boatTypeString, name, year,
                model, length, value, expenses);

        }
//----------------------------------------------------------------------------------------------------------------------
    public String getName() {

        //---- Returns a boat's name in uppercase
        return(name.toUpperCase());
    }
//----------------------------------------------------------------------------------------------------------------------
    public double getExpenses() {
        return(expenses);
    }
//----------------------------------------------------------------------------------------------------------------------
    public double getValue() {
        return(value);
    }
//----------------------------------------------------------------------------------------------------------------------
    public void addExpenses(double expense) {
        expenses += expense;
    }
//----------------------------------------------------------------------------------------------------------------------
    public String toString() {

        //---- Prints all the boat data in a formatted fashion
        return(String.format("    %8s %-20s %-5d%-10s%2d' Paid: $%10.2f Expenses: $%10.2f\n", boatTypeString, name, year,
            model, length, value, expenses));

    }
//----------------------------------------------------------------------------------------------------------------------
 }
//======================================================================================================================


