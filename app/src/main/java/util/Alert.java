package util;
import touch.salezone.com.salezonem.MainActivity;
import touch.salezone.com.salezonem.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by MOBICASH on 09-May-15.
 */
public class Alert {
   SharedPreferences prefs;
   SharedPreferences.Editor edit;
   Vars vars;
   TextView serverText;
   AlertDialog levelDialog;

   Context context;

   public Alert(Context context) {
      this.context = context;


   }


   public void alerterSuccess(String header, String message){

      final String header2 = header;

      LayoutInflater li = LayoutInflater.from(context);
      View promptsView;
      promptsView = li.inflate(R.layout.dialog_success_simple, null);

      TextView headerTxt = (TextView) promptsView.findViewById(R.id.success_simple_header);
      TextView messageTxt = (TextView) promptsView.findViewById(R.id.success_simple_message);

      headerTxt.setText(header);
      messageTxt.setText(message);

      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
             context);

      // set prompts.xml to alertdialog builder
      alertDialogBuilder.setView(promptsView);
      // set dialog message
      alertDialogBuilder
             .setCancelable(false)
             .setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog,int id) {
                          //dialog.cancel();
                          dialog.cancel();

                       }
                    });


      // create alert dialog
      AlertDialog alertDialog = alertDialogBuilder.create();

      // show it
      alertDialog.show();
   }
}
