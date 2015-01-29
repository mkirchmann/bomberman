package de.neuenberger.games.bomberman;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
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
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

import de.neuenberger.games.bomberman.presenter.BombermanPresenter;

public class BombermanGame implements ApplicationListener {
	Mesh floorTile;

	private final Matrix4 transformBox = new Matrix4();

	float acc = 50.0f;
	
	BombermanPresenter presenter = new BombermanPresenter();

	enum CamType {

		Ortho(40), Perspective(5);
		float scale;

		CamType(float scale) {
			this.scale = scale;
		}

		public float getScale() {
			return scale;
		}
	}

	CamType camType = CamType.Perspective;
	private StillModel sphere;

	@Override
	public void create() {
		sphere = loadObjFile("sphere.obj");

		presenter.create();

	}

	public static StillModel loadObjFile(String filename) {
		ObjLoader loader = new ObjLoader();
		StillModel stillModel = loader.loadObj(Gdx.files
				.internal(getAssetsDirectory() + filename));
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

	public static String getAssetsDirectory() {
		return System.getProperty("assets.dir", "");
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

	public static ShaderProgram createShader(String string) {
		ShaderProgram program = new ShaderProgram(
				Gdx.files.internal(getAssetsDirectory() + "shader/" + string
						+ ".vs"), Gdx.files.internal(getAssetsDirectory()
						+ "shader/" + string + ".fs"));
		if (!program.isCompiled()) {
			System.err.println(program.getLog());
		}
		return program;
	}

	public void resize(int width, int height) {
		presenter.resize(width,height);
	}

	@Override
	public void render() {
		presenter.render();
	}

	private void renderDebugSpheres(ShaderProgram shader) {
		for (int x = -1; x < 2; x += 2) {
			for (int y = -1; y < 2; y += 2) {
				for (int z = -1; z < 2; z += 2) {
					//transformBox.set(camera.combined);
					transformBox.translate(x, y, z);
					shader.setUniformMatrix("u_projTrans", transformBox);
					sphere.render(shader);
				}
			}
		}
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		presenter.dispose();
	}

	public Mesh getFloorTile() {
		return floorTile;
	}

}
