package de.neuenberger.games.core.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;

public class ResourceManager {
	private Map<String, Resource> resourceMap = new HashMap<String, Resource>(100);
	
	AssetManager assetManager = new AssetManager();
	
	private static final ResourceManager instance = new ResourceManager();
	int loaded;
	public Resource addLoadRequest(String id, ResourceType type) {
		return addLoadRequest(id, type, null);
	}
	
	public void addResource(String id, Resource res) {
		resourceMap.put(id, res);
	}

	public <M> Resource<M> getResource(String id, Class<M> class1) {
		return Resource.class.cast(getResource(id));
	}
	
	public Resource getResource(String id) {
		return resourceMap.get(id);
	}
	
	public Resource addLoadRequest(String id, ResourceType type, FileHandle handle) {
		Resource resource = resourceMap.get(id);
		if (resource==null) {
			if (handle==null) {
				handle = Gdx.files.internal(id);
			}
			switch (type) {
			case OBJECT:
				resource = new ObjectResource(this, handle);
				break;
			case TEXTURE:
				resource = new TextureResource(this, handle);
				break;
			case FONT:
				resource = new FontResource(this, id);
				break;
			default:
				throw new IllegalArgumentException("Invalid type: "+type);
			}
			addResource(id, resource);
		}
		return resource;
	}
	
	public void loadResources() {
		int size = resourceMap.size();
		Set<Entry<String,Resource>> set = resourceMap.entrySet();
		for (Entry<String, Resource> entry : set) {
			Resource value = entry.getValue();
			if (!value.isLoaded()) {
				value.getResource();
				loaded++;
			}
		}
	}
	
	public double getLoadingPercentag() {
		int size = resourceMap.size();
		
		return (double)loaded/size;
	}

	public static ResourceManager getInstance() {
		return instance;
	}
	
	public void dispose() {
		Set<Entry<String,Resource>> set = resourceMap.entrySet();
		for (Entry<String, Resource> entry : set) {
			entry.getValue().dispose();
		}
		set.clear();
	}

	public boolean isAllLoaded() {
		boolean result = resourceMap.size()<=loaded;
		if (!result) {
			loadNext();
			result = resourceMap.size()<=loaded;
		}
		return result;
	}

	private void loadNext() {
		Set<Entry<String,Resource>> set = resourceMap.entrySet();
		boolean loadedSomething = false;
		for (Entry<String, Resource> entry : set) {
			Resource value = entry.getValue();
			if (!value.isLoaded()) {
				value.getResource();
				loaded++;
				loadedSomething = true;
				break;
			}
		}
		if (!loadedSomething) {
			loaded=set.size();
		}
	}

	
	
}
