package com.Juliano1612.cadeira;

import com.Juliano1612.cadeira.algebra.Transformation2D;
import com.Juliano1612.cadeira.algebra.Utilities;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class CiCADa extends ApplicationAdapter implements ApplicationListener {
    SpriteBatch batch;
    Texture img;

    OrthographicCamera camera;
    Stage stage;
    TextButton button;

    Float[][] coordinates;
    ArrayList<Float[][]> objects = new ArrayList<Float[][]>();

    Float relX, relY, theta;
    int transformationSelected, objectToTransform;

    ShapeRenderer renderer;

    ArrayList<Vector2> line = new ArrayList<Vector2>();

    boolean dLine = false, dCircle = false, dTriangle = false, dSquare = false;
    boolean dTerminal = false, dDeleting = false, dSelectingObject = false, dSelectingTransformation = false, dTransforming = false;

    int contaPos = 0;

    private String text, terminalCommand;
    private BitmapFont font, fontPos;

    float posX, posY;
    DecimalFormat df = new DecimalFormat("#.00");

    @Override
    public void create() {
        font = new BitmapFont();
        font.setColor(0.44f, 0.44f, 0.44f, 1);

        fontPos = new BitmapFont();
        fontPos.setColor(0.66f, 0.66f, 0.66f, 1);
        fontPos.getData().setScale(.9f);

        renderer = new ShapeRenderer();
        text = "";
        terminalCommand = "";

        line.add(new Vector2(10f, 50f));
        line.add(new Vector2(60f, 100f));

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); //2D camera
        camera.setToOrtho(false);// y increases upward, viewport = window
        camera.update();

        batch = new SpriteBatch();//desenhando no batch

        stage = new Stage(new ScreenViewport(camera)); //window is stage
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
            public boolean mouseMoved(InputEvent event, float x, float y) {
                posX = x;
                posY = y;

                if (dLine && contaPos < 2) {
                    coordinates[0][contaPos] = x;
                    coordinates[1][contaPos] = y;
                } else if (dTriangle && contaPos < 3) {
                    coordinates[0][contaPos] = x;
                    coordinates[1][contaPos] = y;
                } else if (dSquare && contaPos < 4) {
                    coordinates[0][contaPos] = x;
                    coordinates[1][contaPos] = y;
                } else if (dCircle && contaPos < 2) {
                    if (coordinates[0][0] != null && coordinates[1][0] != null) {
                        coordinates[0][contaPos] = x;
                        coordinates[1][contaPos] = y;
                        double raio = Math.sqrt(Math.pow((coordinates[0][0] - coordinates[0][1]), 2) + Math.pow((coordinates[1][0] - coordinates[1][1]), 2));
                        coordinates[0][1] = (float) raio;
                        coordinates[1][1] = (float) Double.NEGATIVE_INFINITY;
                    }
                } else return false;
                return true;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (dTransforming && transformationSelected == 0) {
                    relX = x;
                    relY = y;

                    Float[][] objTmp = null;
                    if (objects.get(objectToTransform)[1][1] == Float.NEGATIVE_INFINITY) {//circle
                        objTmp = objects.get(objectToTransform);
                        objTmp[0][0] = relX;
                        objTmp[1][0] = relY;
                    } else {
                        objTmp = new Transformation2D().translateFigure(objects.get(objectToTransform), relX, relY);
                    }
                    objects.remove(objectToTransform);
                    objects.add(objTmp);

                    transformationSelected = -1;
                    dTransforming = false;
                    terminalCommand = "";
                    text = "";
                    dTerminal = false;
                }
                if (dLine && contaPos < 2) {
                    coordinates[0][contaPos] = x;
                    coordinates[1][contaPos] = y;

                    contaPos++;
                    if (contaPos == 2) {
                        dLine = false;
                        for (int i = 0; i < 2; i++) {
                            System.out.println("Vertice [" + coordinates[0][i] + "][" + coordinates[1][i] + "]");
                        }
                        coordinates[2][0] = 1f;
                        coordinates[2][1] = 1f;
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
                        coordinates[2][0] = 1f;
                        coordinates[2][1] = 1f;
                        coordinates[2][2] = 1f;
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
                        coordinates[2][0] = 1f;
                        coordinates[2][1] = 1f;
                        coordinates[2][2] = 1f;
                        coordinates[2][3] = 1f;

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
                return true;
            }
        });
        stage.addListener(terminal);

    }

    public void resetDFlags() {
        dTransforming = false;
        dSelectingTransformation = false;
        dSelectingObject = false;
        dDeleting = false;
        dLine = false;
        dCircle = false;
        dSquare = false;
        dTriangle = false;
        contaPos = 0;
        coordinates = null;
        objectToTransform = -1;
        transformationSelected = -1;
    }

    public String listMessage() {
        String msg = "";

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


        if (!dTerminal) {
            if (Gdx.input.isKeyPressed(Input.Keys.T)) {
                if (objects.size() == 0) {
                    terminalCommand = "Não há nada para ser transformado! Press ESC ";

                } else {
                    terminalCommand = "Selecione o objeto para a transformação: ";
                    terminalCommand += listMessage();
                    dSelectingObject = true;
                }
                text = "";
                dTerminal = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
                    objects = new ArrayList<Float[][]>();
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                terminalCommand = "Digite o que deseja : ";
                text = "";
                dTerminal = true;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
                if (objects.size() == 0) {
                    terminalCommand = "Não há nada para ser apagado! Press ESC ";

                } else {
                    terminalCommand = "Selecione o objeto a ser removido:";
                    terminalCommand += listMessage();
                    dDeleting = true;
                }
                text = "";
                dTerminal = true;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                resetDFlags();
            }

            if (!(dLine || dCircle || dSquare || dTriangle)) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
                    System.out.println("L was pressed. Lets draw a line");
                    dLine = true;
                    coordinates = new Float[3][2];
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.F4)) {
                    System.out.println("S was pressed. Lets draw a square");
                    dSquare = true;
                    coordinates = new Float[3][4];
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
                    System.out.println("C was pressed. Lets draw a circle");
                    dCircle = true;
                    coordinates = new Float[3][2];
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
                    System.out.println("T was pressed. Lets draw a triangle");
                    dTriangle = true;
                    coordinates = new Float[3][3];
                }
            }
        } else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                dTerminal = false;
                text = "";
                resetDFlags();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                System.out.println("Capturei : " + text);
                if (dTransforming) {
                    dTransforming = false;
                    StringTokenizer tokenizer;
                    Float[][] objTmp;
                    switch (transformationSelected) {
                        case 0:
                            tokenizer = new StringTokenizer(text);
                            relX = Float.parseFloat(tokenizer.nextToken().trim());
                            relY = Float.parseFloat(tokenizer.nextToken().trim());
                            if (objects.get(objectToTransform)[1][1] == Float.NEGATIVE_INFINITY) {//circle
                                objTmp = objects.get(objectToTransform);
                                objTmp[0][0] = relX;
                                objTmp[1][0] = relY;
                            } else {
                                objTmp = new Transformation2D().translateFigure(objects.get(objectToTransform), relX, relY);
                            }
                            objects.remove(objectToTransform);
                            objects.add(objTmp);

                            transformationSelected = -1;
                            terminalCommand = "";
                            text = "";
                            dTerminal = false;
                            break;
                        case 1:
                            tokenizer = new StringTokenizer(text);
                            relX = Float.parseFloat(tokenizer.nextToken());
                            relY = Float.parseFloat(tokenizer.nextToken());
                            if (objects.get(objectToTransform)[1][1] == Float.NEGATIVE_INFINITY) {//circle
                                objTmp = objects.get(objectToTransform);
                                objTmp[0][1] *= relX;
                            } else {
                                objTmp = new Transformation2D().scaleFigure(objects.get(objectToTransform), relX, relY);
                            }
                            objects.remove(objectToTransform);
                            objects.add(objTmp);

                            transformationSelected = -1;
                            terminalCommand = "";
                            text = "";
                            dTerminal = false;
                            break;
                        case 2:
                            theta = Float.parseFloat(text.trim());
                            objTmp = new Transformation2D().rotateFigure(objects.get(objectToTransform), theta);
                            objects.remove(objectToTransform);
                            objects.add(objTmp);

                            transformationSelected = -1;
                            terminalCommand = "";
                            text = "";
                            dTerminal = false;
                            break;
                    }
                } else if (dSelectingTransformation) {
                    dSelectingTransformation = false;
                    dTransforming = true;
                    transformationSelected = Integer.parseInt(text.trim());
                    switch (transformationSelected) {
                        case 0:
                            terminalCommand = "Escreva um ponto ou clique na tela para a translação\n";
                            text = "";
                            break;
                        case 1:
                            terminalCommand = "Escreva o valor em relação a X e em relação a Y\n";
                            text = "";
                            break;
                        case 2:
                            terminalCommand = "Escreva o ângulo desejado para a rotação\n";
                            text = "";
                            break;
                    }
                } else if (dSelectingObject) {
                    objectToTransform = Integer.parseInt(text.trim());
                    dSelectingTransformation = true;
                    dSelectingObject = false;
                    terminalCommand = "Selecione a transformação desejada:\n";
                    terminalCommand += "[0] - Translação\n";
                    terminalCommand += "[1] - Mudança de Escala\n";
                    terminalCommand += "[2] - Rotação\n";
                    text = "";
                }
                if (dDeleting) {
                    objects.remove(Integer.parseInt(text.trim()));
                    resetDFlags();
                    dTerminal = false;

                } else {

                    StringTokenizer tokenizer = new StringTokenizer(text);
                    if (dLine) {
                        coordinates[0][0] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][0] = Float.valueOf(tokenizer.nextToken());
                        coordinates[0][1] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][1] = Float.valueOf(tokenizer.nextToken());
                        coordinates[2][0] = 1f;
                        coordinates[2][1] = 1f;
                        objects.add(coordinates);
                        resetDFlags();
                        dTerminal = false;

                    } else if (dCircle) {
                        coordinates[0][0] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][0] = Float.valueOf(tokenizer.nextToken());
                        coordinates[0][1] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][1] = Float.NEGATIVE_INFINITY;
                        objects.add(coordinates);
                        resetDFlags();
                        dTerminal = false;

                    } else if (dSquare) {
                        coordinates[0][0] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][0] = Float.valueOf(tokenizer.nextToken());
                        coordinates[0][1] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][1] = Float.valueOf(tokenizer.nextToken());
                        coordinates[0][2] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][2] = Float.valueOf(tokenizer.nextToken());
                        coordinates[0][3] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][3] = Float.valueOf(tokenizer.nextToken());
                        coordinates[2][0] = 1f;
                        coordinates[2][1] = 1f;
                        coordinates[2][2] = 1f;
                        coordinates[2][3] = 1f;
                        objects.add(coordinates);
                        resetDFlags();
                        dTerminal = false;

                    } else if (dTriangle) {
                        coordinates[0][0] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][0] = Float.valueOf(tokenizer.nextToken());
                        coordinates[0][1] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][1] = Float.valueOf(tokenizer.nextToken());
                        coordinates[0][2] = Float.valueOf(tokenizer.nextToken());
                        coordinates[1][2] = Float.valueOf(tokenizer.nextToken());
                        coordinates[2][0] = 1f;
                        coordinates[2][1] = 1f;
                        coordinates[2][2] = 1f;
                        objects.add(coordinates);
                        resetDFlags();
                        dTerminal = false;


                    }
                }
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

    public void writeVertexPoints() {
        if (objects.size() > 0) {
            for (Float[][] obj : objects) {
                if (obj[1][1] != Float.NEGATIVE_INFINITY) {//if is not a circle
                    for (int i = 0; i < obj[0].length; i++) {
                        fontPos.draw(batch, df.format(obj[0][i]) + "," + df.format(obj[1][i]), obj[0][i], obj[1][i]);
                    }
                }
            }
        }
    }

    public void drawObjects() {
        if (objects.size() > 0) {
            for (Float[][] obj : objects) {
                switch (obj[0].length) {
                    case 2:
                        if (obj[1][1] == Float.NEGATIVE_INFINITY) {//circle
                            renderer.circle(obj[0][0], obj[1][0], obj[0][1], 256);
                            //renderer.ellipse(obj[0][0], obj[1][0], obj[0][1], obj[0][1]);
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
        if (dLine || dTriangle || dSquare) {
            if (coordinates != null)
                for (int i = 0; i < coordinates[0].length; i++)
                    if (coordinates[0][i] != null && coordinates[1][i] != null && coordinates[0][(i + 1) % coordinates[0].length] != null && coordinates[1][(i + 1) % coordinates[0].length] != null)
                        renderer.line(coordinates[0][i], coordinates[1][i], coordinates[0][(i + 1) % coordinates[0].length], coordinates[1][(i + 1) % coordinates[0].length]);
        } else if (dCircle) {
            if (coordinates != null)
                if (coordinates[0][0] != null && coordinates[1][0] != null && coordinates[0][1] != null)
                    renderer.circle(coordinates[0][0], coordinates[1][0], coordinates[0][1], 256);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);//limpa buffer com cor limpa

        verifyInputs();

        stage.act();
        stage.draw();

        //Gdx.gl30.glLineWidth(1);
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(0.44f, 0.44f, 0.44f, 1);
        //renderer.line(line.get(0), line.get(1));

        drawObjects();
        renderer.end();

        batch.begin();
        writeVertexPoints();
        fontPos.draw(batch, df.format(posX) + "," + df.format(posY), posX - 35, posY - 17);

        if (dTerminal)
            font.draw(batch, terminalCommand + text, 0, 800);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.position.set(new Vector3(width / 2, height / 2, 0f));
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }
}
