package io.musician101.bukkitier.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.musician101.bukkitier.Bukkitier;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    @Override
    default LiteralArgumentBuilder<CommandSender> toBrigadier() {
        LiteralArgumentBuilder<CommandSender> builder = Bukkitier.literal(name()).executes(this::execute).requires(this::canUse);
        arguments().stream().map(Command::toBrigadier).forEach(builder::then);
        return builder;
    }

    @NotNull
    @Override
    default String usage(@NotNull CommandSender sender) {
        String usage = name();
        if (isRoot()) {
            return "/" + usage;
        }

        return usage;
    }
}
