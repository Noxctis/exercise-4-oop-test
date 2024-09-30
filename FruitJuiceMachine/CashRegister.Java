package FruitJuiceMachine;

public class CashRegister {
    private int cashOnHand;

    /**
     * Default constructor. Sets the cash in the register to 500 cents.
     */
    public CashRegister() {
        this.cashOnHand = 500;
    }

    /**
     * Accepts the deposited amount from the customer and returns the change if necessary.
     * @param amountDeposited The amount deposited by the customer
     * @param cost The total cost of the product(s)
     * @return The change to return to the customer
     */
    public int acceptAmount(int amountDeposited, int cost) {
        if (amountDeposited >= cost) {
            cashOnHand += cost;
            return amountDeposited - cost; // Return the change
        }
        return 0;
    }

    /**
     * Shows the current balance in the cash register.
     * @return The current balance
     */
    public int getCurrentBalance() {
        return cashOnHand;
    }
}

