package de.neuenberger.games.core.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class FontResource extends DisposableResource<BitmapFont> {

	private FileHandle descriptionFile;

	protected FontResource(ResourceManager resourceManager, String name) {
		super(resourceManager, ResourceType.FONT, Gdx.files.internal(name + ".png"));
		descriptionFile = Gdx.files.internal(name+".fnt");
	}

	@Override
	BitmapFont loadResource(FileHandle fileHandle2) {
		return new BitmapFont(descriptionFile, fileHandle2, true);
	}

}
