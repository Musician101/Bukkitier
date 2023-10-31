package io.musician101.bukkitier.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import io.musician101.bukkitier.Bukkitier;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * An interface that represents a simpler {@link ArgumentCommandNode}
 */
public interface ArgumentCommand<T> extends Command<RequiredArgumentBuilder<CommandSender, T>> {

    @NotNull
    @Override
    default RequiredArgumentBuilder<CommandSender, T> toBrigadier() {
        RequiredArgumentBuilder<CommandSender, T> builder = Bukkitier.argument(name(), type()).executes(this::execute).requires(this::canUse);
        arguments().stream().map(Command::toBrigadier).forEach(builder::then);
        return builder;
    }

    /**
     * @return The object that will provide the parsing and tab completion.
     */
    @NotNull
    ArgumentType<T> type();

    @NotNull
    @Override
    default String usage(@NotNull CommandSender sender) {
        return "<" + name() + ">";
    }
}
