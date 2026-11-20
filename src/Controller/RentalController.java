package Controller;

import Model.RentalManager;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RentalController {

    private RentalManager rentalManager;

    public RentalController() {
        rentalManager = new RentalManager();
    }

    // Create Rental
    public boolean createRental(int customerId, int carId,
                                Date rentalDate, Date returnDate) {
        return rentalManager.createRental(
            customerId, carId, rentalDate, returnDate
        );
    }

    // Return Car
    public boolean returnCar(int rentalId) {
        return rentalManager.returnCar(rentalId);
    }

    // Display All Rental Records
    public ResultSet getAllRentals() throws SQLException {
        return rentalManager.getAllRentals();
    }

    // Search Rental Records
    public ResultSet searchRental(String keyword) throws SQLException {
        return rentalManager.searchRental(keyword);
    }

    // ================= NEW: USED BY THE VIEWS =================

    // {rental_id, customer name, "Brand Model", rental date, return date,
    //  total amount, status}
    public List<Object[]> getRentalRows() {
        return rentalManager.getRentalRows();
    }

    // {rental_id, brand, model, plate number, date taken, return date, status}
    public List<Object[]> getActiveRentalRows() {
        return rentalManager.getActiveRentalRows();
    }

    // {car_id, brand, model, plate number, daily rate}
    public List<Object[]> getAvailableCarRows() {
        return rentalManager.getAvailableCarRows();
    }

    // {customer_id, full_name}
    public List<Object[]> getCustomerRows() {
        return rentalManager.getCustomerRows();
    }
}