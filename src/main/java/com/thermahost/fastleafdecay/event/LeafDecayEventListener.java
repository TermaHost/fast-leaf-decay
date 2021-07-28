package com.thermahost.fastleafdecay.event;

import com.thermahost.fastleafdecay.FastLeafDecay;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;

public class LeafDecayEventListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onLeavesDecay(LeavesDecayEvent event) {
        final Block block = event.getBlock();
        FastLeafDecay.onBreak(block, 2);
    }
}
