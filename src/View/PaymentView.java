package View;

import Controller.PaymentController;
import Controller.RentalController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class PaymentView extends JFrame {

    private final Color HEADER_COLOR = new Color(88, 112, 148);
    private final Color SIDEBAR_COLOR = new Color(116, 126, 132);
    private final Color CARD_COLOR = new Color(218, 218, 218);
    private final Color BLUE_BUTTON_COLOR = new Color(30, 80, 200);

    private final PaymentController controller = new PaymentController();

    // {payment_id, rental_id, customer, amount, date, method, status}
    private List<Object[]> paymentRows = new ArrayList<>();

    private JTable paymentTable;
    private DefaultTableModel model;
    private JTextField searchField;

    private JLabel lblTotalPayments = new JLabel("\u20B10.00");
    private JLabel lblUnpaid = new JLabel("0");
    private JLabel lblPaid = new JLabel("0");

    public PaymentView() {

        setTitle("Car Rental Management System");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // HEADER

        JPanel header = new JPanel();
        header.setBackground(HEADER_COLOR);
        header.setPreferredSize(new Dimension(1100, 75));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("CAR RENTAL MANAGEMENT SYSTEM");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 23));
        title.setBorder(BorderFactory.createEmptyBorder(10, 25, 0, 0));

        JLabel subtitle = new JLabel(
            "Manage vehicles, customer, reservation and payment in one place"
        );
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("Arial", Font.ITALIC, 12));
        subtitle.setBorder(BorderFactory.createEmptyBorder(2, 25, 8, 0));

        header.add(title);
        header.add(subtitle);

        // SIDEBAR

        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(155, 575));

        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(SIDEBAR_COLOR);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        JLabel menuTitle = new JLabel("MENU");
        menuTitle.setFont(new Font("Arial", Font.BOLD, 15));
        menuTitle.setBorder(
            BorderFactory.createEmptyBorder(15, 12, 12, 0)
        );

        JButton btnDashboard = createMenuButton("Dashboard");
        JButton btnCars = createMenuButton("Car Management");
        JButton btnCustomers = createMenuButton("Customer Info");
        JButton btnRentals = createMenuButton("Rental Records");
        JButton btnPayments = createMenuButton("Payments");

        menuPanel.add(menuTitle);
        menuPanel.add(btnDashboard);
        menuPanel.add(btnCars);
        menuPanel.add(btnCustomers);
        menuPanel.add(btnRentals);
        menuPanel.add(btnPayments);

        JButton btnExit = createMenuButton("EXIT");

        JPanel exitPanel = new JPanel(new FlowLayout());
        exitPanel.setBackground(SIDEBAR_COLOR);
        exitPanel.add(btnExit);

        sidebar.add(menuPanel, BorderLayout.NORTH);
        sidebar.add(exitPanel, BorderLayout.SOUTH);

        // MAIN CONTENT (white, like the wireframe)

        JPanel content = new JPanel(new BorderLayout(0, 15));
        content.setBackground(Color.WHITE);
        content.setBorder(
            BorderFactory.createEmptyBorder(15, 25, 25, 25)
        );

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);

        JLabel sectionTitle = new JLabel("Payment Management");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 18));
        sectionTitle.setForeground(Color.BLACK);

        JLabel description = new JLabel(
            "Manage rental payments, track payment status, and monitor transactions"
        );
        description.setFont(new Font("Arial", Font.PLAIN, 12));
        description.setForeground(Color.GRAY);

        titlePanel.add(sectionTitle);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(description);

        // STATISTICS CARDS

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(Color.WHITE);

        statsPanel.add(createCard(lblTotalPayments, "Total Payments"));
        statsPanel.add(createCard(lblUnpaid, "Unpaid Transactions"));
        statsPanel.add(createCard(lblPaid, "Paid Transaction"));

        JPanel topPanel = new JPanel(new BorderLayout(0, 12));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.CENTER);

        // PAYMENT RECORDS

        JPanel recordsPanel = new JPanel(new BorderLayout(0, 10));
        recordsPanel.setBackground(Color.WHITE);

        JPanel recordsHeader = new JPanel();
        recordsHeader.setLayout(new BoxLayout(recordsHeader, BoxLayout.Y_AXIS));
        recordsHeader.setBackground(Color.WHITE);

        JLabel recordsTitle = new JLabel("Payment Records");
        recordsTitle.setForeground(Color.BLACK);
        recordsTitle.setFont(new Font("Arial", Font.BOLD, 15));
        recordsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator line = new JSeparator();
        line.setForeground(Color.GRAY);
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        line.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Left side: blue button + search field

        JButton btnRecordPayment = new JButton("+ Record New Payments");
        btnRecordPayment.setFont(new Font("Arial", Font.BOLD, 11));
        btnRecordPayment.setBackground(BLUE_BUTTON_COLOR);
        btnRecordPayment.setForeground(Color.WHITE);
        btnRecordPayment.setFocusPainted(false);
        btnRecordPayment.setPreferredSize(new Dimension(170, 25));

        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 12));
        searchField.setPreferredSize(new Dimension(260, 23));
        searchField.setToolTipText("Search payment record via Payment ID");

        JPanel leftTools = new JPanel();
        leftTools.setLayout(new BoxLayout(leftTools, BoxLayout.Y_AXIS));
        leftTools.setBackground(Color.WHITE);

        JPanel recordBtnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        recordBtnRow.setBackground(Color.WHITE);
        recordBtnRow.add(btnRecordPayment);

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        searchRow.setBackground(Color.WHITE);
        searchRow.add(searchField);

        leftTools.add(recordBtnRow);
        leftTools.add(Box.createVerticalStrut(6));
        leftTools.add(searchRow);

        // Right side: View / Edit / Delete

        JButton btnViewDetails = new JButton("VIEW DETAILS");
        JButton btnEditPayment = new JButton("EDIT PAYMENT");
        JButton btnDeletePayment = new JButton("DELETE PAYMENT");

        styleSmallButton(btnViewDetails);
        styleSmallButton(btnEditPayment);
        styleSmallButton(btnDeletePayment);

        JPanel rightTools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightTools.setBackground(Color.WHITE);
        rightTools.add(btnViewDetails);
        rightTools.add(btnEditPayment);
        rightTools.add(btnDeletePayment);

        JPanel toolsPanel = new JPanel(new BorderLayout());
        toolsPanel.setBackground(Color.WHITE);
        toolsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        toolsPanel.add(leftTools, BorderLayout.WEST);
        toolsPanel.add(rightTools, BorderLayout.EAST);

        recordsHeader.add(recordsTitle);
        recordsHeader.add(Box.createVerticalStrut(4));
        recordsHeader.add(line);
        recordsHeader.add(Box.createVerticalStrut(8));
        recordsHeader.add(toolsPanel);

        // TABLE

        String[] columns = {
            "Payment ID",
            "Rental ID",
            "Customer Name",
            "Amount",
            "Payment Date",
            "Method",
            "Status"
        };

        model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        paymentTable = new JTable(model);
        paymentTable.setRowHeight(30);
        paymentTable.setFont(new Font("Arial", Font.PLAIN, 12));
        paymentTable.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 12)
        );
        paymentTable.setBackground(CARD_COLOR);
        paymentTable.setFillsViewportHeight(true);
        paymentTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane = new JScrollPane(paymentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(CARD_COLOR);

        recordsPanel.add(recordsHeader, BorderLayout.NORTH);
        recordsPanel.add(scrollPane, BorderLayout.CENTER);

        // SEARCH FILTER

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        paymentTable.setRowSorter(sorter);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { filter(); }
            public void removeUpdate(DocumentEvent e)  { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }

            private void filter() {
                String text = searchField.getText().trim();
                sorter.setRowFilter(
                    text.isEmpty() ? null
                        : RowFilter.regexFilter("(?i)" + Pattern.quote(text))
                );
            }
        });

        // LAYOUT

        content.add(topPanel, BorderLayout.NORTH);
        content.add(recordsPanel, BorderLayout.CENTER);

        JPanel body = new JPanel(new BorderLayout());
        body.add(sidebar, BorderLayout.WEST);
        body.add(content, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);

        // BUTTON ACTIONS

        btnRecordPayment.addActionListener(e -> recordPayment());
        btnViewDetails.addActionListener(e -> viewDetails());
        btnEditPayment.addActionListener(e -> editPayment());
        btnDeletePayment.addActionListener(e -> deletePayment());

        // NAVIGATION

        btnDashboard.addActionListener(e -> {
            dispose();
            new DashboardView();
        });

        btnCars.addActionListener(e -> {
            dispose();
            new CarManagementView();
        });

        btnCustomers.addActionListener(e -> {
            dispose();
            new CustomerView();
        });

        btnRentals.addActionListener(e -> {
            dispose();
            new RentalRecordView();
        });

        btnPayments.addActionListener(e -> {
            dispose();
            new PaymentView();
        });

        btnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Exit",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginView();
            }
        });

        // LOAD DATA FROM THE DATABASE
        refreshData();

        setVisible(true);
    }

    // ================= LOAD DATA =================

    private void refreshData() {

        paymentRows = controller.getAllPayments();

        model.setRowCount(0);

        for (Object[] p : paymentRows) {
            model.addRow(new Object[] {
                p[0],
                p[1],
                p[2],
                money((double) p[3]),
                dateText(p[4]),
                p[5],
                p[6]
            });
        }

        lblTotalPayments.setText(money(controller.getTotalPaidAmount()));
        lblUnpaid.setText(String.valueOf(controller.getUnpaidCount()));
        lblPaid.setText(String.valueOf(controller.getPaidCount()));
    }

    // ================= RECORD NEW PAYMENT =================

    private void recordPayment() {

        // {id, customer, "Brand Model", rental date, return date, total, status}
        List<Object[]> rentals = new RentalController().getRentalRows();

        if (rentals.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "There are no rentals yet.\nPlease create a rental first."
            );
            return;
        }

        JComboBox<String> cbRental = new JComboBox<>();
        for (Object[] r : rentals) {
            cbRental.addItem(
                "#" + r[0] + " - " + r[1] + " - " + r[2]
                + " (" + money((double) r[5]) + ")"
            );
        }

        JTextField txtAmount = new JTextField(plainAmount((double) rentals.get(0)[5]));

        cbRental.addActionListener(e -> {
            int i = cbRental.getSelectedIndex();
            if (i >= 0) {
                txtAmount.setText(plainAmount((double) rentals.get(i)[5]));
            }
        });

        JComboBox<String> cbMethod = createCombo(
            new String[] { "Cash", "GCash", "Credit Card", "Bank Transfer" }, null
        );

        JComboBox<String> cbStatus = createCombo(
            new String[] { "Paid", "Unpaid" }, null
        );

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Rental:"));
        form.add(cbRental);
        form.add(new JLabel("Amount:"));
        form.add(txtAmount);
        form.add(new JLabel("Payment Method:"));
        form.add(cbMethod);
        form.add(new JLabel("Status:"));
        form.add(cbStatus);

        while (true) {

            int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Record New Payment",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) return;

            Double amount = parseAmount(txtAmount.getText());

            if (amount == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid amount greater than 0."
                );
                continue;
            }

            int rentalId = (int) rentals.get(cbRental.getSelectedIndex())[0];

            boolean saved = controller.addPayment(
                rentalId,
                amount,
                String.valueOf(cbMethod.getSelectedItem()),
                String.valueOf(cbStatus.getSelectedItem())
            );

            if (saved) {
                JOptionPane.showMessageDialog(this, "Payment recorded successfully.");
                refreshData();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Could not record the payment.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }

            return;
        }
    }

    // ================= VIEW DETAILS =================

    private void viewDetails() {

        Object[] p = getSelectedPayment("view");

        if (p == null) return;

        String details =
            "Payment ID:      " + p[0] + "\n"
            + "Rental ID:       " + p[1] + "\n"
            + "Customer:        " + p[2] + "\n"
            + "Amount:          " + money((double) p[3]) + "\n"
            + "Payment Date:    " + dateText(p[4]) + "\n"
            + "Method:          " + p[5] + "\n"
            + "Status:          " + p[6];

        JOptionPane.showMessageDialog(
            this,
            details,
            "Payment Details",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ================= EDIT PAYMENT =================

    private void editPayment() {

        Object[] p = getSelectedPayment("edit");

        if (p == null) return;

        JTextField txtAmount = new JTextField(plainAmount((double) p[3]));

        JComboBox<String> cbMethod = createCombo(
            new String[] { "Cash", "GCash", "Credit Card", "Bank Transfer" },
            String.valueOf(p[5])
        );

        JComboBox<String> cbStatus = createCombo(
            new String[] { "Paid", "Unpaid" },
            String.valueOf(p[6])
        );

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Payment ID:"));
        form.add(new JLabel(String.valueOf(p[0])));
        form.add(new JLabel("Customer:"));
        form.add(new JLabel(String.valueOf(p[2])));
        form.add(new JLabel("Amount:"));
        form.add(txtAmount);
        form.add(new JLabel("Payment Method:"));
        form.add(cbMethod);
        form.add(new JLabel("Status:"));
        form.add(cbStatus);

        while (true) {

            int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Edit Payment",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) return;

            Double amount = parseAmount(txtAmount.getText());

            if (amount == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid amount greater than 0."
                );
                continue;
            }

            boolean saved = controller.updatePayment(
                (int) p[0],
                amount,
                String.valueOf(cbMethod.getSelectedItem()),
                String.valueOf(cbStatus.getSelectedItem())
            );

            if (saved) {
                JOptionPane.showMessageDialog(this, "Payment updated successfully.");
                refreshData();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Could not update the payment.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }

            return;
        }
    }

    // ================= DELETE PAYMENT =================

    private void deletePayment() {

        Object[] p = getSelectedPayment("delete");

        if (p == null) return;

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete payment #" + p[0] + " (" + money((double) p[3]) + ")?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        if (controller.deletePayment((int) p[0])) {
            JOptionPane.showMessageDialog(this, "Payment deleted successfully.");
            refreshData();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Could not delete the payment.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= HELPERS =================

    // Returns the selected payment row, or null (with a message) if none
    private Object[] getSelectedPayment(String action) {

        int viewRow = paymentTable.getSelectedRow();

        if (viewRow < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Please select a payment to " + action + "."
            );
            return null;
        }

        int modelRow = paymentTable.convertRowIndexToModel(viewRow);

        return paymentRows.get(modelRow);
    }

    // Combo box that also contains the current value if it is not in the list
    private JComboBox<String> createCombo(String[] options, String current) {

        JComboBox<String> combo = new JComboBox<>(options);

        if (current != null && !current.isEmpty()) {

            boolean found = false;

            for (String option : options) {
                if (option.equalsIgnoreCase(current)) {
                    combo.setSelectedItem(option);
                    found = true;
                    break;
                }
            }

            if (!found) {
                combo.addItem(current);
                combo.setSelectedItem(current);
            }
        }

        return combo;
    }

    // Returns the amount, or null if invalid
    private Double parseAmount(String text) {

        try {
            double amount = Double.parseDouble(text.trim().replace(",", ""));

            return amount > 0 ? amount : null;

        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String money(double amount) {
        return "\u20B1" + String.format("%,.2f", amount);
    }

    private String plainAmount(double amount) {
        return String.format(Locale.US, "%.2f", amount);
    }

    private String dateText(Object value) {

        if (value == null) return "";

        if (value instanceof Timestamp) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format((Timestamp) value);
        }

        return String.valueOf(value);
    }

    // ================= CARD DESIGN =================

    // value on top, label below, like the wireframe
    private JPanel createCard(JLabel lblValue, String title) {

        JPanel card = new JPanel();
        card.setBackground(CARD_COLOR);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(
            BorderFactory.createEmptyBorder(12, 15, 10, 10)
        );

        lblValue.setFont(new Font("Arial", Font.BOLD, 25));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 12));

        card.add(lblValue);
        card.add(Box.createVerticalStrut(3));
        card.add(lblTitle);

        return card;
    }

    // ================= SIDEBAR BUTTON =================

    private JButton createMenuButton(String text) {

        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setForeground(Color.BLACK);
        button.setBackground(SIDEBAR_COLOR);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(
            BorderFactory.createEmptyBorder(10, 12, 10, 5)
        );

        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setMaximumSize(new Dimension(155, 40));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        return button;
    }

    // ================= SMALL BUTTONS =================

    private void styleSmallButton(JButton button) {

        button.setFont(new Font("Arial", Font.BOLD, 9));
        button.setFocusPainted(false);
        button.setBackground(new Color(220, 220, 220));
        button.setForeground(Color.BLACK);
        button.setPreferredSize(new Dimension(105, 23));
    }
}