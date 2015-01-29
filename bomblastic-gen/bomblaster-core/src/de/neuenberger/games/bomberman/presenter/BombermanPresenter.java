package de.neuenberger.games.bomberman.presenter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g3d.model.SubMesh;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import de.neuenberger.games.bomberman.BombermanGame;
import de.neuenberger.games.bomberman.model.Antenna;
import de.neuenberger.games.bomberman.model.BombermanModel;
import de.neuenberger.games.bomberman.model.Exit;
import de.neuenberger.games.bomberman.model.IDynamicCellContent;
import de.neuenberger.games.bomberman.model.ILivecontent;
import de.neuenberger.games.bomberman.model.LevelMapFactory;
import de.neuenberger.games.bomberman.model.Player;
import de.neuenberger.games.bomberman.model.Transparent;
import de.neuenberger.games.bomberman.model.Wall;
import de.neuenberger.games.core.interaction.ControllablePosition;
import de.neuenberger.games.core.mesh.BoxCreator;
import de.neuenberger.games.core.model.MapPosition;
import de.neuenberger.games.core.model.NCell;
import de.neuenberger.games.core.model.NCellContent;

public class BombermanPresenter implements PropertyChangeListener {
	public static final float SPACE_PER_TILE = 2f;
	Texture textureConcrete;
	Texture textureFire;
	Texture textureFloor;
	Texture textureExit;

	BombermanModel model;
	Matrix4 transform = new Matrix4();
	private Mesh floorTile;
	private Mesh boxMesh;
	private ShaderProgram shaderLightning;
	private PerspectiveCamera camera;

	ControllablePosition position = new ControllablePosition();
	private Vector3 lightdir;
	private StillModel sphere;
	private StillModel player;

	private PlayerControl playerControl;

	private LevelMapFactory factory;
	private Texture textureBombableWall;

	List<BaseSubPresenter> subRenderer = new LinkedList<BaseSubPresenter>();
	private StillModel gravestone;

	private StillModel tree;
	private StillModel antenna;
	private StillModel glasssphere;
	private StillModel levelcomplete;
	private Texture textureGlassphere;
	private StillModel gameOver;

	public void create() {
		lightdir = new Vector3(0.5f, 1f, -0.75f);
		shaderLightning = BombermanGame.createShader("spinctex");
		// shaderLightning = BombermanGame.createShader("simple");
		floorTile = new BoxCreator().create(1);
		Matrix4 m = new Matrix4();
		m.idt().translate(0, -2, 0);
		floorTile.transform(m);
		boxMesh = new BoxCreator().create(6);
		textureConcrete = loadTexture("concrete_x_64.png");
		textureFire = loadTexture("fireworks.png");
		textureFloor = loadTexture("floor.png");
		textureExit = loadTexture("exit.png");
		textureGlassphere = loadTexture("glasssphere.png");
		textureBombableWall = loadTexture("bombablewall.png");
		sphere = BombermanGame.loadObjFile("sphere.obj");
		player = BombermanGame.loadObjFile("bomberman.obj");
		gravestone = BombermanGame.loadObjFile("gravestone.obj");
		antenna = BombermanGame.loadObjFile("antenna.obj");
		glasssphere = BombermanGame.loadObjFile("glassphere.obj");
		levelcomplete = BombermanGame.loadObjFile("levelcomplete.obj");
		gameOver = BombermanGame.loadObjFile("gameOver.obj");
		for (SubMesh mesh : glasssphere.getSubMeshes()) {
			mesh.getMesh().scale(2f, 2f, 2f);
		}
		tree = BombermanGame.loadObjFile("baum.obj");
		model = new BombermanModel();

		Player p = new Player(model);
		p.addPropertyChangeListener(this);
		playerControl = new PlayerControl(p, model);
		model.getDynamicContent().add(p);
		nextLevel();
		subRenderer.add(new BombPresenter(this.getModel()));
		subRenderer.add(new MonsterPresenter(this.getModel()));
		subRenderer.add(new ItemPresenter(getModel()));
		subRenderer.add(new FirePresenter(this.getModel()));
		for (BaseSubPresenter renderer : subRenderer) {
			renderer.create();
		}
	}

	private void nextLevel() {
		if (factory == null) {
			factory = new LevelMapFactory(Gdx.files.internal(
					"leveldesign").read());
		}
		model.setLevelCompleteTime(null);
		model.setLevel(1+model.getLevel());
		factory.updateGame(model);
	}

	private void resetLevel() {
		factory.resetDynamics(model);
	}

	private void resetGame() {
		// TODO implement game over.
	}

	public static Texture loadTexture(String string) {
		Texture texture = new Texture("" + string);
		texture.setWrap(TextureWrap.MirroredRepeat, TextureWrap.MirroredRepeat);
		return texture;
	}

	public void dispose() {
		textureConcrete.dispose();
		textureExit.dispose();
		textureFire.dispose();
		textureFloor.dispose();
		textureGlassphere.dispose();
		gravestone.dispose();
		player.dispose();
		antenna.dispose();
		glasssphere.dispose();
		levelcomplete.dispose();
		gameOver.dispose();
		for (BaseSubPresenter renderer : subRenderer) {
			renderer.dispose();
		}
	}
	
	/**
	 * @param ray
	 * @return
	 */
	public MapPosition touchRay(Ray ray) {
		MapPosition result = null;
		// Bodenflaeche: y=1 (normalenform)
		// (x,y,z)+m*(a,b,c)=(0,1,0)
		// -> y+m*b=1
		// -> m = (1-y)/b
		// m in geradengleichung einsetzen, um x und z zu erhalten, dabei groesse der Kacheln beachten
		// -> x als x und z als y in map einsetzen.
		float b = ray.direction.y; // b
		float y = ray.origin.y; // y
		if (b != 0f) {
			float m = (1 - y) / b;
			float tile_raw_x = ray.origin.x + m * ray.direction.x;
			float tile_raw_z = ray.origin.z + m * ray.direction.z;

			int result_x = Math.round(tile_raw_x / SPACE_PER_TILE);
			int result_z = Math.round(tile_raw_z / SPACE_PER_TILE);
			if (0 <= result_x && result_x < model.getMap().getWidth()) {
				if (0 <= result_z && result_z < model.getMap().getHeight()) { // z is y in map coordinates (2d map)
					result = MapPosition.valueOf(result_x, result_z);
				}
			}
		}
		return result;
	}

	public void render() {
		if (!model.isLevelComplete() && !model.isGameOver()) {
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				MapPosition position = touchRay(camera.getPickRay(Gdx.input.getX(), Gdx.input.getY()));
				playerControl.handleLeftClick(position, Gdx.graphics.getDeltaTime());
			} else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
				playerControl.tryLayBomb();
			}
			playerControl.control(Gdx.input, Gdx.graphics.getDeltaTime());
		}
		// position.control(Gdx.input);
		// position.apply(camera);
		playerControl.apply(camera);
		Gdx.gl.glClearColor(0.8f, 0.8f, 1f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		shaderLightning.begin();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);
		Gdx.gl.glEnable(GL10.GL_FRONT_AND_BACK);
		Gdx.gl.glEnable(GL10.GL_TEXTURE_2D);
		Gdx.gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glActiveTexture(0);

		shaderLightning.setUniformf("u_lightdir", lightdir);
		shaderLightning.setUniformf("ambience", 0.2f);
		shaderLightning.setUniformi("u_texture", 0);
		List<NCell> cells = model.getMap().getCells();

		PlayerControl.camDebug(camera);
		for (NCell nCell : cells) {
			renderCellContent(nCell, nCell.getContent());
		}
		for (NCell nCell : cells) {
			if (nCell.getContent() instanceof Exit) {
				renderExit((Exit) nCell.getContent(), nCell.getPosition());
			}
			renderCellContent(nCell, nCell.getContent());
		}
		transform.set(camera.combined);
		List<IDynamicCellContent> content = model.getDynamicContent();
		Player deadPlayer = null;
		for (IDynamicCellContent dynamicCellContent : content) {
			if (dynamicCellContent instanceof Wall) {
				continue;
			}
			MapPosition position = dynamicCellContent.getPosition();
			float offsetX = 0;
			float offsetY = 0;
			if (dynamicCellContent instanceof ILivecontent) {
				offsetX = ((ILivecontent) dynamicCellContent).getOffsetX();
				offsetY = ((ILivecontent) dynamicCellContent).getOffsetY();
			}
			transform.translate((position.getX() + offsetX) * SPACE_PER_TILE,
					0, (position.getY() + offsetY) * SPACE_PER_TILE);
			float rotation = 0f;
			if (dynamicCellContent.getOrientation() != null) {
				rotation = dynamicCellContent.getOrientation().getRotation();
			}

			transform.rotate(0, 1, 0, rotation);
			shaderLightning.setUniformMatrix("u_projTrans", transform);
			if (dynamicCellContent instanceof Player) {
				if (((Player) dynamicCellContent).isDying()) {
					// Player died logic
					Long diedAt = ((Player) dynamicCellContent).getDiedAt();
					if (System.currentTimeMillis() > diedAt + 1500) {
						deadPlayer = ((Player) dynamicCellContent);
					}
					gravestone.render(shaderLightning);
				} else {
					player.render(shaderLightning);
				}
			}
		}
		boolean continuerender = true;
		if (model.isLevelComplete()) {
			long millis = model.getLevelCompleteTime()+3000-System.currentTimeMillis();
			if (millis<0) {
				nextLevel();
			} else {
				continuerender = false;
				MapPosition pos = playerControl.getPosition();
				renderObjectAnimated(millis, pos, levelcomplete);
			}
		} else if (model.isGameOver()) {
			long millis = model.getGameOverTime()+10000-System.currentTimeMillis();
			if (millis<0) {
				model.setGameOverTime(null);
				nextLevel(); // i.e. level 0
			} else {
				continuerender=false;
				MapPosition pos = playerControl.getPosition();
				renderObjectAnimated(millis, pos, gameOver);
			}
		}

		shaderLightning.end();

		if (continuerender) {
			for (BaseSubPresenter renderer : subRenderer) {
				List list = model.getDynamicContent(renderer.getClazz());
				renderer.render(list, camera);
			}
		}
		if (model.getExit()!=null) {
			// exit free check
			boolean exitIsOpen = model.getExit().isOpen();
			if (!exitIsOpen) {
				List<Antenna> antennas = model.getDynamicContent(Antenna.class);
				// open exit if all antennas are destroyed.
				if (antennas.isEmpty()) {
					model.getExit().setExitOpenTime(System.currentTimeMillis());
				}
			} else if (!model.isLevelComplete()) {
				if (playerControl.getPosition()==model.getExit().getPosition()) {
					playerControl.getPlayer().setTargetPosition(null);
					playerControl.getPlayer().setLastPath(null);
					model.setLevelCompleteTime(System.currentTimeMillis());
				}
			}
		}
		if (deadPlayer != null) {
			System.out.println("Resetting level");
			deadPlayer.setDiedAt(null);
			if (deadPlayer.getLives()<0) {
				deadPlayer.setLives(Player.DEFAULT_LIVES);
				model.setLevel(-1);
				model.setGameOverTime(System.currentTimeMillis());
			} else {
				resetLevel();
			}
		}
	}

	private void renderObjectAnimated(long millis, MapPosition pos, StillModel text) {
		float factor = 1.0f + Math.min(millis*0.005f, 3);
		transform.set(camera.combined);
		transform.translate((pos.getX()) * SPACE_PER_TILE,
				2, (pos.getY()) * SPACE_PER_TILE);
		transform.rotate(0, 1, 0, 270f+0.18f*millis);
		transform.rotate(0, 0, 1, 90f);
		transform.scale(1.f+0.25f*factor, 2.f, factor);
		shaderLightning.setUniformMatrix("u_projTrans", transform);
		textureGlassphere.bind();
		text.render(shaderLightning);
	}

	private void renderCellContent(NCell nCell, NCellContent content) {
		MapPosition position = nCell.getPosition();
		locatePosition(position);
		boolean renderfloor = true;
		if (content == null) {
			// renders floor
		} else if (content instanceof Transparent) {
			renderfloor = false;
		} else if (content instanceof Exit) {
			// do nothing
			renderfloor = false;
		} else if (content instanceof Antenna) {
			antenna.render(shaderLightning);
		} else if (content instanceof Wall) {
			boolean bombable = ((Wall) content).isBombable();
			if (bombable) {
				// tree.render(shaderLightning);
				textureBombableWall.bind();
				boxMesh.render(shaderLightning, GL20.GL_TRIANGLES);
			} else {
				renderfloor = false;
				textureConcrete.bind();
				boxMesh.render(shaderLightning, GL20.GL_TRIANGLES);
			}
		}
		if (renderfloor) {
			textureFloor.bind();
			floorTile.render(shaderLightning, GL20.GL_TRIANGLES);
		}
	}

	private void locatePosition(MapPosition position) {
		transform.set(camera.combined);
		transform.translate(position.getX() * SPACE_PER_TILE, 0,
				position.getY() * SPACE_PER_TILE);

		shaderLightning.setUniformMatrix("u_projTrans", transform);
	}

	private void renderExit(Exit content, MapPosition position) {
		locatePosition(position);
		textureExit.bind();
		floorTile.render(shaderLightning, GL20.GL_TRIANGLES);
		if (!content.isOpen()) {
			// render glas sphere
			glasssphere.render(shaderLightning);
		}
	}

	public void resize(int width, int height) {
		camera = new PerspectiveCamera(67, width, height);
		camera.position.set(10, 10, 0);
		camera.lookAt(0, 0, 0);
		camera.up.set(0, 1, 0);
	}

	public BombermanModel getModel() {
		return model;
	}

	public void setModel(BombermanModel model) {
		this.model = model;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// do nothing
	}

}
