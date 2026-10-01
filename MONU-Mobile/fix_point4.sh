#!/usr/bin/env bash
set -e

echo "=========================================="
echo "    Applying Fix for Point 4...          "
echo "=========================================="

PACKAGE_DIR=$(find app/src/main/java -type d -name "network" -o -name "util" 2>/dev/null | head -n 1)

if [ -z "$PACKAGE_DIR" ]; then
    BASE_DIR=$(find app/src/main/java -mindepth 3 -maxdepth 5 -type d | head -n 1)
    PACKAGE_DIR="${BASE_DIR}/network"
    mkdir -p "$PACKAGE_DIR"
fi

PACKAGE_NAME=$(echo "$PACKAGE_DIR" | sed 's/.*app\/src\/main\/java\///' | tr '/' '.')

cat << 'KOTLIN_EOF' > "$PACKAGE_DIR/MONUNetworkObserver.kt"
package PACKAGE_NAME_PLACEHOLDER

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MONUNetworkObserver(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun observeNetworkStatus(): Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        val isCurrentlyConnected = isNetworkAvailable()
        trySend(isCurrentlyConnected)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    fun isNetworkAvailable(): Boolean {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
KOTLIN_EOF

sed -i "s/PACKAGE_NAME_PLACEHOLDER/$PACKAGE_NAME/g" "$PACKAGE_DIR/MONUNetworkObserver.kt"

echo "[✓] Point 4 Resolved: MONUNetworkObserver created for real-time connection status monitoring & offline fallback."
echo "=========================================="
echo "    Execution Completed Successfully.     "
echo "=========================================="
