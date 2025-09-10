/*
 * ConnectBot: simple, powerful, open-source SSH client for Android
 * Copyright 2024 ConnectBot Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.connectbot.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.connectbot.bean.HostBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for exporting and importing host configurations
 */
public class HostExportImport {
    private static final String TAG = "CB.HostExportImport";
    
    private static final String JSON_VERSION = "1.0";
    private static final String JSON_KEY_VERSION = "version";
    private static final String JSON_KEY_HOSTS = "hosts";
    private static final String JSON_KEY_NICKNAME = "nickname";
    private static final String JSON_KEY_PROTOCOL = "protocol";
    private static final String JSON_KEY_USERNAME = "username";
    private static final String JSON_KEY_HOSTNAME = "hostname";
    private static final String JSON_KEY_PORT = "port";
    private static final String JSON_KEY_COLOR = "color";
    private static final String JSON_KEY_USE_KEYS = "useKeys";
    private static final String JSON_KEY_USE_AUTH_AGENT = "useAuthAgent";
    private static final String JSON_KEY_POST_LOGIN = "postLogin";
    private static final String JSON_KEY_PUBKEY_ID = "pubkeyId";
    private static final String JSON_KEY_WANT_SESSION = "wantSession";
    private static final String JSON_KEY_DEL_KEY = "delKey";
    private static final String JSON_KEY_FONT_SIZE = "fontSize";
    private static final String JSON_KEY_COMPRESSION = "compression";
    private static final String JSON_KEY_ENCODING = "encoding";
    private static final String JSON_KEY_STAY_CONNECTED = "stayConnected";
    private static final String JSON_KEY_QUICK_DISCONNECT = "quickDisconnect";

    /**
     * Export hosts to JSON format
     */
    public static void exportHosts(Context context, Uri uri, List<HostBean> hosts) throws IOException, JSONException {
        Log.d(TAG, "Exporting " + hosts.size() + " hosts to " + uri);
        
        JSONObject root = new JSONObject();
        root.put(JSON_KEY_VERSION, JSON_VERSION);
        
        JSONArray hostsArray = new JSONArray();
        for (HostBean host : hosts) {
            JSONObject hostObject = new JSONObject();
            hostObject.put(JSON_KEY_NICKNAME, host.getNickname());
            hostObject.put(JSON_KEY_PROTOCOL, host.getProtocol());
            hostObject.put(JSON_KEY_USERNAME, host.getUsername());
            hostObject.put(JSON_KEY_HOSTNAME, host.getHostname());
            hostObject.put(JSON_KEY_PORT, host.getPort());
            hostObject.put(JSON_KEY_COLOR, host.getColor());
            hostObject.put(JSON_KEY_USE_KEYS, host.getUseKeys());
            hostObject.put(JSON_KEY_USE_AUTH_AGENT, host.getUseAuthAgent());
            hostObject.put(JSON_KEY_POST_LOGIN, host.getPostLogin());
            hostObject.put(JSON_KEY_PUBKEY_ID, host.getPubkeyId());
            hostObject.put(JSON_KEY_WANT_SESSION, host.getWantSession());
            hostObject.put(JSON_KEY_DEL_KEY, host.getDelKey());
            hostObject.put(JSON_KEY_FONT_SIZE, host.getFontSize());
            hostObject.put(JSON_KEY_COMPRESSION, host.getCompression());
            hostObject.put(JSON_KEY_ENCODING, host.getEncoding());
            hostObject.put(JSON_KEY_STAY_CONNECTED, host.getStayConnected());
            hostObject.put(JSON_KEY_QUICK_DISCONNECT, host.getQuickDisconnect());
            
            hostsArray.put(hostObject);
        }
        root.put(JSON_KEY_HOSTS, hostsArray);
        
        // Write to file
        try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
             OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            writer.write(root.toString(2)); // Pretty print with indent of 2
        }
    }

    /**
     * Import hosts from JSON format
     */
    public static List<HostBean> importHosts(Context context, Uri uri) throws IOException, JSONException {
        Log.d(TAG, "Importing hosts from " + uri);
        
        StringBuilder json = new StringBuilder();
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        }
        
        JSONObject root = new JSONObject(json.toString());
        String version = root.optString(JSON_KEY_VERSION, "1.0");
        
        // Future version compatibility can be handled here
        if (!JSON_VERSION.equals(version)) {
            Log.w(TAG, "Import file version " + version + " may not be fully compatible with current version " + JSON_VERSION);
        }
        
        JSONArray hostsArray = root.getJSONArray(JSON_KEY_HOSTS);
        List<HostBean> hosts = new ArrayList<>();
        
        for (int i = 0; i < hostsArray.length(); i++) {
            JSONObject hostObject = hostsArray.getJSONObject(i);
            HostBean host = new HostBean();
            
            host.setNickname(hostObject.optString(JSON_KEY_NICKNAME, ""));
            host.setProtocol(hostObject.optString(JSON_KEY_PROTOCOL, "ssh"));
            host.setUsername(hostObject.optString(JSON_KEY_USERNAME, ""));
            host.setHostname(hostObject.optString(JSON_KEY_HOSTNAME, ""));
            host.setPort(hostObject.optInt(JSON_KEY_PORT, 22));
            host.setColor(hostObject.optString(JSON_KEY_COLOR, null));
            host.setUseKeys(hostObject.optBoolean(JSON_KEY_USE_KEYS, true));
            host.setUseAuthAgent(hostObject.optString(JSON_KEY_USE_AUTH_AGENT, HostDatabase.AUTHAGENT_NO));
            host.setPostLogin(hostObject.optString(JSON_KEY_POST_LOGIN, null));
            host.setPubkeyId(hostObject.optLong(JSON_KEY_PUBKEY_ID, HostDatabase.PUBKEYID_ANY));
            host.setWantSession(hostObject.optBoolean(JSON_KEY_WANT_SESSION, true));
            host.setDelKey(hostObject.optString(JSON_KEY_DEL_KEY, HostDatabase.DELKEY_DEL));
            host.setFontSize(hostObject.optInt(JSON_KEY_FONT_SIZE, HostBean.DEFAULT_FONT_SIZE));
            host.setCompression(hostObject.optBoolean(JSON_KEY_COMPRESSION, false));
            host.setEncoding(hostObject.optString(JSON_KEY_ENCODING, HostDatabase.ENCODING_DEFAULT));
            host.setStayConnected(hostObject.optBoolean(JSON_KEY_STAY_CONNECTED, false));
            host.setQuickDisconnect(hostObject.optBoolean(JSON_KEY_QUICK_DISCONNECT, false));
            
            hosts.add(host);
        }
        
        Log.d(TAG, "Successfully parsed " + hosts.size() + " hosts");
        return hosts;
    }
}