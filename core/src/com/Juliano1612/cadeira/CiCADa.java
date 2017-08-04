package com.Juliano1612.cadeira;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.StringTokenizer;

public class CiCADa extends ApplicationAdapter implements ApplicationListener {

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

        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(15f, 15f, 15f);
        camera.lookAt(0, 0, 0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        System.out.println(camera.viewportWidth + "  " + camera.viewportHeight);


        renderer = new ShapeRenderer();

        /*TERMINAL*/
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


        processor = new CameraInputController(camera);

        Gdx.input.setInputProcessor(processor);
        stage.addListener(terminal);


        Gdx.input.setInputProcessor(stage); //stage is responsive
    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        verifyInputs();

        processor.update();
        stage.act();
        stage.draw();

        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        if(showLines){
            renderer.setColor(Color.RED);
            renderer.line(0f, 0f, 0f, 0f, 0f, 999999f);
            renderer.setColor(Color.GREEN);
            renderer.line(0f, 0f, 0f, 0f, 999999f, 0f);
            renderer.setColor(Color.BLUE);
            renderer.line(0f, 0f, 0f, 999999f, 0f, 0f);
            renderer.setColor(0.44f, 0.44f, 0.44f, 1);
        }

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
        }else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && dTerminal){
            dTerminal = false;
            System.out.println("PO!!! capturei o ponto! " + text);
            StringTokenizer strtok = new StringTokenizer(text.trim());
            cx = Float.parseFloat(strtok.nextToken());
            cy = Float.parseFloat(strtok.nextToken());
            cz = Float.parseFloat(strtok.nextToken());
            System.out.println("POS1: " + cx + " POS2: " + cy + " POS3: " + cz);


        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.F12) && !showLines){
            showLines = true;
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.F12) && showLines){
            showLines = false;
        }

//        camera.update();
    }

}