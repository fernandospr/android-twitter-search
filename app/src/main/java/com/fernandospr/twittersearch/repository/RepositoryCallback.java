package com.fernandospr.twittersearch.repository;

public interface RepositoryCallback<T> {
    void onSuccess(T object);
    void onFailure(Throwable error);
}
