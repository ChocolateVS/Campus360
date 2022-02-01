package com.example.campus360.ui.camera;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.campus360.R;
import com.example.campus360.databinding.FragmentCameraBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CameraFragment extends Fragment {

    private CameraViewModel cameraViewModel;
    private FragmentCameraBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;

    String shared_pref = "PHOTOS";

    ArrayList<ListImage> list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCameraBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ListView listView = binding.list;
        final ImageView testImage = binding.testImage;

        updateListView(listView);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    //Get Taken Image
                    Bundle bundle = result.getData().getExtras();
                    Bitmap image = (Bitmap) bundle.get("data");

                    //Create an Image View
                    ImageView imageView = new ImageView(CameraFragment.super.getContext());
                    imageView.setImageBitmap(image);

                    AlertDialog.Builder builder = new AlertDialog.Builder(CameraFragment.super.getContext());
                    builder.setTitle("Save");
                    builder.setMessage("Please Enter Filename");
                    builder.setIcon(R.drawable.save_icon);

                    EditText input = new EditText(CameraFragment.super.getContext());
                    builder.setView(input);
                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String filename = input.getText().toString();
                            String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                            String time= new SimpleDateFormat("HH:mm:ss").format(new Date());

                            String path = saveToInternalStorage(image, filename);
                            Log.d("PATH", "ACTUAL PATH" + path);

                            File f = new File(path, filename + ".jpg");
                            Bitmap b = null;
                            try {
                                b = BitmapFactory.decodeStream(new FileInputStream(f));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            testImage.setImageBitmap(b);

                            list.add(new ListImage(filename, "narnia", date, time,"2D", path));
                            try {
                                saveImages();
                            }
                            catch (Exception e){
                                Log.d("SHARE SAVE FAIL", String.valueOf(e));
                            }

                            updateListView(listView);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog ad = builder.create();
                    ad.show();
                }
            }
        });

        binding.openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    activityResultLauncher.launch(cameraIntent);
                }
                catch (Exception e) {
                    Snackbar.make(view, "Failed to Open Camera" + e, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    Log.d("BIG FAT ERROR", ":/" + e);
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String name){
        ContextWrapper cw = new ContextWrapper(CameraFragment.super.getContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", MODE_PRIVATE);

        // Create imageDir
        File mypath = new File(directory,name + ".jpg");

        Log.d("PATH", String.valueOf(mypath));
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);

            Log.d("PATH", "SUCCESS");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                fos.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public void saveImages() {
        Log.d("SHARE", "SAVE");
        SharedPreferences sharedPref = getActivity().getSharedPreferences(shared_pref, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        Gson gson = new Gson();

        String json = gson.toJson(list);
        Log.d("SHARE", "SAVE JSON: " + json);

        editor.putString(shared_pref, json);
        editor.apply();
    }

    public void getSavedImages() {
        Log.d("SHARE", "GET");

        SharedPreferences sharedPref = getActivity().getSharedPreferences(shared_pref, MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString(shared_pref, null);

        Type type = new TypeToken<ArrayList<ListImage>>() {}.getType();

        if (json != null) {
            list = gson.fromJson(json, type);
        }

        Log.d("SHARE", "LOAD JSON: " + json);
    }

    public void updateListView(ListView listView) {
        try {
            getSavedImages();
            final TextView textView = binding.text;
            textView.setVisibility(View.VISIBLE);
            if (list.size() > 0) textView.setVisibility(View.GONE);
        } catch (Exception e) {
            Log.d("SHARE", "NO STORED PHOTOS WERE PRESENT");
        }

        ImageListAdapter adapter = new ImageListAdapter(CameraFragment.super.getContext(), R.layout.adapter_view_layout, list);
        listView.setAdapter(adapter);

    }
}