package ai.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import javax.swing.JLayeredPane;

public class VideoBackgroundPanel extends JLayeredPane {
    private final DrawingPanel drawingPanel;
    private Component videoComponent;
    private Object fxMediaView;

    public VideoBackgroundPanel(DrawingPanel drawingPanel) {
        super();
        this.drawingPanel = drawingPanel;
        this.drawingPanel.setOpaque(false); // allow underlying video to show through
        add(this.drawingPanel, JLayeredPane.PALETTE_LAYER); // UI/game layer on top

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeChildren();
            }

            @Override
            public void componentShown(ComponentEvent e) {
                resizeChildren();
            }
        });
    }

    public void setBackgroundVideo(String resourceName) {
        URL resourceUrl = getClass().getResource(resourceName);
        if (resourceUrl == null) {
            System.err.println("VideoBackgroundPanel: video resource not found: " + resourceName + ". Falling back to static background image.");
            drawingPanel.setBackgroundImage("images/ocean_background.png");
            repaint();
            return;
        }

        if (videoComponent != null) {
            remove(videoComponent);
            videoComponent = null;
            fxMediaView = null;
        }

        Component newVideoComponent = createJfxVideoComponent(resourceUrl);
        if (newVideoComponent != null) {
            videoComponent = newVideoComponent;
            add(videoComponent, JLayeredPane.DEFAULT_LAYER); // video background on bottom layer
            resizeChildren();
            revalidate();
            repaint();
            drawingPanel.clearBackgroundImage();
        } else {
            System.err.println("VideoBackgroundPanel: failed to create video component. Falling back to static background image.");
            drawingPanel.setBackgroundImage("images/ocean_background.png");
            repaint();
        }
    }

    public void clearBackgroundVideo() {
        if (videoComponent != null) {
            remove(videoComponent);
            videoComponent = null;
            fxMediaView = null;
            revalidate();
            repaint();
        }
        drawingPanel.clearBackgroundImage();
    }

    private void resizeChildren() {
        Dimension size = getSize();
        if (videoComponent != null) {
            videoComponent.setBounds(0, 0, size.width, size.height);
            updateVideoViewSize(size.width, size.height);
        }
        drawingPanel.setBounds(0, 0, size.width, size.height);
    }

    private Component createJfxVideoComponent(URL resourceUrl) {
        try {
            Class<?> jfxPanelClass = Class.forName("javafx.embed.swing.JFXPanel");
            Component jfxPanel = (Component) jfxPanelClass.getDeclaredConstructor().newInstance();

            Class<?> platformClass = Class.forName("javafx.application.Platform");
            java.lang.reflect.Method runLater = platformClass.getMethod("runLater", Runnable.class);
            runLater.invoke(null, (Runnable) () -> {
                try {
                    Class<?> mediaClass = Class.forName("javafx.scene.media.Media");
                    Object media = mediaClass.getConstructor(String.class)
                            .newInstance(resourceUrl.toExternalForm());

                    Class<?> mediaPlayerClass = Class.forName("javafx.scene.media.MediaPlayer");
                    Object mediaPlayer = mediaPlayerClass.getConstructor(mediaClass).newInstance(media);
                    int indefinite = mediaPlayerClass.getField("INDEFINITE").getInt(null);
                    mediaPlayerClass.getMethod("setCycleCount", int.class).invoke(mediaPlayer, indefinite);
                    mediaPlayerClass.getMethod("setMute", boolean.class).invoke(mediaPlayer, true);

                    Class<?> mediaViewClass = Class.forName("javafx.scene.media.MediaView");
                    Object mediaView = mediaViewClass.getConstructor(mediaPlayerClass).newInstance(mediaPlayer);
                    mediaViewClass.getMethod("setPreserveRatio", boolean.class).invoke(mediaView, false);
                    mediaViewClass.getMethod("setSmooth", boolean.class).invoke(mediaView, true);
                    mediaViewClass.getMethod("setFitWidth", double.class).invoke(mediaView, (double) getWidth());
                    mediaViewClass.getMethod("setFitHeight", double.class).invoke(mediaView, (double) getHeight());

                    Class<?> groupClass = Class.forName("javafx.scene.Group");
                    Object root = groupClass.getDeclaredConstructor().newInstance();
                    Object children = groupClass.getMethod("getChildren").invoke(root);
                    children.getClass().getMethod("add", Object.class).invoke(children, mediaView);

                    Class<?> sceneClass = Class.forName("javafx.scene.Scene");
                    Object scene = sceneClass.getConstructor(Class.forName("javafx.scene.Parent"), double.class, double.class)
                            .newInstance(root, (double) getWidth(), (double) getHeight());
                    jfxPanelClass.getMethod("setScene", sceneClass).invoke(jfxPanel, scene);

                    mediaPlayerClass.getMethod("play").invoke(mediaPlayer);
                    fxMediaView = mediaView;
                } catch (Throwable t) {
                    System.err.println("VideoBackgroundPanel: failed to initialize JavaFX video: " + t.getMessage());
                    t.printStackTrace();
                }
            });

            return jfxPanel;
        } catch (ClassNotFoundException e) {
            System.err.println("VideoBackgroundPanel: JavaFX classes not found in classpath. Ensure JavaFX jars are added to lib/ and classpath includes them. Error: " + e.getMessage());
            return null;
        } catch (Throwable t) {
            System.err.println("VideoBackgroundPanel: unexpected error creating JavaFX video component: " + t.getMessage());
            t.printStackTrace();
            return null;
        }
    }

    private void updateVideoViewSize(int width, int height) {
        if (fxMediaView == null) {
            return;
        }

        try {
            Class<?> platformClass = Class.forName("javafx.application.Platform");
            java.lang.reflect.Method runLater = platformClass.getMethod("runLater", Runnable.class);
            runLater.invoke(null, (Runnable) () -> {
                try {
                    fxMediaView.getClass().getMethod("setFitWidth", double.class).invoke(fxMediaView, (double) width);
                    fxMediaView.getClass().getMethod("setFitHeight", double.class).invoke(fxMediaView, (double) height);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
