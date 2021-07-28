package com.thermahost.fastleafdecay;

import com.thermahost.fastleafdecay.event.LeafDecayEventListener;
import com.thermahost.fastleafdecay.event.BlockBreakEventListener;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FastLeafDecay extends JavaPlugin {
    private static Plugin plugin;

    private static final ArrayList<BlockFace> FACES = new ArrayList<>(Arrays.asList(BlockFace.values()));

    @Override
    public void onEnable() {
        //Variable initialization, needs to be first.
        plugin = this;
        FACES.remove(BlockFace.SELF);
        //------------------------------------------

        getServer().getPluginManager().registerEvents(new BlockBreakEventListener(), this);
        getServer().getPluginManager().registerEvents(new LeafDecayEventListener(), this);
    }

    @Override
    public void onDisable() {

    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static void onBreak(Block block, int delay){
        for (BlockFace blockFace : FACES){
            final Block relativeBlock = block.getRelative(blockFace);
            if(Tag.LEAVES.isTagged(relativeBlock.getType())){
                Bukkit.getServer().getScheduler().runTaskLater(FastLeafDecay.getPlugin(), ()->{
                    FastLeafDecay.decayLeaf(relativeBlock);}, delay);
            }
        }
    }

    public static boolean decayLeaf(Block block){
        //TODO: Not randomly break
        Leaves leaves;
        if(!(block.getBlockData() instanceof Leaves)){
            Bukkit.getLogger().info("Failed to get l e a f");
            if (block.getLocation().getWorld() == null) {
                return false;
            }
            leaves = (Leaves) block.getLocation().getWorld().getBlockAt(block.getLocation()).getBlockData();
        }else{
            leaves = (Leaves) block.getBlockData();
        }



        if (leaves.isPersistent()) {
            return false;
        }
        if(leaves.getDistance() > 6){
            block.breakNaturally();
            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 0.05f, 1.2f);
            block.getWorld().spawnParticle(Particle.BLOCK_DUST, block.getLocation().add(0.5, 0.5, 0.5), 8, 0.2, 0.2, 0.2, 0, block.getType().createBlockData());
            //To make this recursive like we will call the LeafDecayEvent wich already exists on every leaf decay.
            LeavesDecayEvent event = new LeavesDecayEvent(block);
            Bukkit.getServer().getPluginManager().callEvent(event);
            return true;
        }
        return false;
    }
}
