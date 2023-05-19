package io.musician101.bukkitier.command.help;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.bukkitier.command.Command;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A help command set with a customizable name.
 */
public final class HelpMainCommand extends AbstractHelpCommand {

    @Nonnull
    private final List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments;
    @Nonnull
    private final String name;

    public HelpMainCommand(@Nonnull String name, @Nonnull JavaPlugin plugin, @Nonnull List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments) {
        super(plugin);
        this.name = name;
        this.arguments = arguments;
    }

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return arguments;
    }

    @Nonnull
    @Override
    public String description() {
        return "Shows the help info for /" + name;
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @Nonnull
    @Override
    public String name() {
        return name;
    }

    @Override
    protected void sendUsage(CommandSender sender) {
        sender.sendMessage(usage() + " - " + ChatColor.AQUA + description());
        sendUsage(sender, this);
    }

    @Deprecated(since = "1.3.1", forRemoval = true)
    @Nonnull
    @Override
    public String usage() {
        return "/" + name;
    }
}
