/*
 * VanishNoPacket
 * Copyright (C) 2011-2021 Matt Baxter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kitteh.vanish.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.kitteh.vanish.VanishPerms;
import org.kitteh.vanish.VanishPlugin;

public final class VanishPickupCommand implements CommandExecutor {

    private final VanishPlugin plugin;

    public VanishPickupCommand(@NonNull VanishPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        // Goodbye console!
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.AQUA + "Did you mean " + ChatColor.WHITE + "vanish reload" + ChatColor.AQUA + " or " + ChatColor.WHITE + "vanish list" + ChatColor.AQUA + "?");
            return true;
        }
        // No more console options below this point
        final Player player = (Player) sender;

        final StringBuilder message = new StringBuilder();
        boolean status = false;
        if (VanishPerms.canToggleNoPickup(player)) {
            status = VanishPerms.toggleNoPickup(player);
            message.append("no pickup");
        }

        if (message.length() > 0) {
            message.insert(0, ChatColor.DARK_AQUA + "Status: ");
            message.append(": ");
            if (status) {
                message.append("enabled");
            } else {
                message.append("disabled");
            }
            player.sendMessage(message.toString());
        } else if (VanishPerms.canVanish(player)) {
            player.sendMessage(ChatColor.DARK_AQUA + "You can't toggle that!");
        }

        return true;
    }
}
