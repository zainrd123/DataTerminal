package com.drozal.dataterminal;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import static org.controlsfx.tools.Utils.clamp;

/**
 * The type Map controller.
 */
public class mapController {
    /**
     * The Los santos map.
     */
    public ImageView losSantosMap;
    private double lastX;
    private double lastY;

    /**
     * On los santos map scroll.
     *
     * @param scrollEvent the scroll event
     */
    public void onLosSantosMapScroll(ScrollEvent scrollEvent) {
        double deltaYScroll = scrollEvent.getDeltaY();
        double MIN_SCALE = 1.0;
        double MAX_SCALE = 10.0;
        double SCROLL_FACTOR = 0.015;

        deltaYScroll = -deltaYScroll;

        double mouseX = scrollEvent.getX();
        double mouseY = scrollEvent.getY();

        double currentScaleX = losSantosMap.getScaleX();
        double currentScaleY = losSantosMap.getScaleY();

        double newScale = currentScaleX + deltaYScroll * SCROLL_FACTOR;
        newScale = clamp(newScale, MIN_SCALE, MAX_SCALE);

        double pivotX = mouseX - losSantosMap.getBoundsInParent().getMinX();
        double pivotY = mouseY - losSantosMap.getBoundsInParent().getMinY();
        double pivotDeltaX = (pivotX * (newScale - currentScaleX));
        double pivotDeltaY = (pivotY * (newScale - currentScaleY));

        double deltaX = pivotDeltaX - pivotX * (newScale - currentScaleX);
        double deltaY = pivotDeltaY - pivotY * (newScale - currentScaleY);

        losSantosMap.setTranslateX(losSantosMap.getTranslateX() + deltaX);
        losSantosMap.setTranslateY(losSantosMap.getTranslateY() + deltaY);

        losSantosMap.setScaleX(newScale);
        losSantosMap.setScaleY(newScale);

        scrollEvent.consume();
    }

    /**
     * Map mouse drag.
     *
     * @param mouseEvent the mouse event
     */
    public void mapMouseDrag(MouseEvent mouseEvent) {
        double deltaX = mouseEvent.getSceneX() - lastX;
        double deltaY = mouseEvent.getSceneY() - lastY;

        losSantosMap.setTranslateX(losSantosMap.getTranslateX() + deltaX);
        losSantosMap.setTranslateY(losSantosMap.getTranslateY() + deltaY);

        lastX = mouseEvent.getSceneX();
        lastY = mouseEvent.getSceneY();
    }

    /**
     * Map mouse press.
     *
     * @param mouseEvent the mouse event
     */
    public void mapMousePress(MouseEvent mouseEvent) {
        lastX = mouseEvent.getSceneX();
        lastY = mouseEvent.getSceneY();
    }

    /**
     * On zoom in pressed.
     *
     * @param mouseEvent the mouse event
     */
    public void onZoomInPressed(MouseEvent mouseEvent) {
        zoom(losSantosMap, 1.5);
    }

    /**
     * On zoom out pressed.
     *
     * @param mouseEvent the mouse event
     */
    public void onZoomOutPressed(MouseEvent mouseEvent) {
        zoom(losSantosMap, 0.5);
    }


    private void zoom(ImageView imageView, double factor) {
        double currentScaleX = imageView.getScaleX();
        double currentScaleY = imageView.getScaleY();

        double newScaleX = currentScaleX * factor;
        double newScaleY = currentScaleY * factor;

        double MIN_SCALE = 1.0;
        double MAX_SCALE = 10.0;
        newScaleX = clamp(newScaleX, MIN_SCALE, MAX_SCALE);
        newScaleY = clamp(newScaleY, MIN_SCALE, MAX_SCALE);

        imageView.setScaleX(newScaleX);
        imageView.setScaleY(newScaleY);
    }

}