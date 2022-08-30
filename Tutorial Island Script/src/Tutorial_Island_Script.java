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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@ScriptManifest(name = "Tutorial Island Completer by CloudSE", gameType = GameType.OS)

public class Tutorial_Island_Script extends LoopScript {

    public final Boolean DEBUG = true;
    public Timer timer = new Timer(60);
    public String status = "";
    public int profit = 0;
    public int experienceGained = 0;
    public int experiencePerChop = 175;
    public float experiencePerSecond = 0;
    public float experiencePerMinute = 0;
    public float experiencePerHour = 0;
    public DiscordMessage discordMessage;
    public DiscordMessageBuilder messageBuilder;
    
}
