package com.mygdx.platformer.Tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.mygdx.platformer.Objects.Player.Player;
import com.mygdx.platformer.Screens.PlayScreen;

import static com.mygdx.platformer.Tools.Constants.PPM;

public class TileMapHelper {

    private TiledMap tiledMap;
    private PlayScreen gameScreen;

    public TileMapHelper(PlayScreen gameScreen){
        this.gameScreen = gameScreen;
    }

    public OrthogonalTiledMapRenderer setupMap(){
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMinFilter = Texture.TextureFilter.Nearest;
        params.textureMagFilter = Texture.TextureFilter.Nearest;
        tiledMap = new TmxMapLoader().load("testLvl.tmx", params);
        parseMapObjects(tiledMap.getLayers().get("Objects").getObjects());
        return new OrthogonalTiledMapRenderer(tiledMap);
    }

    private void parseMapObjects(MapObjects mapObjects){
        for(MapObject mapObject : mapObjects){

            if(mapObject instanceof PolygonMapObject){
                createStaticBody((PolygonMapObject) mapObject);
            }

            if(mapObject instanceof EllipseMapObject) {
                Ellipse ellipse = ((EllipseMapObject) mapObject).getEllipse();
                String ellipseName = mapObject.getName();

                if(ellipseName.equals("blueBall")){
                    Body body = BodyHelperService.createBody(
                            ellipse.x + ellipse.width / 2, ellipse.y + ellipse.width / 2,
                            ellipse.width, ellipse.height, false,
                            gameScreen.getWorld()
                    );
                    gameScreen.setPlayer1(new Player(ellipse.width, ellipse.height, body, 1));
                }
                if(ellipseName.equals("orangeBall")){
                    Body body = BodyHelperService.createBody(
                            ellipse.x + ellipse.width / 2, ellipse.y + ellipse.width / 2,
                            ellipse.width, ellipse.height, false,
                            gameScreen.getWorld()
                    );
                    gameScreen.setPlayer2(new Player(ellipse.width, ellipse.height, body, 2));
                }
            }
        }
    }

    private void createStaticBody(PolygonMapObject polygonMapObject){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = gameScreen.getWorld().createBody(bodyDef);
        Shape shape = createPolygonShape(polygonMapObject);
        body.createFixture(shape, 1000);
        shape.dispose();
    }

    private Shape createPolygonShape(PolygonMapObject polygonMapObject){
        float[] vertices = polygonMapObject.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for(int i = 0; i < vertices.length / 2; i++){
            Vector2 current = new Vector2(vertices[i * 2]/PPM, vertices[i * 2 + 1]/PPM);
            worldVertices[i] = current;
        }

        PolygonShape shape = new PolygonShape();
        shape.set(worldVertices);
        return shape;
    }
}
