package com.cosine.helpinfo.main;

import com.cosine.helpinfo.command.OpCommand;
import com.cosine.helpinfo.command.UserCommand;
import com.cosine.helpinfo.config.Config;
import org.bukkit.plugin.java.JavaPlugin;

public final class HelpInfo extends JavaPlugin {

    private Config config;

    @Override
    public void onEnable() {
        getLogger().info("도움말 플러그인 활성화");

        config = new Config(this, "HelpInfo.yml");
        config.saveDefaultConfig();

        getCommand("도움말관리").setExecutor(new OpCommand(this));
        getCommand("도움말").setExecutor(new UserCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("도움말 플러그인 비활성화");
    }
    public Config config() {
        return this.config;
    }
}
