#!/usr/bin/env bash

# Configure HiDPI scaling before launching the application.
# OpenJDK must be informed of the display scale factor at startup;
# otherwise the user interface may be rendered incorrectly.

if [ "`command xrdb -query 2> /dev/null | grep Xft.dpi | cut -d ':' -f2 | xargs`" = 192 ]
then
    echo "HiDPI display detected (Xft.dpi=192); enabling GDK_SCALE=2"
    export GDK_SCALE=2
fi

if [ "`command xdpyinfo 2> /dev/null | grep 'resolution:.*dots per inch' | cut -d ':' -f2 | cut -d 'x' -f1 | sort -u | xargs`" = 192 ]
then
    echo "HiDPI display detected (xdpyinfo=192); enabling GDK_SCALE=2"
    export GDK_SCALE=2
fi

# Change the current working directory to the script location.
cd "$(dirname "$(readlink -f "$0")")"

# Launch the application.
exec ./bin/FractalMusicGenerator "$@"
