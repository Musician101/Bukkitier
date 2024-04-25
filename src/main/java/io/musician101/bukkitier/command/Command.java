package io.musician101.bukkitier.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * An interface that represents a simpler {@link CommandNode}
 */
public interface Command<B extends ArgumentBuilder<CommandSender, B>> {

    /**
     * @return A list of subcommands and arguments.
     */
    @NotNull
    default List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of();
    }

    /**
     * Use this method to see if {@link CommandSender#hasPermission(String)} is true or check if {@link CommandSender} is in fact a {@link Player}
     *
     * @param sender The {@link CommandSender} attempting to run the command.
     * @return Whether the {@link CommandSender} can run the command.
     */
    default boolean canUse(@NotNull CommandSender sender) {
        return true;
    }

    /**
     * A base description of the command/argument.
     *
     * @param sender The {@link CommandSender} receiving the description
     * @return A description
     */
    @NotNull
    default String description(@NotNull CommandSender sender) {
        return "";
    }

    /**
     * @param context The {@link CommandContext <CommandSender>} for this command.
     * @return An integer where anything greater than 0 is usually a successful run of the command.
     * @throws CommandSyntaxException If an error during execution occurs.
     */
    default int execute(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
        CommandSender sender = context.getSource();
        sender.sendMessage(ChatColor.RED + "Unknown or incomplete command, see below for error");
        sender.sendMessage(context.getInput() + ChatColor.RED + "<--[HERE]");
        return 1;
    }

    /**
     * @return The name of the argument.
     */
    @NotNull
    String name();

    /**
     * @return A Brigadier representation of the command.
     */
    @NotNull
    B toBrigadier();

    /**
     * @param sender The {@link CommandSender} for checking permissions based usages.
     * @return Usage of the command.
     */
    @NotNull
    String usage(@NotNull CommandSender sender);
}
