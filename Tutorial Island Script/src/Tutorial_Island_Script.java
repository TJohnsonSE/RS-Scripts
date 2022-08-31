import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.GameType;
import com.epicbot.api.shared.discord.model.DiscordMessage;
import com.epicbot.api.shared.discord.model.DiscordMessage.DiscordMessageBuilder;
import com.epicbot.api.shared.script.LoopScript;
import com.epicbot.api.shared.script.ScriptManifest;
import com.epicbot.api.shared.util.paint.frame.PaintFrame;
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
import com.epicbot.api.shared.methods.INPCsAPI;
import com.epicbot.api.shared.methods.IObjectsAPI;
import com.epicbot.api.shared.methods.ISkillsAPI;
import com.epicbot.api.shared.methods.ITabsAPI;
import com.epicbot.api.shared.methods.IWebWalkingAPI;
import com.epicbot.api.shared.methods.IEquipmentAPI.Slot;
import com.epicbot.api.shared.methods.ITabsAPI.Tabs;
import com.epicbot.api.shared.model.Spell;
import com.epicbot.api.shared.model.Tile;
import com.epicbot.api.shared.util.time.Timer;
import com.epicbot.api.shared.util.time.Time;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@ScriptManifest(name = "Tutorial Island Completer by CloudSE", gameType = GameType.OS)

public class Tutorial_Island_Script extends LoopScript {

    public final Boolean DEBUG = true;
    public Timer timer = new Timer(60);
    public String status = "";
    public int experienceGained = 0;
    public Boolean gielenorGuideDone = false;
    public DiscordMessage discordMessage;
    public DiscordMessageBuilder messageBuilder;

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

    /*
     * API to interact with NPC's around the player character
     */
    public INPCsAPI npcs() {
        return getAPIContext().npcs();
    }

    // End API variables

    // --------------------

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean completeGielinorGuide(List<NPC> npcsList) {

        if (DEBUG) {
            System.out.println(npcsList.toString());
        }

        NPC gielinorGuide = npcs().query().named("Gielinor Guide").reachable().results().first();
        
        if(gielinorGuide.canReach(getAPIContext())){

            gielinorGuide.click();
        }

        return true;
    }

    @Override
    public boolean onStart(String... strings) {
        setStatus("Script starting");

        return true;
    }

    @Override
    protected int loop() {

        List<NPC> npcsList = npcs().getAll();

        if (gielenorGuideDone == false) {
            setStatus("Gielenor guide");

            if (completeGielinorGuide(npcsList)) {

                gielenorGuideDone = true;
            }
        }

        return 50;
    }

    @Override
    protected void onPaint(Graphics2D g, APIContext ctx) {
        PaintFrame frame = new PaintFrame("High Alcher by CloudSE");
        frame.addLine("Script run-time: ", Time.getFormattedTime(timer.getElapsed()));
        frame.addLine("Action being performed: ", status);
        frame.addLine("Experience gained: ", experienceGained);
        frame.draw(g, 0, 0, ctx);
    }

}
