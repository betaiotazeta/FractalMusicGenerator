#!/usr/bin/env bash

set -e

# Change to the directory containing this script.
SCRIPT_DIR="$(cd -- "$(dirname -- "$0")" && pwd)"
cd "$SCRIPT_DIR"

# Linux-specific HiDPI detection.
if [ "$(uname)" = "Linux" ]; then
    if [ "$(xrdb -query 2>/dev/null | awk -F: '/Xft\.dpi/ {gsub(/ /,"",$2); print $2}')" = "192" ]; then
        echo "HiDPI display detected (Xft.dpi=192); enabling GDK_SCALE=2"
        export GDK_SCALE=2
    elif [ "$(xdpyinfo 2>/dev/null | awk '/resolution:/ {print $2}' | cut -dx -f1 | sort -u)" = "192" ]; then
        echo "HiDPI display detected (192 DPI); enabling GDK_SCALE=2"
        export GDK_SCALE=2
    fi
fi

# Launch the application.
exec java -jar FractalMusicGenerator-*.jar "$@"
