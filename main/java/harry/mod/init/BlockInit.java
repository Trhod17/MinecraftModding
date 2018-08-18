package harry.mods.tutorialmod.init;

import java.util.ArrayList;
import java.util.List;

import harry.mods.tutorialmod.Main;
import harry.mods.tutorialmod.blocks.BlockBase;
import harry.mods.tutorialmod.blocks.BlockCopperChest;
import harry.mods.tutorialmod.blocks.BlockElectricSinteringFurnace;
import harry.mods.tutorialmod.blocks.BlockGlowstoneGenerator;
import harry.mods.tutorialmod.blocks.BlockSinteringFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockInit 
{
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block COPPER_BLOCK = new BlockBase("copper_block", Material.IRON, Main.TUTORIAL);
	public static final Block COPPER_CHEST = new BlockCopperChest("copper_chest");
	
	public static final Block SINTERING_FURNACE = new BlockSinteringFurnace("sintering_furnace");
	public static final Block ELECTRIC_SINTERING_FURNACE = new BlockElectricSinteringFurnace("electric_sintering_furnace");
	public static final Block GLOWSTONE_GENERATOR = new BlockGlowstoneGenerator("glowstone_generator");
}
