package io.musician101.bukkitier.command.help;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

abstract class AbstractHelpCommand implements LiteralCommand {

    @Nonnull
    private final JavaPlugin plugin;

    protected AbstractHelpCommand(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final int execute(@Nonnull CommandContext<CommandSender> context) {
        CommandSender sender = context.getSource();
        PluginDescriptionFile pdf = plugin.getDescription();
        String pluginInfo = ChatColor.RESET + plugin.getName() + " v" + pdf.getVersion();
        String authors = ChatColor.GREEN + " by " + ChatColor.RESET + String.join(", ", pdf.getAuthors());
        sender.sendMessage(ChatColor.GREEN + "===== " + pluginInfo + authors + ChatColor.GREEN + " =====");
        sendUsage(context.getSource());
        return 1;
    }

    protected abstract void sendUsage(CommandSender sender);

    protected final void sendUsage(CommandSender sender, Command<? extends ArgumentBuilder<CommandSender, ?>> command) {
        command.arguments().forEach(c -> {
            try {
                if (!c.getClass().getMethod("execute", CommandContext.class).getDeclaringClass().getName().equals(Command.class.getName())) {
                    sender.sendMessage(c.usage(sender) + " " + ChatColor.AQUA + c.description());
                }
            }
            catch (NoSuchMethodException ignored) {

            }

            sendUsage(sender, c);
        });
    }
}
