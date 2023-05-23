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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nonnull;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandSender.Spigot;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import static net.md_5.bungee.api.ChatColor.DARK_GRAY;
import static net.md_5.bungee.api.ChatColor.DARK_GREEN;
import static net.md_5.bungee.api.ChatColor.GOLD;
import static net.md_5.bungee.api.ChatColor.GRAY;
import static net.md_5.bungee.api.ChatColor.GREEN;
import static net.md_5.bungee.api.ChatColor.of;

/**
 * A help command set as an argument.
 */
public abstract class HelpSubCommand extends AbstractHelpCommand {

    @Nonnull
    private final LiteralCommand root;

    /**
     * @param root   The root {@link LiteralCommand}
     * @param plugin The {@link JavaPlugin} that's registering this command.
     */
    public HelpSubCommand(@Nonnull LiteralCommand root, @Nonnull JavaPlugin plugin) {
        super(plugin);
        this.root = root;
    }

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new HelpCommandArgument());
    }

    @Nonnull
    @Override
    protected TextComponent commandInfo(@Nonnull Command<? extends ArgumentBuilder<CommandSender, ?>> command, @Nonnull CommandSender sender) {
        TextComponent cmd = new TextComponent(command.usage(sender));
        TextComponent dash = new TextComponent("\n - ");
        dash.setColor(DARK_GRAY);
        TextComponent description = new TextComponent(command.description());
        description.setColor(GRAY);
        cmd.addExtra(dash);
        cmd.addExtra(description);
        cmd.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, usage(sender) + " help " + command.name()));
        return cmd;
    }

    @Nonnull
    private TextComponent defaultCommandInfo(@Nonnull Command<? extends ArgumentBuilder<CommandSender, ?>> command, @Nonnull CommandSender sender) {
        TextComponent cmd = new TextComponent(command.usage(sender));
        TextComponent dash = new TextComponent(" - ");
        dash.setColor(DARK_GRAY);
        TextComponent description = new TextComponent(command.description());
        description.setColor(GRAY);
        cmd.addExtra(dash);
        cmd.addExtra(description);
        cmd.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, usage(sender) + " help " + command.name()));
        return cmd;
    }

    @Nonnull
    @Override
    public String description() {
        return "Shows the help info for /" + root.name();
    }

    @Override
    public int execute(@Nonnull CommandContext<CommandSender> context) throws CommandSyntaxException {
        CommandSender sender = context.getSource();
        Spigot spigot = sender.spigot();
        spigot.sendMessage(header());
        if (root instanceof HelpMainCommand) {
            root.execute(context);
        }
        else {
            spigot.sendMessage(defaultCommandInfo(root, sender));
            arguments().stream().filter(cmd -> cmd.canUse(sender)).forEach(cmd -> spigot.sendMessage(defaultCommandInfo(cmd, sender)));
        }

        return 1;
    }

    @Nonnull
    protected TextComponent header() {
        PluginDescriptionFile pdf = plugin.getDescription();
        TextComponent begin = new TextComponent("> ===== ");
        begin.setColor(DARK_GREEN);
        TextComponent middle = new TextComponent(pdf.getFullName());
        middle.setColor(GREEN);
        TextComponent end = new TextComponent(" ===== <");
        end.setColor(DARK_GREEN);
        begin.addExtra(middle);
        begin.addExtra(end);
        TextComponent developed = new TextComponent("Developed by ");
        developed.setColor(GOLD);
        List<String> authors = pdf.getAuthors();
        int last = authors.size() - 1;
        TextComponent authorsComponent = new TextComponent(switch (last) {
            case 0 -> authors.get(0);
            case 1 -> String.join(" and ", authors);
            default -> String.join(", and ", String.join(", ", authors.subList(0, last)), authors.get(last));
        });
        authorsComponent.setColor(of("#BDB76B"));
        begin.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new Text(new BaseComponent[]{developed, authorsComponent})));
        return begin;
    }

    @Nonnull
    @Override
    public String name() {
        return "help";
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return root.usage(sender) + " help";
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

        @Override
        public int execute(@Nonnull CommandContext<CommandSender> context) {
            CommandSender sender = context.getSource();
            Spigot spigot = sender.spigot();
            spigot.sendMessage(header());
            Command<? extends ArgumentBuilder<CommandSender, ?>> command = context.getArgument(name(), Command.class);
            spigot.sendMessage(commandInfo(command, sender));
            return 1;
        }

        @Nonnull
        @Override
        public String name() {
            return "command";
        }

        @Nonnull
        @Override
        public ArgumentType<Command<? extends ArgumentBuilder<CommandSender, ?>>> type() {
            return new CommandArgumentType();
        }

        @Nonnull
        @Override
        public String usage(@Nonnull CommandSender sender) {
            return ArgumentCommand.super.usage(sender);
        }
    }
}
