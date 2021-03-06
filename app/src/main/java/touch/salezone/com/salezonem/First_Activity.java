package touch.salezone.com.salezonem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.Settings;
import android.os.Bundle;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import android.location.Location;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import util.Alert;
import util.GPSTracker;
import util.Vars;


public class First_Activity extends Activity implements ConnectionCallbacks,
       OnConnectionFailedListener {
   // LogCat tag
   AlertDialog alertDialog;
    boolean ismage= false;
   private static final String TAG = First_Activity.class.getSimpleName();
    String myloc;
   private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
     final static String URL_FEED = "http://salezone.co/salezonem/SaleZone/register.php";
   private Location mLastLocation;
   double latitude ;
    Bitmap photo;
   double longitude ;
   GPSTracker gps;
   Vars vars;
    String number;
   // Google client to interact with Google API
   private GoogleApiClient mGoogleApiClient;

   // boolean flag to toggle periodic location updates
   private boolean mRequestingLocationUpdates = false;

   private LocationRequest mLocationRequest;

   // Location updates intervals in sec
   private static int UPDATE_INTERVAL = 10000; // 10 sec
   private static int FATEST_INTERVAL = 5000; // 5 sec
   private static int DISPLACEMENT = 10; // 10 meters
   Alert alert;
   // UI elements
   private TextView lblLocation;
   EditText business_name,mobile;
    ImageView iv_ProfilePic;
    private static int RESULT_LOAD_IMAGE = 1;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      gps = new GPSTracker(this);
      vars = new Vars(this);
       alert = new Alert(this);
      lblLocation = (TextView) findViewById(R.id.lblLocation);
      business_name = (EditText) findViewById(R.id.business_name);
       iv_ProfilePic = (ImageView) findViewById(R.id.iv_profilePic);
      mobile = (EditText) findViewById(R.id.mobile);


      // First we need to check availability of play services
      if (checkPlayServices()) {

         // Building the GoogleApi client
         buildGoogleApiClient();
      }

      // Show location button click listener
    /*  btnShowLocation.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View v) {
            displayLocation();
         }
      });*/
   }

   /**
    * Method to display the location on UI
    * */
   private void displayLocation() {

      mLastLocation = LocationServices.FusedLocationApi
             .getLastLocation(mGoogleApiClient);


      if (mLastLocation != null) {
         latitude = mLastLocation.getLatitude();
         longitude = mLastLocation.getLongitude();
         (new GetAddressTask(this)).execute(mLastLocation);

  //       lblLocation.setText(latitude + ", " + longitude);

      } else {

         alertbox("","");

      }
   }

   /**
    * Creating google api client object
    * */
   protected synchronized void buildGoogleApiClient() {
      mGoogleApiClient = new GoogleApiClient.Builder(this)
             .addConnectionCallbacks(this)
             .addOnConnectionFailedListener(this)
             .addApi(LocationServices.API).build();
   }

   /**
    * Method to verify google play services on the device
    * */
   private boolean checkPlayServices() {
      int resultCode = GooglePlayServicesUtil
             .isGooglePlayServicesAvailable(this);
      if (resultCode != ConnectionResult.SUCCESS) {
         if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                   PLAY_SERVICES_RESOLUTION_REQUEST).show();
         } else {
            Toast.makeText(getApplicationContext(),
                   "This device is not supported.", Toast.LENGTH_LONG)
                   .show();
            finish();
         }
         return false;
      }
      return true;
   }

   @Override
   protected void onStart() {
      super.onStart();
      if (mGoogleApiClient != null) {
         mGoogleApiClient.connect();
      }
   }

   @Override
   protected void onResume() {
      super.onResume();

      checkPlayServices();
   }

   /**
    * Google api callback methods
    */
   @Override
   public void onConnectionFailed(ConnectionResult result) {
      Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
             + result.getErrorCode());
   }

   @Override
   public void onConnected(Bundle arg0) {

      // Once connected with google api, get the location
      displayLocation();
   }

   @Override
   public void onConnectionSuspended(int arg0) {
      mGoogleApiClient.connect();
   }
   public String mylocation_is( Double latitude,Double longitude){
      String mylocation= null;

      String locale = null;

      if(gps.canGetLocation()){


         Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
         try {
            List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);

            String add = "";
            if (addresses.size() > 0)
            {
               locale = addresses.get(0).getCountryName();
               add += addresses.get(0).getAddressLine(1);

            }
            mylocation= add+" "+locale;
            //   addressLabel.setText(add);
         }
         catch (IOException e1) {
            e1.printStackTrace();
         }
      }

      return mylocation;

   }
   protected void alertbox(String title, String mymessage) {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setMessage("Please enable it once and we promise your location will never be shared with anyone.")
             .setCancelable(false)
             .setTitle("Your GPS is disabled")
             .setPositiveButton("Turn on",
                    new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {

                          Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                          startActivity(intent);
                          dialog.cancel();
                       }
                    })
             .setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                          // cancel the dialog box
                          dialog.cancel();
                       }
                    });
      AlertDialog alert = builder.create();
      alert.show();
   }
   private class GetAddressTask extends AsyncTask<Location, String, String> {
      Context mContext;
      public GetAddressTask(Context context) {
         super();
         mContext = context;
      }

      /*
       * When the task finishes, onPostExecute() displays the address.
       */
      @Override
      protected void onPostExecute(String address) {
         vars.log("my location is =========="+address);
          myloc = address;
         lblLocation.setText(address);
      }
      @Override
      protected String doInBackground(Location... params) {
         Geocoder geocoder =
                new Geocoder(mContext, Locale.getDefault());
         // Get the current location from the input parameter list
         Location loc = params[0];
         // Create a list to contain the result address
         List<Address> addresses = null;
         try {

            addresses = geocoder.getFromLocation(latitude,
                   longitude, 1);
         } catch (IOException e1) {
            Log.e("LocationSampleActivity",
                   "IO Exception in getFromLocation()");
            e1.printStackTrace();
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            return mylocation_is(latitude,longitude);
         } catch (IllegalArgumentException e2) {
            // Error message to post in the log
            String errorString = "Illegal arguments " +
                   Double.toString(loc.getLatitude()) +
                   " , " +
                   Double.toString(loc.getLongitude()) +
                   " passed to address service";
            Log.e("LocationSampleActivity", errorString);
            e2.printStackTrace();
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            return mylocation_is(latitude,longitude);
         }
         // If the reverse geocode returned an address
         if (addresses != null && addresses.size() > 0) {
            // Get the first address
            Address address = addresses.get(0);
            /*
            * Format the first line of address (if available),
            * city, and country name.
            */
            String addressText = String.format(
                   "%s, %s, %s",
                   // If there's a street address, add it
                   address.getMaxAddressLineIndex() > 0 ?
                          address.getAddressLine(0) : "",
                   // Locality is usually a city
                   address.getLocality(),
                   // The country of the address
                   address.getCountryName());
            // Return the text
            return addressText;
         } else {
            return "No address found";
         }
      }
   }// AsyncTask class
   public void register(View view){
      displayLocation();

       TelephonyManager manager = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
       String CountryID = manager.getSimCountryIso().toUpperCase();

       number = mobile.getText().toString();
       PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
       try {
           Phonenumber.PhoneNumber phone_number = phoneUtil.parse(number, CountryID);

           boolean isValid = phoneUtil.isValidNumber(phone_number);
           if (isValid) {
               if(gps_is()){
               if(lblLocation.getText()!=null || myloc==null ) {
                   if (business_name.getText().toString().equalsIgnoreCase("")) {
                       Toast.makeText(this, "Enter business name", Toast.LENGTH_SHORT).show();
                   } else {
                       vars.edit.putString("location", lblLocation.getText().toString());
                       vars.edit.putString("latitudes", Double.toString(latitude));
                       vars.edit.putString("longitude", Double.toString(longitude));
                       vars.edit.putString("name", business_name.getText().toString());
                       vars.edit.putString("mobile", mobile.getText().toString());
                    //   vars.edit.commit();
                       aync_data();
                    //   Intent main = new Intent(this, MainActivity.class);
                     //  startActivity(main);
                   //    finish();

                   }
               }else{
                   displayLocation();
               }
               }else{
                   alertbox("","");

               }

           } else {

               alert.alerterSuccess("Error", "Mobile number invalid");
           }
       } catch (NumberParseException e) {
           System.err.println("NumberParseException was thrown: " + e.toString());
       }


   }
    public boolean gps_is(){
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }
    public void puticon(View v){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            ismage = true;
            iv_ProfilePic.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            photo = BitmapFactory.decodeFile(picturePath);
        }


    }
    private void aync_data(){
        if(ismage) {
            AQuery aq = new AQuery(this);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();


            alertDialog = ProgressDialog.show(this, "", "Please wait...");
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("name",  business_name.getText().toString());
            params.put("location", lblLocation.getText().toString());
            params.put("latitude",  Double.toString(latitude));
            params.put("longititude",  Double.toString(longitude));
            params.put("mobile", mobile.getText().toString());
            params.put("data", data);

            aq.ajax(URL_FEED, params, JSONObject.class, new AjaxCallback<JSONObject>() {

                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    int source = status.getSource();
                    int responseCode = status.getCode();
                    long duration = status.getDuration();
                    Date fetchedTime = status.getTime();
                    String message = status.getMessage();
                    String redirect = status.getRedirect();
                    DefaultHttpClient client = status.getClient();

                    //returns the html response when response code is not 2xx
                    String errorResponse = status.getError();
                    vars.log("sou========" + source);
                    vars.log("responseCode========" + responseCode);
                    vars.log("duration========" + duration);
                    vars.log("fetchedTime========" + fetchedTime);
                    vars.log("message========" + message);
                    vars.log("redirect========" + redirect);
                    vars.log("client========" + client);
                    vars.log("errorResponse========" + errorResponse);

                    if (responseCode != 200) {
                        alertDialog.dismiss();
                        alert.alerterSuccess("Error", "No internet connection");
                    } else if (json != null) {
                        vars.log("===before if statement=======" + json.toString());
                        if (json.toString() != null) {
                            alertDialog.dismiss();
                            alert.alerterSuccess("Goood", json.toString());
                        }
                    } else {
                        vars.log("====================string========================null");
                    }
                }


            });
        }else{
            alert.alerterSuccess("Error", "Upload your icon");
        }
    }
}
