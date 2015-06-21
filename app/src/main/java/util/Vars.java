package util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.SharedPreferences.Editor;

/**
 * Created by MOBICASH on 05-May-15.
 */
public class Vars {
   boolean logg = true;
   public  String longitude;
   public  String latitudes;
   public String location;
   public String name;
   public Editor edit;
   public String mobile;


   public SharedPreferences prefs;

   public Vars (Context context){
      prefs = PreferenceManager.getDefaultSharedPreferences(context);
      location = prefs.getString("location", null);
      longitude = prefs.getString("longitude", null);
      latitudes = prefs.getString("latitudes", null);
      name = prefs.getString("name", null);
      edit = prefs.edit();
      mobile = prefs.getString("mobile",null);

   }
   public void log(String string) {
      if (logg) {
         System.out.println(string);
      } else {

      }
   }
}
