package win.cuteguimc.zombieshelper.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.HUD;
import cc.polyfrost.oneconfig.config.annotations.KeyBind;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import cc.polyfrost.oneconfig.utils.Notifications;
import win.cuteguimc.zombieshelper.ZombiesHelper;
import win.cuteguimc.zombieshelper.hud.ZombiesHelperHud;

public class ZombiesHelperConfig extends Config {
    @Switch(
            name = "Block UseEntity"
    )
    public static boolean blockUseEntity = false;

    @KeyBind(
            name = "Toggle Block UseEntity Keybind"
    )
    public static OneKeyBind toggleBlockUseEntityKeyBind = new OneKeyBind(UKeyboard.KEY_LEFT);

    @Switch(
            name = "Aim Bot",
            description = "Auto Aim"
    )
    public static boolean aimBot = false;

    @KeyBind(
            name = "Toggle Aim Bot"
    )
    public static OneKeyBind toggleAimBotKeybind = new OneKeyBind(UKeyboard.KEY_V);

    @Slider(
            name = "Turn Speed",
            min = 0.1f, max = 50f
    )
    public static float turnSpeed = 1.5f;

    @Slider(
            name = "Predict",
            min = 0f, max = 10f
    )
    public static float predict = 0.5f;

    @Switch(
            name = "Auto Revive",
            description = "Automatically revives players"
    )
    public static boolean autoRevive = false;

    @Switch(
            name = "No Puncher"
    )
    public static boolean noPuncher = true;

    @Switch(
            name = "Hide Player"
    )
    public static boolean hidePlayer = false;

    @Switch(
            name = "ESP"
    )
    public static boolean esp = false;

    @Slider(
            name = "ESP Arrow Radius",
            min = 55f, max = 160f
    )
    public static float espArrowRadius = 70f;

    @Slider(
            name = "ESP Range",
            min = 0f, max = 100f
    )
    public static float espRange = 50f;

    @Switch(
            name = "Auto Send",
            description = "Automatically sends packets when not holding sword or book"
    )
    public static boolean autoSend = false;

    @KeyBind(
            name = "Toggle Auto Send"
    )
    public static OneKeyBind toggleAutoSendKeybind = new OneKeyBind(UKeyboard.KEY_B);

    @HUD(
            name = "Nearby Entity HUD",
            category = "NEHUD"
    )
    public ZombiesHelperHud hud = new ZombiesHelperHud();

    public ZombiesHelperConfig() {
        super(new Mod(ZombiesHelper.NAME, ModType.UTIL_QOL), ZombiesHelper.MODID + ".json");
        registerKeyBind(toggleBlockUseEntityKeyBind, () -> {
            blockUseEntity = !blockUseEntity;
            Notifications.INSTANCE.send("Zombies Helper", "Block UseEntity -> " + (blockUseEntity?"On":"Off"));
        });
        registerKeyBind(toggleAimBotKeybind, () -> {
            aimBot = !aimBot;
            Notifications.INSTANCE.send("Zombies Helper", "Aim Bot -> " + (aimBot?"On":"Off"));
        });
        registerKeyBind(toggleAutoSendKeybind, () -> {
            autoSend = !autoSend;
            Notifications.INSTANCE.send("Zombies Helper", "Auto Send -> " + (autoSend?"On":"Off"));
        });
        initialize();
    }
}

