package com.moh.clinicalguideline.helper;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
public abstract class ViewModel<T> extends BaseObservable {

    public ObservableField<String> StatusText = new ObservableField<>();

    protected T navigator;

    public T getNavigator() {
        return navigator;
    }

    public void setNavigator(T navigator) {
        this.navigator = navigator;
    }

}
