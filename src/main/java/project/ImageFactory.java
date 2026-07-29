package project;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageFactory {
    private ImageFactory(){}

    public static void setNodePosition(Node node, double x, double y) {
        node.setLayoutX(x);
        node.setLayoutY(y);
    }

    public static ImageView createImageView(String path, double width, double height) {
        ImageView imageView = new ImageView(new Image(ImageFactory.class.getResource("/Pictures/" + path).toExternalForm()));
        setNodeSize(imageView, width, height);
        return imageView;
    }

    public static void setNodeSize(Node node, double width, double height){
        switch (node){
            case Button btn -> btn.setPrefSize(width, height);
            case Label label -> label.setPrefSize(width, height);
            case ImageView imageView -> {
                imageView.setFitWidth(width);
                imageView.setFitHeight(height);
            }
            default -> {}
        }
    }
}
