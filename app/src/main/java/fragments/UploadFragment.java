package fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Toast;

import touch.salezone.com.salezonem.MainActivity;
import touch.salezone.com.salezonem.R;

public class UploadFragment extends Fragment implements View.OnClickListener{
	EditText editText_startdate,editText_enddate,editText_description;
	CheckBox checkBox_image;
	ImageView uploadimage;
	private static int RESULT_LOAD_IMAGE = 1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_upload, container, false);
		checkBox_image = (CheckBox) rootView.findViewById(R.id.checkBox_image);
		uploadimage = (ImageView) rootView.findViewById(R.id.imageView_upload);
		uploadimage.setVisibility(View.GONE);
		uploadimage.setOnClickListener(this);
		editText_startdate = (EditText) rootView.findViewById(R.id.editText_startdate);
		editText_enddate = (EditText) rootView.findViewById(R.id.editText_enddate);
		editText_description = (EditText) rootView.findViewById(R.id.editText_description);
		editText_enddate.setFocusable(false);
		editText_startdate.setFocusable(false);
		editText_startdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(getActivity(),"fire",Toast.LENGTH_SHORT).show();
				DialogFragment newFragment = new DatePickerFragment() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int day) {
						editText_startdate.setText("" + year + "-" + month + "-" + day);
						super.onDateSet(view, year, month, day);
					}
				};
				newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

			}
		});
		editText_enddate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(getActivity(),"fire",Toast.LENGTH_SHORT).show();
				DialogFragment newFragment = new DatePickerFragment() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int day) {
						editText_enddate.setText("" + year+ "-" + month+ "-" + day);
						super.onDateSet(view, year, month, day);
					}
				};
				newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

			}
		});
		checkBox_image.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					uploadimage.setVisibility(View.VISIBLE);
				}else{
					uploadimage.setVisibility(View.GONE);
				}

			}
		});

		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.imageView_upload:
				//Toast.makeText(getActivity(),"bebbeeb",Toast.LENGTH_SHORT).show();
				Intent i = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			     startActivityForResult(i, RESULT_LOAD_IMAGE);

				break;
			default:
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {
			//MainActivity activity = (MainActivity)getActivity();
			Bitmap bitmap = getBitmapFromCameraData(data, this.getActivity());
			uploadimage.setImageBitmap(bitmap);

		}

	}
	public static Bitmap getBitmapFromCameraData(Intent data, Context context){
		Uri selectedImage = data.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();
		return BitmapFactory.decodeFile(picturePath);
	}
}
