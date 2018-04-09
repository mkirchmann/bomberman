package de.neuenberger.games.core.resource;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.loaders.wavefront.ObjLoader;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.MaterialAttribute;
import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.SubMesh;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import com.badlogic.gdx.graphics.g3d.model.still.StillSubMesh;

public class ObjectResource extends Resource<StillModel> {

	protected ObjectResource(FileHandle fileHandle) {
		super(ResourceType.OBJECT, fileHandle);
	}

	@Override
	StillModel loadResource(FileHandle fileHandle2) {
		return loadObjFile(fileHandle2);
	}
	private static void prepareMeshForShader(String targetName, SubMesh subMesh) {
		List<MaterialAttribute> removeAttribue = new LinkedList<MaterialAttribute>();
		if (subMesh instanceof StillSubMesh) {
			int numberOfAttributes = subMesh.material.getNumberOfAttributes();
			for (int i = 0; i < numberOfAttributes; i++) {
				MaterialAttribute attribute = subMesh.material.getAttribute(i);

				if (attribute instanceof TextureAttribute) {
					((TextureAttribute) attribute).name = targetName;
				} else if (attribute instanceof ColorAttribute) {
					System.out.println("Removing attribute " + attribute.name);
					removeAttribue.add(attribute);
				}
			}
			for (MaterialAttribute materialAttribute : removeAttribue) {
				subMesh.material.removeAttribute(materialAttribute);
			}
		}
	}
	
	public static StillModel loadObjFile(FileHandle handle) {
		ObjLoader loader = new ObjLoader();
		StillModel stillModel = loader.loadObj(handle);
		String targetName = "u_texture";
		SubMesh[] subMeshes = stillModel.getSubMeshes();
		for (SubMesh subMesh : subMeshes) {
			prepareMeshForShader(targetName, subMesh);
			VertexAttributes attributes = subMesh.getMesh()
					.getVertexAttributes();
			int size = attributes.size();
			for (int i = 0; i < size; i++) {
				VertexAttribute attribute = attributes.get(i);
				System.out.println("name: " + attribute.alias);
				switch (attribute.usage) {
				case Usage.Position:
					System.out.println("Attribute: Position");
					break;
				case Usage.Color:
					System.out.println("Attribute: Color");
					break;
				case Usage.TextureCoordinates:
					System.out.println("Attribute: uv");
					attribute.alias = "a_texCoord";
					break;
				case Usage.Normal:
					System.out.println("Attribute: normal");
					break;
				default:
					System.out.println("Unknown usage: " + attribute.usage);
				}
			}
		}
		return stillModel;
	}

	@Override
	void dispose() {
		if (isLoaded()) {
			getResource().dispose();
		}
		
	}
}