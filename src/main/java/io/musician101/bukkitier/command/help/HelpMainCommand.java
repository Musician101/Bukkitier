package io.musician101.bukkitier.command.help;

import com.mojang.brigadier.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * A help command set with a customizable name.
 */
@Deprecated
public abstract class HelpMainCommand extends AbstractHelpCommand {

    protected HelpMainCommand(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Displays help and plugin info.";
    }

    @Override
    public int execute(@NotNull CommandContext<CommandSender> context) {
        CommandSender sender = context.getSource();
        sender.sendMessage(header());
        arguments().stream().filter(cmd -> cmd.canUse(sender)).forEach(cmd -> sender.sendMessage(commandInfo(this, cmd, sender)));
        return 1;
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/" + name();
    }
}
