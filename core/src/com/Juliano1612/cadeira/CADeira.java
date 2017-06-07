package com.Juliano1612.cadeira;

import com.Juliano1612.cadeira.algebra.Utilities;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.StringTokenizer;

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
    boolean dTerminal = false, dDeleting = false;

    int contaPos = 0;

    private String text, terminalCommand;
    private BitmapFont font;

    @Override
    public void create() {
        font = new BitmapFont();
        font.setColor(0, 0, 0, 1);
        renderer = new ShapeRenderer();
        text = "";
        terminalCommand = "";

        line.add(new Vector2(10f, 50f));
        line.add(new Vector2(60f, 100f));

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); //2D camera
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());// y increases upward, viewport = window
        camera.update();

        batch = new SpriteBatch();//desenhando no batch

        stage = new Stage(); //window is stage
        stage.clear();
        InputListener terminal = new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if (dTerminal) {
                    switch (character) {
                        case '\n':
                            System.out.println("ENTER");
                            break;
                        case '\b':
                            if ((text.length() - 1) >= 0)
                                text = text.substring(0, text.length() - 1);
                            break;
                        default:
                            text += character;
                            break;
                    }
                    return true;
                }
                return false;
            }
        };
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
        stage.addListener(terminal);

    }


    public void myObjects() {
        for (Float[][] f : objects) {
            System.out.println("Tem um desenho!");
        }
        System.out.println("\tTotal de " + objects.size() + " figuras desenhadas");
    }

    public void resetDFlags() {
        dDeleting = false;
        dLine = false;
        dCircle = false;
        dSquare = false;
        dTriangle = false;
        contaPos = 0;
        coordinates = null;
    }

    public String deletingMessage() {
        String msg = "Digite o número do objeto que deseja excluir:";

        for (int i = 0; i < objects.size(); i++) {
            switch (objects.get(i)[0].length) {
                case 2:
                    if (objects.get(i)[1][1] == Float.NEGATIVE_INFINITY) {//Circle
                        msg += "\n[" + i + "] Círculo de centro [" + objects.get(i)[0][0] + "][" + objects.get(i)[1][0] + "] e raio " + objects.get(i)[0][1];
                    } else {//line
                        msg += "\n[" + i + "] Linha de coordenadas [" + objects.get(i)[0][0] + "][" + objects.get(i)[1][0] + "] e [" + objects.get(i)[0][1] + "][" + objects.get(i)[1][1] + "]";
                    }
                    break;
                case 3:
                    msg += "\n[" + i + "] Triângulo de coordenadas [" + objects.get(i)[0][0] + "][" + objects.get(i)[1][0] + "], [" + objects.get(i)[0][1] + "][" + objects.get(i)[1][1] + "] e [" + objects.get(i)[0][2] + "][" + objects.get(i)[1][2] + "]";
                    break;
                case 4:
                    msg += "\n[" + i + "] Poligono de coordenadas [" + objects.get(i)[0][0] + "][" + objects.get(i)[1][0] + "], [" + objects.get(i)[0][1] + "][" + objects.get(i)[1][1] + "], [" + objects.get(i)[0][2] + "][" + objects.get(i)[1][2] + "] e [" + objects.get(i)[0][3] + "][" + objects.get(i)[1][3] + "]";
                    break;
            }
        }
        msg += "\n";
        return msg;
    }


    public void verifyInputs() {


        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
                objects = new ArrayList<Float[][]>();
            }
        }

        if (!dTerminal) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                terminalCommand = "Digite o que deseja : ";
                text = "";
                dTerminal = true;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
                if (objects.size() == 0) {
                    terminalCommand = "Não há nada para ser apagado! Press ESC ";

                } else {
                    terminalCommand = deletingMessage();
                    dDeleting = true;
                }
                text = "";
                dTerminal = true;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                resetDFlags();
            }

            if (Gdx.input.isKeyJustPressed((Input.Keys.O))) {
                myObjects();
            }

            if (!(dLine || dCircle || dSquare || dTriangle)) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
                    System.out.println("L was pressed. Lets draw a line");
                    dLine = true;
                    coordinates = new Float[2][2];
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.F4)) {
                    System.out.println("S was pressed. Lets draw a square");
                    dSquare = true;
                    coordinates = new Float[2][4];
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
                    System.out.println("C was pressed. Lets draw a circle");
                    dCircle = true;
                    coordinates = new Float[2][2];
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
                    System.out.println("T was pressed. Lets draw a triangle");
                    dTriangle = true;
                    coordinates = new Float[2][3];
                }
            }
        } else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                dTerminal = false;
                text = "";
                if (dLine || dCircle || dSquare || dTriangle || dDeleting) {
                    resetDFlags();
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                System.out.println("Capturei : " + text);

                if(dDeleting){
                    int pos = Integer.parseInt(text);
                    System.out.println("pos" + pos);
                    objects.remove(pos);
                }else{

                    StringTokenizer tokenizer = new StringTokenizer(text);
                    if (dLine) {
                        coordinates[0][0] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][0] = Float.valueOf(tokenizer.nextToken());
                        coordinates[0][1] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][1] = Float.valueOf(tokenizer.nextToken());
                        objects.add(coordinates);
                    } else if (dCircle) {
                        coordinates[0][0] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][0] = Float.valueOf(tokenizer.nextToken());
                        coordinates[0][1] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][1] = Float.NEGATIVE_INFINITY;
                        objects.add(coordinates);

                    } else if (dSquare) {
                        coordinates[0][0] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][0] = Float.valueOf(tokenizer.nextToken());
                        coordinates[0][1] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][1] = Float.valueOf(tokenizer.nextToken());
                        coordinates[0][2] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][2] = Float.valueOf(tokenizer.nextToken());
                        coordinates[0][3] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][3] = Float.valueOf(tokenizer.nextToken());
                        objects.add(coordinates);

                    } else if (dTriangle) {
                        coordinates[0][0] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][0] = Float.valueOf(tokenizer.nextToken());
                        coordinates[0][1] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][1] = Float.valueOf(tokenizer.nextToken());
                        coordinates[0][2] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][2] = Float.valueOf(tokenizer.nextToken());
                        objects.add(coordinates);
                    }
                }
                resetDFlags();
                dTerminal = false;
                text = "";
            }
            if (dLine) {
                terminalCommand = "Digite as coordenadas da linha : ";
            } else if (dSquare) {
                terminalCommand = "Digite as coordenadas do poligono de 4 lados : ";
            } else if (dCircle) {
                terminalCommand = "Digite as coordenadas do centro e o raio : ";
            } else if (dTriangle) {
                terminalCommand = "Digite as coordenadas do triângulo : ";
            }

        }
    }

    public void drawObjects() {
        if (objects.size() > 0) {
            for (Float[][] obj : objects) {
                switch (obj[0].length) {
                    case 2:
                        if (obj[1][1] == Float.NEGATIVE_INFINITY) {//circle
                            renderer.circle(obj[0][0], obj[1][0], obj[0][1]);
                        } else {//line
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
        stage.draw();

        //Gdx.gl30.glLineWidth(1);
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(0, 0, 0, 0);
        //renderer.line(line.get(0), line.get(1));

        drawObjects();
        renderer.end();

        batch.begin();
        if (dTerminal)
            font.draw(batch, terminalCommand + text, 0, 800);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }
}
