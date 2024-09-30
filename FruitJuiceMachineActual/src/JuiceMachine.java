package FruitJuiceMachine;

import javax.swing.JOptionPane;

public class JuiceMachine {
    private CashRegister cashRegister;
    private DispenserType[] dispensers;

    /**
     * Initializes the Juice Machine with default products and costs.
     */
    public JuiceMachine() {
        cashRegister = new CashRegister();
        dispensers = new DispenserType[]{
            new DispenserType(10, 100), // Apple Juice
            new DispenserType(10, 120), // Orange Juice
            new DispenserType(0, 150),  // Mango Lassi (Out of stock initially for testing)
            new DispenserType(10, 180)  // Fruit Punch
        };
    }

    /**
     * Handles the sale of the selected product and quantity.
     * @param productIndex The index of the selected product
     * @param quantity The number of items selected
     */
    public void sellProduct(int productIndex, int quantity) {
        if (productIndex < 0 || productIndex >= dispensers.length) {
            JOptionPane.showMessageDialog(null, "Invalid selection.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DispenserType dispenser = dispensers[productIndex];
        int cost = dispenser.getCost() * quantity;
        int totalDeposited = 0;
        int attempts = 0;
        boolean successfulTransaction = false;

        // Allow up to two tries to deposit enough money
        while (attempts < 2 && totalDeposited < cost) {
            int deposit = getValidPayment("Please deposit " + (cost - totalDeposited) + " cents", "Payment");
            totalDeposited += deposit;

            if (totalDeposited >= cost) {
                successfulTransaction = true;
                break;
            } else {
                JOptionPane.showMessageDialog(null, "You still need to deposit " + (cost - totalDeposited) + " cents.", "Insufficient Funds", JOptionPane.WARNING_MESSAGE);
            }

            attempts++;
        }

        if (successfulTransaction) {
            // Call acceptAmount from CashRegister and return the change to the customer
            int change = cashRegister.acceptAmount(totalDeposited, cost);
            dispenser.makeSale(quantity);
            if (change > 0) {
                JOptionPane.showMessageDialog(null, "Thank you! Your change is " + change + " cents.", "Transaction Complete", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Thank you for your purchase!", "Transaction Complete", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Transaction canceled. Returning " + totalDeposited + " cents.", "Transaction Canceled", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Prompts the customer to deposit money.
     * Handles null input for canceling the action.
     * @param message The message to display in the dialog
     * @param title The title of the dialog
     * @return The amount deposited by the customer
     */
    private int getValidPayment(String message, String title) {
        int amountDeposited = 0;
        boolean validInput = false;

        while (!validInput) {
            String inputStr = JOptionPane.showInputDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);

            if (inputStr == null) {
                // Handle cancel action
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Thank you for using the Juice Machine. Goodbye!", "Goodbye", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
            } else {
                try {
                    amountDeposited = Integer.parseInt(inputStr);
                    if (amountDeposited > 0) {
                        validInput = true;
                    } else {
                        JOptionPane.showMessageDialog(null, "Please enter a positive amount.", "Invalid Payment", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        return amountDeposited;
    }

    /**
     * Main method that runs the Juice Machine program.
     */
    public void run() {
        boolean keepRunning = true;
        while (keepRunning) {
            int productIndex = selectProduct(); // Show selection and get user input

            if (productIndex == -2) { // Handle exit case
                keepRunning = false;
                JOptionPane.showMessageDialog(null, "Thank you for using the Juice Machine. Goodbye!", "Goodbye", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }

            if (productIndex == -1) { // Handle balance check or out-of-stock case
                continue;
            }

            int quantity = selectQuantity(productIndex); // Ask for the number of items
            if (quantity != -1) { // Process product sale if valid quantity
                sellProduct(productIndex, quantity);
            }
        }
    }

    /**
     * Displays the products and allows the customer to pick one.
     * Adds an option for the owner to check the cash register balance.
     * Uses buttons for product selection.
     * @return The index of the selected product, or -1 if balance check or exit is chosen
     */
    public int selectProduct() {
        Object[] options = {"Apple Juice", "Orange Juice", "Mango Lassi", "Fruit Punch", "Exit", "Check Balance"};
        int selection = JOptionPane.showOptionDialog(
                null,
                "Select a product or check the balance:",
                "Juice Machine",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        // Handle "Exit" or "Check Balance" options
        if (selection == 4) { // Exit
            return -2;  // Special code to indicate exit
        }

        if (selection == 5) { // Check Balance
            int currentBalance = cashRegister.getCurrentBalance();
            JOptionPane.showMessageDialog(null, "The current balance in the register is: " + currentBalance + " cents.", "Cash Register Balance", JOptionPane.INFORMATION_MESSAGE);
            return -1; // Return to product selection
        }

        // Check if the product is out of stock and return -1 to prevent further actions
        if (dispensers[selection].getNoOfItems() == 0) {
            JOptionPane.showMessageDialog(null, "Sorry, the selected product is out of stock!", "Out of Stock", JOptionPane.ERROR_MESSAGE);
            return -1;  // Return to product selection without proceeding to quantity input
        }

        return selection;
    }

    /**
     * Allows the customer to input the number of items they want to buy.
     * Displays the available stock for the selected product.
     * Handles null input for canceling the action.
     * @param productIndex The index of the selected product
     * @return The number of items selected, or -1 if canceled
     */
    public int selectQuantity(int productIndex) {
        DispenserType dispenser = dispensers[productIndex];
        int availableStock = dispenser.getNoOfItems();
        int quantity = -1;
        boolean validInput = false;

        while (!validInput) {
            String quantityStr = JOptionPane.showInputDialog(null, "There are " + availableStock + " items available. How many would you like to purchase?", "Select Quantity", JOptionPane.PLAIN_MESSAGE);

            if (quantityStr == null) {
                // Handle cancel action
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Thank you for using the Juice Machine. Goodbye!", "Goodbye", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
            } else {
                try {
                    quantity = Integer.parseInt(quantityStr);
                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(null, "Please enter a positive number.", "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                    } else if (quantity > availableStock) {
                        JOptionPane.showMessageDialog(null, "Not enough stock available! Please enter a quantity up to " + availableStock, "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                    } else {
                        validInput = true;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        return quantity;
    }

    /**
     * Main entry point for the program.
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        JuiceMachine machine = new JuiceMachine();
        machine.run();
    }
}

