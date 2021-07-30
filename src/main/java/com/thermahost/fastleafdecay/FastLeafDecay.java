package com.thermahost.fastleafdecay;

import com.thermahost.fastleafdecay.event.BlockBreakEventListener;
import com.thermahost.fastleafdecay.event.LeafDecayEventListener;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public final class FastLeafDecay extends JavaPlugin {
    private static final List<BlockFace> FACES = new ArrayList<>(Arrays.asList(BlockFace.values()));

    @Override
    public void onEnable() {
        //Variable initialization, needs to be first.
        FACES.remove(BlockFace.SELF);
        //------------------------------------------

        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new BlockBreakEventListener(this), this);
        manager.registerEvents(new LeafDecayEventListener(this), this);
        getLogger().info("Plugin enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled");
        //Who ever uses the /reload command get lost. Jk there is nothing to disable ere
    }

    public void onBreak(Block block, int delay) {
        for (BlockFace blockFace : FACES) {
            final Block relativeBlock = block.getRelative(blockFace);
            if (Tag.LEAVES.isTagged(relativeBlock.getType())) {
                Bukkit.getServer().getScheduler().runTaskLater(this, () -> {
                    decayLeaf(relativeBlock);
                }, delay);

            }
        }
    }


    public void decayLeaf(Block block) {
        //TODO: Not randomly break
        final Logger logger = Bukkit.getLogger();
        Leaves leaves;
        if (((BlockData) block.getBlockData()) instanceof Leaves) {
            logger.info("Sucsess - " + block);
            leaves = (Leaves) ((BlockData) block.getBlockData());
        } else {
            logger.warning("Faliure - " + block);
            return;
        }


        if (leaves.isPersistent()) {
            return;
        }
        if (leaves.getDistance() > 6) {
            if (leaves.getDistance() > 6) {
                logger.info(ChatColor.COLOR_CHAR + "aBroke leaf" + block);
                block.breakNaturally();
                block.getWorld().playSound(
                        block.getLocation(),
                        Sound.BLOCK_GRASS_BREAK,
                        SoundCategory.BLOCKS,
                        0.05f, 1.2f
                );
                block.getWorld().spawnParticle(
                        Particle.BLOCK_DUST,
                        block.getLocation().add(0.5, 0.5, 0.5),
                        8, 0.2, 0.2, 0.2, 0,
                        block.getType().createBlockData()
                );
                //To make this recursive like we will call the LeafDecayEvent wich already exists on every leaf decay.
                LeavesDecayEvent event = new LeavesDecayEvent(block);
                getServer().getPluginManager().callEvent(event);
            }
        }
    }
}
