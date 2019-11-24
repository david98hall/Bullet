package bulletapp.view.javafx.nodes.content;

import bulletapp.bullet.Bullet;
import bulletapp.controller.BulletController;
import bulletapp.util.Id;
import bulletapp.view.interfaces.IContentView;
import bulletapp.view.javafx.nodes.interfaces.INode;
import bulletapp.view.javafx.utils.FXMLUtils;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * @author David Hall, Anton Forsberg
 */
public abstract class ContentNode implements INode, IContentView {

    private final Pane rootPane;

    private Id<String> containerBulletPointId;
    private Id<Integer> indexId;

    protected final static BulletController bulletController = BulletController.getInstance();

    protected ContentNode(String fxmlPath, Pane root) {
        super();
        this.rootPane = root;
        FXMLUtils.loadFXMLNode(fxmlPath, this, rootPane);
    }

    protected ContentNode(String data, String fxmlPath, Pane root) {
        this(fxmlPath, root);
        setContentData(data);
    }

    public abstract String getData();

    @Override
    public Node getNode() {
        return rootPane;
    }

    @Override
    public void setContentData(String data) {
        if (indexId != null && containerBulletPointId != null) {
            bulletController.updateData(containerBulletPointId.getId(), indexId.getId(), data);
        }
    }

    protected void setPrefSize(double width, double height) {
        rootPane.setPrefSize(width, height);
    }

    public void setContainerBulletPointId(Id<String> containerBulletPointId) {
        this.containerBulletPointId = containerBulletPointId;
    }

    public void setIndexId(Id<Integer> indexId) {
        this.indexId = indexId;
    }

    public void setIds(Id<String> containerBulletPointId, Id<Integer> indexId) {
        setContainerBulletPointId(containerBulletPointId);
        setIndexId(indexId);
        setContentData(getData());
    }

    public String getContainerId() {
        return containerBulletPointId.getId();
    }

}
