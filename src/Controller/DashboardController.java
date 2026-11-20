package Controller;

import Model.CarManager;
import Model.CustomerManager;

public class DashboardController {

    // Navigate to Car Management
    public void openCarManagement() {
        System.out.println("Opening Car Management...");
    }

    // Navigate to Customer Management
    public void openCustomerManagement() {
        System.out.println("Opening Customer Management...");
    }

    // Navigate to Rental Records
    public void openRentalRecords() {
        System.out.println("Opening Rental Records...");
    }

    // Navigate to Payments
    public void openPayments() {
        System.out.println("Opening Payments...");
    }

    // Navigate to Dashboard
    public void openDashboard() {
        System.out.println("Opening Dashboard...");
    }

    // Logout
    public void logout() {
        System.out.println("Logging out...");
    }

    // ================= DASHBOARD STATISTICS =================

    // returns {totalCars, availableCars, rentedCars, totalCustomers}
    public int[] getDashboardStats() {

        CarManager cars = new CarManager();
        CustomerManager customers = new CustomerManager();

        return new int[] {
            cars.countAllCars(),
            cars.countCarsByStatus("Available"),
            cars.countCarsByStatus("Rented"),
            customers.countAllCustomers()
        };
    }
}