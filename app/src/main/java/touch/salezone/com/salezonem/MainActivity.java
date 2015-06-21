package touch.salezone.com.salezonem;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import adapter.TabsPagerAdapter;
import util.Alert;
import util.IntentIntegrator;
import util.IntentResult;
import util.Vars;


public class MainActivity extends ActionBarActivity {
   private ActionBar actionBar;

   TabsPagerAdapter mSectionsPagerAdapter;
   private String[] tabs = { "Scan ", "Review Logs", "Upload Ads" };


   ViewPager mViewPager;
   Vars vars;
   Alert alert;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      vars= new Vars(this);
      vars.log("location==========="+vars.location);
     if (vars.location==null){
         Intent login = new Intent(this, First_Activity.class);
         startActivity(login);
         finish();

      }
      setContentView(R.layout.mainlayout);
      alert = new Alert(this);

      // Create the adapter that will return a fragment for each of the three
      // primary sections of the activity.
      mSectionsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());

      // Set up the ViewPager with the sections adapter.
      mViewPager = (ViewPager) findViewById(R.id.pager);
      actionBar = getSupportActionBar();
      mViewPager.setAdapter(mSectionsPagerAdapter);

      actionBar.setHomeButtonEnabled(false);
      actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


      for (String tab_name : tabs) {
         actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
               mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
             @Override
             public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

             }
         }));
      }
      mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

         @Override
         public void onPageSelected(int position) {
            // on changing the page
            // make respected tab selected
            actionBar.setSelectedNavigationItem(position);
         }

         @Override
         public void onPageScrolled(int arg0, float arg1, int arg2) {
         }

         @Override
         public void onPageScrollStateChanged(int arg0) {
         }
      });

   }


   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.menu_main, menu);
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
   public void scan_code(View view){
     // IntentIntegrator scanIntegrator = new IntentIntegrator(this);
  //    scanIntegrator.initiateScan();

   }


   public void onActivityResult(int requestCode, int resultCode, Intent intent) {
      //retrieve scan result
       super.onActivityResult(requestCode, resultCode, intent);
      IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
//      vars.log("scanningResult===============" + scanningResult.toString());
      if (scanningResult != null) {
         //we have a result
         String scanContent = scanningResult.getContents();

         String scanFormat = scanningResult.getFormatName();

         if (scanContent!= null){
            //get application content to the next activity
            alert.alerterSuccess("Success","User valid");

         }else{
            Intent back = new Intent(this, MainActivity.class);
            startActivity(back);
         }
      }
      /*else{
         Toast toast = Toast.makeText(getApplicationContext(),
                "No scan data received!", Toast.LENGTH_SHORT);
         toast.show();
         Intent us = new Intent(this,MainActivity.class);
         startActivity(us);
      }*/
   }

/*

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //retrieve scan result
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//      vars.log("scanningResult===============" + scanningResult.toString());
        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();

            String scanFormat = scanningResult.getFormatName();

            if (scanContent!= null){
                //get application content to the next activity
                alert.alerterSuccess("Success","User valid");

            }else{
                Intent back = new Intent(this, MainActivity.class);
                startActivity(back);
            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
            Intent us = new Intent(this,MainActivity.class);
            startActivity(us);
        }

        }
*/

    }


