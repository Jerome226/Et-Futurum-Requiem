package ganymedes01.etfuturum.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ganymedes01.etfuturum.EtFuturum;
import ganymedes01.etfuturum.IConfigurable;
import ganymedes01.etfuturum.configuration.ConfigurationHandler;
import ganymedes01.etfuturum.core.utils.Utils;
import ganymedes01.etfuturum.lib.RenderIDs;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockWoodTrapdoor extends BlockTrapDoor implements IConfigurable{
	
	private final int meta;

	public BlockWoodTrapdoor(int meta) {
		super(Material.wood);
		this.meta = meta;
		disableStats();
		setHardness(3.0F);
		setStepSound(soundTypeWood);
		setBlockName(Utils.getUnlocalisedName("trapdoor_" + BlockWoodDoor.names[meta]));
		setCreativeTab(isEnabled() ? EtFuturum.creativeTabBlocks : null);
		
	}
	/*
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return Blocks.planks.getIcon(side, this.meta);
	}
	*/
	
	public IIcon[] icon = new IIcon[6];
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister ir) {
		
		//this.icon[0] = ir.registerIcon("minecraft:trapdoor");
		this.icon[1] = ir.registerIcon("minecraft:spruce_trapdoor");
		this.icon[2] = ir.registerIcon("minecraft:birch_trapdoor");
		this.icon[3] = ir.registerIcon("minecraft:jungle_trapdoor");
		this.icon[4] = ir.registerIcon("minecraft:acacia_trapdoor");
		this.icon[5] = ir.registerIcon("minecraft:dark_oak_trapdoor");
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return this.icon[this.meta];
	}

	
	@Override
	public int getRenderType()
	{
		return RenderIDs.TRAP_DOOR;
	}

	@Override
	public boolean isEnabled() {
//      if(meta == 6 && !ConfigurationHandler.enableCrimsonBlocks)
//          return false;
//      if(meta == 7 && !ConfigurationHandler.enableWarpedBlocks)
//          return false;
		return ConfigurationHandler.enableTrapdoors;
	}

}
