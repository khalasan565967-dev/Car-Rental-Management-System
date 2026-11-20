package View;

import Controller.RentalController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class RentalRecordView extends JFrame {

    private final Color HEADER_COLOR = new Color(88, 112, 148);
    private final Color SIDEBAR_COLOR = new Color(116, 126, 132);
    private final Color BUTTON_COLOR = new Color(88, 112, 148);
    private final Color CARD_COLOR = new Color(218, 218, 218);

    private final RentalController controller = new RentalController();

    // {rental_id, customer, car, rental date, return date, total, status}
    private List<Object[]> rentalRows = new ArrayList<>();

    private JLabel lblTotalRecords = new JLabel("0");
    private JLabel lblOngoing = new JLabel("0");
    private JLabel lblCompleted = new JLabel("0");

    private JTextField txtSearch;
    private JTable rentalTable;
    private DefaultTableModel tableModel;

    public RentalRecordView() {
        setTitle("Car Rental Management System");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        createHeader();
        createSidebar();
        createMainContent();

        refreshData();

        setVisible(true);
    }

    // HEADER

    private void createHeader() {
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

        add(header, BorderLayout.NORTH);
    }

    // SIDEBAR

    private void createSidebar() {
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

        add(sidebar, BorderLayout.WEST);
    }

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

    // MAIN CONTENT

    private void createMainContent() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 25, 25));

        // TITLE + DESCRIPTION

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);

        JLabel heading = new JLabel("Rental Records");
        heading.setFont(new Font("Arial", Font.BOLD, 18));
        heading.setForeground(Color.BLACK);

        JLabel description = new JLabel(
            "View and monitor all current and previous rental transactions"
        );
        description.setFont(new Font("Arial", Font.PLAIN, 12));
        description.setForeground(Color.GRAY);

        titlePanel.add(heading);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(description);

        // STATISTICS CARDS

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(Color.WHITE);

        statsPanel.add(createCard("TOTAL RECORDS", lblTotalRecords));
        statsPanel.add(createCard("ONGOING RENTALS", lblOngoing));
        statsPanel.add(createCard("COMPLETED RENTALS", lblCompleted));

        JPanel topPanel = new JPanel(new BorderLayout(0, 12));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.CENTER);

        // RENTAL HISTORY

        JPanel historyPanel = new JPanel(new BorderLayout(0, 10));
        historyPanel.setBackground(Color.WHITE);

        JLabel historyTitle = new JLabel("Rental History");
        historyTitle.setFont(new Font("Arial", Font.BOLD, 15));

        JButton btnNew = new JButton("NEW RENTAL");
        JButton btnReturn = new JButton("RETURN CAR");
        styleSmallButton(btnNew);
        styleSmallButton(btnReturn);

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 12));
        txtSearch.setPreferredSize(new Dimension(220, 23));
        txtSearch.setToolTipText("Search by Rental ID or Customer Name");

        JButton btnSearch = new JButton("SEARCH");
        btnSearch.setFont(new Font("Arial", Font.BOLD, 9));
        btnSearch.setBackground(BUTTON_COLOR);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setPreferredSize(new Dimension(65, 23));

        btnSearch.addActionListener(e -> searchRental());
        btnNew.addActionListener(e -> newRental());
        btnReturn.addActionListener(e -> returnCar());

        JPanel toolsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        toolsPanel.setBackground(Color.WHITE);
        toolsPanel.add(btnNew);
        toolsPanel.add(btnReturn);
        toolsPanel.add(txtSearch);
        toolsPanel.add(btnSearch);

        JPanel tableTop = new JPanel(new BorderLayout());
        tableTop.setBackground(Color.WHITE);
        tableTop.add(historyTitle, BorderLayout.WEST);
        tableTop.add(toolsPanel, BorderLayout.EAST);

        String[] columns = {
            "Rental ID",
            "Customer Name",
            "Car Model",
            "Rental Date",
            "Return Date",
            "Total Amount",
            "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        rentalTable = new JTable(tableModel);
        rentalTable.setRowHeight(30);
        rentalTable.setFont(new Font("Arial", Font.PLAIN, 12));
        rentalTable.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 12)
        );
        rentalTable.getTableHeader().setBackground(
            new Color(230, 230, 230)
        );
        rentalTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );
        rentalTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(rentalTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        historyPanel.add(tableTop, BorderLayout.NORTH);
        historyPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(historyPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    // ================= LOAD DATA =================

    private void refreshData() {

        rentalRows = controller.getRentalRows();

        tableModel.setRowCount(0);

        int ongoing = 0;
        int completed = 0;

        for (Object[] r : rentalRows) {

            tableModel.addRow(new Object[] {
                r[0],
                r[1],
                r[2],
                r[3],
                r[4],
                "\u20B1" + String.format("%,.2f", (double) r[5]),
                r[6]
            });

            if ("Active".equalsIgnoreCase(String.valueOf(r[6]))) ongoing++;
            if ("Completed".equalsIgnoreCase(String.valueOf(r[6]))) completed++;
        }

        lblTotalRecords.setText(String.valueOf(rentalRows.size()));
        lblOngoing.setText(String.valueOf(ongoing));
        lblCompleted.setText(String.valueOf(completed));
    }

    // ================= NEW RENTAL =================

    private void newRental() {

        List<Object[]> customers = controller.getCustomerRows();
        List<Object[]> cars = controller.getAvailableCarRows();

        if (customers.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "There are no customers yet.\nPlease add a customer first."
            );
            return;
        }

        if (cars.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "There are no available cars right now."
            );
            return;
        }

        JComboBox<String> cbCustomer = new JComboBox<>();
        for (Object[] c : customers) {
            cbCustomer.addItem(c[0] + " - " + c[1]);
        }

        JComboBox<String> cbCar = new JComboBox<>();
        for (Object[] c : cars) {
            cbCar.addItem(
                c[1] + " " + c[2] + " (" + c[3] + ") - \u20B1"
                + String.format("%,.2f", (double) c[4]) + "/day"
            );
        }

        JTextField txtRentalDate = new JTextField(LocalDate.now().toString());
        JTextField txtReturnDate = new JTextField(LocalDate.now().plusDays(1).toString());

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Customer:"));
        form.add(cbCustomer);
        form.add(new JLabel("Car:"));
        form.add(cbCar);
        form.add(new JLabel("Rental Date (yyyy-mm-dd):"));
        form.add(txtRentalDate);
        form.add(new JLabel("Return Date (yyyy-mm-dd):"));
        form.add(txtReturnDate);

        while (true) {

            int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "New Rental",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) return;

            LocalDate rentalDate;
            LocalDate returnDate;

            try {
                rentalDate = LocalDate.parse(txtRentalDate.getText().trim());
                returnDate = LocalDate.parse(txtReturnDate.getText().trim());

            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please enter dates like 2026-11-20 (year-month-day)."
                );
                continue;
            }

            if (returnDate.isBefore(rentalDate)) {
                JOptionPane.showMessageDialog(
                    this,
                    "The return date cannot be before the rental date."
                );
                continue;
            }

            int customerId = (int) customers.get(cbCustomer.getSelectedIndex())[0];
            int carId = (int) cars.get(cbCar.getSelectedIndex())[0];

            boolean saved = controller.createRental(
                customerId,
                carId,
                java.sql.Date.valueOf(rentalDate),
                java.sql.Date.valueOf(returnDate)
            );

            if (saved) {
                JOptionPane.showMessageDialog(this, "Rental created successfully.");
                refreshData();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Could not create the rental.\nThe car may no longer be available.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }

            return;
        }
    }

    // ================= RETURN CAR =================

    private void returnCar() {

        int viewRow = rentalTable.getSelectedRow();

        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a rental to return.");
            return;
        }

        int rentalId = (int) rentalTable.getValueAt(viewRow, 0);
        String status = String.valueOf(rentalTable.getValueAt(viewRow, 6));

        if (!"Active".equalsIgnoreCase(status)) {
            JOptionPane.showMessageDialog(this, "This rental is already completed.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Mark rental #" + rentalId + " as returned?",
            "Return Car",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        if (controller.returnCar(rentalId)) {
            JOptionPane.showMessageDialog(this, "Car returned successfully.");
            refreshData();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Could not return the car.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= SEARCH (unchanged) =================

    private void searchRental() {
        String searchText = txtSearch.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter a Rental ID or Customer Name."
            );
            return;
        }

        for (int i = 0; i < rentalTable.getRowCount(); i++) {
            String rentalID = String.valueOf(
                rentalTable.getValueAt(i, 0)
            ).toLowerCase();

            String customerName = String.valueOf(
                rentalTable.getValueAt(i, 1)
            ).toLowerCase();

            if (rentalID.contains(searchText)
                    || customerName.contains(searchText)) {
                rentalTable.setRowSelectionInterval(i, i);
                rentalTable.scrollRectToVisible(
                    rentalTable.getCellRect(i, 0, true)
                );
                return;
            }
        }

        JOptionPane.showMessageDialog(
            this,
            "No matching rental record found."
        );
    }

    // ================= CARD DESIGN =================

    private JPanel createCard(String title, JLabel lblValue) {

        JPanel card = new JPanel();
        card.setBackground(CARD_COLOR);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(
            BorderFactory.createEmptyBorder(12, 15, 10, 10)
        );

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 12));

        lblValue.setFont(new Font("Arial", Font.BOLD, 25));

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(3));
        card.add(lblValue);

        return card;
    }

    // ================= SMALL BUTTON =================

    private void styleSmallButton(JButton button) {

        button.setFont(new Font("Arial", Font.BOLD, 9));
        button.setFocusPainted(false);
        button.setBackground(new Color(220, 220, 220));
        button.setForeground(Color.BLACK);
        button.setPreferredSize(new Dimension(95, 23));
    }
}