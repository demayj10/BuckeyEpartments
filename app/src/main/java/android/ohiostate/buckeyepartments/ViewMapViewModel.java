package android.ohiostate.buckeyepartments;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewMapViewModel extends ViewModel {
    private final MutableLiveData<String> searchText = new MutableLiveData<>();

    public void setSearchText(String s) {
        Log.d(ViewMapViewModel.this.getClass().getSimpleName(),
                "Internal Search Text Updated");
        searchText.setValue(s);
    }

    public LiveData<String> getSearchText() {
        return searchText;
    }
}
