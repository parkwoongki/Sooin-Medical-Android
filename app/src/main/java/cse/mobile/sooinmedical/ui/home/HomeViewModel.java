package cse.mobile.sooinmedical.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(String.format("%04d", Integer.parseInt("323")));
    }

    public LiveData<String> getText() {
        return mText;
    }
}