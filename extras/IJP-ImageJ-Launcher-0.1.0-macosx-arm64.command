#!/bin/bash
DIR=$(cd "$(dirname "$0")" && pwd -P)
echo "$DIR"
"$DIR"/IJP-ImageJ-Launcher-0.1.0-macosx-arm64 --debug --ij-dir "$DIR"
