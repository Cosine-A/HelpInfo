package com.cosine.helpinfo.command;

import com.cosine.helpinfo.config.Config;
import com.cosine.helpinfo.main.HelpInfo;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class UserCommand implements CommandExecutor {

    private final Config config0;
    private final FileConfiguration config;

    public UserCommand(HelpInfo plugin) {
        config0 = plugin.config();
        config = config0.getConfig();
    }

    private final String option = "§6§l[ 도움말 ] §f§l";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                showCategory(player);
            }
            if (args.length == 1) {
                showHead(player, args[0]);
            }
            if (args.length == 2) {
                showInfo(player, args[0], args[1]);
            }
        }
        return false;
    }
    private void showCategory(Player player) {
        for (int loop = 0; loop < 50; loop++) {
            player.sendMessage("");
        }
        player.sendMessage("§c§l[ ! ]§f§l 목록 : 카테고리");
        player.sendMessage("");

        for (String category : config.getConfigurationSection("도움말").getKeys(false)) {
            TextComponent textComponent = new TextComponent("§6§l[ ! ] §f§l" + category + "");
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("클릭 시 §6" + category + "§f 카테고리를 확인합니다.")));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/도움말 " + category));
            player.spigot().sendMessage(textComponent);
        }

        player.sendMessage("");
    }
    private void showHead(Player player, String category) {
        for (int loop = 0; loop < 50; loop++) {
            player.sendMessage("");
        }
        player.sendMessage("§c§l[ ! ]§f§l 카테고리 : " + category);
        player.sendMessage("");

        for (String head : config.getConfigurationSection("도움말." + category).getKeys(false)) {
            TextComponent textComponent = new TextComponent("§a§l[ ! ] §f§l" + head + "");
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("클릭 시 §a" + head + "§f 머릿말을 확인합니다.")));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/도움말 " + category + " " + head));
            player.spigot().sendMessage(textComponent);
        }

        player.sendMessage("");
        back(player, "");
    }
    private void showInfo(Player player, String category, String head) {
        for (int loop = 0; loop < 50; loop++) {
            player.sendMessage("");
        }
        player.sendMessage("§c§l[ ! ]§f§l 머릿말 : " + head + "");
        player.sendMessage("");
        List<String> list = config.getStringList("도움말." + category + "." + head);
        for (String info : list) {
            player.sendMessage(color("&f&l  " + info));
        }

        player.sendMessage("");
        back(player, " " + category);
    }
    private void back(Player player, String value) {
        TextComponent textComponent = new TextComponent("§7§l[ 뒤로가기 ]");
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("클릭 시 뒤로 돌아갑니다.")));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/도움말" + value));
        player.spigot().sendMessage(textComponent);
    }
    private String color(String message) {
        return message.replace("&", "§");
    }
}
