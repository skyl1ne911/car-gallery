package com.myapp.autogallery.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> screenClick = new MutableLiveData<>();

    public void setScreenClick(boolean clicked) {
        screenClick.setValue(clicked);
    }

    public LiveData<Boolean> getScreenClicked() {
        return screenClick;
    }
}
