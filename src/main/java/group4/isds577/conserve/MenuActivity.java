package group4.isds577.conserve;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.widget.EditText;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ParseQuery;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


//Johnson Liu
//I used tutorial and code samples from parse android tutorial here
//REFERENCES
//https://parse.com/docs/android/guide
//https://developer.android.com/training/index.html
//https://parse.com/tutorials
//https://developer.android.com/training/basics/firstapp/building-ui.html
//http://developer.android.com/training/basics/firstapp/starting-activity.html
//http://www.androidbegin.com/tutorial/android-parse-com-listview-images-and-texts-tutorial/

public class MenuActivity extends ActionBarActivity {


    /** Called when the user clicks the sign in button */
    //public void openLogin(View view) {
        //Intent intent = new Intent(this, LoginActivity.class);
       // Button button = (Button)findViewById(R.id.btSignInMain);
       // startActivity(intent);
        // Do something in response to button
   // }

    public void openProfile(View view)
    {
        System.out.println("openProfile");
        setContentView(R.layout.activity_profile2);

        ParseUser user2 = ParseUser.getCurrentUser();
        EditText profileUserName = (EditText)findViewById(R.id.editTextUsername);
        profileUserName.setText(user2.getUsername());

        System.out.println("user is " + ParseUser.getCurrentUser().getUsername());

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(user2.getObjectId(),new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseObject, ParseException e) {
                String email= parseObject.getEmail();
                System.out.println("email is " + email);
                EditText profileEmail = (EditText)findViewById(R.id.editTextEmail);
                profileEmail.setText(email);


            }
        });

        //ParseQuery<ParseUser> query = ParseUser.getQuery();

        EditText profilePassword = (EditText)findViewById(R.id.editTextPassword);
       // profileUserName.setText(user2.get);

    }

    public void backButtonPressed(View view)
    {
        System.out.println("going back");
        setContentView(R.layout.activity_menu);
    }

    public void saveProfileButtonPressed(View view)
    {
        System.out.println("saving");
        //setContentView(R.layout.activity_menu);

        //we should save their new email and password
        EditText profileEmail = (EditText)findViewById(R.id.editTextEmail);

        EditText profilePassword = (EditText)findViewById(R.id.editTextPassword);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(ParseUser.getCurrentUser().getObjectId(),new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseObject, ParseException e) {

                EditText UserText = (EditText)findViewById(R.id.editTextUsername);
                String username = UserText.getText().toString();
                System.out.println("new username is " + username);
                //UserText.setText(username);
                if(username != null || username != "")
                {parseObject.setEmail(username);}

                EditText profileEmail = (EditText)findViewById(R.id.editTextEmail);
                String email = profileEmail.getText().toString();
                System.out.println("new email is " + email);
                //profileEmail.setText(email);
                if(email != null || email != "")
                {parseObject.setEmail(email);}

                EditText profilePassword = (EditText)findViewById(R.id.editTextPassword);
                String newPassword = profilePassword.getText().toString();
                System.out.println("new password is " + newPassword);
                //profileEmail.setText(newPassword);
                if(newPassword != null || newPassword != "")
                {parseObject.setPassword(newPassword);}
            }
        });
    }

    public void viewUserBadges(View view)
    {
        //view user badges
        setContentView(R.layout.user_badge_page);

        //list out all badge titles for each user

        //create a list
        ParseQuery<ParseObject> userBadgeQuery = new ParseQuery<ParseObject>(
                "UserBadges");
        userBadgeQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

        userBadgeQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {

                final ListView userListView = (ListView)findViewById(R.id.userBadgeListView);
                ArrayList<String> userBList = new ArrayList<String>();
                for(int i = 0; i < results.size(); i++)
                {
                    System.out.println("results for open profile size " + results.size());
                    //System.out.println(results.get(i).getString("badgeName"));
                    userBList.add(results.get(i).getString("badgeName"));
                }
                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(MenuActivity.this, android.R.layout.simple_list_item_1, userBList);
                userListView.setAdapter(listAdapter);

                userListView.setClickable(true);
                userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    //http://stackoverflow.com/questions/2468100/android-listview-click-howto
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        final Object o = userListView.getItemAtPosition(position);
                        System.out.println("clicked on a list view " + o.toString());
                        //pass in badge name and then pass in table name

                        ParseQuery<ParseObject> ubq2 = new ParseQuery<ParseObject>(
                                "UserBadges");
                        //inception, we need to go deeper
                        ubq2.whereEqualTo("badgeName", o.toString());
                        ubq2.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> results, ParseException e) {
                                System.out.println("results is inception " + results.size());
                                String bquery = results.get(0).getString("badgeQuery");
                                //pass in current progress and badge class name
                                openBadgePage(o.toString(), bquery);
                                System.out.println("passed in for user badges " + o.toString() + " " + bquery);
                            }  });


                    }
                });

            }
        });
    }



    public void openWaterPage(View view)
    {
        //we open water resource page
        setContentView(R.layout.resource_landing_page);
        TextView title = (TextView)findViewById(R.id.resourceText);

        title.setText("Water");

        final TextView waterTip = (TextView)findViewById(R.id.tipOfTheDay);
        int waterCount = 0;

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "WaterTips");

        query.whereExists("tipText");

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {
                System.out.println("results size " + results.size());
                Random r = new Random();
                int min = 0;
                int max = results.size();
                int i1 = r.nextInt(results.size());
                System.out.println("random number is " + i1);
                ParseObject tip = results.get(i1);
                waterTip.setText(tip.getString("tipText"));
                System.out.println("query find " + tip.getString("tipText"));
            }
        });


        //create a list
        ParseQuery<ParseObject> waterBadgeQuery = new ParseQuery<ParseObject>(
                "WaterBadges");
        waterBadgeQuery.whereExists("badgeTitle");

        ArrayList<String> waterBList = new ArrayList<String>();

        waterBadgeQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {

                final ListView waterList = (ListView)findViewById(R.id.resourceBadgeListView);
                System.out.println("results water list size " + results.size());
                ArrayList<String> waterBList = new ArrayList<String>();
                for(int i = 0; i < results.size(); i++)
                {
                    System.out.println(results.get(i).getString("badgeTitle"));
                    waterBList.add(results.get(i).getString("badgeTitle"));
                }
                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(MenuActivity.this, android.R.layout.simple_list_item_1, waterBList);
                waterList.setAdapter(listAdapter);

                waterList.setClickable(true);
                waterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    //http://stackoverflow.com/questions/2468100/android-listview-click-howto
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        Object o = waterList.getItemAtPosition(position);
                        System.out.println("clicked on a list view " + o.toString());
                        openBadgePage(o.toString(), "WaterBadges");
                    }
                });

            }
        });

    }

    public void openElectricityPage(View view)
    {
        //we open electricity resource page
        setContentView(R.layout.resource_landing_page);
        TextView title = (TextView)findViewById(R.id.resourceText);

        title.setText("Electricity");

        final TextView waterTip = (TextView)findViewById(R.id.tipOfTheDay);
        int electricityCount = 0;

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "ElectricityTips");

        query.whereExists("tipText");

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {
                System.out.println("results size " + results.size());
                Random r = new Random();
                int min = 0;
                int max = results.size();
                int i1 = r.nextInt(results.size());
                System.out.println("random number is " + i1);
                ParseObject tip = results.get(i1);
                waterTip.setText(tip.getString("tipText"));
                System.out.println("query find " + tip.getString("tipText"));
            }
        });


        //create a list
        ParseQuery<ParseObject> electricityBadgeQuery = new ParseQuery<ParseObject>(
                "ElectricityBadges");
        electricityBadgeQuery.whereExists("badgeTitle");



        electricityBadgeQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {

                final ListView electricityList = (ListView)findViewById(R.id.resourceBadgeListView);
                System.out.println("results electricity list size " + results.size());
                ArrayList<String> electricityBList = new ArrayList<String>();
                for(int i = 0; i < results.size(); i++)
                {
                    System.out.println(results.get(i).getString("badgeTitle"));
                    electricityBList.add(results.get(i).getString("badgeTitle"));
                }
                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(MenuActivity.this, android.R.layout.simple_list_item_1, electricityBList);
                electricityList.setAdapter(listAdapter);

                electricityList.setClickable(true);
                electricityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    //http://stackoverflow.com/questions/2468100/android-listview-click-howto
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        Object o = electricityList.getItemAtPosition(position);
                        System.out.println("clicked on a list view " + o.toString());
                        openBadgePage(o.toString(), "ElectricityBadges");
                    }
                });

            }
        });
    }

    public void openWastePage(View view)
    {
        //we open electricity resource page
        setContentView(R.layout.resource_landing_page);
        TextView title = (TextView)findViewById(R.id.resourceText);

        title.setText("Waste");

        final TextView waterTip = (TextView)findViewById(R.id.tipOfTheDay);


        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "WasteTips");

        query.whereExists("tipText");

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {
                System.out.println("results size " + results.size());
                Random r = new Random();
                int min = 0;
                int max = results.size();
                int i1 = r.nextInt(results.size());
                System.out.println("random number is " + i1);
                ParseObject tip = results.get(i1);
                waterTip.setText(tip.getString("tipText"));
                System.out.println("query find " + tip.getString("tipText"));
            }
        });


        //create a list
        ParseQuery<ParseObject> wasteBadgeQuery = new ParseQuery<ParseObject>(
                "WasteBadges");
        wasteBadgeQuery.whereExists("badgeTitle");

        wasteBadgeQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {

                final ListView wasteList = (ListView)findViewById(R.id.resourceBadgeListView);
                System.out.println("results waste list size " + results.size());
                ArrayList<String> wasteBList = new ArrayList<String>();
                for(int i = 0; i < results.size(); i++)
                {
                    System.out.println(results.get(i).getString("badgeTitle"));
                    wasteBList.add(results.get(i).getString("badgeTitle"));
                }
                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(MenuActivity.this, android.R.layout.simple_list_item_1, wasteBList);
                wasteList.setAdapter(listAdapter);

                wasteList.setClickable(true);
               wasteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//http://stackoverflow.com/questions/2468100/android-listview-click-howto
                   @Override
                   public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                       Object o = wasteList.getItemAtPosition(position);
                       System.out.println("clicked on a list view " + o.toString());
                       openBadgePage(o.toString(), "WasteBadges");
                   }
               });

            }
        });
    }

    public void openBadgePage(String badgeTitle, final String badgeQuery)
    {
        System.out.println("opened badge page with " + badgeTitle + " " + badgeQuery);
        setContentView(R.layout.badge_landing_page);
        TextView bTitle = (TextView)findViewById(R.id.badgeText);
        bTitle.setText(badgeTitle);

        //check if the badge already has a current progress and set it first
        //set current progress to be what we set in parse
        ParseQuery<ParseObject> bUserQuery = new ParseQuery<ParseObject>(
                "UserBadges");

        bUserQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
bUserQuery.whereEqualTo("badgeName", badgeTitle);

        //make 2nd query
        ParseQuery<ParseObject> bUserQuery1 = new ParseQuery<ParseObject>(
                "UserBadges");
        bUserQuery1.whereEqualTo("badgeName", badgeTitle);

        //make 1st and 2nd
        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(bUserQuery);
        queries.add(bUserQuery1);
        final String b2Title = badgeTitle;

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);

        bUserQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {
                // results has the list of players that win a lot or haven't won much.
                if(results.size() == 0)
                {
                    TextView add2Profile = (TextView) findViewById(R.id.add2Profile);
                    add2Profile.setText("Added badge to profile");
                    System.out.println("failed in updating progress");

                    ParseObject userBadge = new ParseObject("UserBadges");
                    ParseUser user2 = ParseUser.getCurrentUser();
                    String username = user2.getUsername();
                    userBadge.put("username", username);
                    userBadge.put("badgeName", b2Title);
                    System.out.println("b2title " + b2Title);
                    userBadge.put("badgeQuery", badgeQuery);
                    userBadge.saveInBackground();
                    //failed
                }
                else
                {
                    //retrieved
                    //we want to update the current progress
                    //set max progress of badge
                    System.out.println("Retrieved");
                    ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressBar);

                    ParseObject object = results.get(0);

                    if(object.get("currentProgress") != null) {
                        int currentProgress = Integer.parseInt(object.get("currentProgress").toString());
                        System.out.println("Set current progress at page load to be " + currentProgress);
                        mProgress.setProgress(currentProgress);
                    }
                }
            }
        });







        ParseQuery<ParseObject> BadgeQuery = new ParseQuery<ParseObject>(
                badgeQuery);

        //https://parse.com/docs/android_guide#queries-basic
        BadgeQuery.whereEqualTo("badgeTitle", badgeTitle);
        BadgeQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    //failed
                } else {
                    //retrieved

                    System.out.println("trying to set max progress... badgequery is " + badgeQuery);

                    //we want to set the picture depending on what type of badge we get
                    ImageButton imgB = (ImageButton) findViewById(R.id.imageButton4);

                    if(badgeQuery.equals("ElectricityBadges"))
                    {imgB.setImageResource(R.mipmap.electricity_icon);}
                    else if(badgeQuery.equals("WaterBadges"))
                    {
                        System.out.println("got water badge image");
                        imgB.setImageResource(R.mipmap.water_icon);}
                    else if(badgeQuery.equals("WasteBadges"))
                    {imgB.setImageResource(R.mipmap.waste_icon);}



                    String badge = object.get("badgeObjective").toString();
                    TextView objText = (TextView) findViewById(R.id.objectiveText);
                    objText.setText(badge);

                    //set max progress of badge
                    ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressBar);
                    int maxProgress = Integer.parseInt(object.get("endProgress").toString());
                    mProgress.setMax(maxProgress);
                }
            }
        });

        //set query to save badge to user
        ParseObject userBadge = new ParseObject("UserBadges");

        //get user name
        //if we cannot find an existing one, we make one
//        TextView add2Profile = (TextView) findViewById(R.id.add2Profile);
//        String add2ProfileText = add2Profile.getText().toString();
//
//        if(add2ProfileText == "Added badge to profile") {
//            ParseUser user2 = ParseUser.getCurrentUser();
//            String username = user2.getUsername();
//            userBadge.put("username", username);
//            userBadge.put("badgeName", badgeTitle);
//            System.out.println("b2title " + b2Title);
//            userBadge.put("badgeQuery", badgeQuery);
//            userBadge.saveInBackground();
//        }
    }

    public void addProgress(View view)
    {
        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressBar);

        //get progress of badge
        int i = mProgress.getProgress();
System.out.println("add progress, current progress is " + i);
        mProgress.setProgress(i + 1);

        //set current progress to be what we set in parse
        ParseQuery<ParseObject> BadgeUserQuery = new ParseQuery<ParseObject>(
                "UserBadges");

        //https://parse.com/docs/android_guide#queries-basic

        //get the badge name and pass it
        TextView bTitle = (TextView)findViewById(R.id.badgeText);
        String bName = bTitle.getText().toString();

        BadgeUserQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        BadgeUserQuery.whereEqualTo("badgeName", bName);
        //make 2nd query
        //ParseQuery<ParseObject> BadgeUserQuery1 = new ParseQuery<ParseObject>(
        //        "UserBadges");
        //BadgeUserQuery1.whereEqualTo("badgeName", bName);

        //make 1st and 2nd
        //List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        //queries.add(BadgeUserQuery);
        //queries.add(BadgeUserQuery1);

        //ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);


        BadgeUserQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    System.out.println("failed in updating progress");
                    //failed
                } else {
                    //retrieved
                    //we update the object
                    object.put("currentProgress", mProgress.getProgress());
                    object.saveInBackground();
                    System.out.println("We saved progress " + mProgress.getProgress());
                }
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ParseUser.getCurrentUser().logOut();
            startActivity(new Intent(MenuActivity.this, DispatchActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
