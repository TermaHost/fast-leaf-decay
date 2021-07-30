package com.thermahost.fastleafdecay.event;

import com.thermahost.fastleafdecay.FastLeafDecay;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.jetbrains.annotations.NotNull;

public class LeafDecayEventListener implements Listener {

    private final FastLeafDecay plugin;

    public LeafDecayEventListener(FastLeafDecay plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onLeavesDecay(@NotNull LeavesDecayEvent event) {
        final Block block = event.getBlock();
        plugin.onBreak(block, 2);
    }
}
