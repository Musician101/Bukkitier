package io.musician101.bukkitier.command.help;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

abstract class AbstractHelpCommand implements LiteralCommand {

    @NotNull
    protected final JavaPlugin plugin;

    protected AbstractHelpCommand(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @NotNull
    protected abstract TextComponent commandInfo(@NotNull Command<? extends ArgumentBuilder<CommandSender, ?>> command, @NotNull CommandSender sender);

    @NotNull
    protected abstract TextComponent header();
}
