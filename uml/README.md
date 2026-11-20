# Car Rental Management System

## Description

The Car Rental Management System is a Java desktop application that helps a car rental business manage vehicles, customers, rentals and payments in one place. It is built with Java Swing and uses a MariaDB database. The project follows the MVC (Model-View-Controller) pattern.

### Features

- **Dashboard:** live totals for cars, available cars, rented cars and customers, plus a list of active rentals.
- **Car Management:** add, edit, delete and search vehicles.
- **Customer Info:** add, edit, delete and search customers.
- **Rental Records:** create a rental, return a car, and view rental history. The car's status changes automatically between Available and Rented.
- **Payments:** record, view, edit and delete payments, with totals for paid and unpaid transactions.
- **Login and Register:** user accounts to access the system.

## Team Members

| Name | Role |
|------|------|
| Kevin John Halasan | _e.g. Developer_ |
| Jon Marl Kane Sartiga | _e.g. UI Designer_ |
| Shougo Gabriel Aray | _e.g. Database Developer / Tester_ |

## Technologies Used

- Java (JDK 21)
- Java Swing (user interface)
- JDBC with MariaDB (database)
- Eclipse IDE

## Project Structure

```
Car_Rental_Management_System/
├── src/
│   ├── App/
│   │   └── Main.java                    Starts the program
│   │
│   ├── View/                            Screens (Java Swing)
│   │   ├── LoginView.java
│   │   ├── RegisterView.java
│   │   ├── DashboardView.java
│   │   ├── CarManagementView.java
│   │   ├── CustomerView.java
│   │   ├── RentalRecordView.java
│   │   └── PaymentView.java
│   │
│   ├── Controller/                      Connects the screens to the data
│   │   ├── LoginController.java
│   │   ├── DashboardController.java
│   │   ├── CarController.java
│   │   ├── CustomerController.java
│   │   ├── RentalController.java
│   │   └── PaymentController.java
│   │
│   ├── Model/                           Database operations
│   │   ├── UserManager.java
│   │   ├── CarManager.java
│   │   ├── CustomerManager.java
│   │   ├── RentalManager.java
│   │   └── PaymentManager.java
│   │
│   └── database/                        Database connection
│       ├── DataBaseConnection.java
│       └── TestConnection.java
│
├── screenshots/                         Screenshots of every screen
│   ├── 01_Login_Page.png
│   ├── 02_Register_Page.png
│   ├── 03_Dashboard.png
│   ├── 04_Car_Management.png
│   ├── 05_Add_Vehicle_Form.png
│   ├── 06_Customer_Info.png
│   ├── 07_Add_Customer_Form.png
│   ├── 08_Rental_Records.png
│   ├── 09_New_Rental_Form.png
│   ├── 10_Payment_Management.png
│   ├── 11_Record_Payment_Form.png
│   └── 12_Payment_Details.png
│
├── uml/                                 UML diagram
│   ├── class_diagram.png
│   └── class_diagram.pdf
│
├── car_rental_db.sql                    Database file to import
└── README.md
```

## How to Run the Project

### 1. Requirements

- Java JDK 21 or newer
- Eclipse IDE (or any Java IDE)
- MariaDB Server (for example through XAMPP or the MariaDB installer)
- MariaDB JDBC driver (`mariadb-java-client-x.x.x.jar`)

### 2. Download the project

```
git clone <your-repository-link>
```

Or click **Code > Download ZIP** on GitHub and extract it.

### 3. Set up the database

1. Start MariaDB.
2. Create the database:
   ```sql
   CREATE DATABASE car_rental_db;
   ```
3. Import the database file included in this repository:
   ```
   mysql -u root -p car_rental_db < car_rental_db.sql
   ```
   (Or import `car_rental_db.sql` using HeidiSQL or phpMyAdmin.)

### 4. Open the project in Eclipse

1. Go to **File > Import > Existing Projects into Workspace** and select the project folder.
2. Add the MariaDB driver: right-click the project, then **Build Path > Add External Archives**, and choose the `mariadb-java-client` `.jar` file.

### 5. Configure the database connection

Open `src/database/DataBaseConnection.java` and set your own database user and password:

```java
private static final String URL = "jdbc:mariadb://localhost:3306/car_rental_db";
private static final String USER = "your_username";
private static final String PASSWORD = "your_password";
```

### 6. Run

Open `src/App/Main.java`, right-click it, and choose **Run As > Java Application**. The Login screen will appear.

## Screenshots

| Screen | Image |
|--------|-------|
| Login Page | ![Login Page](screenshots/01_Login_Page.png) |
| Register Page | ![Register Page](screenshots/02_Register_Page.png) |
| Dashboard | ![Dashboard](screenshots/03_Dashboard.png) |
| Car Management | ![Car Management](screenshots/04_Car_Management.png) |
| Add Vehicle Form | ![Add Vehicle Form](screenshots/05_Add_Vehicle_Form.png) |
| Customer Info | ![Customer Info](screenshots/06_Customer_Info.png) |
| Add Customer Form | ![Add Customer Form](screenshots/07_Add_Customer_Form.png) |
| Rental Records | ![Rental Records](screenshots/08_Rental_Records.png) |
| New Rental Form | ![New Rental Form](screenshots/09_New_Rental_Form.png) |
| Payment Management | ![Payment Management](screenshots/10_Payment_Management.png) |
| Record Payment Form | ![Record Payment Form](screenshots/11_Record_Payment_Form.png) |
| Payment Details | ![Payment Details](screenshots/12_Payment_Details.png) |

## UML Diagram

![UML Class Diagram](uml/class_diagram.png)

A PDF version is available at [`uml/class_diagram.pdf`](uml/class_diagram.pdf).
