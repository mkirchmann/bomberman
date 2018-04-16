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
import de.neuenberger.games.core.resource.Resource;
import de.neuenberger.games.core.resource.ResourceManager;
import de.neuenberger.games.core.resource.ResourceType;

public class BombermanPresenter extends DefaultScreen implements PropertyChangeListener {

	public static final float SPACE_PER_TILE = 2f;
	Resource<Texture> resourceTextureConcrete;
	Resource<Texture> resourceTextureFire;
	Resource<Texture> resourceTextureFloor;
	Resource<Texture> resourceTextureExit;

	BombermanModel model;
	Matrix4 transform = new Matrix4();
	private Mesh floorTile;
	private Mesh boxMesh;
	private Resource<ShaderProgram> shaderLightningResource;
	private PerspectiveCamera camera;

	ControllablePosition position = new ControllablePosition();
	private Vector3 lightdir;
	private Resource<StillModel> resourcePlayer;

	private PlayerControl playerControl;

	private LevelMapFactory factory;
	private Resource<Texture> resourceTextureBombableWall;

	List<BaseSubPresenter> subRenderer = new LinkedList<BaseSubPresenter>();
	private Resource<StillModel> resourceGravestone;

	private Resource<StillModel> resourceTree;
	private Resource<StillModel> resourceAntenna;
	private Resource<StillModel> resourceGlasssphere;
	private Resource<StillModel> resourceLevelcomplete;
	private Resource<Texture> resourceTextureGlassphere;
	private Resource<StillModel> resourceGameOver;
	private ShaderProgram shaderLightning;
	private StillModel tree;
	private StillModel gameOver;
	private StillModel levelcomplete;
	private StillModel glasssphere;
	private StillModel antenna;
	private StillModel gravestone;
	private StillModel player;
	private Texture textureBombableWall;
	private Texture textureGlassphere;
	private Texture textureExit;
	private Texture textureFloor;
	private Texture textureFire;
	private Texture textureConcrete;
	private OverlayPresenter overlayPresenter;
	private LoadScreen loadScreen;

	public BombermanPresenter(BombermanGame game, LoadScreen theLoadScreen) {
		super(game);
		this.loadScreen = theLoadScreen;
	}

	public void create() {
		lightdir = new Vector3(0.5f, 1f, -0.75f);
		ResourceManager rm = ResourceManager.getInstance();
		shaderLightningResource = rm.getResource("spinctex");
		// shaderLightning = BombermanGame.createShader("simple");
		floorTile = new BoxCreator().create(1);
		Matrix4 m = new Matrix4();
		m.idt().translate(0, -2, 0);
		floorTile.transform(m);
		boxMesh = new BoxCreator().create(6);
		resourceTextureConcrete = rm.addLoadRequest("concrete_x_64.png", ResourceType.TEXTURE);
		resourceTextureFire = rm.addLoadRequest("fireworks.png", ResourceType.TEXTURE);
		resourceTextureFloor = rm.addLoadRequest("floor.png", ResourceType.TEXTURE);
		resourceTextureExit = rm.addLoadRequest("exit.png", ResourceType.TEXTURE);
		resourceTextureGlassphere =rm.addLoadRequest("glasssphere.png", ResourceType.TEXTURE);
		resourceTextureBombableWall = rm.addLoadRequest("bombablewall.png", ResourceType.TEXTURE);
		resourcePlayer = rm.addLoadRequest("bomberman.obj", ResourceType.OBJECT);
		resourceGravestone = rm.addLoadRequest("gravestone.obj", ResourceType.OBJECT);
		resourceAntenna = rm.addLoadRequest("antenna.obj", ResourceType.OBJECT);
		resourceGlasssphere = rm.addLoadRequest("glassphere.obj", ResourceType.OBJECT);
		resourceLevelcomplete = rm.addLoadRequest("levelcomplete.obj", ResourceType.OBJECT);
		resourceGameOver = rm.addLoadRequest("gameover.obj", ResourceType.OBJECT);
		resourceTree = rm.addLoadRequest("baum.obj", ResourceType.OBJECT);
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
		overlayPresenter = new OverlayPresenter(this.getModel());
		overlayPresenter.create();
	}
	
	public void postAsyncCreate() {
		for (SubMesh mesh : resourceGlasssphere.getResource().getSubMeshes()) {
			mesh.getMesh().scale(2f, 2f, 2f);
		}
		for (BaseSubPresenter renderer : subRenderer) {
			renderer.postAsyncCreate();
		}
		
		shaderLightning = shaderLightningResource.getResource();
		
		textureConcrete = resourceTextureConcrete.getResource();
		textureFire = resourceTextureFire.getResource();
		textureFloor = resourceTextureFloor.getResource();
		textureExit = resourceTextureExit.getResource();
		textureGlassphere = resourceTextureGlassphere.getResource();
		textureBombableWall = resourceTextureBombableWall.getResource();
		player = resourcePlayer.getResource();
		gravestone = resourceGravestone.getResource();
		antenna = resourceAntenna.getResource();
		glasssphere = resourceGlasssphere.getResource();
		levelcomplete = resourceLevelcomplete.getResource();
		gameOver = resourceGameOver.getResource();
		tree = resourceTree.getResource();
		
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

	public void dispose() {
		for (BaseSubPresenter renderer : subRenderer) {
			renderer.dispose();
		}
		overlayPresenter.dispose();
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

	public void render(float delta) {
		if (!model.isLevelComplete() && !model.isGameOver()) {
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				// MapPosition position = touchRay(camera.getPickRay(Gdx.input.getX(),
				// Gdx.input.getY()));
				// playerControl.handleLeftClick(position, Gdx.graphics.getDeltaTime());
			} else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
				// playerControl.tryLayBomb();
			} else if (Gdx.input.isKeyPressed(Input.Keys.MENU)) {
				// playerControl.tryLayBomb();
			}

			ControlButtonStates controlButtonStates = overlayPresenter.control(Gdx.input);
			controlButtonStates.setUpButtonPressed(
					controlButtonStates.isUpButtonPressed() || Gdx.input.isKeyPressed(Input.Keys.UP));
			controlButtonStates.setDownButtonPressed(
					controlButtonStates.isDownButtonPressed() || Gdx.input.isKeyPressed(Input.Keys.DOWN));
			controlButtonStates.setLeftButtonPressed(
					controlButtonStates.isLeftButtonPressed() || Gdx.input.isKeyPressed(Input.Keys.LEFT));
			controlButtonStates.setRightButtonPressed(
					controlButtonStates.isRightButtonPressed() || Gdx.input.isKeyPressed(Input.Keys.RIGHT));
			controlButtonStates.setFireButtonPressed(
					controlButtonStates.isFireButtonPressed() || Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT));

			playerControl.control(controlButtonStates, delta);
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
		// Gdx.gl.glActiveTexture(0);

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
				loadScreen.present();
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
				renderer.render(list, camera, delta);
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
		overlayPresenter.render();
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
		overlayPresenter.resize(width, height);
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
