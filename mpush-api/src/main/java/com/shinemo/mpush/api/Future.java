package com.shinemo.mpush.api;

public interface Future {
	
    Object get();

    Object get(int timeout);

    boolean isDone();
	
	interface Callback {
        void onSuccess(String userId);

        void onFailure(String userId);

        void onOffline(String userId);

        void onTimeout(String userId);
    }
	
}
