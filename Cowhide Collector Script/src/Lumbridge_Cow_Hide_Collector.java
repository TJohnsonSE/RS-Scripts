import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.GameType;
import com.epicbot.api.shared.script.LoopScript;
import com.epicbot.api.shared.script.ScriptManifest;
import com.epicbot.api.shared.util.paint.frame.PaintFrame;
import com.epicbot.api.shared.util.time.Time;
import com.epicbot.api.shared.webwalking.model.RSBank;
import com.epicbot.api.shared.entity.*;
import com.epicbot.api.shared.methods.IBankAPI;
import com.epicbot.api.shared.methods.ICameraAPI;
import com.epicbot.api.shared.methods.IGameAPI;
import com.epicbot.api.shared.methods.IGroundItemsAPI;
import com.epicbot.api.shared.methods.IInventoryAPI;
import com.epicbot.api.shared.methods.ILocalPlayerAPI;
import com.epicbot.api.shared.methods.IWebWalkingAPI;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.model.Tile;
import com.epicbot.api.shared.util.time.Timer;

import java.awt.*;
import java.util.ListIterator;

@ScriptManifest(name = "Lumbridge Cow Hide Collector by CloudSE", gameType = GameType.OS)

public class Lumbridge_Cow_Hide_Collector extends LoopScript {

    // Variables
    public String status = "";
    public int fieldCurrentlyIn = 0;

    public Timer timer = new Timer(60);

    public Area cowFields1 = new Area(3194, 3285, 3210, 3300);
    public Area cowFields1Entrance = new Area(3198, 3283, 3201, 3287);

    public Area cowFields2 = new Area(new Tile(3158, 3317), new Tile(3155, 3345), new Tile(3172, 3341),
            new Tile(3197, 3332),
            new Tile(3208, 3313), new Tile(3192, 3309), new Tile(3175, 3317), new Tile(3164, 3318));

    public Area cowFields2Entrance = new Area(3200, 3284, 3204, 3288);

    // The current local player, meaning you; returns the API reference to your
    // player character
    public ILocalPlayerAPI myPlayer() {

        return getAPIContext().localPlayer();
    }

    public ICameraAPI globalCamera() {
        return getAPIContext().camera();
    }

    public IGameAPI game() {
        return getAPIContext().game();
    }

    /*
     * // The current items lying on the ground; retruns the API reference to all
     * items
     * // lying on the ground
     */
    public IGroundItemsAPI groundItems() {
        return getAPIContext().groundItems();
    }

    // Your inventory; returns the API reference to your inventory
    public IInventoryAPI myInventory() {

        return getAPIContext().inventory();
    }

    // Your bank; returns the API reference to your bank
    public IBankAPI myBank() {
        return getAPIContext().bank();
    }

    // A variable that utilizes several methods to move the player character around
    public IWebWalkingAPI walk() {
        return getAPIContext().webWalking();
    }

    // End variables

    /*
     * Method checks if two names (Strings) are lexicographically equal
     */
    public Boolean nameMatches(String itemName, String expectedName) {

        if (itemName.compareTo(expectedName) == 0) {

            return true;
        }

        return false;
    }

    public void pickUpItem(GroundItem item) {

        if (myPlayer().isInCombat()) {
            initiateCombat();
        }

        // Hover over the item for easy pickup
        item.hover();

        // If the player is not moving, pick up the item
        if (!myPlayer().isMoving() && !myPlayer().isAnimating()) {
            item.interact("Take");
        }

        return;
    }

    /*
     * This method will collect items from the ground; the passed in 'itemID'
     * argument
     * determines what item(s) will be collected.
     * ###Buggy, need to figure out how to be more precise with the clicks to avoid
     * combat###
     */
    public void collectItems(GroundItem item) {

        if (myPlayer().isInCombat()) {
            initiateCombat();
        }

        if (item != null) {

            if (nameMatches(item.getName(), "Cowhide")) {
                setStatus("Picking up cowhide");
                pickUpItem(item);
            }

            else if (nameMatches(item.getName(), "Bones")) {
                setStatus("Picking up bones");
                pickUpItem(item);
            }

        }

    }

    public void initiateCombat() {

        Actor currentCombatTarget = myPlayer().getInteracting();

        setStatus("Fighting " + currentCombatTarget.getName());

        if (myPlayer().isInCombat()) {

            currentCombatTarget.hover();
        }

        return;
    }

    /*
     * // This method will bury all bones in the players inventory; train prayer
     * while
     * // collecting cowhides!
     */
    public void buryBones(int itemID) {

        if (myPlayer().isInCombat()) {
            initiateCombat();
        }

        while (myInventory().contains(itemID)) {
            setStatus("Burying bones!");
            myInventory().getItem(itemID).click();
        }
    }

    /*
     * Drops all items that do not match the itemID argument from the players
     * inventory
     */
    public void dropItems(int itemID) {
        myInventory().dropAllExcept(itemID);
    }

    /*
     * Method checks to see if the player's inventory is full; returns True if full,
     * // and False otherwise
     */
    public boolean inventoryFull() {

        if (myInventory().isFull()) {
            System.out.println("Inventory is full");
            return true;
        }

        else {
            return false;
        }
    }

    // TODO: Figure out how to check for Cowhides in the immediate area, without
    // leaving
    public Boolean checkForItem(java.util.List<GroundItem> groundItems) {

        ListIterator<GroundItem> iterator = groundItems.listIterator(0);

        while (iterator.hasNext()) {
            if (iterator.next().getId() == 1739) {
                return true;
            }
        }

        return false;

    }

    public Boolean inCowFields() {

        if (cowFields1.contains(myPlayer().getLocation())) {
            fieldCurrentlyIn = 1;
            return true;
        }

        else if (cowFields2.contains(myPlayer().getLocation())) {
            fieldCurrentlyIn = 2;
            return true;
        }

        return false;
    }

    /*
     * Method will walk the player to a randomized tile in the Lumbridge cow fields.
     * !!!COMPLETELY WORKING!!!
     */
    public void gotoCowFields(int fieldNumber) {

        Tile randomCowFieldTile = cowFields1.getRandomTile();
        Tile randomCowField2Tile = cowFields2.getRandomTile();

        if (fieldNumber == 1) {

            System.out.println(
                    "Walking to cow field 1 at (" + randomCowFieldTile.getX() + ", " + randomCowFieldTile.getY() + ")");

            setStatus("Walking to cow pasture (1)");

            fieldCurrentlyIn = fieldNumber;

            walk().walkTo(randomCowFieldTile);

        }

        else if (fieldNumber == 2) {
            System.out.println(
                    "Walking to cow field 2 at (" + randomCowField2Tile.getX() + ", " + randomCowField2Tile.getY()
                            + ")");

            setStatus("Walking to cow pasture (2)");

            walk().walkTo(randomCowField2Tile);

            fieldCurrentlyIn = fieldNumber;
        }

    }

    // Will navigate the player to the entrance of whatever cow field they are in
    public void goToFieldEntrance() {

        System.out.print("Walking to field entrance");

        if (fieldCurrentlyIn == 1) {
            walk().walkTo(cowFields1Entrance.getRandomTile());
        }

        else if (fieldCurrentlyIn == 2) {
            walk().walkTo(cowFields2Entrance.getRandomTile());
        }

        return;
    }

    /*
     * This method sends the local player to the location specified for the first
     * argument of the 'walkTo' method call
     * --- second argument is how far off the player is allowed to be from the
     * destination tile
     * 
     * !!!COMPLETELY WORKING!!!
     */
    public void gotoLumbridgeBank() {

        System.out.println("Walking to Lumbridge bank");
        setStatus("Walking to Lumbridge bank");
        walk().walkToBank(RSBank.LUMBRIDGE_TOP);
    }

    /*
     * This method will check if myBank() is open, open it if not, deposit every
     * instance of the passed in
     * 'item' argument, then close the bank.
     * CURRENTLY BANKS ALL ITEMS IN INVENTORY, CAN BE SWITCHED
     * !!!COMPLETELY WORKING!!!
     */
    public void bankAllItems(String... itemName) {

        if (!myBank().isOpen()) {
            myBank().open();
            myBank().depositAll(itemName);
        }

        else {
            myBank().depositAll(itemName);
        }
    }

    // Sets the status to display on the script window
    public void setStatus(String status) {

        this.status = status;

    }

    public Boolean waitForItemSpawn(GroundItem item, int milliseconds) {

        System.out.println("Waiting for " + item + " to spawn");

        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Slept for 5 seconds successfully");

        return (checkForItem(groundItems().getAll()));
    }

    @Override
    public boolean onStart(String... strings) {
        setStatus("Script starting, walking to initial pasture");
        return true;
    }

    @Override
    protected int loop() {

        // Set the camera to nearly maximum height
        globalCamera().setPitch(95);

        // References to the nearest Cowhide and Bones item to the player
        GroundItem cowHide = groundItems().query().named("Cowhide").reachable().results().nearest();
        GroundItem bones = groundItems().query().named("Bones").reachable().results().nearest();

        // If the player character has a destination, turn the camera towards the
        // destination
        if (myPlayer().getDestination() != null) {
            globalCamera().turnTo(myPlayer().getDestination());
        }

        // If the player's inventory is full, send player to the bank in Lumbridge
        if (inventoryFull()) {
            gotoLumbridgeBank(); // Then go to Lumbridge bank
            bankAllItems("Cowhide"); // Deposit all cowhides into the bank
        }

        // If the player's inventory is NOT full, start the collection of items
        else {

            // If the player isn't close to either of the cow fields, set them to walk one
            // or the other
            if (inCowFields() == false && !myInventory().isFull()) {

                if (fieldCurrentlyIn == 0) {
                    double randomChoice = Math.random();

                    if (randomChoice < .5) {
                        gotoCowFields(1);
                    }

                    else {
                        gotoCowFields(2);
                    }
                }

                // If the player was previously in a field and is not depositing items in the
                // bank,
                // return them to the current field
                else if (fieldCurrentlyIn == 1) {
                    gotoCowFields(fieldCurrentlyIn);
                }

                else {
                    gotoCowFields(fieldCurrentlyIn);
                    ;
                }
            }

        }

        // Make sure the player isn't in combat before collecting anything
        if (!myPlayer().isInCombat()) {

            // If there are no cowhides in the immediate area, walk around 3 times checking
            // before
            // moving to another field
            if (checkForItem(groundItems().getAll()) == false && !myInventory().isFull()) {

                switch (fieldCurrentlyIn) {
                    case 1:
                        System.out.println("No cowhides found nearby, going to field 2");
                        setStatus("Walking to field 2");
                        gotoCowFields(2);

                    case 2:
                        System.out.println("No cowhides found nearby, going to field 1");
                        setStatus("Walking to field 1");
                        gotoCowFields(1);
                }

            }

            else if (!myInventory().isFull()) {
                // Start collecting cowhides and bones, while dropping all other items
                globalCamera().turnTo(cowHide.getLocation());
                collectItems(cowHide);
                collectItems(bones);
                buryBones(526);
                dropItems(1739);
            }

            else {
                gotoLumbridgeBank();
                bankAllItems("Cowhide");
            }

        }

        else if (myPlayer().isInCombat()) {
            initiateCombat();
        }

        return 50;

    }

    @Override
    protected void onPaint(Graphics2D g, APIContext ctx) {
        PaintFrame frame = new PaintFrame("Test Script");
        frame.addLine("Action being performed: ", status);
        frame.draw(g, 0, 0, ctx);
    }
}