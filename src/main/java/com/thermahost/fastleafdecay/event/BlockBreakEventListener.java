package com.thermahost.fastleafdecay.event;

import com.thermahost.fastleafdecay.FastLeafDecay;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakEventListener implements Listener {

    /*
    Monitor event is used to optimise "snappiness" of block breaking in the
    long term, as it is the lowest priority, so user block interaction is done
    by the time this event handler is called.
    */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        final Block block = event.getBlock();
        int delay;

        //if (Tag.LEAVES.isTagged(block.getType())){
        if (Tag.LEAVES.isTagged(block.getType())){
            delay = 2;
        }else{
            return;
        }

        //if(Tag.LOGS.isTagged(block.getType())){
        if(Tag.LOGS.isTagged(block.getType())){
            delay = 5;
        }else{
            return;
        }

        FastLeafDecay.onBreak(block, delay);
    }
}
