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

import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class VanishUser {
    private boolean seeAll;
    private boolean noPickup;
    private boolean noFollow;
    private boolean preventIncomingDamage;
    private boolean preventOutgoingDamage;
    private boolean noInteract;
    private boolean noHunger;
    private boolean silentChestReads;

    public VanishUser(@NonNull Player player) {
        this.seeAll = player.hasPermission("vanish.see");
        this.noPickup = player.hasPermission("vanish.nopickup");
        this.noFollow = player.hasPermission("vanish.nofollow");
        this.preventIncomingDamage = player.hasPermission("vanish.preventincomingdamage");
        this.preventOutgoingDamage = player.hasPermission("vanish.preventoutgoingdamage");
        this.noInteract = player.hasPermission("vanish.nointeract");
        this.noHunger = player.hasPermission("vanish.nohunger");
        this.silentChestReads = player.hasPermission("vanish.silentchests");
    }

    public boolean getNoFollow() {
        return this.noFollow;
    }

    public boolean getNoHunger() {
        return this.noHunger;
    }

    public boolean getNoInteract() {
        return this.noInteract;
    }

    public boolean getNoPickup() {
        return this.noPickup;
    }

    public boolean getPreventIncomingDamage() {
        return this.preventIncomingDamage;
    }

    public boolean getPreventOutgoingDamage() {
        return this.preventOutgoingDamage;
    }

    public boolean getReadChestsSilently() {
        return this.silentChestReads;
    }

    public boolean getSeeAll() {
        return this.seeAll;
    }

    public boolean toggleIncomingDamage() {
        this.preventIncomingDamage = !this.preventIncomingDamage;
        return this.preventIncomingDamage;
    }

    public boolean toggleNoFollow() {
        this.noFollow = !this.noFollow;
        return this.noFollow;
    }

    public boolean toggleNoHunger() {
        this.noHunger = !this.noHunger;
        return this.noHunger;
    }

    public boolean toggleNoInteract() {
        this.noInteract = !this.noInteract;
        return this.noInteract;
    }

    public boolean toggleNoPickup() {
        this.noPickup = !this.noPickup;
        return this.noPickup;
    }

    public boolean toggleOutgoingDamage() {
        this.preventOutgoingDamage = !this.preventOutgoingDamage;
        return this.preventOutgoingDamage;
    }

    public boolean toggleSeeAll() {
        this.seeAll = !this.seeAll;
        return this.seeAll;
    }

    public boolean toggleSilentChestReads() {
        this.silentChestReads = !this.silentChestReads;
        return this.silentChestReads;
    }
}
