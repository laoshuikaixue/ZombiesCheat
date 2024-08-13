package win.cuteguimc.zombieshelper.command;

import win.cuteguimc.zombieshelper.ZombiesHelper;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

/**
 * An example command implementing the Command api of OneConfig.
 * Registered in ExampleMod.java with `CommandManager.INSTANCE.registerCommand(new ExampleCommand());`
 *
 * @see Command
 * @see Main
 * @see ZombiesHelper
 */
@Command(value = ZombiesHelper.MODID, description = "Access the " + ZombiesHelper.NAME + " GUI.")
public class OpenGUICommand {
    @Main
    private void handle() {
        ZombiesHelper.INSTANCE.config.openGui();
    }
}