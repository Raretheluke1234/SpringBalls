package com.mygdx.platformer.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.platformer.Objects.Player.Player;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Tools.TileMapHelper;

import static com.mygdx.platformer.Tools.Constants.PPM;

public class PlayScreen implements Screen {

    private Viewport gamePort;
    private OrthographicCamera gameCam;
    private SpriteBatch batch;
    private Platformer game;

    private World world;
    private Box2DDebugRenderer b2dr;

    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TileMapHelper tileMapHelper;

    //game objects
    private Player player1;
    private Player player2;

    //joint
    private DistanceJoint joint;
    private float jointLength;
    private float jointFrequency;
    private float jointDampingRatio;

    //Runs once for setup purposes
    public PlayScreen(Platformer game){

        //creates necessary objects for rendering
        this.game = game;
        batch = new SpriteBatch();
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(Platformer.V_WIDTH, Platformer.V_HEIGHT, gameCam);


        //Set initial camera position to bottom left rather than 0,0.
        gameCam.position.set(gamePort.getWorldWidth() / 2,gamePort.getWorldHeight() / 2, 0);


        //Create the Box2D world
        this.world = new World(new Vector2(0,-25f), true);

        //Create the Box2D debug renderer to see bodies without overlaying sprites
        this.b2dr = new Box2DDebugRenderer();


        //tileMap setup
        this.tileMapHelper = new TileMapHelper(this);
        this.orthogonalTiledMapRenderer = tileMapHelper.setupMap();

        //this is painful, but I don't know where else to reference p1 and p2
        DistanceJointDef djd = new DistanceJointDef();
        //connect joint
        djd.bodyA = player1.getBody();
        djd.bodyB = player2.getBody();
        //equilibrium length
        this.jointLength = 30 / PPM;
        djd.length = jointLength;
        //springyness
        this.jointFrequency = 4;
        djd.frequencyHz = jointFrequency; //1 - 5
        djd.dampingRatio = 0.5f;// 0 - 1

        //create joint
        joint = (DistanceJoint) world.createJoint(djd);
    }

    @Override
    public void show() {

    }

    //Renders the current frame using various methods
    @Override
    public void render(float delta) {
        //delegate tasks not directly related to rendering, i.e. collision, interaction, input
        this.update();

        //clear the screen for this frame
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT);

        //render the tiled map
        orthogonalTiledMapRenderer.render();

        //begin the process of rendering sprites
        batch.begin();
        //VVVV render sprites here VVVV


        //end the process of rendering sprites
        batch.end();

        //render B2D debug view of this world to the scale of PPM
        b2dr.render(world, gameCam.combined.scl(PPM));

    }

    private void update() {

        //move the B2D world forward one frame
        world.step(1 / 60f, 6, 2);

        //delegate tasks related to the camera
        cameraUpdate();

        //gameCam.combined returns a matrix for batch to reference. this allows batch to set up for rendering
        batch.setProjectionMatrix(gameCam.combined);

        //fixes the orthogonalTiledMapRenderer to the gameCam for correct rendering
        orthogonalTiledMapRenderer.setView(gameCam);

        //delegate tasks related to player input and body transformation
        player1.update(this);
        player2.update(this);

        //quit out if ESCAPE is pressed
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
    }

    private void cameraUpdate(){
        //Insert gameCam logic before the update method


        //finalizes changes to the gameCam for this frame
        gameCam.update();
    }

    //This method is run whenever the window is resized
    @Override
    public void resize(int width, int height) {
        //update the viewport with the new width and height of the screen
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    //clears resources once the frame is completed
    @Override
    public void dispose() {
        batch.dispose();
        orthogonalTiledMapRenderer.dispose();
        world.dispose();
        b2dr.dispose();
    }

    //world getter
    public World getWorld() {
        return world;
    }

    //joint getters
    public DistanceJoint getJoint(){ return joint;}
    public float getJointLength(){return jointLength;}
    public float getJointFrequency(){return jointFrequency;}
    public float getJointDampingRatio(){return jointDampingRatio;}

    //player setter
    public void setPlayer1(Player player1){
        this.player1 = player1;
    }
    public void setPlayer2(Player player2){this.player2 = player2;}
}
