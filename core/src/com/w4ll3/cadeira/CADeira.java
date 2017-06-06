package com.w4ll3.cadeira;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.w4ll3.cadeira.algebra.Utilities;

import java.util.ArrayList;

public class CADeira extends ApplicationAdapter implements ApplicationListener {
    SpriteBatch batch;
    Texture img;

    OrthographicCamera camera;
    Stage stage;
    TextButton button;
    Float[][] coordinates;
    ArrayList<Float[][]> objects = new ArrayList<Float[][]>();

    ShapeRenderer renderer;

    ArrayList<Vector2> line = new ArrayList<Vector2>();

    boolean dLine = false, dCircle = false, dTriangle = false, dSquare = false;
    int contaPos = 0;

    @Override
    public void create() {
        renderer = new ShapeRenderer();

        line.add(new Vector2(10f, 50f));
        line.add(new Vector2(60f, 100f));

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); //2D camera
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());// y increases upward, viewport = window
        camera.update();

        batch = new SpriteBatch();//desenhando no batch

        stage = new Stage(); //window is stage
        stage.clear();
        Gdx.input.setInputProcessor(stage); //stage is responsive


        stage.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent evt, float x, float y) {
                if (dLine && contaPos < 2) {
                    coordinates[0][contaPos] = x;
                    coordinates[1][contaPos] = y;
                    contaPos++;
                    if (contaPos == 2) {
                        dLine = false;
                        for (int i = 0; i < 2; i++) {
                            System.out.println("Vertice [" + coordinates[0][i] + "][" + coordinates[1][i] + "]");
                        }
                        objects.add(coordinates);
                        contaPos = 0;
                    }
                } else if (dTriangle && contaPos < 3) {
                    coordinates[0][contaPos] = x;
                    coordinates[1][contaPos] = y;
                    contaPos++;
                    if (contaPos == 3) {
                        dTriangle = false;
                        for (int i = 0; i < 3; i++) {
                            System.out.println("Vertice [" + coordinates[0][i] + "][" + coordinates[1][i] + "]");
                        }
                        objects.add(coordinates);
                        contaPos = 0;
                    }
                } else if (dSquare && contaPos < 4) {
                    coordinates[0][contaPos] = x;
                    coordinates[1][contaPos] = y;
                    contaPos++;
                    if (contaPos == 4) {
                        dSquare = false;
                        for (int i = 0; i < 4; i++) {
                            System.out.println("Vertice [" + coordinates[0][i] + "][" + coordinates[1][i] + "]");
                        }
                        objects.add(coordinates);
                        contaPos = 0;
                    }
                } else if (dCircle && contaPos < 2) {
                    coordinates[0][contaPos] = x;
                    coordinates[1][contaPos] = y;
                    contaPos++;
                    if (contaPos == 2) {
                        dCircle = false;
                        System.out.println("Centro = [" + coordinates[0][0] + "][" + coordinates[1][0] + "]");
                        double raio = Math.sqrt(Math.pow((coordinates[0][0] - coordinates[0][1]), 2) + Math.pow((coordinates[1][0] - coordinates[1][1]), 2));
                        coordinates[0][1] = (float) raio;
                        coordinates[1][1] = (float) Double.NEGATIVE_INFINITY;
                        System.out.println("Raio = " + raio);
                        objects.add(coordinates);
                        contaPos = 0;
                    }
                }
            }
        });

    }

    public void myObjects() {
        for (Float[][] f : objects) {
            System.out.println("Tem um desenho!");
        }
        System.out.println("\tTotal de " + objects.size() + " figuras desenhadas");
    }

    public void verifyInputs() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            dLine = false;
            dCircle = false;
            dSquare = false;
            dTriangle = false;
            contaPos = 0;
            coordinates = null;
        }

        if (Gdx.input.isKeyJustPressed((Input.Keys.O))) {
            myObjects();
        }

        if (!(dLine || dCircle || dSquare || dTriangle)) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
                System.out.println("L was pressed. Lets draw a line");
                dLine = true;
                coordinates = new Float[2][2];
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                System.out.println("S was pressed. Lets draw a square");
                dSquare = true;
                coordinates = new Float[2][4];
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                System.out.println("C was pressed. Lets draw a circle");
                dCircle = true;
                coordinates = new Float[2][2];
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
                System.out.println("T was pressed. Lets draw a triangle");
                dTriangle = true;
                coordinates = new Float[2][3];
            }
        }

    }

    public void drawObjects(){
        if(objects.size() > 0){
            for(Float[][] obj : objects){
                switch (obj[0].length){
                    case 2:
                        if(obj[1][1] == Float.NEGATIVE_INFINITY){//circle
                            renderer.circle(obj[0][0], obj[1][0], obj[0][1]);
                        }else{//line
                            renderer.line(obj[0][0], obj[1][0], obj[0][1], obj[1][1]);
                        }
                        break;
                    case 3://triangle
                        renderer.triangle(obj[0][0], obj[1][0], obj[0][1], obj[1][1], obj[0][2], obj[1][2]);
                        break;
                    case 4://square
                        renderer.polygon(new Utilities().floatMatrixToArray(obj));
                        break;
                }
            }
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);//limpa buffer com cor limpa

        verifyInputs();

        stage.act();

        batch.setProjectionMatrix(camera.combined);//seta a matriz de projeção
        batch.begin();

        stage.draw();

        //Gdx.gl30.glLineWidth(1);
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(0, 0, 0, 0);
        //renderer.line(line.get(0), line.get(1));

        drawObjects();

        renderer.end();

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }
}
