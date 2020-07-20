package com.moh.clinicalguideline.core;

public class RecyclerUpdate {
    private int typeOfUpdate;
    private int updateIndex;

    public RecyclerUpdate(int typeOfUpdate, int updateIndex) {
        this.typeOfUpdate = typeOfUpdate;
        this.updateIndex = updateIndex;
    }

    public int getUpdateIndex() { return updateIndex; }

    public void setUpdateIndex(int updateIndex) {
        this.updateIndex = updateIndex;
    }

    public int getTypeOfUpdate() {
        return typeOfUpdate;
    }

    public void setTypeOfUpdate(int typeOfUpdate) {
        this.typeOfUpdate = typeOfUpdate;
    }
}
