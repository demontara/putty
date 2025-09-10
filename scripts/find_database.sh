#!/bin/bash

# ConnectBot Server Database Finder Script
# This script helps locate the ConnectBot server database on Android devices

echo "============================================"
echo "ConnectBot Server Database Finder"
echo "============================================"
echo ""

# Check if ADB is available
if ! command -v adb &> /dev/null; then
    echo "❌ ADB (Android Debug Bridge) not found!"
    echo "   Please install Android SDK Platform Tools"
    echo ""
    exit 1
fi

# Check if device is connected
if ! adb devices | grep -q "device$"; then
    echo "❌ No Android device connected or device not authorized"
    echo "   Please connect your device and enable USB debugging"
    echo ""
    exit 1
fi

echo "📱 Android device detected!"
echo ""

# Check if ConnectBot is installed
if adb shell pm list packages | grep -q "org.connectbot"; then
    echo "✅ ConnectBot is installed"
else
    echo "❌ ConnectBot is not installed on this device"
    echo ""
    exit 1
fi

echo ""
echo "🔍 Looking for ConnectBot database..."
echo ""

# Database location
DB_PATH="/data/data/org.connectbot/databases/hosts"

# Check if database exists (this may require root)
echo "Database should be located at:"
echo "   $DB_PATH"
echo ""

# Try to check database without root first
if adb shell "test -f $DB_PATH && echo 'exists' || echo 'not_accessible'" | grep -q "exists"; then
    echo "✅ Database file found!"
    
    # Get file info
    echo ""
    echo "📊 Database Information:"
    adb shell "ls -la $DB_PATH" 2>/dev/null || echo "   (File details require root access)"
    
    # Try to get database size
    SIZE=$(adb shell "stat -c%s $DB_PATH 2>/dev/null" | tr -d '\r')
    if [ ! -z "$SIZE" ]; then
        echo "   Size: $SIZE bytes"
    fi
    
else
    echo "⚠️  Database file not directly accessible"
    echo "   This is normal - Android protects app data"
    echo ""
    echo "💡 To access the database, you can:"
    echo "   1. Use ConnectBot's export feature (JSON format)"
    echo "   2. Root your device for direct file access"
    echo "   3. Use ConnectBot's backup feature"
fi

echo ""
echo "📋 Alternative access methods:"
echo ""
echo "1. 📤 Export from ConnectBot:"
echo "   Menu → Settings → Export/Import → Export connections"
echo ""
echo "2. 🔧 ADB with root (if device is rooted):"
echo "   adb shell su -c 'cp $DB_PATH /sdcard/connectbot_hosts.db'"
echo ""
echo "3. 🗄️ Android Backup:"
echo "   adb backup -f connectbot_backup.ab org.connectbot"
echo ""

# Check for exported files on SD card
echo "🔍 Checking for existing exported files..."
EXPORTED_FILES=$(adb shell "find /sdcard -name '*connectbot*' -o -name '*hosts*' 2>/dev/null" | head -10)
if [ ! -z "$EXPORTED_FILES" ]; then
    echo "   Found possible ConnectBot exports:"
    echo "$EXPORTED_FILES"
else
    echo "   No exported files found on SD card"
fi

echo ""
echo "============================================"
echo "Summary:"
echo "✓ ConnectBot stores server list in SQLite database"
echo "✓ Location: $DB_PATH"
echo "✓ Protected by Android security (app private data)"
echo "✓ Use app's export feature for user-accessible backup"
echo "============================================"