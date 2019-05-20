#!/bin/bash
cd "$(dirname "$0")"
java -Xmx1024m -jar CxConsolePlugin-CLI-8.90.0.jar "$@"
