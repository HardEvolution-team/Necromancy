package com.electrifiedded.necromancy.block;

import com.electrifiedded.archaiclib.v1710.api.block.ArchaicBlock;
import com.electrifiedded.archaiclib.v1710.api.client.IIcon;
import com.electrifiedded.archaiclib.v1710.api.client.IIconRegister;
import com.electrifiedded.necromancy.Necromancy;
import com.electrifiedded.necromancy.tileentity.TileEntitySkullWall;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.Random;

public class BlockSkullWall extends ArchaicBlock {
    private IIcon icon;

    public BlockSkullWall() {
        super(Material.CIRCUITS);
        setHardness(50.0F);
        setResistance(2000.0F);
        setSoundType(SoundType.STONE);
        setTranslationKey("skullWall");
        setCreativeTab(Necromancy.tabNecromancy);
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        icon = reg.registerIcon("obsidian");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return icon;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntitySkullWall();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Items.SKULL);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.SKULL;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }
}
