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
     * @return The name of the command.
     */
    @Nonnull
    String literal();

    @Override
    default LiteralArgumentBuilder<CommandSender> toBrigadier() {
        LiteralArgumentBuilder<CommandSender> builder = Bukkitier.literal(literal()).executes(this::execute).requires(this::canUse);
        arguments().stream().map(Command::toBrigadier).forEach(builder::then);
        return builder;
    }
}
