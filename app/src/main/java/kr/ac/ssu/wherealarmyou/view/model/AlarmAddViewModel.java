package kr.ac.ssu.wherealarmyou.view.model;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlarmAddViewModel<T> extends ViewModel {
    private final MutableLiveData<T> liveData = new MutableLiveData<>();

    private final MutableLiveData<String> infoString = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isComplete = new MutableLiveData<>(Boolean.FALSE);

    public LiveData<T> getLiveData() {
        return liveData;
    }

    public void setLiveData(T liveData) {
        this.liveData.setValue(liveData);
    }

    public LiveData<String> getInfoString() {
        return infoString;
    }

    public void setInfoString(String infoString) {
        this.infoString.setValue(infoString);
    }

    public void setOnComplete() {
        isComplete.setValue(Boolean.TRUE);
    }

    public LiveData<Boolean> onComplete() {
        return isComplete;
    }
}
