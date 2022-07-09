package com.cosine.helpinfo.command;

import com.cosine.helpinfo.config.Config;
import com.cosine.helpinfo.main.HelpInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class OpCommand implements CommandExecutor {

    private final Config config0;
    private final FileConfiguration config;

    public OpCommand(HelpInfo plugin) {
        config0 = plugin.config();
        config = config0.getConfig();
    }

    private final String option = "§6§l[ 도움말 ] §f§l";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) return false;
            if (args.length == 0) {
                help(player);
                return false;
            }
            switch (args[0]) {
                case "카테고리추가": {
                    if (args.length == 1) {
                        player.sendMessage(option + "추가할 카테고리의 이름을 적어주세요.");
                        return false;
                    }
                    if (config.contains("도움말." + args[1])) {
                        player.sendMessage(option + "이미 존재하는 카테고리입니다.");
                        return false;
                    }
                    createCategory(args[1]);
                    player.sendMessage(option + "§e§l" + args[1] + "§f§l 카테고리를 추가하였습니다.");
                    return false;
                }
                case "카테고리삭제": {
                    if (args.length == 1) {
                        player.sendMessage(option + "삭제할 카테고리의 이름을 적어주세요.");
                        return false;
                    }
                    if (!config.contains("도움말." + args[1])) {
                        player.sendMessage(option + "존재하지 않는 카테고리입니다.");
                        return false;
                    }
                    removeCategory(args[1]);
                    player.sendMessage(option + "§e§l" + args[1] + "§f§l 카테고리를 삭제하였습니다.");
                    return false;
                }
                case "머릿말추가": {
                    if (args.length == 1) {
                        player.sendMessage(option + "카테고리의 이름을 적어주세요.");
                        return false;
                    }
                    if (!config.contains("도움말." + args[1])) {
                        player.sendMessage(option + "존재하지 않는 카테고리입니다.");
                        return false;
                    }
                    if (args.length == 2) {
                        player.sendMessage(option + "머릿말을 적어주세요.");
                        return false;
                    }
                    if (config.contains("도움말." + args[1] + "." + args[2])) {
                        player.sendMessage(option + "이미 존재하는 머릿말입니다.");
                        return false;
                    }
                    createHead(args[1], args[2]);
                    player.sendMessage(option + "§e§l" + args[1] + "§f§l 카테고리에 §a§l" + args[2] + "§f§l 머릿말을 추가하였습니다.");
                    return false;
                }
                case "머릿말삭제": {
                    if (args.length == 1) {
                        player.sendMessage(option + "카테고리의 이름을 적어주세요.");
                        return false;
                    }
                    if (!config.contains("도움말." + args[1])) {
                        player.sendMessage(option + "존재하지 않는 카테고리입니다.");
                        return false;
                    }
                    if (args.length == 2) {
                        player.sendMessage(option + "머릿말을 적어주세요.");
                        return false;
                    }
                    if (!config.contains("도움말." + args[1] + "." + args[2])) {
                        player.sendMessage(option + "존재하지 않는 머릿말입니다.");
                        return false;
                    }
                    removeHead(args[1], args[2]);
                    player.sendMessage(option + "§e§l" + args[1] + "§f§l 카테고리에서 §a§l" + args[2] + "§f§l 머릿말을 삭제하였습니다.");
                    return false;
                }
                case "리로드": {
                    config0.reloadConfig();
                    player.sendMessage(option + "컨피그를 리로드하였습니다.");
                }
                default: help(player);
            }
        }
        return false;
    }
    private void help(Player player) {
        player.sendMessage(option + "도움말 관리 시스템");
        player.sendMessage("");
        player.sendMessage(option + "§f/도움말관리 카테고리추가 [카테고리] - 카테고리를 추가합니다.");
        player.sendMessage(option + "§f/도움말관리 카테고리삭제 [카테고리] - 카테고리를 삭제합니다.");
        player.sendMessage(option + "§f/도움말관리 머릿말추가 [카테고리] [머릿말] - 카테고리에 머릿말을 추가합니다.");
        player.sendMessage(option + "§f/도움말관리 머릿말추가 [카테고리] [머릿말] - 카테고리에 있는 머릿말을 삭제합니다.");
        player.sendMessage(option + "§f/도움말관리 리로드 - 컨피그를 리로드합니다.");
        player.sendMessage("§7[ 내용은 컨피그에서 수정하세요. ]");
    }
    private void createCategory(String category) {
        config.set("도움말." + category, 0);
        config0.saveConfig();
    }
    private void removeCategory(String category) {
        config.set("도움말." + category, null);
        config0.saveConfig();
    }
    private void createHead(String category, String head) {
        List<String> list = new ArrayList<>();
        list.add("내용을 계속");
        list.add("추가할 수 있다고?");
        config.set("도움말." + category + "." + head, list);
        config0.saveConfig();
    }
    private void removeHead(String category, String head) {
        config.set("도움말." + category + "." + head, null);
        config0.saveConfig();
    }
}
