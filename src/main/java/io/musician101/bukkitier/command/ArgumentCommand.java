package io.musician101.bukkitier.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import javax.annotation.Nonnull;

/**
 * An interface that represents a simpler {@link ArgumentCommandNode}
 */
public interface ArgumentCommand<T> extends Command {

    /**
     * @return The name of the argument.
     */
    @Nonnull
    String name();

    /**
     * @return The object that will provide the parsing and tab completion.
     */
    @Nonnull
    ArgumentType<T> type();
}
