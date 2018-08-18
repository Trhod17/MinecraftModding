package harry.mods.tutorialmod.handlers;

import harry.mods.tutorialmod.Reference;
import harry.mods.tutorialmod.blocks.container.ContainerCopperChest;
import harry.mods.tutorialmod.blocks.container.ContainerElectricSinteringFurnace;
import harry.mods.tutorialmod.blocks.container.ContainerGlowstoneGenerator;
import harry.mods.tutorialmod.blocks.container.ContainerSinteringFurnace;
import harry.mods.tutorialmod.blocks.gui.GuiCopperChest;
import harry.mods.tutorialmod.blocks.gui.GuiElectricSinteringFurnace;
import harry.mods.tutorialmod.blocks.gui.GuiGlowstoneGenerator;
import harry.mods.tutorialmod.blocks.gui.GuiSinteringFurnace;
import harry.mods.tutorialmod.blocks.tileentity.TileEntityCopperChest;
import harry.mods.tutorialmod.blocks.tileentity.TileEntityElectricSinteringFurnace;
import harry.mods.tutorialmod.blocks.tileentity.TileEntityGlowstoneGenerator;
import harry.mods.tutorialmod.blocks.tileentity.TileEntitySinteringFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		if(ID == Reference.GUI_SINTERING_FURNACE) return new ContainerSinteringFurnace(player.inventory, (TileEntitySinteringFurnace)world.getTileEntity(new BlockPos(x,y,z)));
		if(ID == Reference.GUI_COPPER_CHEST) return new ContainerCopperChest(player.inventory, (TileEntityCopperChest)world.getTileEntity(new BlockPos(x,y,z)), player);
		if(ID == Reference.GUI_GLOWSTONE_GENERATOR) return new ContainerGlowstoneGenerator(player.inventory, (TileEntityGlowstoneGenerator)world.getTileEntity(new BlockPos(x,y,z)));
		if(ID == Reference.GUI_ELECTRIC_SINTERING_FURNACE) return new ContainerElectricSinteringFurnace(player.inventory, (TileEntityElectricSinteringFurnace)world.getTileEntity(new BlockPos(x,y,z)));
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		if(ID == Reference.GUI_SINTERING_FURNACE) return new GuiSinteringFurnace(player.inventory, (TileEntitySinteringFurnace)world.getTileEntity(new BlockPos(x,y,z)));
		if(ID == Reference.GUI_COPPER_CHEST) return new GuiCopperChest(player.inventory, (TileEntityCopperChest)world.getTileEntity(new BlockPos(x,y,z)), player);
		if(ID == Reference.GUI_GLOWSTONE_GENERATOR) return new GuiGlowstoneGenerator(player.inventory, (TileEntityGlowstoneGenerator)world.getTileEntity(new BlockPos(x,y,z)));
		if(ID == Reference.GUI_ELECTRIC_SINTERING_FURNACE) return new GuiElectricSinteringFurnace(player.inventory, (TileEntityElectricSinteringFurnace)world.getTileEntity(new BlockPos(x,y,z)));
		return null;
	}
}
