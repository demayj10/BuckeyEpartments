package android.ohiostate.buckeyepartments;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewMapViewModel extends ViewModel {
    private final MutableLiveData<String> searchText = new MutableLiveData<>();
    private final MutableLiveData<Integer> radius = new MutableLiveData<>();

    public void setSearchText(String s) {
        Log.d(ViewMapViewModel.this.getClass().getSimpleName(),
                "Internal Search Text Updated");
        searchText.setValue(s);
    }

    public void setRadius(Integer i) {
        Log.d(ViewMapViewModel.this.getClass().getSimpleName(),
                "Internal Radius Updated");
        radius.setValue(i);
    }

    public LiveData<String> getSearchText() {
        return searchText;
    }

    public LiveData<Integer> getRadius() { return radius; }
}
