import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.GameType;
import com.epicbot.api.shared.discord.model.DiscordMessage;
import com.epicbot.api.shared.discord.model.DiscordMessage.DiscordMessageBuilder;
import com.epicbot.api.shared.script.LoopScript;
import com.epicbot.api.shared.script.ScriptManifest;
import com.epicbot.api.shared.util.paint.frame.PaintFrame;
import com.epicbot.api.shared.util.time.Time;
import com.epicbot.api.shared.webwalking.model.RSBank;

import com.epicbot.api.shared.entity.*;
import com.epicbot.api.shared.methods.IBankAPI;
import com.epicbot.api.shared.methods.ICameraAPI;
import com.epicbot.api.shared.methods.IDiscordAPI;
import com.epicbot.api.shared.methods.IEquipmentAPI;
import com.epicbot.api.shared.methods.IGameAPI;
import com.epicbot.api.shared.methods.IGrandExchangeAPI;
import com.epicbot.api.shared.methods.IGroundItemsAPI;
import com.epicbot.api.shared.methods.IInventoryAPI;
import com.epicbot.api.shared.methods.ILocalPlayerAPI;
import com.epicbot.api.shared.methods.IMagicAPI;
import com.epicbot.api.shared.methods.IObjectsAPI;
import com.epicbot.api.shared.methods.ISkillsAPI;
import com.epicbot.api.shared.methods.ITabsAPI;
import com.epicbot.api.shared.methods.IWebWalkingAPI;
import com.epicbot.api.shared.methods.IEquipmentAPI.Slot;
import com.epicbot.api.shared.methods.ITabsAPI.Tabs;
import com.epicbot.api.shared.model.Spell;
import com.epicbot.api.shared.model.Tile;
import com.epicbot.api.shared.util.time.Timer;

import java.awt.*;
import java.util.List;
import java.util.ListIterator;

@ScriptManifest(name = "High Alcher by CloudSE", gameType = GameType.OS)

public class High_Alcher extends LoopScript {

    public final Boolean DEBUG = false;
    public Timer timer = new Timer(60);
    public String status = "";
    public int profit = 0;
    public int experienceGained = 0;
    public int experiencePerCast = 65;
    public float experiencePerSecond = 0;
    public float experiencePerMinute = 0;
    public float experiencePerHour = 0;
    public int currentItemGEPrice = 0;
    public int natureRunePrice = 0;
    public DiscordMessage discordMessage;
    public DiscordMessageBuilder messageBuilder;
    public List<ItemWidget> highAlchItemsList;

    /*
     * // The current local player, meaning you; returns the API reference to your
     * // player character
     */
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
     * A reference to all the objects in the scene
     */
    public IObjectsAPI sceneObjects() {
        return getAPIContext().objects();
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

    public IEquipmentAPI equipment() {
        return getAPIContext().equipment();
    }

    public ITabsAPI tabs() {
        return getAPIContext().tabs();
    }

    public IMagicAPI magic() {
        return getAPIContext().magic();
    }

    public IDiscordAPI discord() {
        return getAPIContext().discord();
    }

    public IGrandExchangeAPI grandExchange() {
        return getAPIContext().grandExchange();
    }

    public ISkillsAPI playerSkills() {
        return getAPIContext().skills();
    }

    // End API variables

    // --------------------

    // Woodcutter methods

    // Sets the status to display on the script window
    public void setStatus(String status) {

        this.status = status;

    }

    public void setProfit(int profitAmount) {
        this.profit = profitAmount;
    }

    public void setCurrentItemGEPrice(int exchangePrice) {
        this.currentItemGEPrice = exchangePrice;
    }

    public void setNatureRunePrice(int price) {
        this.natureRunePrice = price;
    }

    public void setDiscordMessage(String item, int profit, float experience) {
        this.discordMessage = DiscordMessage.DiscordMessageBuilder.aDiscordMessage().withUsername("CloudAlthorBOT")
                .withContent("@Jbombjohnson Done high alching " + item + "\nTotal profit: " + profit
                        + "\nTotal experience gained: "
                        + experienceGained)
                .build();
    }

    /*
     * Opens the player's spellbook, if not already opened
     * Works perfectly
     */
    public void openSpellBook() {

        if (!tabs().isOpen(Tabs.MAGIC)) {

            setStatus("Opening spellbook...");
            tabs().open(Tabs.MAGIC);

            if (DEBUG) {
                System.out.println("Finished opening spellbook, exiting openSpellBook() method");
            }

            return;
        }
    }

    /*
     * Equips all necessary items for high-alching
     * Works perfectly...
     */
    public Boolean equipHighAlchEquip() {

        // If a Staff of Fire is not equipped as your weapon, equip it
        if (!equipment().contains(Slot.WEAPON, 1387)) {

            if (DEBUG) {
                System.out.println("You don't have a Staff of Fire equipped.");
            }

            // Open the inventory widget if not already open
            if (!tabs().isOpen(Tabs.INVENTORY)) {
                tabs().open(Tabs.INVENTORY);
            }

            if (myInventory().contains(1387)) {
                myInventory().interactItem("Wield", 1387);

                if (DEBUG) {
                    System.out.println("Equipped Staff of Fire");
                }

                return true;
            }

            else {
                if (DEBUG) {
                    System.out.println("You don't have a Staff of Fire in your inventory");
                }

                return false;
            }

        }

        return true;
    }

    /*
     * Grabs all necessary equipment for high-alching from the bank
     * Works perfectly...
     */
    public void grabEquipmentFromBank() {

        setStatus("Walking to Grand Exchange Bank; in grabEquipmentFromBank()");
        // Walk to the Grand Exchange bank
        walk().walkToBank(RSBank.GRAND_EXCHANGE);

        // Open the bank if it's not already open
        if (!myBank().isOpen()) {
            setStatus("Withdrawing x1 Staff of Fire; in grabEquipmentFromBank()");
            myBank().open();
            myBank().withdraw(1, 1387); // Withdraw one Staff of Fire
            myBank().withdraw(1, "Coins");
            myBank().close();
        }
    }

    /*
     * Grabs all necessary runes for high-alching from the bank if they exist
     * Works perfectly...
     */
    public Boolean grabRunesFromBank() {

        setStatus("Walking to Grand Exchange Bank; in grabRunesFromBank()");

        // Walk to the Grand Exchange bank
        walk().walkToBank(RSBank.GRAND_EXCHANGE);

        // Open the bank if it's not already open
        if (!myBank().isOpen()) {
            myBank().open();
        }

        if (myBank().contains("Nature rune")) {

            myBank().withdrawAll("Nature rune"); // Withdraw all Nature runes
            walk().walkTo(new Tile(3157, 3485));
            return true;
        }

        else {

            setStatus("Buying nature runes from Grand Exchange");

            return false;

        }
    }

    public Boolean checkItemsLeftToAlch() {

        if (!myInventory().onlyContains("Coins", "Nature rune")) {

            if (DEBUG) {
                System.out.println(("There are still items other than coins and nature runes in your inventory"));
            }

            return true;
        }

        return false;
    }

    public List<ItemWidget> generateHighAlchItemsList() {

        List<ItemWidget> inventoryItems = myInventory().getItems();
        ListIterator<ItemWidget> iter = inventoryItems.listIterator(0);

        /*
         * // Iterate through the player's inventory, searching for Coins and Nature
         * runes,
         * // then
         * // remove those from the list of items, because we want to print out only
         * items
         * // that aren't those two
         */
        while (iter.hasNext()) {

            ItemWidget listItem = iter.next();

            if (DEBUG) {

                System.out.println(
                        "There is another element to print, attempting to print now; in generateHighAlchItemsList()");
            }

            if (listItem.getName().compareTo("Coins") == 0) {

                if (DEBUG) {
                    System.out.println("Found " + iter.previous().getName());
                }

                iter.remove();
            }

            else if (listItem.getName().compareTo("Nature rune") == 0) {

                if (DEBUG) {
                    System.out.println("Found " + iter.previous().getName());
                }

                iter.remove();
            }

        }

        // Reset the iterator to the beginning of the list
        iter = inventoryItems.listIterator(0);

        if (DEBUG) {
            // Print out the results of the list after removing the elements
            while (iter.hasNext()) {
                System.out.println("List index " + iter.nextIndex() + ": " + iter.next().getName());
            }

            System.out.println("End of list of items that are not Coins or Nature runes");
        }

        return inventoryItems;
    }

    public float getExperienceOverTime(String timeChoice) {

        long seconds = timer.getElapsed() / 1000;
        long minutes = seconds % 60;
        long hours = minutes % 60;

        long[] timeArray = new long[] { seconds, minutes, hours };

        experiencePerSecond = experienceGained / seconds;
        experiencePerMinute = experiencePerSecond * 60;
        experiencePerHour = experiencePerMinute * 60;

        switch (timeChoice) {

            case ("Second"):
                return experiencePerSecond;
            case ("Minute"):
                return experiencePerMinute;
            case ("Hour"):
                return experiencePerHour;
            default:
                return -1;
        }
    }

    public Boolean highAlchItem(String item) {

        // Select High-Alch spell from spellbook
        magic().cast(Spell.Modern.HIGH_LEVEL_ALCHEMY);

        setStatus("High alching " + item);

        /*
         * If the item is in the player's inventory, declare an ItemWidget variable of
         * the item in the second slot
         * of the inventory. Click on that item, casting High-Alchemy on it. Set the
         * price of that item to the
         * current price of the item in the Grand Exchange. Calculate the profit from
         * casting high-alchemy on the item,
         * and update the value of the 'profit' variable. Then rest for a random period
         * of time between 2 and 3 seconds.
         */
        if (myInventory().contains(item)) {

            ItemWidget targetItem = myInventory().getItem(item);

            targetItem.click();

            experienceGained += experiencePerCast;

            setCurrentItemGEPrice(
                    grandExchange().getItemDetails(targetItem.getUnNotedId()).getCurrentPrice());

            System.out.println("Current price in GE of " + targetItem.getName() + ": " + currentItemGEPrice);

            setProfit(profit + (targetItem.getHighAlchemyPrice() - currentItemGEPrice) - natureRunePrice);

            setDiscordMessage(item, profit, experienceGained);

            Time.sleep(com.epicbot.api.shared.util.Random.nextInt(2000, 3001));

            return true;
        }

        else {

            discord().sendMessage(discordMessage);

            currentItemGEPrice = 0;

            return false;
        }

    }

    @Override
    public boolean onStart(String... strings) {
        setStatus("Script starting");

        return true;
    }

    @Override
    protected int loop() {

        // Reset current GE price of item the player is high-alching
        currentItemGEPrice = 0;

        // Initialize value of Nature runes
        if (natureRunePrice == 0) {
            setNatureRunePrice(grandExchange().getItemDetails(561).getCurrentPrice());

            if (DEBUG) {
                System.out.println("Nature rune price: " + natureRunePrice);
            }
        }

        // Check that all items needed are equipped/in inventory
        if (!equipHighAlchEquip()) {

            // If equipment isn't in the inventory, grab it from the bank, along with runes
            grabEquipmentFromBank();
            equipHighAlchEquip();
        }

        // If your inventory does not contain Nature runes, try to grab them from the
        // bank
        else if (!myInventory().contains("Nature rune")) {

            grabRunesFromBank();

        }

        // Attempt to high-alch any item that is in index 1 of the player's inventory
        else {

            // If there are items left to high-alch in the player's inventory
            if (checkItemsLeftToAlch() == true) {

                // Generate a list of those remaining items
                highAlchItemsList = generateHighAlchItemsList();

            }

            // High-alch all items that are not Coins or Nature runes, until there are no
            // more items in the player's inventory
            if (!highAlchItemsList.isEmpty() && myInventory().contains(highAlchItemsList.get(0).getName())) {

                highAlchItem(highAlchItemsList.get(0).getName());
            }

            else {

                // Send a Discord notification
                if (discordMessage != null) {
                    discord().sendMessage(discordMessage);
                }

                if (DEBUG) {
                    System.out.print("Ran out of items to alch...");
                }

                walk().walkToBank(RSBank.GRAND_EXCHANGE);

                myBank().open();
                myBank().depositAll("Coins");
                myBank().close();

                game().logout();

                return -1;

            }

        }

        return 50;
    }

    @Override
    protected void onPaint(Graphics2D g, APIContext ctx) {
        PaintFrame frame = new PaintFrame("High Alcher by CloudSE");
        frame.addLine("Script run-time: ", Time.getFormattedTime(timer.getElapsed()));
        frame.addLine("Action being performed: ", status);
        frame.addLine("Profit: ", profit);
        frame.addLine("Experience gained: ", experienceGained);
        frame.addLine("XP/hr: ", getExperienceOverTime("Hour"));
        frame.draw(g, 0, 0, ctx);
    }

}
