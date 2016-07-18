package group4.isds577.conserve;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by jorge on 4/16/2015.
 */
public class ParseLoginActivity extends Application{
    public void onCreate(){
        // Initialize Parse and enable local datastore for offline data saves
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "5j96qUWZZlWQYlXU2aShw1QDz5ttrAAqpqDl8mUk", "civ3tGjSsL3yWJWhWNJfSAkbG6VS5KcYvjki0AgX");
    }

}
