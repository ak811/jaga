package com.qkedy.jaga.interfaces;

import android.content.SharedPreferences;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileIO {

    /**
     * @param fileName the file name
     * @return the InputStream of the file
     * @throws IOException when the file not found
     * @apiNote converts file to an InputStream
     */
    InputStream readFile(String fileName) throws IOException;

    /**
     * @param fileName the file name
     * @return the OutputStream of the file
     * @throws IOException when the file not found
     * @apiNote converts file to an OutputStream
     */
    OutputStream writeFile(String fileName) throws IOException;

    /**
     * @param fileName the asset name
     * @return the InputStream of the asset
     * @throws IOException when the asset not found
     * @apiNote opens the asset and converts it to an OutputStream
     */
    InputStream readAsset(String fileName) throws IOException;

    /**
     * @return the default SharedPreferences
     */
    SharedPreferences getSharedPreferences();
}
