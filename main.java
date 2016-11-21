package me.ysmokebr.ytelabr;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BanCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener {

	public static ArrayList<Player> array = new ArrayList<Player>();

	@Override
	public void onEnable() {
		if (Bukkit.getPluginManager().getPlugin("Essentials") != null) {
			this.getLogger().info("§a[yTelaBR_] §fEssentials encontrado, ativando plugin....");
		} else {
			this.getLogger().info("§a[yTelaBR_] §fEssentials nao encontrado, desativando plugin....");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		ConsoleCommandSender bc = Bukkit.getConsoleSender();
		bc.sendMessage("§3-=-=-=-=-=-=-=-=-=-=-=-=-");
		bc.sendMessage("§aPlugin: §fyTelaBR_");
		bc.sendMessage("§aVersao: §f" + getDescription().getVersion());
		bc.sendMessage("§aStats: §fAtivado");
		bc.sendMessage("§aDev: §fySmokeBR_");
		bc.sendMessage("§3-=-=-=-=-=-=-=-=-=-=-=-=-");
		Bukkit.getPluginManager().registerEvents(this, this);
		saveDefaultConfig();

	}

	@Override
	public void onDisable() {
		ConsoleCommandSender bc = Bukkit.getConsoleSender();
		bc.sendMessage("§3-=-=-=-=-=-=-=-=-=-=-=-=-");
		bc.sendMessage("§aPlugin: §fyTelaBR_");
		bc.sendMessage("§aVersao: §f" + getDescription().getVersion());
		bc.sendMessage("§aStats: §fDesativado");
		bc.sendMessage("§aDev: §fySmokeBR_");
		bc.sendMessage("§3-=-=-=-=-=-=-=-=-=-=-=-=-");

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§aSomente in-game!");
			return true;
		}
		Player player = (Player) sender;
		if (player.hasPermission(getConfig().getString("Permissao"))) {
			if (command.getName().equalsIgnoreCase("prender")) {
				if (args.length == 0) {
					player.sendMessage("§cuse: /prender <player_name>");
					player.sendMessage("§cuse: /liberar <player_name>");
					return true;
				}

				if (args.length == 1) {
					String cmd2 = args[0];
					Player t = Bukkit.getPlayer(args[0]);
					if (t == null) {
						player.sendMessage("§4Esse player esta offline!");
					} else if (t != null && cmd2.equalsIgnoreCase(t.getName())) {
						if (t == player) {
							player.sendMessage("§4Você não pode prender a si mesmo!");
							return true;
						}
						array.add(t);
						Bukkit.broadcastMessage(getConfig().getString("Broadcast").replace("&", "§")
								.replace("{player}", t.getName()).replace("{Admin}", player.getName()));
						for (String msg1 : getConfig().getStringList("Menssage")) {
							t.sendMessage(msg1.replace("&", "§").replace("{Admin}", player.getName()));
						}
						Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),
								getConfig().getString("Comando.Prender").replace("{player}", t.getName()));
						return true;
					}
					return true;
				}

				return true;
			}
			if (command.getName().equalsIgnoreCase("liberar")) {
				if (args.length == 0) {
					player.sendMessage("§cuse: /liberar <player_name>");
					player.sendMessage("§cuse: /prender <player_name>");
					return true;
				}

				if (args.length == 1) {
					String cmd2 = args[0];
					Player t = Bukkit.getPlayer(args[0]);
					if (t == null) {
						player.sendMessage("§4Esse player esta offline!");
					} else if (t != null && array.contains(t) && cmd2.equalsIgnoreCase(t.getName())) {
						array.remove(t);
						Bukkit.broadcastMessage(getConfig().getString("Broadcast2").replace("&", "§")
								.replace("{player}", t.getName()).replace("{Admin}", player.getName()));
						for (String msg1 : getConfig().getStringList("Menssage2")) {
							t.sendMessage(msg1.replace("&", "§").replace("{Admin}", player.getName()));
						}
						Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),
								getConfig().getString("Comando.Liberar").replace("{player}", t.getName()));
						if (!array.contains(t)) {
							t.setBanned(false);
						}
						return true;
					} else {
						player.sendMessage("§4Esse jogador nao esta prezo!");
					}
					return true;
				}

				return true;
			}

		} else {
			player.sendMessage(getConfig().getString("Sem_Permissao").replace("&", "§"));
			return true;
		}

		return false;
	}

	@EventHandler
	public void deslogou(final PlayerQuitEvent e) {
		e.setQuitMessage(null);
		final Player p = e.getPlayer();
		if (array.contains(p)) {
			array.remove(p);
			Bukkit.broadcastMessage(getConfig().getString("Mensagem_Desconectou").replace("&", "§").replace("{player}", p.getName()));
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), getConfig().getString("Comando_Ban").replace("&", "§").replace("{player}", p.getName()));
		}
	}
	

}
