package group4.isds577.conserve;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.parse.ParseUser;

/**
 * Created by jorge on 4/16/2015.
 */
public class DispatchActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login); need to create a layout with Conserve logo and bye message
        //otherwise when user presses back, blank screen appears
        //method created by rufflez on 7/8/14 edited by jorge 4/16/2015
        // Check if there is current user info
        if (ParseUser.getCurrentUser() != null) {
            // Start an intent for the logged in activity
            startActivity(new Intent(this, MenuActivity.class));
        } else {
            // Start and intent for the logged out activity
            startActivity(new Intent(this, SignUpActivity.class));
        }
    }
}
