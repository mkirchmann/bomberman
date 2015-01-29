/**
 *
 */
package de.neuenberger.games.core.mesh;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Vector3;

/**
 * @author Michael Kirchmann, PRODYNA AG
 * @date 23.04.2013
 */
public class BoxCreator {
	short offset;

	/**
	 * @return
	 */
	public synchronized Mesh create(int faces) {
		offset = 0;
		VertexAttributes vertexAttributes = new VertexAttributes(
				new VertexAttribute(Usage.Position, 3, "a_position"),
				new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord"),
				new VertexAttribute(Usage.Normal, 3, "a_normal"));
		Mesh result = new Mesh(false, 24, 72, vertexAttributes);

		List<Float> floats = new LinkedList<Float>();
		List<Short> indices = new LinkedList<Short>();

		Vector3[] vectors = new Vector3[8];
		int idx = 0;
		for (int i = -1; i < 2; i += 2) {
			vectors[idx++] = new Vector3(-1, -1, i);
			vectors[idx++] = new Vector3(-1, 1, i);
			vectors[idx++] = new Vector3(1, 1, i);
			vectors[idx++] = new Vector3(1, -1, i);
		}

		if (faces > 5) {
			quad(floats, indices, vectors[0], vectors[1], vectors[2],
					vectors[3], new Vector3(0, 0, -1));
			quad(floats, indices, vectors[4], vectors[5], vectors[6],
					vectors[7], new Vector3(0, 0, 1));
			quad(floats, indices, vectors[0], vectors[1], vectors[5],
					vectors[4], new Vector3(-1, 0, 0));
		}
		quad(floats, indices, vectors[1], vectors[2], vectors[6], vectors[5],
				new Vector3(0, 1, 0));
		if (faces > 5) {
			quad(floats, indices, vectors[2], vectors[3], vectors[7],
					vectors[6], new Vector3(1, 0, 0));
			quad(floats, indices, vectors[3], vectors[0], vectors[4],
					vectors[7], new Vector3(0, -1, 0));
		}

		copyToMesh(result, floats, indices);
		return result;
	}

	/**
	 * @param result
	 * @param floats
	 * @param indices
	 */
	private static void copyToMesh(Mesh result, List<Float> floats,
			List<Short> indices) {
		float f[] = new float[floats.size()];
		int idx = 0;
		for (Float float1 : floats) {
			f[idx++] = float1;
		}
		short i[] = new short[indices.size()];
		idx = 0;
		for (Short short1 : indices) {
			i[idx++] = short1;
		}

		result.setVertices(f);
		result.setIndices(i);

	}

	public void quad(List<Float> floats, List<Short> indices, Vector3 a,
			Vector3 b, Vector3 c, Vector3 d, Vector3 n) {
		short position = offset;

		floats.add(a.x);
		floats.add(a.y);
		floats.add(a.z);
		floats.add(0f); // u
		floats.add(0f); // v
		floats.add(n.x);
		floats.add(n.y);
		floats.add(n.z);

		floats.add(b.x);
		floats.add(b.y);
		floats.add(b.z);
		floats.add(1f); // u
		floats.add(0f); // v
		floats.add(n.x);
		floats.add(n.y);
		floats.add(n.z);

		floats.add(c.x);
		floats.add(c.y);
		floats.add(c.z);
		floats.add(1f); // u
		floats.add(1f); // v
		floats.add(n.x);
		floats.add(n.y);
		floats.add(n.z);

		floats.add(d.x);
		floats.add(d.y);
		floats.add(d.z);
		floats.add(0f); // u
		floats.add(1f); // v
		floats.add(n.x);
		floats.add(n.y);
		floats.add(n.z);

		indices.add((short) (position + 0));
		indices.add((short) (position + 1));
		indices.add((short) (position + 2));
		indices.add((short) (position + 3));
		indices.add((short) (position + 2));
		indices.add((short) (position + 0));

//		indices.add((short) (position + 2));
//		indices.add((short) (position + 1));
//		indices.add((short) (position + 0));
//		indices.add((short) (position + 0));
//		indices.add((short) (position + 2));
//		indices.add((short) (position + 3));

		offset += 4;
	}
}
