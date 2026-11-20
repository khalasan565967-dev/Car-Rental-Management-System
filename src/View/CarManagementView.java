package View;

import Controller.CarController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CarManagementView extends JFrame {

    private final Color HEADER_COLOR = new Color(88, 112, 148);
    private final Color SIDEBAR_COLOR = new Color(116, 126, 132);
    private final Color CONTENT_COLOR = new Color(29, 29, 29);
    private final Color CARD_COLOR = new Color(218, 218, 218);

    private final CarController controller = new CarController();

    // Full rows from the database: {id, brand, model, plate, type, year, rate, status}
    private List<Object[]> carRows = new ArrayList<>();

    private JLabel lblTotalCars = new JLabel("0");
    private JLabel lblAvailableCars = new JLabel("0");

    private JTable vehicleTable;
    private DefaultTableModel model;
    private JTextField searchField;

    public CarManagementView() {

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
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("Arial", Font.ITALIC, 12));
        subtitle.setBorder(BorderFactory.createEmptyBorder(2, 25, 8, 0));

        header.add(title);
        header.add(subtitle);

        // ================= SIDEBAR =================

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
        btnExit.setFont(new Font("Arial", Font.BOLD, 12));

        JPanel exitPanel = new JPanel(new FlowLayout());
        exitPanel.setBackground(SIDEBAR_COLOR);
        exitPanel.add(btnExit);

        sidebar.add(menuPanel, BorderLayout.NORTH);
        sidebar.add(exitPanel, BorderLayout.SOUTH);

        // ================= MAIN CONTENT =================

        JPanel content = new JPanel(new BorderLayout(0, 15));
        content.setBackground(CONTENT_COLOR);
        content.setBorder(
            BorderFactory.createEmptyBorder(15, 25, 25, 25)
        );

        JLabel sectionTitle = new JLabel("Car Management");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 18));
        sectionTitle.setForeground(Color.WHITE);

        // ================= STATISTICS =================

        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsPanel.setBackground(CONTENT_COLOR);

        statsPanel.add(createCard("TOTAL CARS", lblTotalCars));
        statsPanel.add(createCard("AVAILABLE CARS", lblAvailableCars));

        // ================= VEHICLE RECORDS =================

        JPanel recordsPanel = new JPanel(new BorderLayout(0, 10));
        recordsPanel.setBackground(CONTENT_COLOR);

        JPanel recordsHeader = new JPanel(new BorderLayout());
        recordsHeader.setBackground(CONTENT_COLOR);

        JLabel recordsTitle = new JLabel("Vehicle Records");
        recordsTitle.setForeground(Color.WHITE);
        recordsTitle.setFont(new Font("Arial", Font.BOLD, 15));

        // BUTTONS AND SEARCH

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
        searchField.setToolTipText("Search by vehicle ID, brand, model or plate number");

        toolsPanel.add(btnAdd);
        toolsPanel.add(btnEdit);
        toolsPanel.add(btnDelete);
        toolsPanel.add(searchField);

        recordsHeader.add(recordsTitle, BorderLayout.NORTH);
        recordsHeader.add(toolsPanel, BorderLayout.CENTER);

        // VEHICLE TABLE

        String[] columns = {
            "Vehicle ID",
            "Brand",
            "Model",
            "Plate No.",
            "Status"
        };

        model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        vehicleTable = new JTable(model);
        vehicleTable.setRowHeight(30);
        vehicleTable.setFont(new Font("Arial", Font.PLAIN, 12));
        vehicleTable.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 12)
        );

        vehicleTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );
        vehicleTable.setFillsViewportHeight(true);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        vehicleTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        recordsPanel.add(recordsHeader, BorderLayout.NORTH);
        recordsPanel.add(scrollPane, BorderLayout.CENTER);

        // ================= CONTENT LAYOUT =================

        JPanel topPanel = new JPanel(new BorderLayout(0, 12));
        topPanel.setBackground(CONTENT_COLOR);

        topPanel.add(sectionTitle, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.CENTER);

        content.add(topPanel, BorderLayout.NORTH);
        content.add(recordsPanel, BorderLayout.CENTER);

        // ================= MAIN LAYOUT =================

        JPanel body = new JPanel(new BorderLayout());
        body.add(sidebar, BorderLayout.WEST);
        body.add(content, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);

        // ================= SEARCH FILTER =================

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

        // ================= ADD / EDIT / DELETE =================

        btnAdd.addActionListener(e -> addCar());
        btnEdit.addActionListener(e -> editCar());
        btnDelete.addActionListener(e -> deleteCar());

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

        carRows = controller.getAllCarRows();

        model.setRowCount(0);

        for (Object[] car : carRows) {
            model.addRow(new Object[] {
                car[0],   // Vehicle ID
                car[1],   // Brand
                car[2],   // Model
                car[3],   // Plate No.
                car[7]    // Status
            });
        }

        lblTotalCars.setText(String.valueOf(controller.getTotalCars()));
        lblAvailableCars.setText(String.valueOf(controller.getAvailableCars()));
    }

    // ================= ADD =================

    private void addCar() {

        String[] v = showCarDialog("Add New Vehicle", null);

        if (v == null) return;

        boolean saved = controller.addCar(
            v[0], v[1], v[2], v[3],
            Integer.parseInt(v[4]),
            Double.parseDouble(v[5])
        );

        if (saved) {
            JOptionPane.showMessageDialog(this, "Vehicle added successfully.");
            refreshData();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Could not add the vehicle.\nCheck that the plate number is not already used.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= EDIT =================

    private void editCar() {

        int viewRow = vehicleTable.getSelectedRow();

        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle to edit.");
            return;
        }

        int modelRow = vehicleTable.convertRowIndexToModel(viewRow);
        Object[] car = carRows.get(modelRow);

        String[] v = showCarDialog("Edit Vehicle", car);

        if (v == null) return;

        boolean saved = controller.updateCar(
            (int) car[0],
            v[0], v[1], v[2], v[3],
            Integer.parseInt(v[4]),
            Double.parseDouble(v[5])
        );

        if (saved) {
            JOptionPane.showMessageDialog(this, "Vehicle updated successfully.");
            refreshData();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Could not update the vehicle.\nCheck that the plate number is not already used.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= DELETE =================

    private void deleteCar() {

        int viewRow = vehicleTable.getSelectedRow();

        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle to delete.");
            return;
        }

        int modelRow = vehicleTable.convertRowIndexToModel(viewRow);
        Object[] car = carRows.get(modelRow);

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete " + car[1] + " " + car[2] + " (" + car[3] + ")?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        if (controller.deleteCar((int) car[0])) {
            JOptionPane.showMessageDialog(this, "Vehicle deleted successfully.");
            refreshData();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Could not delete the vehicle.\nIt may have rental records.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= ADD / EDIT FORM =================

    // Returns {brand, model, plate, type, year, rate} or null if cancelled
    private String[] showCarDialog(String dialogTitle, Object[] car) {

        JTextField txtBrand = new JTextField(car == null ? "" : String.valueOf(car[1]));
        JTextField txtModel = new JTextField(car == null ? "" : String.valueOf(car[2]));
        JTextField txtPlate = new JTextField(car == null ? "" : String.valueOf(car[3]));
        JTextField txtType = new JTextField(car == null ? "" : String.valueOf(car[4]));
        JTextField txtYear = new JTextField(car == null ? "" : String.valueOf(car[5]));
        JTextField txtRate = new JTextField(car == null ? "" : String.valueOf(car[6]));

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Brand:"));
        form.add(txtBrand);
        form.add(new JLabel("Model:"));
        form.add(txtModel);
        form.add(new JLabel("Plate Number:"));
        form.add(txtPlate);
        form.add(new JLabel("Car Type:"));
        form.add(txtType);
        form.add(new JLabel("Year:"));
        form.add(txtYear);
        form.add(new JLabel("Daily Rate:"));
        form.add(txtRate);

        while (true) {

            int result = JOptionPane.showConfirmDialog(
                this,
                form,
                dialogTitle,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) return null;

            String brand = txtBrand.getText().trim();
            String modelName = txtModel.getText().trim();
            String plate = txtPlate.getText().trim();
            String type = txtType.getText().trim();
            String year = txtYear.getText().trim();
            String rate = txtRate.getText().trim();

            if (brand.isEmpty() || modelName.isEmpty() || plate.isEmpty()
                    || type.isEmpty() || year.isEmpty() || rate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                continue;
            }

            try {
                int y = Integer.parseInt(year);
                double r = Double.parseDouble(rate);

                if (y < 1900 || y > 2100) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid year.");
                    continue;
                }

                if (r <= 0) {
                    JOptionPane.showMessageDialog(this, "Daily rate must be greater than 0.");
                    continue;
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Year must be a whole number and Daily Rate must be a number."
                );
                continue;
            }

            return new String[] { brand, modelName, plate, type, year, rate };
        }
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