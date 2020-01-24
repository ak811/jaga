package com.qkedy.jaga.interfaces;

import android.content.SharedPreferences;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileIO {

    InputStream readFile(String fileName) throws IOException;

    OutputStream writeFile(String fileName) throws IOException;

    InputStream readAsset(String fileName) throws IOException;

    SharedPreferences getSharedPreferences();
}
