package com.example.campus360.ui.camera;

import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.campus360.R;

public class CameraViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CameraViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("* Snap Snap *");
    }

    public LiveData<String> getText() {
        return mText;
    }
}