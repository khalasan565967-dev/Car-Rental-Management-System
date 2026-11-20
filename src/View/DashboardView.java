package View;

import Controller.DashboardController;
import Controller.RentalController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

public class DashboardView extends JFrame {

    // COLORS
    private final Color HEADER_COLOR = new Color(88, 112, 148);
    private final Color SIDEBAR_COLOR = new Color(116, 126, 132);
    private final Color CARD_COLOR = new Color(218, 218, 218);
    private final Color TEXT_COLOR = Color.BLACK;

    // STAT LABELS (filled from the database)
    private JLabel lblTotalCars = new JLabel("0");
    private JLabel lblAvailableCars = new JLabel("0");
    private JLabel lblRentedCars = new JLabel("0");
    private JLabel lblTotalCustomers = new JLabel("0");

    // ACTIVE RENTALS TABLE MODEL (filled from the database)
    private DefaultTableModel model;

    public DashboardView() {

        setTitle("Car Rental Management System");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ================= HEADER =================

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
        subtitle.setForeground(new Color(225, 225, 225));
        subtitle.setFont(new Font("Arial", Font.ITALIC, 12));
        subtitle.setBorder(BorderFactory.createEmptyBorder(2, 25, 8, 0));

        header.add(title);
        header.add(subtitle);

        // ================= SIDEBAR =================

        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(155, 575));
        sidebar.setLayout(new BorderLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(SIDEBAR_COLOR);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        JLabel menuTitle = new JLabel("MENU");
        menuTitle.setFont(new Font("Arial", Font.BOLD, 15));
        menuTitle.setForeground(Color.BLACK);
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
        btnExit.setFont(new Font("Arial", Font.BOLD, 12));

        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        exitPanel.setBackground(SIDEBAR_COLOR);
        exitPanel.add(btnExit);

        sidebar.add(menuPanel, BorderLayout.NORTH);
        sidebar.add(exitPanel, BorderLayout.SOUTH);

        // ================= MAIN CONTENT =================

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(Color.WHITE);
        content.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 15, 20)
        );

        // TOP SECTION: CARDS + ACTION BUTTONS

        JPanel topSection = new JPanel(new BorderLayout(20, 0));
        topSection.setBackground(Color.WHITE);
        topSection.setPreferredSize(new Dimension(900, 230));

        // STATISTICS CARDS

        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        cardsPanel.setBackground(Color.WHITE);

        cardsPanel.add(createCard("TOTAL CARS", lblTotalCars));
        cardsPanel.add(createCard("AVAILABLE CARS", lblAvailableCars));
        cardsPanel.add(createCard("CARS RENTED", lblRentedCars));
        cardsPanel.add(createCard("TOTAL CUSTOMER", lblTotalCustomers));

        topSection.add(cardsPanel, BorderLayout.CENTER);

        // QUICK ACTION BUTTONS

        JPanel actionsPanel = new JPanel();
        actionsPanel.setBackground(Color.WHITE);
        actionsPanel.setPreferredSize(new Dimension(240, 220));
        actionsPanel.setLayout(
            new BoxLayout(actionsPanel, BoxLayout.Y_AXIS)
        );

        JButton btnAddVehicle = createActionButton(
            "Add New Vehicle",
            "Register a new vehicle"
        );

        JButton btnRegisterCustomer = createActionButton(
            "Register a Customer",
            "Create a new customer account"
        );

        JButton btnCreateRental = createActionButton(
            "Create new Rental",
            "Create a new rental transaction"
        );

        actionsPanel.add(btnAddVehicle);
        actionsPanel.add(Box.createVerticalStrut(15));
        actionsPanel.add(btnRegisterCustomer);
        actionsPanel.add(Box.createVerticalStrut(15));
        actionsPanel.add(btnCreateRental);

        topSection.add(actionsPanel, BorderLayout.EAST);

        // ================= ACTIVE RENTALS TABLE =================

        JPanel rentalPanel = new JPanel(new BorderLayout(5, 8));
        rentalPanel.setBackground(Color.WHITE);

        JPanel rentalHeading = new JPanel(new BorderLayout());
        rentalHeading.setBackground(Color.WHITE);

        JLabel rentalTitle = new JLabel("Active Rentals");
        rentalTitle.setFont(new Font("Arial", Font.BOLD, 13));

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(250, 25));
        searchField.setToolTipText("Search active rentals");

        rentalHeading.add(rentalTitle, BorderLayout.WEST);
        rentalHeading.add(searchField, BorderLayout.EAST);

        String[] columns = {
            "Rental ID",
            "Brand",
            "Model",
            "Plate Number",
            "Date Taken",
            "Return Date",
            "Status"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable rentalTable = new JTable(model);
        rentalTable.setFont(new Font("Arial", Font.PLAIN, 11));
        rentalTable.setRowHeight(25);
        rentalTable.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 11)
        );

        rentalTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        rentalTable.setFillsViewportHeight(true);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        rentalTable.setRowSorter(sorter);

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

        JScrollPane scrollPane = new JScrollPane(rentalTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(
            new Color(190, 190, 190)
        ));

        rentalPanel.add(rentalHeading, BorderLayout.NORTH);
        rentalPanel.add(scrollPane, BorderLayout.CENTER);

        // ADD SECTIONS TO CONTENT

        content.add(topSection, BorderLayout.NORTH);
        content.add(rentalPanel, BorderLayout.CENTER);

        // ================= MAIN LAYOUT =================

        JPanel body = new JPanel(new BorderLayout());
        body.add(sidebar, BorderLayout.WEST);
        body.add(content, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);

        // ================= NAVIGATION =================

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

        btnAddVehicle.addActionListener(e -> {
            dispose();
            new CarManagementView();
        });

        btnRegisterCustomer.addActionListener(e -> {
            dispose();
            new CustomerView();
        });

        btnCreateRental.addActionListener(e -> {
            dispose();
            new RentalRecordView();
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
        refreshStats();
        loadActiveRentals();

        setVisible(true);
    }

    // ================= LOAD STATISTICS =================

    public void refreshStats() {
        int[] stats = new DashboardController().getDashboardStats();

        lblTotalCars.setText(String.valueOf(stats[0]));
        lblAvailableCars.setText(String.valueOf(stats[1]));
        lblRentedCars.setText(String.valueOf(stats[2]));
        lblTotalCustomers.setText(String.valueOf(stats[3]));
    }

    // ================= LOAD ACTIVE RENTALS =================

    public void loadActiveRentals() {

        List<Object[]> rows = new RentalController().getActiveRentalRows();

        model.setRowCount(0);

        for (Object[] row : rows) {
            model.addRow(row);
        }
    }

    // ================= CARD DESIGN =================

    private JPanel createCard(String title, JLabel lblValue) {

        JPanel card = new JPanel();
        card.setBackground(CARD_COLOR);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 10, 10)
        );

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 12));
        lblTitle.setForeground(TEXT_COLOR);

        lblValue.setFont(new Font("Arial", Font.BOLD, 25));
        lblValue.setForeground(Color.BLACK);

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(3));
        card.add(lblValue);

        return card;
    }

    // ================= SIDEBAR BUTTON DESIGN =================

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

    // ================= QUICK ACTION BUTTON DESIGN =================

    private JButton createActionButton(String title, String description) {

        JButton button = new JButton(
            "<html><b>" + title + "</b><br>" +
            "<font size='2'>" + description + "</font></html>"
        );

        button.setFont(new Font("Arial", Font.PLAIN, 11));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(65, 65, 65));
        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.setBorder(
            BorderFactory.createEmptyBorder(7, 10, 7, 5)
        );

        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(230, 45));
        button.setMaximumSize(new Dimension(230, 45));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        return button;
    }
}