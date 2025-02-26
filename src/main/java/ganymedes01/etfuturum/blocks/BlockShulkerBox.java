package ganymedes01.etfuturum.blocks;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ganymedes01.etfuturum.EtFuturum;
import ganymedes01.etfuturum.ModBlocks.ISubBlocksBlock;
import ganymedes01.etfuturum.configuration.configs.ConfigBlocksItems;
import ganymedes01.etfuturum.core.utils.Utils;
import ganymedes01.etfuturum.items.block.ItemShulkerBox;
import ganymedes01.etfuturum.lib.GUIsID;
import ganymedes01.etfuturum.tileentities.TileEntityShulkerBox;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockShulkerBox extends BlockContainer implements IConfigurable, ISubBlocksBlock {
	
//  @SideOnly(Side.CLIENT)
	public IIcon[] colorIcons = new IIcon[17];

	public BlockShulkerBox() {
		super(Material.rock);
		this.setStepSound(soundTypeStone);
		this.setHardness(2.5F);
		this.setHarvestLevel("pickaxe", 0);
		this.setResistance(2.5F);
		this.setBlockName(Utils.getUnlocalisedName("shulker_box"));
		this.setBlockTextureName("shulker_box");
		this.isBlockContainer = true;
		this.setCreativeTab(isEnabled() ? EtFuturum.creativeTabBlocks : null);
	}
	
	public int damageDropped(int i)
	{
		return ConfigBlocksItems.enableIronShulkerBoxes ? i % (TileEntityShulkerBox.tiers.length + 1) : 0;
	}

	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
	{
		if(world.getBlockMetadata(x, y, z) >= 7) return 10000F;
		return super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
	}
	
	@Override
	public int getMobilityFlag() {
		return 1;
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
		TileEntityShulkerBox box = (TileEntityShulkerBox)world.getTileEntity(x, y, z);
		if(stack.hasTagCompound()) {
			NBTTagList nbttaglist = stack.getTagCompound().getTagList("Items", 10);
			box.chestContents = new ItemStack[box.getSizeInventory()];
			Utils.loadItemStacksFromNBT(nbttaglist, box.chestContents);
			
			if(stack.getTagCompound().hasKey("Color")) {
				box.color = stack.getTagCompound().getByte("Color");
			}

			if (stack.hasDisplayName())
			{
				box.func_145976_a(stack.getDisplayName());
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister i)
	{
		blockIcon = colorIcons[0] = i.registerIcon("shulker_box");
		colorIcons[1] = i.registerIcon("white_shulker_box");
		colorIcons[2] = i.registerIcon("orange_shulker_box");
		colorIcons[3] = i.registerIcon("magenta_shulker_box");
		colorIcons[4] = i.registerIcon("light_blue_shulker_box");
		colorIcons[5] = i.registerIcon("yellow_shulker_box");
		colorIcons[6] = i.registerIcon("lime_shulker_box");
		colorIcons[7] = i.registerIcon("pink_shulker_box");
		colorIcons[8] = i.registerIcon("gray_shulker_box");
		colorIcons[9] = i.registerIcon("light_gray_shulker_box");
		colorIcons[10] = i.registerIcon("cyan_shulker_box");
		colorIcons[11] = i.registerIcon("purple_shulker_box");
		colorIcons[12] = i.registerIcon("blue_shulker_box");
		colorIcons[13] = i.registerIcon("brown_shulker_box");
		colorIcons[14] = i.registerIcon("green_shulker_box");
		colorIcons[15] = i.registerIcon("red_shulker_box");
		colorIcons[16] = i.registerIcon("black_shulker_box");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		return world.getTileEntity(x, y, z) instanceof TileEntityShulkerBox ? Blocks.glowstone.getIcon(0, 0) : Blocks.redstone_block.getIcon(0, 0);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		if (world.isRemote)
		{
			return true;
		}
		TileEntity tileentity = world.getTileEntity(x, y, z);

		if (tileentity instanceof TileEntityShulkerBox)
		{
			ForgeDirection dir = ForgeDirection.getOrientation(((TileEntityShulkerBox) tileentity).facing);
			boolean flag;

			if (((TileEntityShulkerBox)tileentity).func_190591_p() == TileEntityShulkerBox.AnimationStatus.CLOSED)
			{
				AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1)
				.addCoord((double)(0.5F * (float)dir.offsetX), (double)(0.5F * (float)dir.offsetY), (double)(0.5F * (float)dir.offsetZ));
				axisalignedbb.addCoord(-(double)dir.offsetX, -(double)dir.offsetY, -(double)dir.offsetZ);
				
				flag = canOpen(axisalignedbb, world, tileentity);
			}
			else
			{
				flag = true;
			}

			if (flag)
			{
				player.openGui(EtFuturum.instance, GUIsID.SHULKER_BOX, world, x, y, z);
			}

			return true;
		}
		return false;
	}

//  public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
//  {
//      TileEntity tileentity = world.getTileEntity(x, y, z);
//      
//      this.setBlockBounds(0, 0, 0, 1, 1, 1);
//       
//      if(tileentity instanceof TileEntityShulkerBox && ((TileEntityShulkerBox)tileentity).func_190591_p() == TileEntityShulkerBox.AnimationStatus.CLOSED) {
//          AxisAlignedBB bb = ((TileEntityShulkerBox)tileentity).func_190584_a(((TileEntityShulkerBox)tileentity).facing);
//          setBlockBounds((float)bb.minX, (float)bb.minY, (float)bb.minZ, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ);
//      }
//  }
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		TileEntity tileentity = world.getTileEntity(x, y, z);
		return tileentity instanceof TileEntityShulkerBox ? ((TileEntityShulkerBox)tileentity).func_190584_a(((TileEntityShulkerBox)tileentity).facing).offset(x, y, z) : super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}
	
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_, int p_149633_2_, int p_149633_3_, int p_149633_4_)
	{
		return this.getCollisionBoundingBoxFromPool(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
	}
	
	private boolean canOpen(AxisAlignedBB bb, World world, TileEntity tile) {
		List<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();
		int i = MathHelper.floor_double(bb.minX);
		int j = MathHelper.floor_double(bb.maxX + 1.0D);
		int k = MathHelper.floor_double(bb.minY);
		int l = MathHelper.floor_double(bb.maxY + 1.0D);
		int i1 = MathHelper.floor_double(bb.minZ);
		int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);

		for (int k1 = i; k1 < j; ++k1)
		{
			for (int l1 = i1; l1 < j1; ++l1)
			{
				for (int i2 = k - 1; i2 < l; ++i2)
				{
					if(k1 == tile.xCoord && i2 == tile.yCoord && l1 == tile.zCoord) continue;
					Block block;

					if (k1 >= -30000000 && k1 < 30000000 && l1 >= -30000000 && l1 < 30000000)
					{
						block = world.getBlock(k1, i2, l1);
					}
					else
					{
						block = Blocks.stone;
					}

					block.addCollisionBoxesToList(world, k1, i2, l1, bb, boxes, null);
				}
			}
		}
		return boxes.isEmpty();
	}

	public IInventory func_149951_m(World p_149951_1_, int p_149951_2_, int p_149951_3_, int p_149951_4_)
	{
		Object object = p_149951_1_.getTileEntity(p_149951_2_, p_149951_3_, p_149951_4_);

		if(object == null)
			return null;
		
		return (IInventory)object;
	}
	
	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int p_149690_5_, float p_149690_6_, int p_149690_7_)
	{
		super.dropBlockAsItemWithChance(world, x, y, z, p_149690_5_, p_149690_6_, p_149690_7_);
	}
	
	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		if (!((TileEntityShulkerBox)world.getTileEntity(x, y, z)).onBlockBreak(player)) return false;
		return super.removedByPlayer(world, player, x, y, z);
	}
	
	@Override
	public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
		TileEntityShulkerBox container = (TileEntityShulkerBox) world.getTileEntity(x, y, z);
		if (container != null) container.onBlockDestroyed();
	}
	protected void dropBlockAsItem(World p_149642_1_, int p_149642_2_, int p_149642_3_, int p_149642_4_, ItemStack p_149642_5_)
	{
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	protected boolean canSilkHarvest() {
		return false;
	}

	@Override
	public int getRenderType() {
		return 22;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new TileEntityShulkerBox(p_149915_2_);
	}

	@Override
	public Class<? extends ItemBlock> getItemBlockClass() {
		return ItemShulkerBox.class;
	}

	public IIcon getIcon(int side, int metadata)
	{
		/*
		 * This icon is a mask (or something) for redstone wire.
		 * We use it here because it renders an invisible icon.
		 *
		 * Using an invisible icon is important because sprint particles are
		 * hard-coded and will always grab particle icons using this method.
		 * We'll throw our own sprint particles in ClientEventHandler.class.
		 * 
		 * Thanks to Mineshopper from Carpenter's Blocks for this trick
		 * and the references used to get particles from tile entity data.
		 */

		return BlockRedstoneWire.getRedstoneWireIcon("cross_overlay");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, int x, int y, int z, int metadata, EffectRenderer effectRenderer)
	{
		/*
		 * We don't have the ability to accurately determine the entity that is
		 * hitting the block. So, instead we're guessing based on who is
		 * closest. This should be adequate most of the time.
		 */

		TileEntityShulkerBox TE = (TileEntityShulkerBox)world.getTileEntity(x, y, z);

		if (TE != null) {

			byte b0 = 4;
			
			for (int i1 = 0; i1 < b0; ++i1)
			{
				for (int j1 = 0; j1 < b0; ++j1)
				{
					for (int k1 = 0; k1 < b0; ++k1)
					{
						double d0 = (double)x + ((double)i1 + 0.5D) / (double)b0;
						double d1 = (double)y + ((double)j1 + 0.5D) / (double)b0;
						double d2 = (double)z + ((double)k1 + 0.5D) / (double)b0;
						EntityDiggingFX dig = new EntityDiggingFX(world, d0, d1, d2, d0 - x - 0.5D, d1 - y - 0.5D, d2 - z - 0.5D, this, 0);
						dig.setParticleIcon(colorIcons[TE.color]);
						effectRenderer.addEffect((dig).applyColourMultiplier(x, y, z));
					}
				}
			}
		}

		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer)
	{
		int x = target.blockX;
		int y = target.blockY;
		int z = target.blockZ;
		int side = target.sideHit;
		
		Block block = world.getBlock(x, y, z);

		TileEntityShulkerBox TE = (TileEntityShulkerBox)world.getTileEntity(x, y, z);

		if (block.getMaterial() != Material.air && TE != null)
		{
			float f = 0.1F;
			double d0 = (double)x + world.rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinX();
			double d1 = (double)y + world.rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinY();
			double d2 = (double)z + world.rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinZ();

			if (side == 0)
			{
				d1 = (double)y + block.getBlockBoundsMinY() - (double)f;
			}

			if (side == 1)
			{
				d1 = (double)y + block.getBlockBoundsMaxY() + (double)f;
			}

			if (side == 2)
			{
				d2 = (double)z + block.getBlockBoundsMinZ() - (double)f;
			}

			if (side == 3)
			{
				d2 = (double)z + block.getBlockBoundsMaxZ() + (double)f;
			}

			if (side == 4)
			{
				d0 = (double)x + block.getBlockBoundsMinX() - (double)f;
			}

			if (side == 5)
			{
				d0 = (double)x + block.getBlockBoundsMaxX() + (double)f;
			}

			EntityDiggingFX dig = new EntityDiggingFX(world, d0, d1, d2, 0.0D, 0.0D, 0.0D, this, 0);
			dig.setParticleIcon(colorIcons[TE.color]);
			effectRenderer.addEffect((dig).applyColourMultiplier(x, y, z).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
		}
		
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List subItems) {
		for (byte i = 0; i <= (ConfigBlocksItems.enableIronShulkerBoxes ? 7 : 0); i++) {
			for (byte j = 0; j <= (ConfigBlocksItems.enableDyedShulkerBoxes ? 16 : 0); j++) {
				
				NBTTagCompound tag = new NBTTagCompound();
				ItemStack stack = new ItemStack(item, 1, i);
				
				if(j > 0) {
					tag.setByte("Color", j);
				}
				
				if(j > 0 || i > 0) {
					stack.setTagCompound(tag);
				}
				
				subItems.add(stack);
			}
		}
	}
	
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
	{
		ItemStack stack = new ItemStack(this, 1, damageDropped(world.getBlockMetadata(x, y, z)));
		TileEntityShulkerBox box = (TileEntityShulkerBox) world.getTileEntity(x, y, z);
		
		if(box != null) {
			if(box.color > 0 && ConfigBlocksItems.enableDyedShulkerBoxes) {
				if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setByte("Color", box.color);
			}
		}

		return stack;
	}

	@Override
	public boolean isEnabled() {
		return ConfigBlocksItems.enableShulkerBoxes;
	}
}
