package com.ham.library.storage_view;

public class NoStorageItem extends StorageItem{
    private String message;

    public void setMessage(String m){
         message = m;
    }

    public String getMessage(){
        return message;
    }
}
