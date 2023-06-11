package io.musician101.bukkitier.command.help;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.Command;
import java.util.List;
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
 * A help command set with a customizable name.
 */
public abstract class HelpMainCommand extends AbstractHelpCommand {

    protected HelpMainCommand(@Nonnull JavaPlugin plugin) {
        super(plugin);
    }

    @Nonnull
    @Override
    protected TextComponent commandInfo(@Nonnull Command<? extends ArgumentBuilder<CommandSender, ?>> command, @Nonnull CommandSender sender) {
        TextComponent cmd = new TextComponent(command.usage(sender));
        TextComponent dash = new TextComponent(" - ");
        dash.setColor(DARK_GRAY);
        TextComponent description = new TextComponent(command.description(sender));
        description.setColor(GRAY);
        cmd.addExtra(dash);
        cmd.addExtra(description);
        cmd.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, usage(sender) + " help " + command.name()));
        return cmd;
    }

    @Nonnull
    @Override
    public String description(@Nonnull CommandSender sender) {
        return "Displays help and plugin info.";
    }

    @Override
    public int execute(@Nonnull CommandContext<CommandSender> context) {
        CommandSender sender = context.getSource();
        Spigot spigot = sender.spigot();
        spigot.sendMessage(header());
        arguments().stream().filter(cmd -> cmd.canUse(sender)).forEach(cmd -> spigot.sendMessage(commandInfo(cmd, sender)));
        return 1;
    }

    @Nonnull
    @Override
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
        TextComponent click = new TextComponent("\nClick a command for more info.");
        click.setColor(GOLD);
        begin.addExtra(click);
        return begin;
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/" + name();
    }
}
