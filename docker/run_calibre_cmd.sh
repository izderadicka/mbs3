#!/bin/bash

# Get the real path of the symlink
SYMLINK_PATH=$(readlink -f "$0")
# Extract the command from the symlink path
FILE=$(basename "$SYMLINK_PATH")
COMMAND=$(basename "$0")

# Check if the script is a symlink
if [ "$COMMAND" == "$FILE" ]; then
    echo "This script must be run from a symlink." >&2
    exit 1
fi

HOST_DIR=${EBOOKS_DIR:-$HOME}
IMAGE=${CALIBRE_IMAGE:-izderadicka/calibre}
# Run the command in Docker with all arguments
docker run --rm --user $(id -u):$(id -g) -v $HOST_DIR:/work $IMAGE "$COMMAND" "$@"
