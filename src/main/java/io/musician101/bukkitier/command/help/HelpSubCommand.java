package io.musician101.bukkitier.command.help;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.bukkitier.command.LiteralCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * A help command set as an argument.
 */
public abstract class HelpSubCommand extends AbstractHelpCommand {

    @NotNull
    private final LiteralCommand root;

    /**
     * @param root   The root {@link LiteralCommand}
     * @param plugin The {@link JavaPlugin} that's registering this command.
     */
    public HelpSubCommand(@NotNull LiteralCommand root, @NotNull JavaPlugin plugin) {
        super(plugin);
        this.root = root;
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Shows the help info for /" + root.name();
    }

    @Override
    public int execute(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
        if (root instanceof HelpMainCommand) {
            return root.execute(context);
        }

        CommandSender sender = context.getSource();
        sender.sendMessage(header());
        sender.sendMessage(commandInfo(root, sender));
        arguments().stream().filter(cmd -> cmd.canUse(sender)).forEach(cmd -> sender.sendMessage(commandInfo(cmd, sender)));
        return 1;
    }

    @NotNull
    @Override
    public String name() {
        return "help";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return root.usage(sender) + " help [<command>]";
    }
}
