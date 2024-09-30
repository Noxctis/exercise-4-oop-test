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
            new DispenserType(10, 150), // Mango Lassi
            new DispenserType(10, 180)  // Fruit Punch
        };
    }

    /**
     * Displays the products and allows the customer to pick one.
     * Uses buttons for product selection.
     * @return The index of the selected product
     */
    public int selectProduct() {
        Object[] options = {"Apple Juice", "Orange Juice", "Mango Lassi", "Fruit Punch", "Exit"};
        int selection = JOptionPane.showOptionDialog(
                null,
                "Select a product:",
                "Juice Machine",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        return selection; // The index corresponds to the product selected
    }

    /**
     * Allows the customer to input the number of items they want to buy.
     * Displays the available stock for the selected product.
     * @param productIndex The index of the selected product
     * @return The number of items selected
     */
    public int selectQuantity(int productIndex) {
        DispenserType dispenser = dispensers[productIndex];
        int availableStock = dispenser.getNoOfItems();
        int quantity = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                String quantityStr = JOptionPane.showInputDialog(null, "There are " + availableStock + " items available. How many would you like to purchase?", "Select Quantity", JOptionPane.PLAIN_MESSAGE);
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

        return quantity;
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

        // Ask for the deposit
        int totalDeposited = getValidPayment("Please deposit " + cost + " cents", cost, "Payment");

        // Accept the payment and return change if necessary
        int change = cashRegister.acceptAmount(totalDeposited, cost);
        dispenser.makeSale(quantity);

        // Display the appropriate message with the change if applicable
        if (change > 0) {
            JOptionPane.showMessageDialog(null, "Thank you! Your change is " + change + " cents.", "Transaction Complete", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Thank you for your purchase!", "Transaction Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Prompts the customer to deposit money.
     * @param message The message to display in the dialog
     * @param cost The total cost to be paid
     * @param title The title of the dialog
     * @return The amount deposited by the customer
     */
    private int getValidPayment(String message, int cost, String title) {
        int amountDeposited = 0;
        boolean validInput = false;

        while (!validInput) {
            String inputStr = JOptionPane.showInputDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            try {
                amountDeposited = Integer.parseInt(inputStr);
                if (amountDeposited >= cost) {
                    validInput = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Please deposit at least " + cost + " cents.", "Insufficient Payment", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
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

            if (productIndex == 4) { // Exit condition
                keepRunning = false;
                JOptionPane.showMessageDialog(null, "Thank you for using the Juice Machine. Goodbye!", "Goodbye", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int quantity = selectQuantity(productIndex); // Ask for the number of items
                sellProduct(productIndex, quantity); // Process product sale based on selection and quantity
            }
        }
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
