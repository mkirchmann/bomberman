package de.neuenberger.games.core.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderResource extends DisposableResource<ShaderProgram> {

	private String id;

	public ShaderResource(String id) {
		super(ResourceType.SHADER, null);
		this.id = id;
	}

	@Override
	ShaderProgram loadResource(FileHandle fileHandle2) {
		return createShader(id);
	}
	
	public static ShaderProgram createShader(String string) {
		ShaderProgram program = new ShaderProgram(
				Gdx.files.internal("shader/" + string
						+ ".vs"), Gdx.files.internal("shader/" + string + ".fs"));
		if (!program.isCompiled()) {
			System.err.println(program.getLog());
		}
		return program;
	}

}
