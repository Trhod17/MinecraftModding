package harry.mods.tutorialmod.handlers;

import harry.mods.tutorialmod.Reference;
import harry.mods.tutorialmod.blocks.tileentity.TileEntityCopperChest;
import harry.mods.tutorialmod.blocks.tileentity.TileEntityElectricSinteringFurnace;
import harry.mods.tutorialmod.blocks.tileentity.TileEntityGlowstoneGenerator;
import harry.mods.tutorialmod.blocks.tileentity.TileEntitySinteringFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler 
{
	public static void registerTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntityCopperChest.class, new ResourceLocation(Reference.MODID + ":copper_chest"));
		GameRegistry.registerTileEntity(TileEntitySinteringFurnace.class, new ResourceLocation(Reference.MODID + ":sintering_furnace"));
		GameRegistry.registerTileEntity(TileEntityGlowstoneGenerator.class, new ResourceLocation(Reference.MODID + ":glowstone_generator"));
		GameRegistry.registerTileEntity(TileEntityElectricSinteringFurnace.class, new ResourceLocation(Reference.MODID + ":electric_sintering_furnace"));

	}
}
