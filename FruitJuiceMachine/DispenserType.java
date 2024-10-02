/*
============================================================================
FILE : DispenserType.java
AUTHOR : Sid Andre Bordario
DESCRIPTION : Handles the number of stock of the items
COPYRIGHT : created 08/28/2024
REVISION HISTORY:
============================================================================
*/
public class DispenserType {
    private int numberOfItems;
    private int cost;

    /**
     * Default constructor. Sets the dispenser to default cost of 50 cents and 50 items.
     */
    public DispenserType() {
        this.numberOfItems = 50;
        this.cost = 50;
    }
//test ni cyril csosaifusaodfysoarysoyaosdfasfasdkfjas;dlfkjasd;flkajsdf;lkasdjf;alskfja;sdlkfjasdl;fkajsf;dlkj
    /**
     * Constructor with parameters. Sets the number of items and cost to specified values.
     * @param setNoOfItems Initial number of items in the dispenser
     * @param setCost The cost of the product
     */
    public DispenserType(int setNoOfItems, int setCost) {
        this.numberOfItems = setNoOfItems;
        this.cost = setCost;
    }

    /**
     * Returns the number of items left in the dispenser.
     * @return The number of items available
     */
    public int getNoOfItems() {
        return numberOfItems;
    }

    /**
     * Returns the cost of the item.
     * @return The cost of the item
     */
    public int getCost() {
        return cost;
    }

    /**
     * Decreases the number of items by the given quantity (for one sale).
     * @param quantity The number of items sold
     */
    public void makeSale(int quantity) {
        if (numberOfItems >= quantity) {
            numberOfItems -= quantity;
        } else {
            throw new IllegalArgumentException("Not enough items in stock!");
        }
    }
}
