#!/usr/bin/env bash
set -e

echo "=========================================="
echo "    Applying Fix for Point 1...          "
echo "=========================================="

PACKAGE_DIR=$(find app/src/main/java -type d -name "viewmodel" -o -name "ui" 2>/dev/null | head -n 1)

if [ -z "$PACKAGE_DIR" ]; then
    BASE_DIR=$(find app/src/main/java -mindepth 3 -maxdepth 5 -type d | head -n 1)
    PACKAGE_DIR="${BASE_DIR}/viewmodel"
    mkdir -p "$PACKAGE_DIR"
fi

PACKAGE_NAME=$(echo "$PACKAGE_DIR" | sed 's/.*app\/src\/main\/java\///' | tr '/' '.')

cat << 'KOTLIN_EOF' > "$PACKAGE_DIR/MONUMainViewModel.kt"
package PACKAGE_NAME_PLACEHOLDER

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MONUUiState {
    object Idle : MONUUiState()
    object Loading : MONUUiState()
    data class Success(val data: String) : MONUUiState()
    data class Error(val message: String) : MONUUiState()
}

class MONUMainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<MONUUiState>(MONUUiState.Idle)
    val uiState: StateFlow<MONUUiState> = _uiState.asStateFlow()

    fun setSuccessState(data: String) {
        viewModelScope.launch {
            _uiState.value = MONUUiState.Success(data)
        }
    }

    fun setErrorState(message: String) {
        viewModelScope.launch {
            _uiState.value = MONUUiState.Error(message)
        }
    }

    fun setLoading() {
        _uiState.value = MONUUiState.Loading
    }

    fun resetState() {
        _uiState.value = MONUUiState.Idle
    }
}
KOTLIN_EOF

sed -i "s/PACKAGE_NAME_PLACEHOLDER/$PACKAGE_NAME/g" "$PACKAGE_DIR/MONUMainViewModel.kt"

echo "[✓] Point 1 Resolved: ViewModel & Reactive State Management (StateFlow) integrated."
echo "=========================================="
echo "    Execution Completed Successfully.     "
echo "=========================================="
