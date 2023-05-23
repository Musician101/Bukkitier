package io.musician101.bukkitier.command.help;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import javax.annotation.Nonnull;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

abstract class AbstractHelpCommand implements LiteralCommand {

    @Nonnull
    protected final JavaPlugin plugin;

    protected AbstractHelpCommand(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Nonnull
    protected abstract TextComponent commandInfo(@Nonnull Command<? extends ArgumentBuilder<CommandSender, ?>> command, @Nonnull CommandSender sender);

    @Nonnull
    protected abstract TextComponent header();
}
