package fa.training.services;

import fa.training.entities.Customer;
import fa.training.entities.Order;
import fa.training.utils.Constants;
import fa.training.utils.Validator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerService {
    private Scanner scanner;
    private OrderService orderService;
    
    public CustomerService(Scanner scanner) {
        this.scanner = scanner;
        this.orderService = new OrderService(scanner);
    }
    
    /**
     * Creates customer data with orders, asks user whether to continue or finish
     * @return List of customer data as strings
     */
    public List<String> createCustomer() {
        List<String> customers = new ArrayList<>();
        
        System.out.println("=== Customer Data Entry ===");
        
        while (true) {
            Customer customer = createSingleCustomer();
            if (customer != null) {
                customers.add(convertCustomerToString(customer));
                System.out.println("Customer added successfully!");
            }
            
            System.out.print("\nDo you want to add another customer? (y/n): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("n") || choice.equals("no")) {
                break;
            }
        }
        
        return customers;
    }
    
    /**
     * Creates a single customer with orders
     * @return Customer object or null if creation fails
     */
    private Customer createSingleCustomer() {
        System.out.println("\n--- Creating New Customer ---");
        
        String name = getValidName();
        if (name == null) return null;
        
        String phoneNumber = getValidPhoneNumber();
        if (phoneNumber == null) return null;
        
        String address = getValidAddress();
        if (address == null) return null;
        
        List<Order> orders = createOrders();
        
        return new Customer(name, phoneNumber, address, orders);
    }
    
    /**
     * Gets valid customer name from user input
     * @return valid name or null if invalid
     */
    private String getValidName() {
        while (true) {
            System.out.print("Enter customer name: ");
            String name = scanner.nextLine().trim();
            
            if (Validator.isNotEmpty(name)) {
                return name;
            } else {
                System.out.println("Error: Name cannot be empty.");
                System.out.print("Do you want to try again? (y/n): ");
                String choice = scanner.nextLine().trim().toLowerCase();
                if (!choice.equals("y") && !choice.equals("yes")) {
                    return null;
                }
            }
        }
    }
    
    /**
     * Gets valid phone number from user input
     * @return valid phone number or null if invalid
     */
    private String getValidPhoneNumber() {
        while (true) {
            System.out.print("Enter phone number (10-11 digits): ");
            String phoneNumber = scanner.nextLine().trim();
            
            if (Validator.isValidPhoneNumber(phoneNumber)) {
                return phoneNumber;
            } else {
                System.out.println("Error: Phone number must be 10-11 digits.");
                System.out.print("Do you want to try again? (y/n): ");
                String choice = scanner.nextLine().trim().toLowerCase();
                if (!choice.equals("y") && !choice.equals("yes")) {
                    return null;
                }
            }
        }
    }
    
    /**
     * Gets valid address from user input
     * @return valid address or null if invalid
     */
    private String getValidAddress() {
        while (true) {
            System.out.print("Enter address: ");
            String address = scanner.nextLine().trim();
            
            if (Validator.isNotEmpty(address)) {
                return address;
            } else {
                System.out.println("Error: Address cannot be empty.");
                System.out.print("Do you want to try again? (y/n): ");
                String choice = scanner.nextLine().trim().toLowerCase();
                if (!choice.equals("y") && !choice.equals("yes")) {
                    return null;
                }
            }
        }
    }
    
    /**
     * Creates orders for a customer
     * @return List of orders
     */
    private List<Order> createOrders() {
        List<Order> orders = new ArrayList<>();
        
        System.out.println("\n--- Adding Orders ---");
        
        while (true) {
            Order order = orderService.createOrder();
            if (order != null) {
                orders.add(order);
                System.out.println("Order added successfully!");
            }
            
            System.out.print("Do you want to add another order? (y/n): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("n") || choice.equals("no")) {
                break;
            }
        }
        
        return orders;
    }
    
    /**
     * Converts customer object to string for file storage
     * @param customer the customer to convert
     * @return string representation
     */
    private String convertCustomerToString(Customer customer) {
        StringBuilder sb = new StringBuilder();
        sb.append(customer.getName()).append("|");
        sb.append(customer.getPhoneNumber()).append("|");
        sb.append(customer.getAddress()).append("|");
        
        List<Order> orders = customer.getOrders();
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            sb.append(order.getNumber()).append(",").append(order.getDate().getTime());
            if (i < orders.size() - 1) {
                sb.append(";");
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Saves customer list to file
     * @param customers list of customer data as strings
     * @return success message or error message
     */
    public String save(List<String> customers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.CUSTOMER_FILE, true))) {
            for (String customer : customers) {
                writer.write(customer);
                writer.newLine();
            }
            return "Successfully saved " + customers.size() + " customer(s) to " + Constants.CUSTOMER_FILE;
        } catch (IOException e) {
            return "Error saving customers: " + e.getMessage();
        }
    }
    
    /**
     * Gets all customers from file
     * @return list of customer data as strings
     */
    public List<String> findAll() {
        List<String> customers = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.CUSTOMER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    customers.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading customers: " + e.getMessage());
        }
        
        return customers;
    }
    
    /**
     * Displays customer data in formatted table
     * @param customers list of customer data as strings
     */
    public void display(List<String> customers) {
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }
        
        System.out.println("\n" + Constants.SEPARATOR_LINE);
        System.out.println(Constants.DISPLAY_HEADER);
        System.out.println(Constants.SEPARATOR_LINE);
        
        for (String customerData : customers) {
            Customer customer = parseCustomerFromString(customerData);
            if (customer != null) {
                String orderList = formatOrderList(customer.getOrders());
                System.out.printf(Constants.DISPLAY_FORMAT + "\n", 
                    customer.getName(), 
                    customer.getAddress(), 
                    customer.getPhoneNumber(), 
                    orderList);
            }
        }
        
        System.out.println(Constants.SEPARATOR_LINE);
    }
    
    /**
     * Searches for customer by phone number
     * @param phone phone number to search for
     * @return list of matching customer data as strings
     */
    public List<String> search(String phone) {
        List<String> allCustomers = findAll();
        List<String> matchingCustomers = new ArrayList<>();
        
        for (String customerData : allCustomers) {
            Customer customer = parseCustomerFromString(customerData);
            if (customer != null && customer.getPhoneNumber().equals(phone)) {
                matchingCustomers.add(customerData);
            }
        }
        
        return matchingCustomers;
    }
    
    /**
     * Removes customer by phone number from file
     * @param phone phone number of customer to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean remove(String phone) {
        List<String> allCustomers = findAll();
        List<String> remainingCustomers = new ArrayList<>();
        boolean found = false;
        
        for (String customerData : allCustomers) {
            Customer customer = parseCustomerFromString(customerData);
            if (customer != null && !customer.getPhoneNumber().equals(phone)) {
                remainingCustomers.add(customerData);
            } else if (customer != null && customer.getPhoneNumber().equals(phone)) {
                found = true;
            }
        }
        
        if (!found) {
            return false;
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.CUSTOMER_FILE))) {
            for (String customer : remainingCustomers) {
                writer.write(customer);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error removing customer: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Parses customer from string data
     * @param customerData string representation of customer
     * @return Customer object or null if parsing fails
     */
    private Customer parseCustomerFromString(String customerData) {
        try {
            String[] parts = customerData.split("\\|");
            if (parts.length < 3) {
                return null;
            }
            
            String name = parts[0];
            String phoneNumber = parts[1];
            String address = parts[2];
            
            List<Order> orders = new ArrayList<>();
            if (parts.length > 3 && !parts[3].isEmpty()) {
                String[] orderParts = parts[3].split(";");
                for (String orderPart : orderParts) {
                    String[] orderData = orderPart.split(",");
                    if (orderData.length == 2) {
                        String orderNumber = orderData[0];
                        Date orderDate = new Date(Long.parseLong(orderData[1]));
                        orders.add(new Order(orderNumber, orderDate));
                    }
                }
            }
            
            return new Customer(name, phoneNumber, address, orders);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Formats order list for display
     * @param orders list of orders
     * @return formatted string representation
     */
    private String formatOrderList(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return "No orders";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            sb.append(order.getNumber());
            if (i < orders.size() - 1) {
                sb.append(", ");
            }
        }
        
        return sb.toString();
    }
} 