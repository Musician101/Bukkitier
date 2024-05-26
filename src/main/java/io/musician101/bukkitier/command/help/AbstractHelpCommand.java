package io.musician101.bukkitier.command.help;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.bukkitier.tagresolver.TagResolvers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Deprecated
public abstract class AbstractHelpCommand implements LiteralCommand {

    @NotNull
    protected final JavaPlugin plugin;

    protected AbstractHelpCommand(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @NotNull
    protected Component commandInfo(@NotNull Command<? extends ArgumentBuilder<CommandSender, ?>> root, @NotNull Command<? extends ArgumentBuilder<CommandSender, ?>> command, @NotNull CommandSender sender) {
        return MiniMessage.miniMessage().deserialize("<help_command_info>", TagResolvers.helpCommandInfo(root, command, sender));
    }

    @NotNull
    protected Component header() {
        return MiniMessage.miniMessage().deserialize("<help_header>", TagResolvers.helpHeader(plugin));
    }
}
