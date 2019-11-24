package bulletapp.bullet;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTest {

    @Test
    public void usernameTest() {
        User user = new User("username");
        assertEquals("username", user.getUsername());
    }

    @Test
    public void removeDocumentTest() {
        User user = new User("username");
        BulletDocument bulletDocument = new BulletDocument();
        user.addDocument(bulletDocument);
        user.removeDocument(bulletDocument);
        assertEquals(user.getNumDocuments(), 0);
    }

    @Test
    public void addDocumentTest() {
        User user = new User("username");
        BulletDocument bulletDocument = new BulletDocument();
        user.addDocument(bulletDocument);
        assertEquals(user.getDocuments().next(), bulletDocument);
    }

}

