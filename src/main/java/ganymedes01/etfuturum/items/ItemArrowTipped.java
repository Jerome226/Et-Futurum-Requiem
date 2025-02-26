package ganymedes01.etfuturum.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ganymedes01.etfuturum.EtFuturum;
import ganymedes01.etfuturum.ModItems;
import ganymedes01.etfuturum.blocks.IConfigurable;
import ganymedes01.etfuturum.configuration.configs.ConfigBlocksItems;
import ganymedes01.etfuturum.core.utils.Utils;
import ganymedes01.etfuturum.dispenser.DispenserBehaviourTippedArrow;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.Constants;

public class ItemArrowTipped extends Item implements IConfigurable {

	@SideOnly(Side.CLIENT)
	private IIcon tipIcon;

	public ItemArrowTipped() {
		setTextureName("tipped_arrow");
		setUnlocalizedName(Utils.getUnlocalisedName("tipped_arrow"));
		setCreativeTab(isEnabled() ? EtFuturum.creativeTabItems : null);

		if (ConfigBlocksItems.enableTippedArrows)
			BlockDispenser.dispenseBehaviorRegistry.putObject(this, new DispenserBehaviourTippedArrow());
	}

	public static PotionEffect getEffect(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Potion", Constants.NBT.TAG_COMPOUND)) {
			NBTTagCompound nbt = stack.getTagCompound().getCompoundTag("Potion");
			return PotionEffect.readCustomPotionEffectFromNBT(nbt);
		}
		return null;
	}

	public static ItemStack setEffect(ItemStack stack, Potion potion, int duration, int potency) {
		stack.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = new NBTTagCompound();
		stack.getTagCompound().setTag("Potion", nbt);

		PotionEffect effect = new PotionEffect(potion.getId(), potion.isInstant() ? 1 : duration, potency);
		effect.writeCustomPotionEffectToNBT(nbt);

		return stack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		List<ItemStack> potions = new ArrayList<ItemStack>();
		ModItems.lingering_potion.getSubItems(ModItems.lingering_potion, tab, potions);
		for (ItemStack potion : potions) {
			List<PotionEffect> effects = PotionHelper.getPotionEffects(potion.getItemDamage(), false);
			if (effects != null && !effects.isEmpty())
				for (PotionEffect effect : effects)
					list.add(setEffect(new ItemStack(this), Potion.potionTypes[effect.getPotionID()], effect.getDuration() / 2, effect.getAmplifier()) );
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		itemIcon = reg.registerIcon(getIconString() + "_base");
		tipIcon = reg.registerIcon(getIconString() + "_head");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass) {
		PotionEffect effect = getEffect(stack);
		if (effect == null || effect.getPotionID() < 0 || effect.getPotionID() >= Potion.potionTypes.length)
			return super.getColorFromItemStack(stack, pass);
		return pass == 0 ? Potion.potionTypes[effect.getPotionID()].getLiquidColor() : super.getColorFromItemStack(stack, pass);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass) {
		return pass == 0 ? tipIcon : super.getIcon(stack, pass);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		PotionEffect effect = getEffect(stack);
		if (effect == null || effect.getPotionID() < 0 || effect.getPotionID() >= Potion.potionTypes.length)
			//return super.getUnlocalizedName(stack); 
			return "tipped_arrow.effect.empty";

		Potion potion = Potion.potionTypes[effect.getPotionID()];
		return "tipped_arrow." + potion.getName();
	}

	@Override
	public boolean isEnabled() {
		return ConfigBlocksItems.enableTippedArrows;
	}
	
	
	
	
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack stack, EntityPlayer p_77624_2_, List list, boolean p_77624_4_)
	{
		if (stack.getItemDamage() == 0)
		{
			
			PotionEffect effect = ItemArrowTipped.getEffect(stack);
			if (effect != null) {
				
			
			/*
			List list1 = ModItems.tipped_arrow.getEffects(stack);
			HashMultimap hashmultimap = HashMultimap.create();
			Iterator iterator1;

			if (list1 != null && !list1.isEmpty())
			{
				iterator1 = list1.iterator();

				while (iterator1.hasNext())
				{    */
					//PotionEffect potioneffect = (PotionEffect)iterator1.next();
					PotionEffect potioneffect = ItemArrowTipped.getEffect(stack);
					String s1 = StatCollector.translateToLocal(potioneffect.getEffectName()).trim();
					Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
					Map<?, ?> map = potion.func_111186_k();

					if (map != null && map.size() > 0)
					{
						//Iterator<?> iterator = map.entrySet().iterator();
						// TODO This did nothing at all
						//while (iterator.hasNext())
						//{
						//  Entry<?, ?> entry = (Entry<?, ?>)iterator.next();
						//  AttributeModifier attributemodifier = (AttributeModifier)entry.getValue(); 
						//  AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.func_111183_a(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
						//  //hashmultimap.put(((IAttribute)entry.getKey()).getAttributeUnlocalizedName(), attributemodifier1);
						//}
					}

					if (potioneffect.getAmplifier() > 0)
					{
						s1 = s1 + " " + StatCollector.translateToLocal("potion.potency." + potioneffect.getAmplifier()).trim();
					}

					if (potioneffect.getDuration() > 20)
					{
						s1 = s1 + " (" + Potion.getDurationString(potioneffect) + ")";
					}

					if (potion.isBadEffect())
					{
						list.add(EnumChatFormatting.RED + s1);
					}
					else
					{
						list.add(EnumChatFormatting.GRAY + s1);
					}
			}
		}
	}
	
}