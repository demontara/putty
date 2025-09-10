# ConnectBot Server Storage Location - Quick Reference

## Question / Soru
**Turkish**: Bu uygulama var olan sunucu listesi bilgisini nerede saklıyor ? hangi klasörde  
**English**: Where does this application store the existing server list information? In which folder?

## Answer / Cevap

### 🗂️ Primary Storage Location / Ana Depolama Konumu
```
/data/data/org.connectbot/databases/hosts
```

### 📊 Storage Details / Depolama Detayları

| Property | Value |
|----------|-------|
| **Database Type** | SQLite |
| **Database Name** | `hosts` |
| **Main Table** | `hosts` |
| **Android Package** | `org.connectbot` |
| **Access Level** | App private data (requires root or app access) |

### 🔍 What's Stored / Saklanan Veriler

- **Server connections** / Sunucu bağlantıları
- **SSH/Telnet configurations** / SSH/Telnet yapılandırmaları  
- **Connection history** / Bağlantı geçmişi
- **Server settings** / Sunucu ayarları
- **Known host keys** / Bilinen sunucu anahtarları
- **Port forwarding rules** / Port yönlendirme kuralları

### 🛠️ Technical Implementation / Teknik Uygulama

| Component | File |
|-----------|------|
| **Database Manager** | `app/src/main/java/org/connectbot/util/HostDatabase.java` |
| **Data Model** | `app/src/main/java/org/connectbot/bean/HostBean.java` |
| **Storage Interface** | `app/src/main/java/org/connectbot/data/HostStorage.java` |
| **Backup Handler** | `app/src/main/java/org/connectbot/service/BackupAgent.java` |
| **Import/Export** | `app/src/main/java/org/connectbot/util/HostExportImport.java` |

### 📁 Full Directory Structure / Tam Dizin Yapısı

```
/data/data/org.connectbot/
├── databases/
│   ├── hosts              ← Main server database
│   ├── hosts-journal      ← SQLite journal file
│   └── pubkeys           ← SSH keys database
├── shared_prefs/
│   └── org.connectbot_preferences.xml
├── cache/
└── files/
```

### 🔧 Access Methods / Erişim Yöntemleri

1. **App Internal** - Via ConnectBot's own code
2. **Root Access** - Direct file system access
3. **ADB Shell** - Android Debug Bridge (with USB debugging)
4. **Export Feature** - JSON export from within the app

### 📖 Documentation Files / Belgeler

- `SERVER_STORAGE_DOCUMENTATION.md` - Detailed documentation
- `examples/ServerStorageExample.java` - Code examples

### 💡 Key Takeaways / Önemli Noktalar

✅ **Secure Storage** - Stored in app's private directory  
✅ **SQLite Database** - Structured data storage  
✅ **Automatic Backup** - Android backup agent support  
✅ **Export/Import** - JSON format support  
✅ **Multiple Tables** - hosts, knownhosts, portforwards, colors  

---

*This documentation answers the question about where ConnectBot stores server list information.*