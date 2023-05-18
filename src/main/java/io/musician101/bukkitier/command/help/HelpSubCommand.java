package io.musician101.bukkitier.command.help;

import io.musician101.bukkitier.command.LiteralCommand;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A help command set as an argument.
 */
public final class HelpSubCommand extends AbstractHelpCommand {

    @Nonnull
    private final LiteralCommand root;

    /**
     * @param root   The root {@link LiteralCommand}
     * @param plugin The {@link JavaPlugin} that's registering this command.
     */
    public HelpSubCommand(@Nonnull LiteralCommand root, @Nonnull JavaPlugin plugin) {
        super(plugin);
        this.root = root;
    }

    @Nonnull
    @Override
    public String description() {
        return "Shows the help info for /" + root.name();
    }

    @Nonnull
    @Override
    public String name() {
        return "help";
    }

    @Override
    protected void sendUsage(CommandSender sender) {
        sendUsage(sender, root);
    }

    @Nonnull
    @Override
    public String usage() {
        return "/" + root.name() + " help";
    }
}
