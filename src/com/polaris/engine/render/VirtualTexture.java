package com.polaris.engine.render;

public class VirtualTexture implements ITexture
{

	private int textureId = 0;
	
	@Override
	public int getTextureID()
	{
		return textureId;
	}
	
	@Override
	public void setTextureID(int id)
	{
		textureId = id;
	}

}
