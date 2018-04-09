package de.neuenberger.games.core.resource;

import com.badlogic.gdx.files.FileHandle;

public abstract class Resource<R> {
	private ResourceType type;
	protected R resource;
	private FileHandle fileHandle;
	protected Resource(ResourceType type, FileHandle fileHandle) {
		this.type=type;
		this.fileHandle = fileHandle;
	}
	
	public ResourceType getType() {
		return type;
	}

	public R getResource() {
		if (resource==null) {
			System.out.println("Now loading: "+(fileHandle!=null?fileHandle.name():"n/a"));
			resource = loadResource(fileHandle);
		}
		return resource;
	}

	abstract R loadResource(FileHandle fileHandle2);

	public void setResource(R resource) {
		this.resource = resource;
	}
	
	public boolean isLoaded() {
		return resource!=null;
	}
	
	abstract void dispose();
}
