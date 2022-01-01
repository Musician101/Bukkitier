package io.musician101.bukkitier.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import javax.annotation.Nonnull;

/**
 * An interface that represents a simpler {@link LiteralCommandNode}
 */
public interface LiteralCommand extends Command {

    /**
     * @return The name of the command.
     */
    @Nonnull
    String literal();
}
