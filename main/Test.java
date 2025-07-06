package fa.training.main;

import fa.training.services.CustomerService;
import fa.training.utils.Validator;

import java.util.List;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CustomerService customerService = new CustomerService(scanner);
        
        System.out.println("=== JPL.L.A203 - Customer Management System ===");
        
        while (true) {
            displayMenu();
            System.out.print("Enter your choice (1-7): ");
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    createCustomerOption(customerService);
                    break;
                case "2":
                    saveCustomersOption(customerService);
                    break;
                case "3":
                    displayAllCustomersOption(customerService);
                    break;
                case "4":
                    searchCustomerOption(customerService, scanner);
                    break;
                case "5":
                    removeCustomerOption(customerService, scanner);
                    break;
                case "6":
                    displayAllCustomersOption(customerService);
                    break;
                case "7":
                    System.out.println("Thank you for using the Customer Management System!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }
    
    /**
     * Displays the main menu
     */
    private static void displayMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("CUSTOMER MANAGEMENT SYSTEM");
        System.out.println("=".repeat(50));
        System.out.println("1. Create Customer Data");
        System.out.println("2. Save Customers to File");
        System.out.println("3. Display All Customers");
        System.out.println("4. Search Customer by Phone");
        System.out.println("5. Remove Customer by Phone");
        System.out.println("6. Display All Customers (from file)");
        System.out.println("7. Exit");
        System.out.println("=".repeat(50));
    }
    
    /**
     * Handles customer creation option
     */
    private static void createCustomerOption(CustomerService customerService) {
        System.out.println("\n=== Option 1: Create Customer Data ===");
        List<String> customers = customerService.createCustomer();
        System.out.println("Created " + customers.size() + " customer(s).");
    }
    
    /**
     * Handles saving customers option
     */
    private static void saveCustomersOption(CustomerService customerService) {
        System.out.println("\n=== Option 2: Save Customers to File ===");
        List<String> customers = customerService.createCustomer();
        if (!customers.isEmpty()) {
            String result = customerService.save(customers);
            System.out.println(result);
        } else {
            System.out.println("No customers to save.");
        }
    }
    
    /**
     * Handles displaying all customers option
     */
    private static void displayAllCustomersOption(CustomerService customerService) {
        System.out.println("\n=== Option 3: Display All Customers ===");
        List<String> customers = customerService.findAll();
        customerService.display(customers);
    }
    
    /**
     * Handles searching customer option
     */
    private static void searchCustomerOption(CustomerService customerService, Scanner scanner) {
        System.out.println("\n=== Option 4: Search Customer by Phone ===");
        System.out.print("Enter phone number to search: ");
        String phone = scanner.nextLine().trim();
        
        if (Validator.isValidPhoneNumber(phone)) {
            List<String> customers = customerService.search(phone);
            if (customers.isEmpty()) {
                System.out.println("No customer found with phone number: " + phone);
            } else {
                System.out.println("Found " + customers.size() + " customer(s):");
                customerService.display(customers);
            }
        } else {
            System.out.println("Invalid phone number format.");
        }
    }
    
    /**
     * Handles removing customer option
     */
    private static void removeCustomerOption(CustomerService customerService, Scanner scanner) {
        System.out.println("\n=== Option 5: Remove Customer by Phone ===");
        System.out.print("Enter phone number to remove: ");
        String phone = scanner.nextLine().trim();
        
        if (Validator.isValidPhoneNumber(phone)) {
            boolean removed = customerService.remove(phone);
            if (removed) {
                System.out.println("Customer with phone number " + phone + " has been removed successfully.");
            } else {
                System.out.println("No customer found with phone number: " + phone);
            }
        } else {
            System.out.println("Invalid phone number format.");
        }
    }
} 