package com.example.campus360.ui.floor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FloorViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FloorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Start with a floor plan!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}