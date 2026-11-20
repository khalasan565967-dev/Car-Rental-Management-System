package View;

import Controller.CustomerController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CustomerView extends JFrame {

    private final Color HEADER_COLOR = new Color(88, 112, 148);
    private final Color SIDEBAR_COLOR = new Color(116, 126, 132);
    private final Color CONTENT_COLOR = new Color(29, 29, 29);
    private final Color CARD_COLOR = new Color(218, 218, 218);

    private final CustomerController controller = new CustomerController();

    // Full rows from the database:
    // {id, full_name, contact_number, email, address, license_number}
    private List<Object[]> customerRows = new ArrayList<>();

    private JLabel lblTotalCustomers = new JLabel("0");

    private JTable customerTable;
    private DefaultTableModel model;
    private JTextField searchField;

    public CustomerView() {

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

        // MAIN CONTENT

        JPanel content = new JPanel(new BorderLayout(0, 15));
        content.setBackground(CONTENT_COLOR);
        content.setBorder(
            BorderFactory.createEmptyBorder(15, 25, 25, 25)
        );

        JLabel sectionTitle = new JLabel("Customer Management");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 18));
        sectionTitle.setForeground(Color.WHITE);

        // STATISTICS CARDS

        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsPanel.setBackground(CONTENT_COLOR);

        JPanel emptySpace = new JPanel();
        emptySpace.setBackground(CONTENT_COLOR);

        statsPanel.add(createCard("TOTAL CUSTOMERS", lblTotalCustomers));
        statsPanel.add(emptySpace);

        // CUSTOMER RECORDS

        JPanel recordsPanel = new JPanel(new BorderLayout(0, 10));
        recordsPanel.setBackground(CONTENT_COLOR);

        JPanel recordsHeader = new JPanel(new BorderLayout());
        recordsHeader.setBackground(CONTENT_COLOR);

        JLabel recordsTitle = new JLabel("Customer Records");
        recordsTitle.setForeground(Color.WHITE);
        recordsTitle.setFont(new Font("Arial", Font.BOLD, 15));

        JPanel toolsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolsPanel.setBackground(CONTENT_COLOR);

        JButton btnAdd = new JButton("ADD");
        JButton btnEdit = new JButton("EDIT");
        JButton btnDelete = new JButton("DELETE");

        styleSmallButton(btnAdd);
        styleSmallButton(btnEdit);
        styleSmallButton(btnDelete);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(260, 23));
        searchField.setToolTipText("Search by customer ID, name, contact or license number");

        toolsPanel.add(btnAdd);
        toolsPanel.add(btnEdit);
        toolsPanel.add(btnDelete);
        toolsPanel.add(searchField);

        recordsHeader.add(recordsTitle, BorderLayout.NORTH);
        recordsHeader.add(toolsPanel, BorderLayout.CENTER);

        // TABLE

        String[] columns = {
            "Customer ID",
            "Full Name",
            "Contact Number",
            "Email",
            "License Number"
        };

        model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        customerTable = new JTable(model);
        customerTable.setRowHeight(30);
        customerTable.setFont(new Font("Arial", Font.PLAIN, 12));
        customerTable.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 12)
        );

        customerTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );
        customerTable.setFillsViewportHeight(true);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        customerTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        recordsPanel.add(recordsHeader, BorderLayout.NORTH);
        recordsPanel.add(scrollPane, BorderLayout.CENTER);

        // LAYOUT

        JPanel topPanel = new JPanel(new BorderLayout(0, 12));
        topPanel.setBackground(CONTENT_COLOR);

        topPanel.add(sectionTitle, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.CENTER);

        content.add(topPanel, BorderLayout.NORTH);
        content.add(recordsPanel, BorderLayout.CENTER);

        JPanel body = new JPanel(new BorderLayout());
        body.add(sidebar, BorderLayout.WEST);
        body.add(content, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);

        // SEARCH FILTER

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

        // ADD / EDIT / DELETE

        btnAdd.addActionListener(e -> addCustomer());
        btnEdit.addActionListener(e -> editCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());

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

        customerRows = controller.getAllCustomerRows();

        model.setRowCount(0);

        for (Object[] c : customerRows) {
            model.addRow(new Object[] {
                c[0],   // Customer ID
                c[1],   // Full Name
                c[2],   // Contact Number
                c[3],   // Email
                c[5]    // License Number
            });
        }

        lblTotalCustomers.setText(String.valueOf(controller.getTotalCustomers()));
    }

    // ================= ADD =================

    private void addCustomer() {

        String[] v = showCustomerDialog("Add New Customer", null);

        if (v == null) return;

        boolean saved = controller.addCustomer(v[0], v[1], v[2], v[3], v[4]);

        if (saved) {
            JOptionPane.showMessageDialog(this, "Customer added successfully.");
            refreshData();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Could not add the customer.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= EDIT =================

    private void editCustomer() {

        int viewRow = customerTable.getSelectedRow();

        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a customer to edit.");
            return;
        }

        int modelRow = customerTable.convertRowIndexToModel(viewRow);
        Object[] customer = customerRows.get(modelRow);

        String[] v = showCustomerDialog("Edit Customer", customer);

        if (v == null) return;

        boolean saved = controller.updateCustomer(
            (int) customer[0], v[0], v[1], v[2], v[3], v[4]
        );

        if (saved) {
            JOptionPane.showMessageDialog(this, "Customer updated successfully.");
            refreshData();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Could not update the customer.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= DELETE =================

    private void deleteCustomer() {

        int viewRow = customerTable.getSelectedRow();

        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete.");
            return;
        }

        int modelRow = customerTable.convertRowIndexToModel(viewRow);
        Object[] customer = customerRows.get(modelRow);

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete customer " + customer[1] + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        if (controller.deleteCustomer((int) customer[0])) {
            JOptionPane.showMessageDialog(this, "Customer deleted successfully.");
            refreshData();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Could not delete the customer.\nThey may have rental records.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= ADD / EDIT FORM =================

    // Returns {fullName, contactNumber, email, address, licenseNumber}
    // or null if cancelled
    private String[] showCustomerDialog(String dialogTitle, Object[] customer) {

        JTextField txtName = new JTextField(customer == null ? "" : String.valueOf(customer[1]));
        JTextField txtContact = new JTextField(customer == null ? "" : String.valueOf(customer[2]));
        JTextField txtEmail = new JTextField(customer == null ? "" : valueOrEmpty(customer[3]));
        JTextField txtAddress = new JTextField(customer == null ? "" : valueOrEmpty(customer[4]));
        JTextField txtLicense = new JTextField(customer == null ? "" : valueOrEmpty(customer[5]));

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Full Name:"));
        form.add(txtName);
        form.add(new JLabel("Contact Number:"));
        form.add(txtContact);
        form.add(new JLabel("Email (optional):"));
        form.add(txtEmail);
        form.add(new JLabel("Address (optional):"));
        form.add(txtAddress);
        form.add(new JLabel("License Number:"));
        form.add(txtLicense);

        while (true) {

            int result = JOptionPane.showConfirmDialog(
                this,
                form,
                dialogTitle,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) return null;

            String name = txtName.getText().trim();
            String contact = txtContact.getText().trim();
            String email = txtEmail.getText().trim();
            String address = txtAddress.getText().trim();
            String license = txtLicense.getText().trim();

            if (name.isEmpty() || contact.isEmpty() || license.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Full Name, Contact Number and License Number are required."
                );
                continue;
            }

            if (!contact.matches("[0-9+\\-()\\s]{7,20}")) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid contact number."
                );
                continue;
            }

            if (!email.isEmpty() && !email.contains("@")) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid email address."
                );
                continue;
            }

            if (licenseAlreadyUsed(license, customer)) {
                JOptionPane.showMessageDialog(
                    this,
                    "This license number is already registered to another customer."
                );
                continue;
            }

            return new String[] { name, contact, email, address, license };
        }
    }

    // Checks the loaded rows; ignores the customer currently being edited
    private boolean licenseAlreadyUsed(String license, Object[] editing) {

        for (Object[] c : customerRows) {

            if (editing != null && c[0].equals(editing[0])) continue;

            if (c[5] != null && license.equalsIgnoreCase(String.valueOf(c[5]))) {
                return true;
            }
        }

        return false;
    }

    private String valueOrEmpty(Object value) {
        return value == null ? "" : String.valueOf(value);
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

    // ================= SMALL BUTTON =================

    private void styleSmallButton(JButton button) {

        button.setFont(new Font("Arial", Font.BOLD, 9));
        button.setFocusPainted(false);
        button.setBackground(new Color(220, 220, 220));
        button.setForeground(Color.BLACK);
        button.setPreferredSize(new Dimension(65, 23));
    }
}