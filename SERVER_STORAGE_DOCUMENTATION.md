# ConnectBot Sunucu Listesi Depolama Konumu / Server List Storage Location

## Türkçe

### Sunucu Listesi Nerede Saklanıyor?

ConnectBot uygulaması, mevcut sunucu listesi bilgilerini aşağıdaki konumda saklar:

**Ana Depolama Konumu:**
```
/data/data/org.connectbot/databases/hosts
```

### Detaylı Açıklama

1. **Veritabanı Türü**: SQLite veritabanı
2. **Veritabanı Adı**: `hosts`
3. **Konum**: Android uygulamasının özel veritabanı klasörü
4. **Erişim**: Sadece ConnectBot uygulaması bu dosyaya erişebilir (Android güvenlik modeli)

### Veritabanı Yapısı

Sunucu bilgileri `hosts` tablosunda saklanır ve şu alanları içerir:
- `nickname` - Sunucu takma adı
- `protocol` - Protokol türü (ssh, telnet, local)
- `username` - Kullanıcı adı
- `hostname` - Sunucu adresi
- `port` - Port numarası
- `lastconnect` - Son bağlantı zamanı
- `color` - Renk ayarı
- Ve diğer bağlantı ayarları...

### Ek Depolama Alanları

**Diğer ilgili veriler:**
- `knownhosts` tablosu: Bilinen sunucu anahtarları
- `portforwards` tablosu: Port yönlendirme ayarları
- `colors` tablosu: Renk şemaları

### Yedekleme ve Dışa Aktarma

- **Android Yedekleme**: `BackupAgent` sınıfı ile otomatik yedekleme
- **Manuel Dışa Aktarma**: JSON formatında dışa aktarma mümkün
- **İçe Aktarma**: JSON dosyalarından sunucu listesi içe aktarılabilir

---

## English

### Where is the Server List Stored?

ConnectBot application stores the existing server list information at the following location:

**Primary Storage Location:**
```
/data/data/org.connectbot/databases/hosts
```

### Detailed Explanation

1. **Database Type**: SQLite database
2. **Database Name**: `hosts`
3. **Location**: Application's private database directory on Android
4. **Access**: Only ConnectBot application can access this file (Android security model)

### Database Structure

Server information is stored in the `hosts` table with the following fields:
- `nickname` - Server nickname
- `protocol` - Protocol type (ssh, telnet, local)
- `username` - Username
- `hostname` - Server address
- `port` - Port number
- `lastconnect` - Last connection time
- `color` - Color setting
- And other connection settings...

### Additional Storage Areas

**Other related data:**
- `knownhosts` table: Known server keys
- `portforwards` table: Port forwarding settings
- `colors` table: Color schemes

### Backup and Export

- **Android Backup**: Automatic backup via `BackupAgent` class
- **Manual Export**: JSON format export available
- **Import**: Server list can be imported from JSON files

---

## Technical Implementation Details

### Key Source Files

1. **HostDatabase.java** (`/app/src/main/java/org/connectbot/util/HostDatabase.java`)
   - Main database management class
   - Defines database schema and operations
   - Database name constant: `DB_NAME = "hosts"`

2. **HostBean.java** (`/app/src/main/java/org/connectbot/bean/HostBean.java`)
   - Data model for individual server connections
   - Contains all server configuration properties

3. **HostStorage.java** (`/app/src/main/java/org/connectbot/data/HostStorage.java`)
   - Interface defining storage operations
   - Abstracts database operations

4. **BackupAgent.java** (`/app/src/main/java/org/connectbot/service/BackupAgent.java`)
   - Handles Android backup and restore
   - References database path: `"../databases/" + HostDatabase.DB_NAME`

5. **HostExportImport.java** (`/app/src/main/java/org/connectbot/util/HostExportImport.java`)
   - Handles JSON export/import functionality
   - Allows manual backup and restore of server configurations

### Database Schema

The main `hosts` table contains these columns:
```sql
CREATE TABLE hosts (
    _id INTEGER PRIMARY KEY,
    nickname TEXT,
    protocol TEXT DEFAULT 'ssh',
    username TEXT,
    hostname TEXT,
    port INTEGER,
    lastconnect INTEGER,
    color TEXT,
    usekeys TEXT,
    useauthagent TEXT,
    postlogin TEXT,
    pubkeyid INTEGER DEFAULT -1,
    delkey TEXT DEFAULT 'del',
    fontsize INTEGER,
    wantsession TEXT DEFAULT 'true',
    compression TEXT DEFAULT 'false',
    encoding TEXT DEFAULT 'UTF-8',
    stayconnected TEXT DEFAULT 'false',
    quickdisconnect TEXT DEFAULT 'false'
);
```

### Access Requirements

- **Root Access**: Required to directly access the database file
- **App Export**: Use the app's built-in export feature for user-accessible backups
- **ADB Access**: With USB debugging enabled, can access via Android Debug Bridge

### File Permissions

The database file is located in the app's private data directory with restricted permissions:
- Owner: `app_connectbot` (application user)
- Permissions: `rw-------` (readable/writable only by the app)
- SELinux Context: `u:object_r:app_data_file:s0:c512,c768`