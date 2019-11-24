package bulletapp.bullet;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class User {

    @Getter
    @Setter
    private String username;

    private final List<BulletDocument> bulletDocuments = new ArrayList<>(1);

    User(String username) {
        this.username = username;
    }

    void addDocument(BulletDocument bulletDocument) {
        bulletDocuments.add(bulletDocument);
    }

    BulletDocument getDocumentByName(String documentName) {
        for (BulletDocument document : bulletDocuments) {
            if (document.getDocumentName().equals(documentName)) {
                return document;
            }
        }
        return null;
    }


    BulletDocument getDocumentAt(int index) {
        return bulletDocuments.get(index);
    }

    void removeDocument(BulletDocument bulletDocument) {
        bulletDocuments.remove(bulletDocument);
    }

    Iterator<BulletDocument> getDocuments() {
        return bulletDocuments.iterator();
    }

    int getNumDocuments() {
        return bulletDocuments.size();
    }

    int indexOf(BulletDocument bulletDocument) {
        return bulletDocuments.indexOf(bulletDocument);
    }

}
