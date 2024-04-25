package io.musician101.bukkitier.command.help;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

/**
 * A help command set as an argument.
 */
public abstract class HelpSubCommand extends AbstractHelpCommand {

    @NotNull
    private final LiteralCommand root;

    /**
     * @param root   The root {@link LiteralCommand}
     * @param plugin The {@link JavaPlugin} that's registering this command.
     */
    public HelpSubCommand(@NotNull LiteralCommand root, @NotNull JavaPlugin plugin) {
        super(plugin);
        this.root = root;
    }

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new HelpCommandArgument());
    }

    @NotNull
    protected Component argumentCommandInfo(@NotNull Command<? extends ArgumentBuilder<CommandSender, ?>> command, @NotNull CommandSender sender) {
        String string = "<click:suggest_command:'" + root.usage(sender) + " " + command.name() + "'>" + command.usage(sender) + "<newline><dark_gray> - <gray>" + command.description(sender);
        return miniMessage().deserialize(string);
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Shows the help info for /" + root.name();
    }

    @Override
    public int execute(@NotNull CommandContext<CommandSender> context) throws CommandSyntaxException {
        if (root instanceof HelpMainCommand) {
            return root.execute(context);
        }

        CommandSender sender = context.getSource();
        sender.sendMessage(header());
        sender.sendMessage(commandInfo(root, sender));
        arguments().stream().filter(cmd -> cmd.canUse(sender)).forEach(cmd -> sender.sendMessage(commandInfo(cmd, sender)));
        return 1;
    }

    @NotNull
    @Override
    public String name() {
        return "help";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return root.usage(sender) + " help [<command>]";
    }

    class CommandArgumentType implements ArgumentType<Command<? extends ArgumentBuilder<CommandSender, ?>>> {

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            root.arguments().stream().map(Command::name).filter(name -> name.startsWith(builder.getRemaining())).forEach(builder::suggest);
            return builder.buildFuture();
        }

        @Override
        public Command<? extends ArgumentBuilder<CommandSender, ?>> parse(StringReader reader) throws CommandSyntaxException {
            String name = reader.readString();
            return root.arguments().stream().filter(cmd -> cmd.name().equals(name)).findFirst().orElseThrow(() -> new SimpleCommandExceptionType(() -> "A sub command with that name does not exist.").createWithContext(reader));
        }
    }

    class HelpCommandArgument implements ArgumentCommand<Command<? extends ArgumentBuilder<CommandSender, ?>>> {

        @SuppressWarnings("unchecked")
        @Override
        public int execute(@NotNull CommandContext<CommandSender> context) {
            CommandSender sender = context.getSource();
            sender.sendMessage(header());
            Command<? extends ArgumentBuilder<CommandSender, ?>> command = context.getArgument(name(), Command.class);
            sender.sendMessage(argumentCommandInfo(command, sender));
            return 1;
        }

        @NotNull
        @Override
        public String name() {
            return "command";
        }

        @NotNull
        @Override
        public ArgumentType<Command<? extends ArgumentBuilder<CommandSender, ?>>> type() {
            return new CommandArgumentType();
        }

        @NotNull
        @Override
        public String usage(@NotNull CommandSender sender) {
            return HelpSubCommand.this.usage(sender) + " [command]";
        }
    }
}
