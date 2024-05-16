package io.musician101.bukkitier.tagresolver;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.bukkitier.command.Command;
import io.papermc.paper.plugin.configuration.PluginMeta;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TagResolvers {

    @NotNull
    @SuppressWarnings("UnstableApiUsage")
    static TagResolver helpHeader(@NotNull JavaPlugin plugin) {
        return TagResolver.resolver("help_header", (argumentQueue, context) -> {
            String color1 = "<dark_green>";
            String color2 = "<green>";
            String color3 = "<color:#BDB76B>";
            if (argumentQueue.hasNext()) {
                color1 = "<color:" + argumentQueue.pop().value() + ">";
                if (argumentQueue.hasNext()) {
                    color2 = "<color:" + argumentQueue.pop().value() + ">";
                    if (argumentQueue.hasNext()) {
                        color3 = "<color:" + argumentQueue.pop().value() + ">";
                    }
                }
            }

            PluginMeta meta = plugin.getPluginMeta();
            String developedBy = "";
            String developedByCloser = "";
            List<String> authors = meta.getAuthors();
            if (!authors.isEmpty()) {
                int last = authors.size() - 1;
                String authorsString = switch (last) {
                    case 0 -> authors.getFirst();
                    case 1 -> String.join(" and ", authors);
                    default -> String.join(", and ", String.join(", ", authors.subList(0, last)), authors.get(last));
                };
                developedBy = "<hover:show_text:'" + color3 + "Developed by " + authorsString + "'>";
                developedByCloser = "</hover>";
            }

            String string = color1 + "> ===== " + color2 + developedBy + meta.getDisplayName() + developedByCloser + color1 + " ===== <";
            return Tag.selfClosingInserting(MiniMessage.miniMessage().deserialize(string));
        });
    }

    @NotNull
    static TagResolver helpCommandInfo(@NotNull Command<? extends ArgumentBuilder<CommandSender, ?>> root, @NotNull Command<? extends ArgumentBuilder<CommandSender, ?>> command, @NotNull CommandSender sender) {
        return TagResolver.resolver("help_command_info", (argumentQueue, context) -> {
            String color1 = "<white>";
            String color2 = "<dark_green>";
            String color3 = "<color:#BDB76B>";
            if (argumentQueue.hasNext()) {
                color1 = "<color:" + argumentQueue.pop().value() + ">";
                if (argumentQueue.hasNext()) {
                    color2 = "<color:" + argumentQueue.pop().value() + ">";
                    if (argumentQueue.hasNext()) {
                        color3 = "<color:" + argumentQueue.pop().value() + ">";
                    }
                }
            }

            String string = color1 + root.usage(sender) + " " + command.name() + color2 + " - " + color3 + command.description(sender);
            return Tag.selfClosingInserting(MiniMessage.miniMessage().deserialize(string));
        });
    }
}
