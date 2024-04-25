package io.musician101.bukkitier.command.help;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.papermc.paper.plugin.configuration.PluginMeta;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

abstract class AbstractHelpCommand implements LiteralCommand {

    @NotNull
    protected final JavaPlugin plugin;

    protected AbstractHelpCommand(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @NotNull
    protected Component commandInfo(@NotNull Command<? extends ArgumentBuilder<CommandSender, ?>> command, @NotNull CommandSender sender) {
        String string = command.usage(sender) + "<newline> <dark_gray>- <gray><click:run_command:" + usage(sender) + " " + command.name() + ">" + command.description(sender);
        return miniMessage().deserialize(string);
    }

    @SuppressWarnings("UnstableApiUsage")
    @NotNull
    protected Component header() {
        PluginMeta meta = plugin.getPluginMeta();
        List<String> authors = meta.getAuthors();
        int last = authors.size() - 1;
        String authorsString = switch (last) {
            case 0 -> authors.get(0);
            case 1 -> String.join(" and ", authors);
            default -> String.join(", and ", String.join(", ", authors.subList(0, last)), authors.get(last));
        };
        String string = "<dark_green>> ===== <green><hover:show_text:'<color:#BDB76B>Developed by " + authorsString + "'>" + meta.getDisplayName() + "<dark_green> ===== <<newline><gold>Click a command for more info.";
        return miniMessage().deserialize(string);
    }
}
