package cse.mobile.sooinmedical.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SuppliesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SuppliesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is supplies fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}