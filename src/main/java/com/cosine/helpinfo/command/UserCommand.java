package com.cosine.helpinfo.command;

import com.cosine.helpinfo.config.Config;
import com.cosine.helpinfo.main.HelpInfo;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserCommand implements CommandExecutor {

    private final Config config0;
    private final FileConfiguration config;

    private final String header;
    private final String beforePage;
    private final String msg;
    private final String nextPage;
    private final String footer;

    private final String headHeader;
    private final String headMessage;
    private final String headFooter;

    public UserCommand(HelpInfo plugin) {
        config0 = plugin.config();
        config = config0.getConfig();

        header = plugin.message().getConfig().getString("Header");
        beforePage = plugin.message().getConfig().getString("BeforePage");
        msg = plugin.message().getConfig().getString("Message");
        nextPage = plugin.message().getConfig().getString("NextPage");
        footer = plugin.message().getConfig().getString("Footer");

        headHeader = plugin.message().getConfig().getString("HeadHeader");
        headMessage = plugin.message().getConfig().getString("HeadMessage");
        headFooter = plugin.message().getConfig().getString("HeadFooter");
    }

    private final ExecutorService service = Executors.newCachedThreadPool();

    private final String option = "§6§l[ 도움말 ] §f§l";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                showCategory(player, 1);
            }
            if (args.length == 1) return false;
            if (args.length == 2) {
                if (args[0].equals("페이지")) {
                    showCategory(player, Integer.parseInt(args[1]));
                    return false;
                }
                showInfo(player, args[0], args[1]);
            }
        }
        return false;
    }
    private void showCategory(Player player, int page) {
        int maxPage = getMaxPage();
        sendMessage(player, header, page, maxPage);
        player.sendMessage(" ");
        beforePage(player, beforePage, page);
        player.sendMessage(" ");

        sendInfoMessage(player, page);

        player.sendMessage(" ");
        nextPage(player, nextPage, page);
        player.sendMessage(" ");
        sendMessage(player, footer, page, maxPage);
    }
    private void sendInfoMessage(Player player, int page) {
        int prevent = 0;
        List<String> categories = new ArrayList<>(config.getConfigurationSection("도움말").getKeys(false));
        Set<String> set = config.getConfigurationSection("도움말." + categories.get(page - 1)).getKeys(false);
        for (String head : set) {
            if (prevent < set.size()) {
                player.sendMessage(createTextComponent(categories.get(page - 1), head));
                prevent++;
            }
        }
    }
    private void showInfo(Player player, String category, String head) {
        sendMessage2(player, headHeader, category, head);
        player.sendMessage(" ");

        List<String> list = config.getStringList("도움말." + category + "." + head);
        for (String info : list) {
            player.sendMessage(changeMessage2(info));
        }

        player.sendMessage(" ");
        sendMessage2(player, headFooter, category, head);
    }
    private void beforePage(Player player, String message, int nowPage) {
        TextComponent beforePage;
        if (nowPage == 1) {
            beforePage = new TextComponent(color(message).replace("%BeforePage%", "X"));
            beforePage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("이전 페이지가 존재하지 않습니다.")));
        } else {
            beforePage = new TextComponent(color(message).replace("%BeforePage%", "" + (nowPage - 1)));
            beforePage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("이전 페이지로 이동합니다.")));
            beforePage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/도움말 페이지 " + (nowPage - 1)));
        }
        player.sendMessage(beforePage);
    }
    private void nextPage(Player player, String message, int nowPage) {
        TextComponent nextPage;
        if (nowPage == getMaxPage()) {
            nextPage = new TextComponent(color(message).replace("%NextPage%", "X"));
            nextPage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("다음 페이지가 존재하지 않습니다.")));
        } else {
            nextPage = new TextComponent(color(message).replace("%NextPage%", "" + (nowPage + 1)));
            nextPage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("다음 페이지로 이동합니다.")));
            nextPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/도움말 페이지 " + (nowPage + 1)));
        }
        player.sendMessage(nextPage);
    }
    private int getMaxPage() {
        Callable<Integer> callable = () -> config.getConfigurationSection("도움말").getKeys(false).size();
        Future<Integer> future = service.submit(callable);
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private TextComponent createTextComponent(String category, String head) {
        String chat = color(changeMessage("/도움말 " + category + " " + head, "- " + category + "에 있는 " + head + "의 도움말을 확인합니다."));
        TextComponent textComponent = new TextComponent(chat);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("클릭 시 내용을 확인합니다.")));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/도움말 " + category + " " + head));
        return textComponent;
    }
    private String changeMessage(String value, String message) {
        return msg.replace("%Command%", value).replace("%Message%", message);
    }
    private String changeMessage2(String message) {
        return color(headMessage.replace("%Message%", message));
    }
    private void sendMessage(Player player, String message, int nowPage, int maxPage) {
        player.sendMessage(color(message).replace("%NowPage%", "" + nowPage).replace("%MaxPage%", "" + maxPage));
    }
    private void sendMessage2(Player player, String message, String category, String head) {
        player.sendMessage(color(message).replace("%Category%", category).replace("%Head%", head));
    }
    private String color(String message) {
        return message.replace("&", "§");
    }
}
