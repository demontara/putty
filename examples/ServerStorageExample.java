/*
 * Example code showing how to access ConnectBot's server storage
 * This demonstrates the programmatic way to retrieve server information
 */

package org.connectbot.examples;

import android.content.Context;
import org.connectbot.util.HostDatabase;
import org.connectbot.bean.HostBean;
import org.connectbot.data.HostStorage;
import java.util.List;

/**
 * Example class demonstrating how ConnectBot stores and retrieves server information
 */
public class ServerStorageExample {
    
    private Context context;
    private HostStorage hostStorage;
    
    public ServerStorageExample(Context context) {
        this.context = context;
        // Get the singleton instance of HostDatabase
        this.hostStorage = HostDatabase.get(context);
    }
    
    /**
     * Example: Get all stored servers
     */
    public void getAllServers() {
        // Retrieve all hosts from the database
        List<HostBean> hosts = hostStorage.getHosts(false); // false = sort by nickname
        
        System.out.println("Total servers found: " + hosts.size());
        
        for (HostBean host : hosts) {
            System.out.println("Server: " + host.getNickname());
            System.out.println("  Protocol: " + host.getProtocol());
            System.out.println("  Address: " + host.getUsername() + "@" + host.getHostname() + ":" + host.getPort());
            System.out.println("  Last Connected: " + host.getLastConnect());
            System.out.println("  Color: " + host.getColor());
            System.out.println("  ID: " + host.getId());
            System.out.println("---");
        }
    }
    
    /**
     * Example: Add a new server
     */
    public void addNewServer() {
        HostBean newHost = new HostBean();
        newHost.setNickname("Example Server");
        newHost.setProtocol("ssh");
        newHost.setUsername("user");
        newHost.setHostname("example.com");
        newHost.setPort(22);
        newHost.setColor("red");
        
        // Save to database
        HostBean savedHost = hostStorage.saveHost(newHost);
        System.out.println("New server saved with ID: " + savedHost.getId());
    }
    
    /**
     * Example: Find a specific server
     */
    public void findServerByHostname(String hostname) {
        List<HostBean> hosts = hostStorage.getHosts(false);
        
        for (HostBean host : hosts) {
            if (hostname.equals(host.getHostname())) {
                System.out.println("Found server: " + host.getNickname());
                System.out.println("Full connection string: " + host.toString());
                break;
            }
        }
    }
    
    /**
     * Example: Update last connection time
     */
    public void updateLastConnection(long hostId) {
        HostBean host = hostStorage.findHostById(hostId);
        if (host != null) {
            hostStorage.touchHost(host); // Updates last connection time
            System.out.println("Updated last connection time for: " + host.getNickname());
        }
    }
    
    /**
     * Example: Delete a server
     */
    public void deleteServer(long hostId) {
        HostBean host = hostStorage.findHostById(hostId);
        if (host != null) {
            String nickname = host.getNickname();
            hostStorage.deleteHost(host);
            System.out.println("Deleted server: " + nickname);
        }
    }
    
    /**
     * Example: Get database file location (for documentation purposes)
     */
    public static String getDatabaseLocation(Context context) {
        // The actual database path in Android
        String dbPath = context.getDatabasePath(HostDatabase.DB_NAME).getAbsolutePath();
        return dbPath;
        // This typically returns: /data/data/org.connectbot/databases/hosts
    }
    
    /**
     * Example usage in an Android Activity or Service
     */
    public static void exampleUsage(Context context) {
        ServerStorageExample example = new ServerStorageExample(context);
        
        // Display database location
        System.out.println("Database Location: " + getDatabaseLocation(context));
        
        // Get all servers
        example.getAllServers();
        
        // Add a new server
        example.addNewServer();
        
        // Find a specific server
        example.findServerByHostname("example.com");
    }
}

/*
 * Key Points about ConnectBot's Server Storage:
 * 
 * 1. Database Location: /data/data/org.connectbot/databases/hosts
 * 2. Database Type: SQLite
 * 3. Main Table: "hosts" 
 * 4. Additional Tables: "knownhosts", "portforwards", "colors"
 * 5. Access: Only via ConnectBot app or with root privileges
 * 6. Backup: Automatic via BackupAgent, manual via JSON export
 * 
 * Storage Structure:
 * /data/data/org.connectbot/
 * ├── databases/
 * │   ├── hosts (main server database)
 * │   └── pubkeys (SSH key database)
 * ├── shared_prefs/
 * │   └── org.connectbot_preferences.xml (app settings)
 * └── files/
 *     └── (temporary files, if any)
 */