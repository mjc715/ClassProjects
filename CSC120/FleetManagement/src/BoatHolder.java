import java.util.*;
import java.io.*;
//======================================================================================================================
public class BoatHolder implements Serializable {
//----------------------------------------------------------------------------------------------------------------------
    //---- BoatHolder basically just an object that holds an array of Boats and can modify them
    private ArrayList<Boat> fleet;

    public BoatHolder() {
        fleet = new ArrayList<>();
    }
    public BoatHolder (ArrayList<Boat> boats) {
        this();
        fleet = boats;
    }
    public void displayFleet() {
        int i;

        System.out.println("Fleet report: ");
        for (i = 0; i < fleet.size(); i++) {
            System.out.print(fleet.get(i).toString());
        }
    }
//----------------------------------------------------------------------------------------------------------------------
    public void addBoat(String inputLine) {

        //---- Adds boat using a CSV line and splits it up into components to create a new boat
        String [] attributes = inputLine.split(",");
        Boat boat = new Boat(attributes);
        fleet.add(boat);
        System.out.println("Boat added successfully");

    }
//----------------------------------------------------------------------------------------------------------------------
    public int findBoat(String boatName) {
        int i;
        int boatPosition = -1;
        boolean done = false;

        //---- Compares the inputted name to all names for the boats until one is found
        while (!done) {
            for (i = 0; i < fleet.size(); i++) {
                if (boatName.toUpperCase().equals(fleet.get(i).getName())) {
                    done = true;
                    boatPosition = i;
                }
            }
            if (i == fleet.size() && !done) {
                done = true;
            }

        }
        return(boatPosition);
    }
//----------------------------------------------------------------------------------------------------------------------
    public void removeBoat(int boatNumber) {

        fleet.remove(boatNumber);
    }
//----------------------------------------------------------------------------------------------------------------------
    public void addExpense(double expense, int boatNumber) {

        //---- Gets expense and compares it to the boat's value and determines if it is within the budget
        if ((expense + fleet.get(boatNumber).getExpenses()) > fleet.get(boatNumber).getValue()) {
            System.out.printf("Expense unauthorized, $%6.2f left to spend\n", (fleet.get(boatNumber).getValue() -
                    fleet.get(boatNumber).getExpenses() ));

        } else {
            fleet.get(boatNumber).addExpenses(expense);
            System.out.printf("Expense authorized, $%6.2f spent\n",fleet.get(boatNumber).getExpenses());
        }
    }
//----------------------------------------------------------------------------------------------------------------------
}
//======================================================================================================================
