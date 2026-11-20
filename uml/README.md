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
│   ├── Login.png
│   ├── Register.png
│   ├── Dashboard.png
│   ├── Car Management.png
│   ├── Add New Vehicle.png
│   ├── Customer Management.png
│   ├── Add New Customer.png
│   ├── Rental Records.png
│   ├── Add New Rental.png
│   ├── Payment Managment.png
│   └── Record New Payment.png
│
├── uml/                                 UML diagram
│   ├── class_diagram.png
│   └── class_diagram.pdf
│
└── README.md
```

## How to Run the Project

### Requirements

1. Java JDK 21 or newer.
2. Eclipse IDE (or any Java IDE).
3. MariaDB Server (for example through XAMPP or the MariaDB installer).
4. The MariaDB JDBC driver file (mariadb-java-client .jar).

### Steps

1. Download the project. On the GitHub page of this repository, click Code, then Download ZIP, and extract the ZIP file. You can also clone the repository using Git and the repository link.
2. Start MariaDB.
3. Create a database named car_rental_db.
4. Create the tables the project uses: cars, customers, rentals, payments and users.
5. Open Eclipse. Go to File, then Import, then Existing Projects into Workspace. Select the extracted project folder and click Finish. If the project does not import, create a new Java project in Eclipse and copy the src folder into it.
6. Add the MariaDB driver to the project. Right-click the project, choose Build Path, then Add External Archives, and select the mariadb-java-client .jar file.
7. Open src/database/DataBaseConnection.java and set the connection details. The database URL is jdbc:mariadb://localhost:3306/car_rental_db. Replace the username and password with your own MariaDB username and password.
8. Open src/App/Main.java, right-click it, and choose Run As, then Java Application.
9. The Login screen will appear. Register an account, log in, and start using the system.
