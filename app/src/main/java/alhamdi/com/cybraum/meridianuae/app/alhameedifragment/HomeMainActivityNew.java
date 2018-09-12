package alhamdi.com.cybraum.meridianuae.app.alhameedifragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tuyenmonkey.mkloader.MKLoader;

import java.util.HashMap;

public class HomeMainActivityNew extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,Main_Fragment.OnFragmentInteractionListener,ContactUsFragment.OnFragmentInteractionListener,AboutFragment.OnFragmentInteractionListener,CompanyProfileFragment.OnFragmentInteractionListener,LocationFragment.OnFragmentInteractionListener,EnquiryFragment.OnFragmentInteractionListener,
        AskLawyerFragment.OnFragmentInteractionListener,ServiceFragment.OnFragmentInteractionListener,AppointmentFragment.OnFragmentInteractionListener,MyAccountFragment.OnFragmentInteractionListener,MyDocumentFragment.OnFragmentInteractionListener,MyMeetingFragment.OnFragmentInteractionListener,AccountSettingsFragment.OnFragmentInteractionListener
    ,ServiceDetailsFragment.OnFragmentInteractionListener,AddMyMeetingFragment.OnFragmentInteractionListener,EditMyMeetingFragment.OnFragmentInteractionListener,ChangePassword.OnFragmentInteractionListener,MyLawyerFragment.OnFragmentInteractionListener,AddDocumentFragment.OnFragmentInteractionListener,FaqFragment.OnFragmentInteractionListener
    ,SetDocumentFragment.OnFragmentInteractionListener,DocumentDetails.OnFragmentInteractionListener,ChangeDocumentPassword.OnFragmentInteractionListener
{
    public static boolean doubleBackToExitPressedOnce = false;
  public static ImageView navOpen, user_icon;
    public static LinearLayout prev_icon2,prev_icon_nav;


    public static MKLoader progress_loader;
    public static LinearLayout progress_loader_layout;


    public static NavigationView navigationView;
    public static DrawerLayout drawer;
    LinearLayout nav_about_us,nav_company_profile,nav_location,nav_enquiry,nav_contact_us,fb_icon,twitter_icon,logout,nav_rate_this_app,nav_login;

    SessionManager sm;

    FrameLayout frame;
    public  static Toolbar toolbar;
public  String service_click_id="";
    public static String loged_id, loged_username, loged_name, loged_email, loged_location, loged_phone,skiped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home_main_new);



        frame= (FrameLayout) findViewById(R.id.frame);


     toolbar = (Toolbar) findViewById(R.id.toolbar);

        progress_loader=(MKLoader)findViewById(R.id.progress_loader);
        progress_loader_layout=(LinearLayout)findViewById(R.id.progress_loader_layout);



         /*------------------------ LOGIN CHECK --START-----------------------*/
        sm = new SessionManager(getApplicationContext());

        /*------------------------ LOGIN CHECK --END-----------------------*/


        drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setBackgroundResource(R.drawable.homebg);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);



        navigationView = (NavigationView) findViewById(R.id.navigation_drawer_container);

        navigationView.setNavigationItemSelectedListener(this);





//replace  with mainm fragment

         Main_Fragment menu = new Main_Fragment();   // instantiate fragment
        getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame, menu).commit();


        navigationView.setBackgroundResource(R.drawable.sidebar_bg);

        View hView = navigationView.getHeaderView(0);
      /*  nav_drawer_name = (TextView) hView.findViewById(R.id.nav_drawer_name);
        nav_drawer_address = (TextView) hView.findViewById(R.id.nav_drawer_address);*/
        /*nav_image = (ImageView) hView.findViewById(R.id.nav_drawer_imageView);*/


        nav_about_us=(LinearLayout)findViewById(R.id.nav_about_us);
        nav_company_profile=(LinearLayout)findViewById(R.id.nav_company_profile);
        nav_location=(LinearLayout)findViewById(R.id.nav_location);
        nav_enquiry=(LinearLayout)findViewById(R.id.nav_enquiry);
        nav_contact_us=(LinearLayout)findViewById(R.id.nav_contact_us);
        fb_icon=(LinearLayout)findViewById(R.id.fb_icon);
        twitter_icon=(LinearLayout)findViewById(R.id.twitter_icon);
        nav_rate_this_app=(LinearLayout)findViewById(R.id.nav_rate_this_app);
        nav_login=(LinearLayout)findViewById(R.id.nav_login);
        logout=(LinearLayout)findViewById(R.id.nav_logout);

        nav_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                AboutFragment aboutFragment = new AboutFragment() ;  // instantiate fragment
                getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame, aboutFragment).addToBackStack("").commit();

            }
        });
        nav_company_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                CompanyProfileFragment cpf = new CompanyProfileFragment();   // instantiate fragment
                getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame, cpf).addToBackStack("").commit();

            }
        });
        nav_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                LocationFragment lf = new LocationFragment();   // instantiate fragment
                getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame, lf).addToBackStack("").commit();

            }
        });
        nav_enquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                EnquiryFragment lf = new EnquiryFragment();
                Bundle enquirybundle = new Bundle();
                enquirybundle.putSerializable("from", "mainfragment");
                lf.setArguments(enquirybundle);
                // instantiate fragment
                getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame, lf).addToBackStack("").commit();

            }
        });
        nav_contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                ContactUsFragment contactUsFragment = new ContactUsFragment();   // instantiate fragment
                getFragmentManager().beginTransaction().setCustomAnimations( R.animator.slide_in_right, 0, 0, R.animator.slide_out_left).replace(R.id.frame, contactUsFragment).addToBackStack("").commit();

            }
        });

        nav_rate_this_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=alhamdi.com.cybraum.meridianuae.app.alhameedifragment"));
                startActivity(i);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        nav_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), Login.class);

                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SessionManager s=new SessionManager(getApplicationContext());
                s.logoutUser();

                finish();

            }
        });

        fb_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            goToUrl("http://facebook.com/");
            }
        });

        twitter_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUrl("http://twitter.com/");
            }
        });

/* GETTING LOGGED IN PERSON'S DETAILS --START */

        HashMap<String, String> details = sm.getUserDetails();
        loged_id = details.get(sm.KEY_ID);
        loged_username = details.get(sm.KEY_USER_NAME);
        loged_name = details.get(sm.KEY_NAME);
        loged_email = details.get(sm.KEY_EMAIL);
        loged_location = details.get(sm.KEY_LOCATION);
        loged_phone = details.get(sm.KEY_PHONE);
        skiped=details.get(sm.KEY_IS_SKIPED);
        System.out.println("skiped : "+skiped);

        System.out.println("____________________________________________________________________________");
        System.out.println("Inside HomeMainActivityNew(login success)");
        System.out.println("jsonId:" + loged_id + "\n" +
                "jsonUserName:" + loged_username + "\n" +
                "jsonName:" + loged_name + "\n" +
                "jsonEmail:" + loged_email + "\n" +
                "jsonLocation:" + loged_location + "\n" +
                "jsonPhone:" + loged_phone + "\n");
        System.out.println("____________________________________________________________________________");

/* GETTING LOGGED IN PERSON'S DETAILS --END */


if(skiped.equalsIgnoreCase("true")){
    logout.setVisibility(View.GONE);
    nav_login.setVisibility(View.VISIBLE);
}else {
    logout.setVisibility(View.VISIBLE);
    nav_login.setVisibility(View.GONE);
}


        user_icon = (ImageView) findViewById(R.id.user_icon);

        prev_icon2 = (LinearLayout) findViewById(R.id.prev_icon2);
        prev_icon_nav = (LinearLayout) findViewById(R.id.prev_icon_nav);
        navOpen = (ImageView) findViewById(R.id.nav_open);
        navOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(navigationView);
            }
        });


        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        prev_icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer= (DrawerLayout)findViewById(R.id.drawer_layout);
                drawer.setBackgroundResource(R.drawable.homebg);

                HomeMainActivityNew.prev_icon2.setVisibility(View.GONE);
                HomeMainActivityNew.navOpen.setBackgroundResource(R.drawable.ic_dehaze_white_24dp);
                HomeMainActivityNew.navOpen.setVisibility(View.VISIBLE);
                HomeMainActivityNew.user_icon.setVisibility(View.VISIBLE);
                HomeMainActivityNew.prev_icon2.setVisibility(View.GONE);
                HomeMainActivityNew.prev_icon_nav.setVisibility(View.GONE);

                HomeMainActivityNew.super.onBackPressed();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


            }
        });

        prev_icon_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer= (DrawerLayout)findViewById(R.id.drawer_layout);
                drawer.setBackgroundResource(R.drawable.homebg);

                HomeMainActivityNew.prev_icon2.setVisibility(View.GONE);
                HomeMainActivityNew.prev_icon_nav.setVisibility(View.GONE);
                HomeMainActivityNew.navOpen.setBackgroundResource(R.drawable.ic_dehaze_white_24dp);
                HomeMainActivityNew.navOpen.setVisibility(View.VISIBLE);
                HomeMainActivityNew.user_icon.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


                Main_Fragment lf=new Main_Fragment();
                getFragmentManager().beginTransaction().setCustomAnimations(0, 0, 0, R.animator.slide_out_left).replace(R.id.frame, lf).commit();



            }
        });

    }



    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {

            Main_Fragment lf=new Main_Fragment();
            getFragmentManager().beginTransaction().replace(R.id.frame, lf).commit();

            if (doubleBackToExitPressedOnce) {
                /*final AlertDialog.Builder builder=new AlertDialog.Builder(this);

                builder.setMessage("Exit from Al Hammadi ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();*/


                finish();

            }

            this.doubleBackToExitPressedOnce = true;

           /* super.onBackPressed();*/



        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        return true;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

}
