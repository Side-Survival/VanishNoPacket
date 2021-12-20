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
package org.kitteh.vanish;

import lv.sidesurvival.api.SurvivalStaffAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class VanishPerms {
    private static final Map<String, VanishUser> users = Collections.synchronizedMap(new HashMap<>());

    public static boolean blockIncomingDamage(@NonNull Player player) {
        return VanishPerms.getUser(player).getPreventIncomingDamage();
    }

    public static boolean blockOutgoingDamage(@NonNull Player player) {
        return VanishPerms.getUser(player).getPreventOutgoingDamage();
    }

    public static boolean canList(CommandSender sender) {
        return sender.hasPermission("vanish.list");
    }

    public static boolean canNotFollow(@NonNull Player player) {
        return VanishPerms.getUser(player).getNoFollow();
    }

    public static boolean canNotHunger(@NonNull Player player) {
        return VanishPerms.getUser(player).getNoHunger();
    }

    public static boolean canNotInteract(@NonNull Player player) {
        return VanishPerms.getUser(player).getNoInteract();
    }

    public static boolean canNotPickUp(@NonNull Player player) {
        return VanishPerms.getUser(player).getNoPickup();
    }

    public static boolean canNotTrample(@NonNull Player player) {
        return player.hasPermission("vanish.notrample");
    }

    public static boolean canReadChestsSilently(@NonNull Player player) {
        return VanishPerms.getUser(player).getReadChestsSilently();
    }

    public static boolean canReload(CommandSender sender) {
        return sender.hasPermission("vanish.reload");
    }

    public static boolean canSeeAll(@NonNull Player player) {
        return VanishPerms.getUser(player).getSeeAll();
    }

    public static boolean canSeeStatusUpdates(@NonNull Player player) {
        return player.hasPermission("vanish.statusupdates");
    }

    public static boolean canToggleDamageIn(@NonNull Player player) {
        return player.hasPermission("vanish.toggle.damagein");
    }

    public static boolean canToggleDamageOut(@NonNull Player player) {
        return player.hasPermission("vanish.toggle.damageout");
    }

    public static boolean canToggleNoFollow(@NonNull Player player) {
        return player.hasPermission("vanish.toggle.nofollow");
    }

    public static boolean canToggleNoHunger(@NonNull Player player) {
        return player.hasPermission("vanish.toggle.nohunger");
    }

    public static boolean canToggleNoInteract(@NonNull Player player) {
        return player.hasPermission("vanish.toggle.nointeract");
    }

    public static boolean canToggleNoPickup(@NonNull Player player) {
        return player.hasPermission("vanish.toggle.nopickup");
    }

    public static boolean canToggleSee(@NonNull Player player) {
        return player.hasPermission("vanish.toggle.see");
    }

    public static boolean canToggleSilentChestReads(@NonNull Player player) {
        return player.hasPermission("vanish.toggle.silentchests");
    }

    public static boolean canVanish(@NonNull Player player) {
        return player.hasPermission("vanish.vanish");
    }

    public static boolean canVanishOff(@NonNull Player player) {
        return player.hasPermission("vanish.vanish.off");
    }

    public static boolean canVanishOn(@NonNull Player player) {
        return player.hasPermission("vanish.vanish.on");
    }

    public static boolean joinVanished(@NonNull Player player) {
        return SurvivalStaffAPI.shouldVanish(player);
    }

    public static boolean toggleDamageIn(@NonNull Player player) {
        return VanishPerms.getUser(player).toggleIncomingDamage();
    }

    public static boolean toggleDamageOut(@NonNull Player player) {
        return VanishPerms.getUser(player).toggleOutgoingDamage();
    }

    public static boolean toggleNoFollow(@NonNull Player player) {
        return VanishPerms.getUser(player).toggleNoFollow();
    }

    public static boolean toggleNoHunger(@NonNull Player player) {
        return VanishPerms.getUser(player).toggleNoHunger();
    }

    public static boolean toggleNoInteract(@NonNull Player player) {
        return VanishPerms.getUser(player).toggleNoInteract();
    }

    public static boolean toggleNoPickup(@NonNull Player player) {
        return VanishPerms.getUser(player).toggleNoPickup();
    }

    public static boolean toggleSeeAll(@NonNull Player player) {
        return VanishPerms.getUser(player).toggleSeeAll();
    }

    public static boolean toggleSilentChestReads(@NonNull Player player) {
        return VanishPerms.getUser(player).toggleSilentChestReads();
    }

    public static void userQuit(@NonNull Player player) {
        VanishPerms.users.remove(player.getName());
    }

    private static VanishUser getUser(@NonNull Player player) {
        VanishUser user = VanishPerms.users.get(player.getName());
        if (user == null) {
            user = new VanishUser(player);
            VanishPerms.users.put(player.getName(), user);
        }
        return user;
    }
}
