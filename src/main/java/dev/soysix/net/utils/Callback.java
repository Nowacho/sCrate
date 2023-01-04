package dev.soysix.net.utils;

import java.io.Serializable;

public interface Callback<T> extends Serializable {
    void callback(T var1);
}
