package io.musician101.bukkitier.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import io.musician101.bukkitier.Bukkitier;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;

/**
 * An interface that represents a simpler {@link ArgumentCommandNode}
 */
public interface ArgumentCommand<T> extends Command<RequiredArgumentBuilder<CommandSender, T>> {

    @Nonnull
    @Override
    default RequiredArgumentBuilder<CommandSender, T> toBrigadier() {
        RequiredArgumentBuilder<CommandSender, T> builder = Bukkitier.argument(name(), type()).executes(this::execute).requires(this::canUse);
        arguments().stream().map(Command::toBrigadier).forEach(builder::then);
        return builder;
    }

    /**
     * @return The object that will provide the parsing and tab completion.
     */
    @Nonnull
    ArgumentType<T> type();

    @Nonnull
    @Override
    default String usage(@Nonnull CommandSender sender) {
        return "<" + name() + ">";
    }
}
