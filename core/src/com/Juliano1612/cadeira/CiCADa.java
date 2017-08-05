package com.Juliano1612.cadeira;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.StringTokenizer;

public class CiCADa extends ApplicationAdapter implements ApplicationListener, InputProcessor {

    PerspectiveCamera camera;
    Stage stage;
    SpriteBatch batch;
    boolean dTerminal = false, showLines = false;
    String text = "", terminalCommand = "";
    BitmapFont font;

    float cx = 0, cy = 0, cz = 8;

    MeshBuilder meshb;

    ShapeRenderer renderer;
    private CameraInputController processor;

    @Override
    public void create() {
        font = new BitmapFont();
        font.setColor(0.44f, 0.44f, 0.44f, 1);

        batch = new SpriteBatch();
        renderer = new ShapeRenderer();

        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(15f, 15f, 15f);
        camera.lookAt(0, 0, 0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        processor = new CameraInputController(camera);
        Gdx.input.setInputProcessor(new InputMultiplexer(processor, this));
    }

    private float help = 0f;
    private void drawDottedLine(ShapeRenderer shapeRenderer, float x1, float y1, float z1, float x2, float y2, float z2) {
        float dotDist = 0.25f;
        ShapeRenderer.ShapeType revive = null;
        if (shapeRenderer.isDrawing()) {
            revive = shapeRenderer.getCurrentType();
            shapeRenderer.end();
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Point);

        Vector3 vec3 = new Vector3(x2, y2, z2).sub(new Vector3(x1, y1, z1));
        float length = vec3.len();
        for (float i = help; i < length; i += dotDist + help) {
            vec3.clamp(length - i, length - i);
            shapeRenderer.point(x1 + vec3.x, y1 + vec3.y, z1 + vec3.z);
        }

        shapeRenderer.end();
        if (revive != null)
            shapeRenderer.begin(revive);
    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        verifyInputs();

        processor.update();

        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        if (showLines) {
            float inc = 2.5f;
            int max = 999;
            help = 0f;

            Color red = Color.RED.cpy();
            red.a = 0f;
            renderer.setColor(red);
            for (float i = 0; i < max; i += inc) {
                drawDottedLine(renderer, i, 0f, -max, i, 0f, max);
                drawDottedLine(renderer, 0f, i, -max, 0f, i, max);
            }
            help += 0.1f;

            Color green = Color.GREEN.cpy();
            green.a = 0.01f;
            renderer.setColor(green);
            for (float i = 0; i < max; i += inc) {
                drawDottedLine(renderer,  i, -max, 0f, i, max, 0f);
                drawDottedLine(renderer,  0f, -max, i, 0f, max, i);
            }
            help += 0.1f;

            Color blue = Color.BLUE.cpy();
            blue.a = 0.01f;
            renderer.setColor(blue);
            for (float i = 0; i < max; i += inc) {
                drawDottedLine(renderer, -max, i, 0f, max, i, 0f);
                drawDottedLine(renderer, -max, 0f, i, max, 0f, i);
            }

        }

        renderer.setColor(Color.WHITE);
        renderer.box(cx, cy, cz, 8f, 8f, 8f);

        renderer.end();

        batch.begin();
        if (dTerminal)
            font.draw(batch, terminalCommand + text, 0, camera.viewportHeight - 10);
        batch.end();
    }


    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
//        batch.dispose();
        stage.dispose();
    }

    public void verifyInputs() {

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && !dTerminal) {
            System.out.println("oi");
            dTerminal = true;
            terminalCommand = "Vamos transladar\nDigite o ponto para translação: ";
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && dTerminal) {
            dTerminal = false;
            System.out.println("PO!!! capturei o ponto! " + text);
            StringTokenizer strtok = new StringTokenizer(text.trim());
            cx = Float.parseFloat(strtok.nextToken());
            cy = Float.parseFloat(strtok.nextToken());
            cz = Float.parseFloat(strtok.nextToken());
            System.out.println("POS1: " + cx + " POS2: " + cy + " POS3: " + cz);


        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F12) && !showLines) {
            showLines = true;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.F12) && showLines) {
            showLines = false;
        }

//        camera.update();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}