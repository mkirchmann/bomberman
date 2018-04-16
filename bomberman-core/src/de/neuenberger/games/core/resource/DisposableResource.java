package de.neuenberger.games.core.resource;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;

public abstract class DisposableResource<R  extends Disposable> extends Resource<R> {

	protected DisposableResource(ResourceManager resourceManager, ResourceType type, FileHandle fileHandle) {
		super(resourceManager, type, fileHandle);
	}

	@Override
	abstract R loadResource(FileHandle fileHandle2);

	@Override
	void dispose() {
		if (isLoaded()) {
			getResource().dispose();
		}
		
	}

}
