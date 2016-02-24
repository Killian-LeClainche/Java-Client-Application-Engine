package com.polaris.engine.gui;

import static com.polaris.engine.render.Window.gl2d;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.polaris.engine.Application;
import com.polaris.engine.Camera;
import com.polaris.engine.options.Settings;
import com.polaris.engine.render.Window;

public abstract class Gui
{

	private List<Element> elementList = new ArrayList<Element>();
	protected Element currentElement;
	protected double ticksExisted = 0;
	protected Application application;
	protected Gui parent;

	protected Camera camera;

	public Gui(Application app)
	{
		application = app;
		parent = null;
		camera = new Camera();
	}

	public Gui(Gui gui)
	{
		this(gui.application);
		parent = gui;
	}

	public void init() {}

	public void update(double delta)
	{
		ticksExisted += delta;
		for(Element element : elementList)
		{
			element.update(delta);
		}
	}

	public void render(double delta)
	{
		gl2d();
		for(Element element : elementList)
		{
			element.render(delta);
		}
	}

	public boolean mouseClick(int mouseId)
	{
		for(Element element : elementList)
		{
			if(element.isInRegion())
			{
				boolean flag = element.nMouseClick(mouseId);
				if(flag && element != currentElement)
				{
					unbindCurrentElement(element);
				}
				return flag;
			}
		}
		unbindCurrentElement();
		return false;
	}

	public void mouseHeld(int mouseId)
	{
		if(currentElement != null && currentElement.nMouseHeld(mouseId))
		{
			unbindCurrentElement();
		}
	}

	public void mouseRelease(int mouseId)
	{
		if(currentElement != null && !currentElement.nMouseRelease(mouseId))
		{
			unbindCurrentElement();
		}
	}

	public void mouseScroll(double xOffset, double yOffset) 
	{
		if(currentElement != null && currentElement.nMouseScroll(xOffset, yOffset))
		{
			unbindCurrentElement();
		}
	}

	public int keyPressed(int keyId, int mods) 
	{
		if(currentElement != null)
		{
			return currentElement.keyPressed(keyId, mods);
		}
		if(keyId == GLFW_KEY_ESCAPE)
		{
			if((mods & 1) == 1)
			{
				Settings.setNextWindow(Settings.getNextWindow() < 2 ? 2 : 0);
			}
			else
			{
				if(getParent() != null)
				{
					getParent().reinit();
					application.setGui(getParent());
					return 0;
				}
				else
				{
					Window.close();
				}
			}
		}
		return 20;
	}

	public int keyHeld(int keyId, int called, int mods)
	{
		if(currentElement != null)
		{
			return currentElement.nKeyHeld(keyId, called, mods);
		}
		return 20;
	}

	public void keyRelease(int keyId, int mods)
	{
		if(currentElement != null && currentElement.nKeyRelease(keyId, mods))
		{
			unbindCurrentElement();
		}
	}

	public void unbindCurrentElement(Element e)
	{
		unbindCurrentElement();
		currentElement = e;
	}

	public void unbindCurrentElement()
	{
		if(currentElement != null)
		{
			currentElement.unbind();
			currentElement = null;
		}
	}

	public void addElement(Element e)
	{
		e.setId(elementList.size());
		e.setGui(this);
		elementList.add(e);
	}

	public void removeElement(int i)
	{
		elementList.remove(i).close();
	}

	public void removeElements(int i, int i1)
	{
		for(int j = i1 - 1; j >= i; j--)
		{
			elementList.remove(j).close();
		}
	}

	public Element getElement(int i)
	{
		return elementList.get(i);
	}

	public int getSize()
	{
		return elementList.size();
	}

	public void elementUpdate(Element e, int actionId) {}

	public void clearElements()
	{
		elementList.clear();
	}

	protected void reinit() {}

	public void close() 
	{
		this.currentElement = null;
	}

	public Element getCurrentElement()
	{
		return currentElement;
	}

	protected Gui getParent()
	{
		return parent;
	}

	public Camera getCamera()
	{
		return camera;
	}

}
