package fa.training.services;

import fa.training.entities.Order;
import fa.training.utils.Validator;

import java.util.Date;
import java.util.Scanner;

public class OrderService {
    private Scanner scanner;
    
    public OrderService(Scanner scanner) {
        this.scanner = scanner;
    }
    
    /**
     * Creates a new order by getting input from user
     * @return Order object or null if creation fails
     */
    public Order createOrder() {
        System.out.println("\n--- Creating New Order ---");
        
        String orderNumber = getValidOrderNumber();
        if (orderNumber == null) {
            return null;
        }
        
        Date orderDate = new Date(); // Use current date
        
        return new Order(orderNumber, orderDate);
    }
    
    /**
     * Gets valid order number from user input
     * @return valid order number or null if invalid
     */
    private String getValidOrderNumber() {
        while (true) {
            System.out.print("Enter order number (must be exactly 10 characters): ");
            String orderNumber = scanner.nextLine().trim();
            
            if (Validator.isValidOrderNumber(orderNumber)) {
                return orderNumber;
            } else {
                System.out.println("Error: Order number must be exactly 10 characters long.");
                System.out.print("Do you want to try again? (y/n): ");
                String choice = scanner.nextLine().trim().toLowerCase();
                if (!choice.equals("y") && !choice.equals("yes")) {
                    return null;
                }
            }
        }
    }
} 