package baubles.common.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.NameSpace;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import ventivu.core.Core.Register.ItemBase;

import java.util.List;

public class ItemRing extends ItemBase implements IBauble {

    public ItemRing(String UnlocalizedName) {
        super(UnlocalizedName);
        setMaxStackSize(1);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings({"unchecked"})
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(this, 1, 0));
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.RING;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (!par2World.isRemote) {
            InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(par3EntityPlayer);
            for (int i = 0; i < baubles.getSizeInventory(); i++)
                if (baubles.getStackInSlot(i) == null && baubles.isItemValidForSlot(i, par1ItemStack)) {
                    baubles.setInventorySlotContents(i, par1ItemStack.copy());
                    if (!par3EntityPlayer.capabilities.isCreativeMode) {
                        par3EntityPlayer.inventory.setInventorySlotContents(par3EntityPlayer.inventory.currentItem, null);
                    }
                    onEquipped(par1ItemStack, par3EntityPlayer);
                    break;
                }
        }

        return par1ItemStack;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        if (itemstack.getItemDamage() == 0 && !player.isPotionActive(Potion.digSpeed)) {
            player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 40, 0, true));
        }
    }

    @Override
    public boolean hasEffect(ItemStack par1ItemStack, int a) {
        return true;
    }

    @Override
    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.rare;
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
        if (!player.worldObj.isRemote) {
            player.worldObj.playSoundAtEntity(player, "random.orb", 0.1F, 1.3f);
        }
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    public String getModID() {
        return NameSpace.ModID;
    }
}
