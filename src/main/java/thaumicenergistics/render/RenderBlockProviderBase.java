package thaumicenergistics.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import thaumicenergistics.registries.Renderers;
import thaumicenergistics.tileentities.TileProviderBase;
import appeng.api.util.AEColor;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public abstract class RenderBlockProviderBase
	implements ISimpleBlockRenderingHandler
{

	private boolean renderWorldPass( final double x, final double y, final double z, final AEColor overlayColor, int blockBrightness,
										final boolean isActive )
	{
		// Get the tessellator instance
		Tessellator tessellator = Tessellator.instance;

		IIcon texture;

		// Is this the opaque pass?
		if( Renderers.currentRenderPass == Renderers.PASS_OPAQUE )
		{
			// Set texture to base
			texture = this.getBaseTexture();

			// Set the drawing color to full white
			tessellator.setColorRGBA( 255, 255, 255, 255 );
		}
		else
		{
			// Are we transparent?
			if( overlayColor == AEColor.Transparent )
			{
				// Are we active?
				if( isActive )
				{
					// Nothing additional to render
					return false;
				}

				// Inactive, set the drawing color to black
				tessellator.setColorRGBA( 0, 0, 0, 255 );
			}
			else
			{
				// Set the drawing color
				tessellator.setColorOpaque_I( overlayColor.mediumVariant );

				// Are we active?
				if( !isActive )
				{
					// Adjust brightness
					blockBrightness = 0x300030;
				}

			}

			// Set the texture to overlay
			texture = this.getOverlayTexture();
		}

		// Set the brightness
		tessellator.setBrightness( blockBrightness );

		// Get the texture coords
		double minU = texture.getMinU();
		double maxU = texture.getMaxU();
		double minV = texture.getMinV();
		double maxV = texture.getMaxV();

		// Calculate positions
		double zNorth = z + 1.0D;
		double zSouth = z;
		double xEast = x + 1.0D;
		double xWest = x;
		double yUp = y + 1.0D;
		double yDown = y;

		// North face
		tessellator.addVertexWithUV( xWest, yDown, zNorth, minU, maxV );
		tessellator.addVertexWithUV( xEast, yDown, zNorth, maxU, maxV );
		tessellator.addVertexWithUV( xEast, yUp, zNorth, maxU, minV );
		tessellator.addVertexWithUV( xWest, yUp, zNorth, minU, minV );

		// South face
		tessellator.addVertexWithUV( xWest, yUp, zSouth, maxU, minV );
		tessellator.addVertexWithUV( xEast, yUp, zSouth, minU, minV );
		tessellator.addVertexWithUV( xEast, yDown, zSouth, minU, maxV );
		tessellator.addVertexWithUV( xWest, yDown, zSouth, maxU, maxV );

		// East face
		tessellator.addVertexWithUV( xEast, yDown, zSouth, maxU, maxV );
		tessellator.addVertexWithUV( xEast, yUp, zSouth, maxU, minV );
		tessellator.addVertexWithUV( xEast, yUp, zNorth, minU, minV );
		tessellator.addVertexWithUV( xEast, yDown, zNorth, minU, maxV );

		// West face
		tessellator.addVertexWithUV( xWest, yDown, zNorth, maxU, maxV );
		tessellator.addVertexWithUV( xWest, yUp, zNorth, maxU, minV );
		tessellator.addVertexWithUV( xWest, yUp, zSouth, minU, minV );
		tessellator.addVertexWithUV( xWest, yDown, zSouth, minU, maxV );

		// Up face
		tessellator.addVertexWithUV( xWest, yUp, zNorth, maxU, minV );
		tessellator.addVertexWithUV( xEast, yUp, zNorth, minU, minV );
		tessellator.addVertexWithUV( xEast, yUp, zSouth, minU, maxV );
		tessellator.addVertexWithUV( xWest, yUp, zSouth, maxU, maxV );

		// Down face
		tessellator.addVertexWithUV( xWest, yDown, zSouth, minU, maxV );
		tessellator.addVertexWithUV( xEast, yDown, zSouth, maxU, maxV );
		tessellator.addVertexWithUV( xEast, yDown, zNorth, maxU, minV );
		tessellator.addVertexWithUV( xWest, yDown, zNorth, minU, minV );

		// We did some drawing
		return true;
	}

	protected abstract IIcon getBaseTexture();

	protected abstract IIcon getOverlayTexture();

	@Override
	public final void renderInventoryBlock( final Block block, final int metadata, final int modelId, final RenderBlocks renderer )
	{
		// Get the tessellator instance
		Tessellator tessellator = Tessellator.instance;

		IIcon texture = this.getBaseTexture();

		GL11.glTranslatef( -0.5F, -0.5F, -0.5F );

		tessellator.startDrawingQuads();
		tessellator.setNormal( 0.0F, -1.0F, 0.0F );
		renderer.renderFaceYNeg( block, 0.0D, 0.0D, 0.0D, texture );
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal( 0.0F, 1.0F, 0.0F );
		renderer.renderFaceYPos( block, 0.0D, 0.0D, 0.0D, texture );
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal( 0.0F, 0.0F, -1.0F );
		renderer.renderFaceZNeg( block, 0.0D, 0.0D, 0.0D, texture );
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal( 0.0F, 0.0F, 1.0F );
		renderer.renderFaceZPos( block, 0.0D, 0.0D, 0.0D, texture );
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal( -1.0F, 0.0F, 0.0F );
		renderer.renderFaceXNeg( block, 0.0D, 0.0D, 0.0D, texture );
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal( 1.0F, 0.0F, 0.0F );
		renderer.renderFaceXPos( block, 0.0D, 0.0D, 0.0D, texture );
		tessellator.draw();

		GL11.glTranslatef( 0.5F, 0.5F, 0.5F );
	}

	@Override
	public final boolean renderWorldBlock( final IBlockAccess world, final int x, final int y, final int z, final Block block, final int modelId,
											final RenderBlocks renderer )
	{
		// Calculate the brightness based on light hitting each face
		int blockBrightness = world.getLightBrightnessForSkyBlocks( x + 1, y, z, 0 ) | world.getLightBrightnessForSkyBlocks( x - 1, y, z, 0 ) |
						world.getLightBrightnessForSkyBlocks( x, y + 1, z, 0 ) | world.getLightBrightnessForSkyBlocks( x, y - 1, z, 0 ) |
						world.getLightBrightnessForSkyBlocks( x, y, z + 1, 0 ) | world.getLightBrightnessForSkyBlocks( x, y, z - 1, 0 );

		AEColor overlayColor = null;

		boolean isActive = false;

		// Is this the alpha pass?
		if( Renderers.currentRenderPass == Renderers.PASS_ALPHA )
		{
			// Get the provider
			TileProviderBase provider = (TileProviderBase)world.getTileEntity( x, y, z );

			// Get the color of the provider
			overlayColor = provider.getColor();

			// Get the active state
			isActive = provider.isActive();
		}

		// Render and return 
		return this.renderWorldPass( x, y, z, overlayColor, blockBrightness, isActive );
	}

	@Override
	public final boolean shouldRender3DInInventory( final int modelId )
	{
		return true;
	}

}
