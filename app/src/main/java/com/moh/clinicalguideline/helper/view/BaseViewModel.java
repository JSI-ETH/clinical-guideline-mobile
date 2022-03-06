package com.moh.clinicalguideline.helper.view;
import androidx.lifecycle.ViewModel;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.databinding.PropertyChangeRegistry;
import android.util.Log;

import com.moh.clinicalguideline.BR;

public abstract class BaseViewModel<T> extends ViewModel implements Observable {
    protected boolean loading =false;

    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyPropertyChanged(BR.loading);
    }
    public ObservableField<String> StatusText = new ObservableField<>();

    protected T navigator;

    public T getNavigator() {
        return navigator;
    }

    public void setNavigator(T navigator) {
        this.navigator = navigator;
    }
    private PropertyChangeRegistry callbacks = new PropertyChangeRegistry();

    @Override
    public void addOnPropertyChangedCallback(
            Observable.OnPropertyChangedCallback callback) {
        callbacks.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(
            Observable.OnPropertyChangedCallback callback) {
        callbacks.remove(callback);
    }

    /**
     * Notifies observers that all properties of this instance have changed.
     */
    protected void notifyChange() {
        callbacks.notifyCallbacks(this, 0, null);
    }

    /**
     * Notifies observers that a specific property has changed. The getter for the
     * property that changes should be marked with the @Bindable annotation to
     * generate a field in the BR class to be used as the fieldId parameter.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    protected void notifyPropertyChanged(int fieldId) {
        callbacks.notifyCallbacks(this, fieldId, null);
    }
    public void onLoadError(Throwable throwable) {
        Log.e("Error Fetching data", throwable.getMessage());
        setLoading(false);
    }
    @Bindable
    public boolean isLoading() {
        return loading;
    }

}
