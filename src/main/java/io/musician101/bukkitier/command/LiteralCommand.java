package io.musician101.bukkitier.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.musician101.bukkitier.Bukkitier;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;

/**
 * An interface that represents a simpler {@link LiteralCommandNode}
 */
public interface LiteralCommand extends Command<LiteralArgumentBuilder<CommandSender>> {

    /**
     * @return True if this LiteralCommand is the root of the command
     */
    default boolean isRoot() {
        return false;
    }

    /**
     * @return The name of the command.
     * @deprecated Use {@link Command#name()}
     */
    @Deprecated(forRemoval = true, since = "1.3.0")
    @Nonnull
    default String literal() {
        return name();
    }

    @Nonnull
    @Override
    default LiteralArgumentBuilder<CommandSender> toBrigadier() {
        LiteralArgumentBuilder<CommandSender> builder = Bukkitier.literal(name()).executes(this::execute).requires(this::canUse);
        arguments().stream().map(Command::toBrigadier).forEach(builder::then);
        return builder;
    }

    @Nonnull
    @Override
    default String usage(@Nonnull CommandSender sender) {
        String usage = name();
        if (isRoot()) {
            return "/" + usage;
        }

        return usage;
    }
}
