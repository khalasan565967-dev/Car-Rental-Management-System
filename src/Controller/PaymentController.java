package Controller;

import Model.PaymentManager;

import java.util.ArrayList;

public class PaymentController {

    private final PaymentManager paymentManager = new PaymentManager();

    // Add Payment
    public boolean addPayment(int rentalId, double amount,
                              String paymentMethod, String paymentStatus) {
        try {
            return paymentManager.addPayment(
                rentalId, amount, paymentMethod, paymentStatus
            );
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update Payment (amount, method and status)
    public boolean updatePayment(int paymentId, double amount,
                                 String paymentMethod, String paymentStatus) {
        return paymentManager.updatePayment(
            paymentId, amount, paymentMethod, paymentStatus
        );
    }

    // Update Payment Status only
    public boolean updatePaymentStatus(int paymentId, String status) {
        try {
            return paymentManager.updatePaymentStatus(paymentId, status);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete Payment
    public boolean deletePayment(int paymentId) {
        return paymentManager.deletePayment(paymentId);
    }

    // {payment_id, rental_id, customer name, amount (Double),
    //  payment_date (Timestamp), payment_method, payment_status}
    public ArrayList<Object[]> getAllPayments() {
        try {
            return paymentManager.getAllPayments();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<Object[]> searchPayment(String keyword) {
        try {
            return paymentManager.searchPayment(keyword);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<Object[]> getPaymentsByRental(int rentalId) {
        try {
            return paymentManager.getPaymentsByRental(rentalId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ================= SUMMARY CARDS =================

    // Total money received (payments marked Paid)
    public double getTotalPaidAmount() {
        return paymentManager.sumAmountByStatus("Paid");
    }

    public int getPaidCount() {
        return paymentManager.countByStatus("Paid");
    }

    public int getUnpaidCount() {
        return paymentManager.countByStatus("Unpaid");
    }
}