package de.neuenberger.games.core.resource;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;

public class TextureResource extends DisposableResource<Texture> {

	public TextureResource(ResourceManager resourceManager, FileHandle handle) {
		super(resourceManager, ResourceType.TEXTURE, handle);
	}

	@Override
	Texture loadResource(FileHandle fileHandle2) {
		Texture texture = new Texture(fileHandle2);
		texture.setWrap(TextureWrap.MirroredRepeat, TextureWrap.MirroredRepeat);
		return texture;
	}

}
