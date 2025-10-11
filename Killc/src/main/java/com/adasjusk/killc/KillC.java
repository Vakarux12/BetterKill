package com.adasjusk.killc;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import java.util.ArrayList;
import java.util.List;

public final class KillC extends JavaPlugin {

    private boolean pluginEnabled = true;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();
        getLogger().info("KillC plugin has been enabled!");
        KillCommandHandler handler = new KillCommandHandler(this);
        
        // Register commands
        this.getCommand("kill").setExecutor(handler);
        this.getCommand("kill").setTabCompleter(handler);
        this.getCommand("suicide").setExecutor(handler);
        this.getCommand("suicide").setTabCompleter(handler);
    }

    @Override
    public void onDisable() {
        getLogger().info("KillC plugin has been disabled!");
    }

    private void loadConfig() {
        reloadConfig();
        FileConfiguration config = getConfig();
        pluginEnabled = config.getBoolean("enabled", true);
    }

    public boolean isPluginEnabled() {
        return pluginEnabled;
    }

    public void reloadPluginConfig() {
        loadConfig();
    }
    private static class KillCommandHandler implements CommandExecutor, TabCompleter {
        private final KillC plugin;

        public KillCommandHandler(KillC plugin) {
            this.plugin = plugin;
        }
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            String commandName = command.getName().toLowerCase();
            if (commandName.equals("kill") && args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("killc.reload") && !sender.isOp()) {
                    sender.sendMessage(Component.text("You don't have permission to reload this plugin!").color(NamedTextColor.RED));
                    return true;
                }
                plugin.reloadPluginConfig();
                sender.sendMessage(Component.text("KillC plugin reloaded!").color(NamedTextColor.GREEN));
                return true;
            }
            if (!plugin.isPluginEnabled()) {
                sender.sendMessage(Component.text("KillC plugin is currently disabled!").color(NamedTextColor.RED));
                return true;
            }
            if (commandName.equals("suicide")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Component.text("Only players can use the suicide command!").color(NamedTextColor.RED));
                    return true;
                }
                
                Player player = (Player) sender;
                player.setHealth(0.0);
                player.sendMessage(Component.text("You have committed suicide!").color(NamedTextColor.RED));
                return true;
            }

            if (commandName.equals("kill")) {
                if (args.length == 0) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        player.setHealth(0.0);
                        player.sendMessage(Component.text("You have killed yourself!").color(NamedTextColor.RED));
                        return true;
                    } else {
                        sender.sendMessage(Component.text("Only players can kill themselves! Use /kill <player> to kill a specific player.").color(NamedTextColor.RED));
                        return true;
                    }
                }

                String targetName = args[0];
                Player target = Bukkit.getPlayer(targetName);

                if (target == null) {
                    sender.sendMessage(Component.text("Player '" + targetName + "' not found!").color(NamedTextColor.RED));
                    return true;
                }

                if (!sender.hasPermission("killc.others") && !sender.isOp()) {
                    sender.sendMessage(Component.text("You don't have permission to kill other players!").color(NamedTextColor.RED));
                    return true;
                }

                target.setHealth(0.0);
                target.sendMessage(Component.text("You have been killed by " + sender.getName() + "!").color(NamedTextColor.RED));
                sender.sendMessage(Component.text("You have killed " + target.getName() + "!").color(NamedTextColor.GREEN));
                return true;
            }
            
            return false;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            List<String> completions = new ArrayList<>();
            String commandName = command.getName().toLowerCase();
            if (commandName.equals("kill")) {
                if (args.length == 1) {
                    String partial = args[0].toLowerCase();
                    if ((sender.hasPermission("killc.reload") || sender.isOp()) && "reload".startsWith(partial)) {
                        completions.add("reload");
                    }
                    
                    if (sender.hasPermission("killc.others") || sender.isOp()) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.getName().toLowerCase().startsWith(partial)) {
                                completions.add(player.getName());
                            }
                        }
                    }
                }
            }

            return completions;
        }
    }
}