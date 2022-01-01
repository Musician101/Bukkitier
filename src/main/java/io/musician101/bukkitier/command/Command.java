package io.musician101.bukkitier.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * An interface that represents a simpler {@link CommandNode}
 */
public interface Command {

    /**
     * @return A list of subcommands and arguments.
     */
    @Nonnull
    default List<Command> arguments() {
        return Collections.emptyList();
    }

    /**
     * Use this method to see if {@link CommandSender#hasPermission(String)} is true or check if {@link CommandSender} is in fact a {@link Player}
     *
     * @param sender The {@link CommandSender} attempting to run the command.
     * @return Whether the {@link CommandSender} can run the command.
     */
    default boolean canUse(@Nonnull CommandSender sender) {
        return true;
    }

    /**
     *
     * @param commandContext The {@link CommandContext<CommandSender>} for this command.
     * @return An integer where anything greater than 0 is usually a successful run of the command.
     * @throws CommandSyntaxException If an error during execution occurs.
     */
    default int execute(@Nonnull CommandContext<CommandSender> commandContext) throws CommandSyntaxException {
        return 0;
    }

    /**
     * @return A list of aliases.
     */
    @Nonnull
    default List<String> aliases() {
        return Collections.emptyList();
    }
}
